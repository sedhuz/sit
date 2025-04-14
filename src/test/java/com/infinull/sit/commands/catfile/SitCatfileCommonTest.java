package com.infinull.sit.commands.catfile;

import com.infinull.sit.App;
import com.infinull.sit.SitTestWatcher;
import com.infinull.sit.message.MessageUtil;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SitCatfileCommonTest {
    @Rule
    public SitTestWatcher watcher = new SitTestWatcher();

    @Test
    public void testCatfileUsage() {
        String[] args = {"catfile"};
        App.test(args);
        String output = watcher.getOutput();
        assertEquals("Display usage information", MessageUtil.getMsg("usage.catfile"), output);
    }

    // Improper Args Cases
    @Test
    public void testPrintObjectForNoSha() {
        String[] args = {"catfile", "-p"};
        App.test(args);
        String output = watcher.getOutput();
        assertEquals("Display usage information", MessageUtil.getMsg("usage.catfile"), output);
    }

    @Test
    public void testInvalidShaForP() {
        String[] args = {"catfile", "-p", "invalid_sha"};
        App.test(args);
        String output = watcher.getOutput();
        assertEquals("Print Invalid SHA-1", MessageUtil.getMsg("error.sha1.invalid", "invalid_sha"), output);
    }

    @Test
    public void testInvalidShaForT() {
        String[] args = {"catfile", "-t", "invalid_sha"};
        App.test(args);
        String output = watcher.getOutput();
        assertEquals("Print Invalid SHA-1", MessageUtil.getMsg("error.sha1.invalid", "invalid_sha"), output);
    }

    @Test
    public void testInvalidShaForE() {
        String[] args = {"catfile", "-e", "invalid_sha"};
        App.test(args);
        String output = watcher.getOutput();
        assertEquals("Print Invalid SHA-1", MessageUtil.getMsg("error.sha1.invalid", "invalid_sha"), output);
    }

    @Test
    public void testInvalidShaForS() {
        String[] args = {"catfile", "-s", "invalid_sha"};
        App.test(args);
        String output = watcher.getOutput();
        assertEquals("Print Invalid SHA-1", MessageUtil.getMsg("error.sha1.invalid", "invalid_sha"), output);
    }

    @Test
    public void testImproperArgs2() {
        String[] args = {"catfile", "-t", "invalid_sha"};
        App.test(args);
        String output = watcher.getOutput();
        assertEquals("Print Invalid SHA-1", MessageUtil.getMsg("error.sha1.invalid", "invalid_sha"), output);
    }

    @Test
    public void testImproperArgs3() {
        String[] args = {"catfile", "-p", "invalid_sha", "extra_arg"};
        App.test(args);
        String output = watcher.getOutput();
        assertEquals("Print Invalid SHA-1", MessageUtil.getMsg("error.sha1.invalid", "invalid_sha"), output);
    }
}
