package ru.hse.plugin.metrics.commons.listeners;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.errors.RepositoryNotFoundException;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.jetbrains.annotations.NotNull;
import ru.hse.plugin.util.PluginConstants;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Date;

public abstract class GitListenerBase {
    @NotNull
    protected abstract Runnable getCommitHandler();

    private final Path location;

    protected GitListenerBase(Path location) {
        this.location = location;
    }

    public void tryAll() throws IOException {
        justCommitted();
    }

    private void justCommitted() throws IOException {
        if (exists() && didJustCommit()) {
            getCommitHandler().run();
        }
    }

    private boolean exists() throws IOException {
        try (Git ignored = Git.open(location.toFile())) {
            return true;
        } catch (RepositoryNotFoundException ignored) { // простите
            return false;
        }
    }

    private boolean didJustCommit() throws IOException {
        try (Git git = Git.open(location.toFile())) {
            Repository repository = git.getRepository();

            ObjectId id = repository.resolve(Constants.HEAD);

            if (id == null) {
                return false;
            }

            try (RevWalk revWalk = new RevWalk(repository)) {
                RevCommit commit = revWalk.parseCommit(id);
                int commitTime = commit.getCommitTime();
                PersonIdent committer = commit.getCommitterIdent();

                return checkDidJustCommit(committer, commitTime, repository);
            }
        }
    }

    private static boolean checkDidJustCommit(PersonIdent committer, int commitTime, Repository repository) {
        long currentTime = new Date().getTime();
        return currentTime - (long) commitTime * 1000 <= PluginConstants.GIT_JUST_MILLISECONDS
                && isMe(committer, repository);
    }

    private static boolean isMe(PersonIdent committer, Repository repository) {
        var me = new PersonIdent(repository);
        return me.getName().equals(committer.getName())
                && me.getEmailAddress().equals(committer.getEmailAddress())
                && me.getTimeZoneOffset() == committer.getTimeZoneOffset();
    }
}
