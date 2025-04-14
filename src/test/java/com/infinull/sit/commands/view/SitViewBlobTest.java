package com.infinull.sit.commands.view;

import com.infinull.sit.App;
import com.infinull.sit.SitTestWatcher;
import com.infinull.sit.message.MessageUtil;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SitViewBlobTest {

    @Rule
    public SitTestWatcher watcher = new SitTestWatcher();
    private final String testBlobSha = "6ac6bb1e165002f2c9cb1a951bd8ad2032f78a44";
    private final String testNonExistingBlobSha = "6ac6bb1e155002f2c9cb1a951bd8ad2032f78a44";
    private final String testBlobContent = "This is a test object";
    private final String testBlobEntireContent = "blob 21\n" + testBlobContent;

    // Existing Sha
    @Test
    public void testPrintObject() {
        String[] args = {"view", testBlobSha};
        App.test(args);
        String output = watcher.getOutput();
        assertEquals("Print test blob object's entire content", testBlobEntireContent, output);
    }

    @Test
    public void testPrintContent() {
        String[] args = {"view", "-p", testBlobSha};
        App.test(args);
        String output = watcher.getOutput();
        assertEquals("Print test blob object's content", testBlobContent, output);
    }

    @Test
    public void testPrintSize() {
        String[] args = {"view", "-s", testBlobSha};
        App.test(args);
        String output = watcher.getOutput();
        assertEquals("Print test blob object's size", "21", output);
    }

    @Test
    public void testPrintType() {
        String[] args = {"view", "-t", testBlobSha};
        App.test(args);
        String output = watcher.getOutput();
        assertEquals("Print test blob object's type", "blob", output);
    }

    @Test
    public void testCheckExistence() {
        String[] args = {"view", "-e", testBlobSha};
        App.test(args);
        String output = watcher.getOutput();
        assertEquals("Check test blob object's existence", "", output);
    }

    // Non Existing Sha
    @Test
    public void testPrintObjectForNonExistingSha() {
        String[] args = {"view", testNonExistingBlobSha};
        App.test(args);
        String output = watcher.getOutput();
        assertEquals("Print object does not exist", MessageUtil.getMsg("error.object.not_exist", testNonExistingBlobSha), output);
    }

    @Test
    public void testPrintContentForNonExistingSha() {
        String[] args = {"view", "-p", testNonExistingBlobSha};
        App.test(args);
        String output = watcher.getOutput();
        assertEquals("Print object does not exist", MessageUtil.getMsg("error.object.not_exist", testNonExistingBlobSha), output);
    }

    @Test
    public void testPrintSizeForNonExistingSha() {
        String[] args = {"view", "-s", testNonExistingBlobSha};
        App.test(args);
        String output = watcher.getOutput();
        assertEquals("Print object does not exist", MessageUtil.getMsg("error.object.not_exist", testNonExistingBlobSha), output);
    }

    @Test
    public void testPrintTypeForNonExistingSha() {
        String[] args = {"view", "-t", testNonExistingBlobSha};
        App.test(args);
        String output = watcher.getOutput();
        assertEquals("Print object does not exist", MessageUtil.getMsg("error.object.not_exist", testNonExistingBlobSha), output);
    }

    @Test
    public void testCheckExistenceForNonExistingSha() {
        String[] args = {"view", "-e", testNonExistingBlobSha};
        App.test(args);
        String output = watcher.getOutput();
        assertEquals("Just Return with 1 status code", "", output);
    }
}