package ru.hse.plugin.metrics.commons.listeners;

import kotlin.jvm.internal.Ref;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import ru.hse.plugin.util.PluginConstants;

import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GitListenerTest extends AbstractGitListenerTest {
    private final Ref.ObjectRef<Boolean> flag = new Ref.ObjectRef<>();
    {
        flag.element = false;
    }
    private final GitListenerBase gitListener = new GitListenerBase(Paths.get("./playground")) {
        @NotNull
        @Override
        protected Runnable getCommitHandler() {
            return () -> flag.element = true;
        }
    };

    @NotNull
    @Override
    protected GitListenerBase getGitListener() {
        return gitListener;
    }

    @Test
    public void testExistsTrue() throws Exception {
        init();
        assertTrue(exists());
    }

    @Test
    public void testExistsFalse() throws Exception {
        assertFalse(exists());
    }

    @Test
    public void testDidJustCommitWithNoCommit() throws Exception {
        init();
        assertFalse(didJustCommit());
    }

    @Test
    public void testDidJustCommitAfterCommitInstant() throws Exception {
        init();
        createFile("filename.txt", "File content");
        add("filename.txt");
        commit("add filename.txt");
        assertTrue(didJustCommit());
    }

    @Test
    public void testDidJustCommitAfterCommitWait() throws Exception {
        init();
        createFile("filename.txt", "File content");
        add("filename.txt");
        commit("add filename.txt");
        TimeUnit.MILLISECONDS.sleep((long) (1.5 * PluginConstants.GIT_JUST_MILLISECONDS));
        assertFalse(didJustCommit());
    }

    @Test
    public void testDidJustCommitComplicated() throws Exception {
        init();
        assertFalse(didJustCommit());

        createFile("filename.txt", "File content 1");
        add("filename.txt");
        commit("add filename.txt 1");
        assertTrue(didJustCommit());

        createFile("filename.txt", "File content 2");
        add("filename.txt");
        commit("add filename.txt 2");
        assertTrue(didJustCommit());

        createFile("new-file.txt", "File content 3");
        add("new-file.txt");
        commit("add filename.txt 3");
        TimeUnit.MILLISECONDS.sleep((long) (1.5 * PluginConstants.GIT_JUST_MILLISECONDS));
        assertFalse(didJustCommit());
    }

    @Test
    public void testDidJustCommitWithRemoteCommit() throws Exception {
        makeRemoteRepoWithCommit();
        cloneRemote();
        assertFalse(didJustCommit());
    }

    @Test
    public void testDidJustCommitWithRemoteCommitAndCommitAfter() throws Exception {
        makeRemoteRepoWithCommit();
        cloneRemote();
        assertFalse(didJustCommit());

        createFile("filename.txt", "File content 2");
        add("filename.txt");
        commit("add filename.txt 2");
        assertTrue(didJustCommit());
    }

    @Test
    public void testJustCommittedNoRepo() throws Exception {
        testCommitDidNotCallHolder();
    }

    @Test
    public void testJustCommittedAfterCommitInstant() throws Exception {
        init();
        createFile("filename.txt", "File content");
        add("filename.txt");
        commit("add filename.txt");
        testCommitCalledHolder();
    }

    @Test
    public void testJustCommittedAfterCommitWait() throws Exception {
        init();
        createFile("filename.txt", "File content");
        add("filename.txt");
        commit("add filename.txt");
        TimeUnit.MILLISECONDS.sleep((long) (1.5 * PluginConstants.GIT_JUST_MILLISECONDS));
        assertFalse(didJustCommit());
    }

    private void testCommitCalledHolder() throws Exception {
        flag.element = false;
        justCommitted();
        assertTrue(flag.element);
    }

    private void testCommitDidNotCallHolder() throws Exception {
        flag.element = false;
        justCommitted();
        assertFalse(flag.element);
    }
}