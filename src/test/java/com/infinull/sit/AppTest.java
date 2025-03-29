package com.infinull.sit;

import com.infinull.sit.message.MessageUtil;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AppTest {

    @Rule
    public SitTestWatcher watcher = new SitTestWatcher();

    @Test
    public void testShowsUsage() {
        String[] args = {};
        App.test(args);
        String output = watcher.getOutput();
        assertEquals("Show usage information", MessageUtil.getMsg("usage.sit"), output);
    }

    @Test
    public void testUnknownCommand() {
        String[] args = {"screwit"};
        App.test(args);
        String output = watcher.getOutput();
        assertEquals("Show command unknown error", MessageUtil.getMsg("error.command.unknown", args[0]), output);
    }
}