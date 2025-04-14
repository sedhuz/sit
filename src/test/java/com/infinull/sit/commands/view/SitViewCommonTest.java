package com.infinull.sit.commands.view;

import com.infinull.sit.App;
import com.infinull.sit.SitTestWatcher;
import com.infinull.sit.message.MessageUtil;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SitViewCommonTest {
    @Rule
    public SitTestWatcher watcher = new SitTestWatcher();

    @Test
    public void testCatfileUsage() {
        String[] args = {"view"};
        App.test(args);
        String output = watcher.getOutput();
        assertEquals("Display usage information", MessageUtil.getMsg("usage.view"), output);
    }

    // Improper Args Cases
    @Test
    public void testInvalidSha() {
        String[] args = {"view", "invalid_sha"};
        App.test(args);
        String output = watcher.getOutput();
        assertEquals("Print Invalid SHA-1", MessageUtil.getMsg("error.sha1.invalid", "invalid_sha"), output);
    }

    @Test
    public void testInvalidShaForP() {
        String[] args = {"view", "-p", "invalid_sha"};
        App.test(args);
        String output = watcher.getOutput();
        assertEquals("Print Invalid SHA-1", MessageUtil.getMsg("error.sha1.invalid", "invalid_sha"), output);
    }

    @Test
    public void testInvalidShaForT() {
        String[] args = {"view", "-t", "invalid_sha"};
        App.test(args);
        String output = watcher.getOutput();
        assertEquals("Print Invalid SHA-1", MessageUtil.getMsg("error.sha1.invalid", "invalid_sha"), output);
    }

    @Test
    public void testInvalidShaForE() {
        String[] args = {"view", "-e", "invalid_sha"};
        App.test(args);
        String output = watcher.getOutput();
        assertEquals("Print Invalid SHA-1", MessageUtil.getMsg("error.sha1.invalid", "invalid_sha"), output);
    }

    @Test
    public void testInvalidShaForS() {
        String[] args = {"view", "-s", "invalid_sha"};
        App.test(args);
        String output = watcher.getOutput();
        assertEquals("Print Invalid SHA-1", MessageUtil.getMsg("error.sha1.invalid", "invalid_sha"), output);
    }

    @Test
    public void testImproperArgs2() {
        String[] args = {"view", "-t", "invalid_sha"};
        App.test(args);
        String output = watcher.getOutput();
        assertEquals("Print Invalid SHA-1", MessageUtil.getMsg("error.sha1.invalid", "invalid_sha"), output);
    }

    @Test
    public void testImproperArgs3() {
        String[] args = {"view", "-p", "invalid_sha", "extra_arg"};
        App.test(args);
        String output = watcher.getOutput();
        assertEquals("Display usage information", MessageUtil.getMsg("usage.view"), output);
    }
}
