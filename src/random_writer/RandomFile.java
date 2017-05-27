package random_writer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

// Represents a probability distribution among English words used in documents
public class RandomFile {
    
    private static final Set<Character> PUNCTUATION = new HashSet<>(Arrays.asList('\'', '\"', '.', ',', ';', '!', '?', ':'));
    
    private final Distribution probabilities = new Distribution();
    
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
        probabilities.put(withoutPunc, probabilities.getOrDefault(withoutPunc, 0.) + 1);
    }
    
    public static String removePunc(String input) {
        String output = input;
        int length = output.length();
        while (PUNCTUATION.contains(output.charAt(0))) {
            output = output.substring(1, length);
            length--;
        }
        while (PUNCTUATION.contains(output.charAt(length - 1))) {
            output = output.substring(0, length - 1);
            length--;
        }
        return output;
    }
    
    public static void main(String[] args) {
        String check = "\"hello!";
        String without = removePunc(check);
        System.out.println(without);
    }
}