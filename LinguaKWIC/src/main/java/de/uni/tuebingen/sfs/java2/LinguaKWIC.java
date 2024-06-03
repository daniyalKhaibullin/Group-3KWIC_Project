/**
 * V1.0 : in this version the program reads a String as input stream and get the sentences, tokens, postags and lemmas of
 * it. for now, it is working only with english. plan for the next version (v1.1) : instead of getting string as input, get a txt file and read it and do all the things
 * on the txt inside the txt file.
 * please when ever you make any changes, add the descriptions here:
 *
 */

package de.uni.tuebingen.sfs.java2;

import java.io.*;
import java.util.Arrays;
import java.util.List;

import opennlp.tools.lemmatizer.LemmatizerME;
import opennlp.tools.lemmatizer.LemmatizerModel;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;

import java.util.ArrayList;

public class LinguaKWIC {

    private String inputFileToString;// input file => String

    private String text;
    private String[] sentences;
    private List<List<String>> tokens;
    private List<List<String>> posTags;
    private List<List<String>> lemmas;


    /**
     * Default constructor
     */
    public LinguaKWIC() {
        process();
    }
    /**
     * Create a CorpusBuilder which generates POS tags and Lemmas for text.
     *
     * @param text The text which should be annotated.
     */
    public LinguaKWIC(String text) {
        this.text = text;
        process();
    }

    /**
     * Returns the text of this CorpusBuilder
     *
     * @return The text of this CorpusBuilder
     */
    public String getText() {
        return this.text;
    }

    /**
     * Return an array with the sentences of the CorpusBuilder
     *
     * @return An array with the sentences of the CorpusBuildr
     */
    public String[] getSentences() {
        return this.sentences;
    }

    /**
     * Return a List of List with the tokens/words of the text of CorpusBuilder. The
     * first list holds the words of the
     * first sentence, the second list holds the words of the second sentence and so
     * on.
     *
     * @return A List of List the tokens/words of the text of the CorpusBuilder.
     */
    public List<List<String>> getTokens() {
        return tokens;
    }

    /**
     * Return a List of List with the POS tags of the text of CorpusBuilder. The
     * first list holds the POS tags of the
     * first sentence, the second list holds the POS tags of the second sentence and
     * so on.
     *
     * @return A List of List with the POS tags of the text of CorpusBuilder.
     */
    public List<List<String>> getPosTags() {
        return posTags;
    }

    /**
     * Return a List of List with the Lemmas of the text of CorpusBuilder. The first
     * list holds the lemmas of the
     * first sentence, the second list holds the Lemmas of the second sentence and
     * so on.
     *
     * @return A List of List with the Lemmas of the text of CorpusBuilder.
     */
    public List<List<String>> getLemmas() {
        return lemmas;
    }

    /**
     * in this method, we extract the sentences, tokens and postagss and lemmas from
     * the input string
     */
    private void process() {
        // we open the .bin files with try with resources
        try (InputStream sentenceModelIn = new FileInputStream("en-sent.bin");
                InputStream tokenModelIn = new FileInputStream("en-token.bin");
                InputStream posModelIn = new FileInputStream("en-pos-maxent.bin");
                InputStream lemmaModelIn = new FileInputStream("en-lemmatizer.bin")) {

            // sentence
            SentenceModel sentModel = new SentenceModel(sentenceModelIn);
            SentenceDetectorME sentenceDetector = new SentenceDetectorME(sentModel);
            this.sentences = sentenceDetector.sentDetect(getText());

            // tokens
            TokenizerModel tokenModel = new TokenizerModel(tokenModelIn);
            Tokenizer tokenizer = new TokenizerME(tokenModel);

            // pos
            POSModel posModel = new POSModel(posModelIn);
            POSTaggerME posTagger = new POSTaggerME(posModel);

            // lemma
            LemmatizerModel lemmModel = new LemmatizerModel(lemmaModelIn);
            LemmatizerME lemmatizer = new LemmatizerME(lemmModel);

            this.tokens = new ArrayList<>();
            this.posTags = new ArrayList<>();
            this.lemmas = new ArrayList<>();

            for (String sentence : getSentences()) {
                // Tokenize sentence
                String[] sentenceTokens = tokenizer.tokenize(sentence);
                this.tokens.add(Arrays.asList(sentenceTokens));
                // posTag on tokens
                String[] tags = posTagger.tag(sentenceTokens);
                this.posTags.add(Arrays.asList(tags));
                // Lemmatize tokens
                String[] lemmasArray = lemmatizer.lemmatize(sentenceTokens, tags);
                this.lemmas.add(Arrays.asList(lemmasArray));

            }

        } catch (Exception e) {
            // we should chenge this part later but for now it is ok
            e.printStackTrace();
        }
    }

    /**
     * convert a file to an arrayList
     * 
     * @param filePath
     */
    public void readFile(String filePath) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(filePath));
            String line;
            while ((line = reader.readLine()) != null) {
                inputFileToString += line + " ";
                text = inputFileToString;
            }
        } catch (IOException e) {
            e.getStackTrace();
        }
    }

    //just testing whether it works or not, feel free to delete this part
    public static void main(String[] args) {
        LinguaKWIC linguaKWIC = new LinguaKWIC();
        linguaKWIC.readFile("C://Users//tuebi//lab0-ISaySalmonYouSayYes//Group-3//LinguaKWIC//aRandomText.txt");
        System.out.println(linguaKWIC.getText());
    }

}