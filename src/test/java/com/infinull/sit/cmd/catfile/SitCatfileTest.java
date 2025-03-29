package com.infinull.sit.cmd.catfile;

import com.infinull.sit.App;
import com.infinull.sit.SitTestWatcher;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SitCatfileTest {

    @Rule
    public SitTestWatcher watcher = new SitTestWatcher();
    private final String testObjectSha = "6ac6bb1e165002f2c9cb1a951bd8ad2032f78a44";

    @Test
    public void testPrintObject() {
        String[] args = {"catfile", "-p", testObjectSha};
        App.test(args);
        String output = watcher.getOutput();
        assertEquals("Print Test Objects Content", "This is a test object", output);
    }

    @Test
    public void testPrintSize() {
        String[] args = {"catfile", "-s", testObjectSha};
        App.test(args);
        String output = watcher.getOutput();
        assertEquals("Print Test Objects Content", "21", output);
    }

    @Test
    public void testPrintType() {
        String[] args = {"catfile", "-t", testObjectSha};
        App.test(args);
        String output = watcher.getOutput();
        assertEquals("Print Test Objects Content", "blob", output);
    }

    @Test
    public void testCheckExistence() {
        String[] args = {"catfile", "-e", testObjectSha};
        App.test(args);
        String output = watcher.getOutput();
        assertEquals("Print Test Objects Content", "", output);
    }
}
