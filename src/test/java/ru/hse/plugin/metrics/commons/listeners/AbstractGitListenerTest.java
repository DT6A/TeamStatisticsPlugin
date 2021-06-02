package ru.hse.plugin.metrics.commons.listeners;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Repository;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Collection;

public abstract class AbstractGitListenerTest {
    @NotNull
    protected abstract GitListenerBase getGitListener();

    private static final File playground = new File("./playground");
    private static final File playground_remote = new File("./playground-remote");

    @BeforeEach
    protected void cleanPlaygrounds() throws IOException {
        cleanPlayground(playground);
        cleanPlayground(playground_remote);
    }

    private static void cleanPlayground(File pg) throws IOException {
        //noinspection ResultOfMethodCallIgnored
        pg.mkdirs();
        Collection<File> files = FileUtils.listFilesAndDirs(
                pg,
                TrueFileFilter.INSTANCE,
                TrueFileFilter.INSTANCE
        );
        for (File file : files) {
            if (file.exists() && !Files.isSameFile(file.toPath(), pg.toPath())) {
                FileUtils.deleteQuietly(file);
            }
        }
    }

    @AfterAll
    public static void clear() {
        if (playground.exists()) {
            FileUtils.deleteQuietly(playground);
        }
        if (playground_remote.exists()) {
            FileUtils.deleteQuietly(playground_remote);
        }
    }

    protected void init() throws GitAPIException {
        Git.init()
                .setDirectory(playground)
                .call()
                .close();
    }

    protected void add(String filename) throws GitAPIException, IOException {
        Git git = Git.open(playground);
        git.add()
                .addFilepattern(filename)
                .call();
    }

    protected void commit(String message) throws GitAPIException, IOException {
        Git git = Git.open(playground);
        git.commit()
                .setMessage(message)
                .call();
    }

    protected void cloneRemote() throws GitAPIException {
        FileUtils.deleteQuietly(playground);

        Git.cloneRepository()
                .setURI(playground_remote.toPath().resolve(".git").toUri().toString())
                .setDirectory(playground)
                .call()
                .close();
    }

    protected void makeRemoteRepoWithCommit() throws GitAPIException, IOException {
        Git git = Git.init().setDirectory(playground_remote).call();
        File file = new File(playground_remote, "file.txt");
        FileUtils.writeStringToFile(file, "content", Charset.defaultCharset());

        git.add()
                .addFilepattern("file.txt")
                .call();

        git.commit()
                .setCommitter("Some weird name nobody will have so tests will work", "mail@gmail.com")
                .setMessage("add file.txt")
                .call();
    }

    protected void branch(String name) throws GitAPIException {
        Git git = Git.init().setDirectory(playground).call();
        git.branchCreate().setName(name).call();
    }

    protected void checkout(String name) throws GitAPIException {
        Git git = Git.init().setDirectory(playground).call();
        git.checkout().setName(name).call();
    }

    protected void checkoutOnCommit() throws GitAPIException, IOException {
        Git git = Git.init().setDirectory(playground).call();
        Repository repository = git.getRepository();

        ObjectId id = repository.resolve(Constants.HEAD);

        git.checkout().setName(id.getName()).call();
    }

    protected void createFile(@NotNull String fileName, @NotNull String content) throws IOException {
        File file = new File(playground, fileName);
        FileUtils.writeStringToFile(file, content, Charset.defaultCharset());
    }

    protected boolean exists() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method exists = getGitListener().getClass().getSuperclass().getDeclaredMethod("exists");
        exists.setAccessible(true);
        return (boolean) exists.invoke(getGitListener());
    }

    protected boolean didJustCommit() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method didJustCommit = getGitListener().getClass().getSuperclass().getDeclaredMethod("didJustCommit");
        didJustCommit.setAccessible(true);
        return (boolean) didJustCommit.invoke(getGitListener());
    }

    protected void justCommitted() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method justCommitted = getGitListener().getClass().getSuperclass().getDeclaredMethod("justCommitted");
        justCommitted.setAccessible(true);
        justCommitted.invoke(getGitListener());
    }

    protected String getBranch() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method justCommitted = getGitListener().getClass().getSuperclass().getDeclaredMethod("getBranch");
        justCommitted.setAccessible(true);
        return (String) justCommitted.invoke(getGitListener());
    }
}
