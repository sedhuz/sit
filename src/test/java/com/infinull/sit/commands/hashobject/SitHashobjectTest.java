package com.infinull.sit.commands.hashobject;

import com.infinull.sit.App;
import com.infinull.sit.SitTestWatcher;
import com.infinull.sit.message.MessageUtil;
import com.infinull.sit.object.BlobObject;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SitHashobjectTest {

    @Rule
    public SitTestWatcher watcher = new SitTestWatcher();
    private final String TEST_FILE_PATH = "src/test/resources/testobject.txt";
    private final String TEST_OBJECT_SHA = "6ac6bb1e165002f2c9cb1a951bd8ad2032f78a44";

    @Test
    public void testHashobjectUsage() {
        String[] args = {"hashobject"};
        App.test(args);
        String output = watcher.getOutput();
        assertEquals("Display usage information", MessageUtil.getMsg("usage.hashobject"), output);
    }

    @Test
    public void testSampleFileHashMatch() {
        String[] args = {"hashobject", TEST_FILE_PATH};
        App.test(args);
        String output = watcher.getOutput();
        assertEquals("Display correct hash", MessageUtil.getMsg(TEST_OBJECT_SHA), output);
    }

    @Test
    public void testWriteSampleFileObject() {
        String[] args = {"hashobject", "-w", TEST_FILE_PATH};
        App.test(args);
        String output = watcher.getOutput();
        assertEquals("Write and display hash", MessageUtil.getMsg(TEST_OBJECT_SHA), output);
        BlobObject sampleFileBlobObject = new BlobObject(TEST_FILE_PATH);
        assertTrue("Check if the object exists", sampleFileBlobObject.doesObjectExist());
    }
}
