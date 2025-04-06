package com.infinull.sit.commands.catfile;

import com.infinull.sit.App;
import com.infinull.sit.SitTestWatcher;
import com.infinull.sit.message.MessageUtil;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SitCatfileTest {

    @Rule
    public SitTestWatcher watcher = new SitTestWatcher();
    private final String testObjectSha = "6ac6bb1e165002f2c9cb1a951bd8ad2032f78a44";
    private final String testNonExistingObjectSha = "6ac6bb1e155002f2c9cb1a951bd8ad2032f78a44";

    @Test
    public void testCatfileUsage() {
        String[] args = {"catfile"};
        App.test(args);
        String output = watcher.getOutput();
        assertEquals("Display usage information", MessageUtil.getMsg("usage.catfile"), output);
    }

    // Existing Sha
    @Test
    public void testPrintObject() {
        String[] args = {"catfile", "-p", testObjectSha};
        App.test(args);
        String output = watcher.getOutput();
        assertEquals("Print test object's content", "This is a test object", output);
    }

    @Test
    public void testPrintSize() {
        String[] args = {"catfile", "-s", testObjectSha};
        App.test(args);
        String output = watcher.getOutput();
        assertEquals("Print test object's size", "21", output);
    }

    @Test
    public void testPrintType() {
        String[] args = {"catfile", "-t", testObjectSha};
        App.test(args);
        String output = watcher.getOutput();
        assertEquals("Print test object's type", "blob", output);
    }

    @Test
    public void testCheckExistence() {
        String[] args = {"catfile", "-e", testObjectSha};
        App.test(args);
        String output = watcher.getOutput();
        assertEquals("Check test object's existence", "", output);
    }

    // Non Existing Sha
    @Test
    public void testPrintObjectForNonExistingSha() {
        String[] args = {"catfile", "-p", testNonExistingObjectSha};
        App.test(args);
        String output = watcher.getOutput();
        assertEquals("Print object does not exist", MessageUtil.getMsg("error.object.not_exist", testNonExistingObjectSha), output);
    }

    @Test
    public void testPrintSizeForNonExistingSha() {
        String[] args = {"catfile", "-s", testNonExistingObjectSha};
        App.test(args);
        String output = watcher.getOutput();
        assertEquals("Print object does not exist", MessageUtil.getMsg("error.object.not_exist", testNonExistingObjectSha), output);
    }

    @Test
    public void testPrintTypeForNonExistingSha() {
        String[] args = {"catfile", "-t", testNonExistingObjectSha};
        App.test(args);
        String output = watcher.getOutput();
        assertEquals("Print object does not exist", MessageUtil.getMsg("error.object.not_exist", testNonExistingObjectSha), output);
    }

    @Test
    public void testCheckExistenceForNonExistingSha() {
        String[] args = {"catfile", "-e", testNonExistingObjectSha};
        App.test(args);
        String output = watcher.getOutput();
        assertEquals("Just Return with 1 status code", "", output);
    }

    // Improper Args Cases
    @Test
    public void testPrintObjectForInvalidSha() {
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