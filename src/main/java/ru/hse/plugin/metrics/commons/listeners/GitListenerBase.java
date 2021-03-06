package ru.hse.plugin.metrics.commons.listeners;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.errors.RepositoryNotFoundException;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Date;
import java.util.function.Consumer;

import static ru.hse.plugin.util.Constants.GIT_JUST_MILLISECONDS;

public abstract class GitListenerBase {
    @NotNull
    protected abstract Runnable getCommitHandler();
    @NotNull
    protected abstract Consumer<String> getCommitOnBranchHandler();

    private final Path location;

    protected GitListenerBase(Path location) {
        this.location = location;
    }

    public void tryAll() throws IOException {
        justCommitted();
        justCommittedOnBranch();
    }

    private void justCommitted() throws IOException {
        if (exists() && didJustCommit()) {
            getCommitHandler().run();
        }
    }

    private void justCommittedOnBranch() throws IOException {
        String branch = getBranch();
        if (exists() && didJustCommit()) {
            getCommitOnBranchHandler().accept(branch);
        }
    }

    private boolean exists() throws IOException {
        try  {
            Git.open(location.toFile());
            return true;
        } catch (RepositoryNotFoundException ignored) { // простите
            return false;
        }
    }

    @Nullable
    private String getBranch() throws IOException {
        Git git = Git.open(location.toFile());
        Repository repository = git.getRepository();

        Ref head = repository.getRef(Constants.HEAD);

        if (head == null) {
            return null;
        }

        if (head.isSymbolic()) {
            return Repository.shortenRefName(head.getTarget().getName());
        }

        return null;
    }

    private boolean didJustCommit() throws IOException {
        Git git = Git.open(location.toFile());
        Repository repository = git.getRepository();

        ObjectId id = repository.resolve(Constants.HEAD);

        if (id == null) {
            return false;
        }

        RevWalk revWalk = new RevWalk(repository);
        RevCommit commit = revWalk.parseCommit(id);
        int commitTime = commit.getCommitTime();
        PersonIdent committer = commit.getCommitterIdent();

        return checkDidJustCommit(committer, commitTime, repository);
    }

    private static boolean checkDidJustCommit(PersonIdent committer, int commitTime, Repository repository) {
        long currentTime = new Date().getTime();
        return currentTime - (long) commitTime * 1000 <= GIT_JUST_MILLISECONDS
                && isMe(committer, repository);
    }

    private static boolean isMe(PersonIdent committer, Repository repository) {
        var me = new PersonIdent(repository);
        return me.getName().equals(committer.getName())
                && me.getEmailAddress().equals(committer.getEmailAddress())
                && me.getTimeZoneOffset() == committer.getTimeZoneOffset();
    }
}
