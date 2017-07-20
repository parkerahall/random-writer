package random_writer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/*
Represents a probability distribution among English words used in documents
Words are stored with trailing punctuation other than quotation marks
*/

public class RandomFile {
    
    private static final int OFFSET = 2;
    
    private static final Set<Character> QUOTES = new HashSet<>(Arrays.asList('\'', '\"'));
    private static final Set<Character> PUNCTUATION = new HashSet<>(Arrays.asList('.', '?', '!'));
    
    private StringDistribution probabilities = new StringDistribution();
    
    public RandomFile(File input) throws FileNotFoundException {
        BufferedReader reader = new BufferedReader(new FileReader(input));
        try {
            String line = reader.readLine();
            while (line != null) {
                String[] words = line.split("\\s");
                for (String word: words) {
                    addWord(word);
                }
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
    
    public RandomFile(String input) {
        String[] words = input.split("\\s");
        for (String word: words) {
            addWord(word);
        }
    }
    
    /**
     * creates new RandomFile from probability distribution
     * @param dist distribution of words
     */
    public RandomFile(StringDistribution dist) {
        probabilities = dist;
    }
    
    /**
     * Combine two RandomFile distributions
     * @param that RandomFile to add to this
     * @return new RandomFile, the combination of this and that
     */
    public RandomFile combine(RandomFile that) {
        StringDistribution newDist = this.probabilities.combine(that.probabilities);
        return new RandomFile(newDist);
    }
    
    /**
     * Add a single word to the distribution
     * @param word new instance of word to be added
     */
    public void addWord(String word) {
        String lower = word.toLowerCase();
        String withoutPunc = removePunc(lower);
        probabilities = probabilities.addSingle(withoutPunc);
    }
    
    /**
     * Strips input of quotation marks
     * @param input English word to be modified
     * @return new String that is input without leading or trailing quotation marks
     */
    public static String removePunc(String input) {
        int length = input.length();
        
        int start = 0;
        while (QUOTES.contains(input.charAt(start))) {
            start++;
        }
        
        int end = length - 1;
        while (QUOTES.contains(input.charAt(end))) {
            end--;
        }
        return input.substring(start, end + 1);
    }
    
    /**
     * creates randomized String with numWords words
     * @param numWords number of words in output String
     * @return String of randomized words separated by whitespace
     */
    public String randomOutput(int numWords) {
        String output = "";
        for (int i = 0; i < numWords; i++) {
            String newWord = probabilities.randomSelect();
            // if start of new sentence, capitalize next word
            if (output.length() == 0 || PUNCTUATION.contains(output.charAt(output.length() - OFFSET))) {
                newWord = Character.toUpperCase(newWord.charAt(0)) + newWord.substring(1);
            }
            output += newWord + " ";
        }
        return output;
    }
    
    @Override
    public String toString() {
        return probabilities.toString();
    }
    
    public static void main(String[] args) {
        try {
            RandomFile check = new RandomFile(new File("src/random_writer/testFile2"));
            System.out.println(check.randomOutput(20));
        } catch(FileNotFoundException fnfe) {
            System.err.println("You done messed up now A-A-ron");
        }
    }
}