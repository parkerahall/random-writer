package random_writer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

// Represents a probability distribution among English words used in documents
public class RandomFile {
    
    private static final Set<Character> QUOTES = new HashSet<>(Arrays.asList('\'', '\"'));
    
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
    
    public void addWord(String word) {
        String lower = word.toLowerCase();
        String withoutPunc = removePunc(lower);
        probabilities = probabilities.addSingle(withoutPunc);
    }
    
    public static String removePunc(String input) {
        String output = input;
        int length = output.length();
        while (QUOTES.contains(output.charAt(0))) {
            output = output.substring(1, length);
            length--;
        }
        while (QUOTES.contains(output.charAt(length - 1))) {
            output = output.substring(0, length - 1);
            length--;
        }
        return output;
    }
    
    /**
     * creates randomized String equal in word length to input
     * @return String of randomized words separated by whitespace
     */
    public String randomOutput() {
        String output = "";
        for (int i = 0; i < probabilities.getTotal(); i++) {
            output += probabilities.randomSelect() + " ";
        }
        return output;
    }
    
    @Override
    public String toString() {
        return probabilities.toString();
    }
    
    public static void main(String[] args) {
        RandomFile check = new RandomFile("Hello everyone! How is everyone doing?");
        System.out.println(check.randomOutput());
    }
}