package com.infinull.sit;

import org.junit.ComparisonFailure;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class SitTestWatcher extends TestWatcher {

    private final PrintStream originalOut = System.out;
    private ByteArrayOutputStream testOut;

    @Override
    protected void starting(Description description) {
        testOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(testOut));
    }

    @Override
    protected void finished(Description description) {
        System.setOut(originalOut);
    }

    @Override
    protected void succeeded(Description description) {
        originalOut.printf("%-32s ✅%n", "[" + description.getMethodName() + "]");
    }

    @Override
    protected void failed(Throwable e, Description description) {
        originalOut.printf("%-32s ❌%n", "[" + description.getMethodName() + "]");
        if (e instanceof ComparisonFailure) {
            ComparisonFailure failure = (ComparisonFailure) e;
            originalOut.println("\texpected output : " + failure.getExpected());
            originalOut.println("\toutput          : " + failure.getActual());
        } else {
            originalOut.println("\terror           : " + e.getMessage());
        }
    }

    public String getOutput() {
        return testOut.toString().trim();
    }
}