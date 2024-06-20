/**
 * junit test for linguaKWIC
 */

import de.uni.tuebingen.sfs.java2.LinguaKWIC;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class LinguaKWICTest {

    // Test case for processing text from a file
    @Test
    public void testFileProcessing() {
        File file = new File("aRandomText.txt"); // Provide a test file path
        LinguaKWIC linguaKWIC = new LinguaKWIC(file);

        // Assertions
        assertNotNull(linguaKWIC.getSentences());
        assertFalse(linguaKWIC.getSentences().isEmpty());
        assertNotNull(linguaKWIC.getTokens());
        assertFalse(linguaKWIC.getTokens().isEmpty());
        assertNotNull(linguaKWIC.getPosTags());
        assertFalse(linguaKWIC.getPosTags().isEmpty());
        assertNotNull(linguaKWIC.getLemmas());
        assertFalse(linguaKWIC.getLemmas().isEmpty());

        // Example assertion for language detection
        assertEquals("de", linguaKWIC.getLang()); // Assuming the test file is in german
    }

    // Test case for processing text from a URL
    @Test
    public void testURLProcessing() {
        String url = "https://en.wikipedia.org/wiki/Computer"; // Provide a valid Wikipedia URL
        LinguaKWIC linguaKWIC;

        try {
            linguaKWIC = new LinguaKWIC(url);

            // Assertions
            assertNotNull(linguaKWIC.getSentences());
            assertFalse(linguaKWIC.getSentences().isEmpty());
            assertNotNull(linguaKWIC.getTokens());
            assertFalse(linguaKWIC.getTokens().isEmpty());
            assertNotNull(linguaKWIC.getPosTags());
            assertFalse(linguaKWIC.getPosTags().isEmpty());
            assertNotNull(linguaKWIC.getLemmas());
            assertFalse(linguaKWIC.getLemmas().isEmpty());

            // Example assertion for language detection
            assertEquals("en", linguaKWIC.getLang()); // Assuming the Wikipedia page is in English
        } catch (IOException e) {
            fail("Exception thrown while testing URL processing: " + e.getMessage());
        }
    }
}
