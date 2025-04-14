package com.infinull.sit.commands.catfile;

import com.infinull.sit.App;
import com.infinull.sit.SitTestWatcher;
import com.infinull.sit.message.MessageUtil;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SitCatfileTreeTest {

    @Rule
    public SitTestWatcher watcher = new SitTestWatcher();
    private final String testTreeSha = "370ee4582d8c3684707241e851e7447155896535";
    private final String testNonExistingTreeSha = "370ef4582d8c3684707241e851e7447155896535";
    private final String testTreeContent = "100644 blob 13c946e5266bcc18efe53525e93dca7f4c15b280\ttestfile1.txt\n100644 blob 781df67aa7d23269bfedc11465ddb1a791b6ee3d\ttestfile2.txt";

    // Existing Sha
    @Test
    public void testPrintObject() {
        String[] args = {"catfile", "-p", testTreeSha};
        App.test(args);
        String output = watcher.getOutput();
        assertEquals("Print test tree object's content", testTreeContent, output);
    }

    @Test
    public void testPrintSize() {
        String[] args = {"catfile", "-s", testTreeSha};
        App.test(args);
        String output = watcher.getOutput();
        assertEquals("Print test tree object's size", "82", output);
    }

    @Test
    public void testPrintType() {
        String[] args = {"catfile", "-t", testTreeSha};
        App.test(args);
        String output = watcher.getOutput();
        assertEquals("Print test tree object's type", "tree", output);
    }

    @Test
    public void testCheckExistence() {
        String[] args = {"catfile", "-e", testTreeSha};
        App.test(args);
        String output = watcher.getOutput();
        assertEquals("Check test tree object's existence", "", output);
    }

    // Non Existing Sha
    @Test
    public void testPrintObjectForNonExistingSha() {
        String[] args = {"catfile", "-p", testNonExistingTreeSha};
        App.test(args);
        String output = watcher.getOutput();
        assertEquals("Print object does not exist", MessageUtil.getMsg("error.object.not_exist", testNonExistingTreeSha), output);
    }

    @Test
    public void testPrintSizeForNonExistingSha() {
        String[] args = {"catfile", "-s", testNonExistingTreeSha};
        App.test(args);
        String output = watcher.getOutput();
        assertEquals("Print object does not exist", MessageUtil.getMsg("error.object.not_exist", testNonExistingTreeSha), output);
    }

    @Test
    public void testPrintTypeForNonExistingSha() {
        String[] args = {"catfile", "-t", testNonExistingTreeSha};
        App.test(args);
        String output = watcher.getOutput();
        assertEquals("Print object does not exist", MessageUtil.getMsg("error.object.not_exist", testNonExistingTreeSha), output);
    }

    @Test
    public void testCheckExistenceForNonExistingSha() {
        String[] args = {"catfile", "-e", testNonExistingTreeSha};
        App.test(args);
        String output = watcher.getOutput();
        assertEquals("Just Return with 1 status code", "", output);
    }
}
