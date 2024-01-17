import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class FileHandlerTest {

    private final static String TEST_DATA_PATH = "data/traffic_data.csv";
    private final static String TEST_OUTPUT_PATH = "data/test_output.txt";
    private final FileHandler fileHandler = new FileHandler();

    @org.junit.jupiter.api.Test
    void readCSV() {
        fileHandler.readCSV(TEST_DATA_PATH);
        assertNotNull(fileHandler.getData(), "The read data should not be null.");
        assertFalse(fileHandler.getData().isEmpty(), "The list of DataModel instances should not be empty.");

    }

    @org.junit.jupiter.api.Test
    void writeToFile() {
        try {
            String contentToWrite = "Test content for writeToFile method.";
            fileHandler.writeToFile(TEST_OUTPUT_PATH, contentToWrite);
            String content = Files.readString(Paths.get(TEST_OUTPUT_PATH));
            assertEquals(contentToWrite, content, "The content read should match the content written.");
        } catch (IOException e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }
    }
}