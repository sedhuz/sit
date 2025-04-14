package com.infinull.sit.commands.catfile;

import com.infinull.sit.App;
import com.infinull.sit.SitTestWatcher;
import com.infinull.sit.message.MessageUtil;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SitCatfileBlobTest {

    @Rule
    public SitTestWatcher watcher = new SitTestWatcher();
    private final String testBlobSha = "6ac6bb1e165002f2c9cb1a951bd8ad2032f78a44";
    private final String testNonExistingBlobSha = "6ac6bb1e155002f2c9cb1a951bd8ad2032f78a44";

    // Existing Sha
    @Test
    public void testPrintObject() {
        String[] args = {"catfile", "-p", testBlobSha};
        App.test(args);
        String output = watcher.getOutput();
        assertEquals("Print test blob object's content", "This is a test object", output);
    }

    @Test
    public void testPrintSize() {
        String[] args = {"catfile", "-s", testBlobSha};
        App.test(args);
        String output = watcher.getOutput();
        assertEquals("Print test blob object's size", "21", output);
    }

    @Test
    public void testPrintType() {
        String[] args = {"catfile", "-t", testBlobSha};
        App.test(args);
        String output = watcher.getOutput();
        assertEquals("Print test blob object's type", "blob", output);
    }

    @Test
    public void testCheckExistence() {
        String[] args = {"catfile", "-e", testBlobSha};
        App.test(args);
        String output = watcher.getOutput();
        assertEquals("Check test blob object's existence", "", output);
    }

    // Non Existing Sha
    @Test
    public void testPrintObjectForNonExistingSha() {
        String[] args = {"catfile", "-p", testNonExistingBlobSha};
        App.test(args);
        String output = watcher.getOutput();
        assertEquals("Print object does not exist", MessageUtil.getMsg("error.object.not_exist", testNonExistingBlobSha), output);
    }

    @Test
    public void testPrintSizeForNonExistingSha() {
        String[] args = {"catfile", "-s", testNonExistingBlobSha};
        App.test(args);
        String output = watcher.getOutput();
        assertEquals("Print object does not exist", MessageUtil.getMsg("error.object.not_exist", testNonExistingBlobSha), output);
    }

    @Test
    public void testPrintTypeForNonExistingSha() {
        String[] args = {"catfile", "-t", testNonExistingBlobSha};
        App.test(args);
        String output = watcher.getOutput();
        assertEquals("Print object does not exist", MessageUtil.getMsg("error.object.not_exist", testNonExistingBlobSha), output);
    }

    @Test
    public void testCheckExistenceForNonExistingSha() {
        String[] args = {"catfile", "-e", testNonExistingBlobSha};
        App.test(args);
        String output = watcher.getOutput();
        assertEquals("Just Return with 1 status code", "", output);
    }
}