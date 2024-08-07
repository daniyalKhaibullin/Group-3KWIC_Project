import de.uni.tuebingen.sfs.java2.LinguaKWIC;
import de.uni.tuebingen.sfs.java2.XMLWriter;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.AnnotatedArrayType;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class LinguaKWICTest {

    // Test case for processing text from a file
    @Test
    public void testFileProcessing() throws Exception {
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
        assertEquals("de", linguaKWIC.getLang()); // Assuming the test file is in German

        // Test XMLWriter with the created LinguaKWIC object
    }

    // Test case for processing text from a URL
    @Test
    public void testURLProcessing() {
        String url = "https://en.wikipedia.org/wiki/Computer"; // Provide a valid Wikipedia URL
        File afile = new File("aRandomText.txt");
        LinguaKWIC linguaKWIC;
        LinguaKWIC linguaKWIC2;

        try {
            linguaKWIC = new LinguaKWIC(url);
            linguaKWIC2= new LinguaKWIC(afile);

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

            List<LinguaKWIC> list = new ArrayList<>();
            list.add(linguaKWIC);
            List<LinguaKWIC> list2 = new ArrayList<>();
            list2.add(linguaKWIC2);
            // Test XMLWriter with the created LinguaKWIC object
        } catch (IOException e) {
            fail("Exception thrown while testing URL processing: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
