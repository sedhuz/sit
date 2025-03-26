package com.infinull.sit;

import com.infinull.sit.message.MessageUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.assertEquals;

public class AppTest {

    private final PrintStream originalOut = System.out;
    private ByteArrayOutputStream testOut;

    @Before
    public void redirectOutput() {
        // Redirect System.out to testOut before each test
        testOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(testOut));
    }

    @After
    public void restoreOutput() {
        // Restore original System.out after each test
        System.setOut(originalOut);
    }

    @Test
    public void testShowsUsage() {
        String[] args = {};
        App.test(args);
        String output = testOut.toString().trim();
        assertEquals("Show usage information", output, MessageUtil.getMsg("usage.message"));
    }

    @Test
    public void testUnknownCommand() {
        String[] args = {"screwit"};
        App.test(args);
        String output = testOut.toString().trim();
        assertEquals("Show command unknown error", output, MessageUtil.getMsg("error.command.unknown", args[0]));
    }
}
