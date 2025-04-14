package com.infinull.sit.commands.view;

import com.infinull.sit.App;
import com.infinull.sit.SitTestWatcher;
import com.infinull.sit.message.MessageUtil;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SitViewTreeTest {

    @Rule
    public SitTestWatcher watcher = new SitTestWatcher();
    private final String testTreeSha = "370ee4582d8c3684707241e851e7447155896535";
    private final String testNonExistingTreeSha = "370ef4582d8c3684707241e851e7447155896535";
    private final String testTreeContent = "100644 blob 13c946e5266bcc18efe53525e93dca7f4c15b280\ttestfile1.txt\n100644 blob 781df67aa7d23269bfedc11465ddb1a791b6ee3d\ttestfile2.txt";
    private final String testTreeEntireContent = "tree 82\n" + testTreeContent;

    // Existing Sha
    @Test
    public void testPrintObject() {
        String[] args = {"view", testTreeSha};
        App.test(args);
        String output = watcher.getOutput();
        assertEquals("Print test tree object's entire content", testTreeEntireContent, output);
    }

    @Test
    public void testPrintContent() {
        String[] args = {"view", "-p", testTreeSha};
        App.test(args);
        String output = watcher.getOutput();
        assertEquals("Print test tree object's content", testTreeContent, output);
    }

    @Test
    public void testPrintSize() {
        String[] args = {"view", "-s", testTreeSha};
        App.test(args);
        String output = watcher.getOutput();
        assertEquals("Print test tree object's size", "82", output);
    }

    @Test
    public void testPrintType() {
        String[] args = {"view", "-t", testTreeSha};
        App.test(args);
        String output = watcher.getOutput();
        assertEquals("Print test tree object's type", "tree", output);
    }

    @Test
    public void testCheckExistence() {
        String[] args = {"view", "-e", testTreeSha};
        App.test(args);
        String output = watcher.getOutput();
        assertEquals("Check test tree object's existence", "", output);
    }

    // Non Existing Sha
    @Test
    public void testPrintObjectForNonExistingSha() {
        String[] args = {"view", testNonExistingTreeSha};
        App.test(args);
        String output = watcher.getOutput();
        assertEquals("Print object does not exist", MessageUtil.getMsg("error.object.not_exist", testNonExistingTreeSha), output);
    }

    @Test
    public void testPrintContentForNonExistingSha() {
        String[] args = {"view", "-p", testNonExistingTreeSha};
        App.test(args);
        String output = watcher.getOutput();
        assertEquals("Print object does not exist", MessageUtil.getMsg("error.object.not_exist", testNonExistingTreeSha), output);
    }

    @Test
    public void testPrintSizeForNonExistingSha() {
        String[] args = {"view", "-s", testNonExistingTreeSha};
        App.test(args);
        String output = watcher.getOutput();
        assertEquals("Print object does not exist", MessageUtil.getMsg("error.object.not_exist", testNonExistingTreeSha), output);
    }

    @Test
    public void testPrintTypeForNonExistingSha() {
        String[] args = {"view", "-t", testNonExistingTreeSha};
        App.test(args);
        String output = watcher.getOutput();
        assertEquals("Print object does not exist", MessageUtil.getMsg("error.object.not_exist", testNonExistingTreeSha), output);
    }

    @Test
    public void testCheckExistenceForNonExistingSha() {
        String[] args = {"view", "-e", testNonExistingTreeSha};
        App.test(args);
        String output = watcher.getOutput();
        assertEquals("Just Return with 1 status code", "", output);
    }
}
