package com.infinull.sit.commands.hash;

import com.infinull.sit.App;
import com.infinull.sit.SitTestWatcher;
import com.infinull.sit.message.MessageUtil;
import com.infinull.sit.store.FileStore;
import com.infinull.sit.store.ObjectStore;
import com.infinull.sit.util.Sha;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SitHashTest {

    @Rule
    public SitTestWatcher watcher = new SitTestWatcher();
    private final String TEST_FILE_PATH = "src/test/resources/testobject.txt";
    private final String TEST_FOLDER_PATH = "src/test/resources/testfolder";
    private final String TEST_FILE_SHA = "6ac6bb1e165002f2c9cb1a951bd8ad2032f78a44";
    private final String TEST_FOLDER_SHA = "370ee4582d8c3684707241e851e7447155896535";

    @Test
    public void testHashUsage() {
        String[] args = {"hash"};
        App.test(args);
        String output = watcher.getOutput();
        assertEquals("Display usage information", MessageUtil.getMsg("usage.hash"), output);
    }

    @Test
    public void testSampleFileHashMatch() {
        String[] args = {"hash", TEST_FILE_PATH};
        App.test(args);
        String output = watcher.getOutput();
        assertEquals("Display correct hash", MessageUtil.getMsg(TEST_FILE_SHA), output);
    }

    @Test
    public void testWriteSampleFileObject() {
        String[] args = {"hash", "-w", TEST_FILE_PATH};
        App.test(args);
        String output = watcher.getOutput();
        assertEquals("Write and display hash", MessageUtil.getMsg(TEST_FILE_SHA), output);
        assertTrue("Check if the file exists", FileStore.fileExists(TEST_FILE_PATH));
        assertTrue("Check if the object exists", ObjectStore.objectExists(new Sha(TEST_FILE_SHA)));
    }

    @Test
    public void testSampleFolderHashMatch() {
        String[] args = {"hash", TEST_FOLDER_PATH};
        App.test(args);
        String output = watcher.getOutput();
        assertEquals("Display correct hash", MessageUtil.getMsg(TEST_FOLDER_SHA), output);
    }

    @Test
    public void testWriteSampleFolderObject() {
        String[] args = {"hash", "-w", TEST_FOLDER_PATH};
        App.test(args);
        String output = watcher.getOutput();
        assertEquals("Write and display hash", MessageUtil.getMsg(TEST_FOLDER_SHA), output);
        assertTrue("Check if the file exists", FileStore.fileExists(TEST_FOLDER_PATH));
        assertTrue("Check if the object exists", ObjectStore.objectExists(new Sha(TEST_FOLDER_SHA)));
    }
}
