package random_writer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * User interface to the random writer system
 * 
 * @author ParkerHall
 *
 */
public class Main {
    
    private static final String EXIT_MESSAGE = "see ya";
    private static final Set<Character> DIGITS = new HashSet<>(Arrays.asList('0', '1', '2', '3', '4', '5', '6', '7', '8', '9'));
    
    /**
     * Read user input and produce random output based on word frequency
     *      if user inputs words, adds words to distribution and outputs random message of same length
     *      if user inputs a number, output random message of that length
     * @param args filenames to be loaded into distribution
     * 
     * system exits if user enters "see ya" or an empty string
     * @throws IOException if there is an error reading the input
     */
    public static void main(String[] args) throws IOException {
        RandomFile generator = new RandomFile(new StringDistribution());
        for (String filename: args) {
            File newFile = new File(filename);
            try {
                generator = generator.combine(new RandomFile(newFile));
            } catch (FileNotFoundException e) {
                System.err.println("Error: " + filename + " not found.");
            }
        }
        
        final BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        
        while (true) {
            System.out.print("> ");
            String input = in.readLine();
            
            if (input.isEmpty() || input.equals(EXIT_MESSAGE)) {
                return;
            }
            
            int numWords;
            if (allDigits(input)) {
                numWords = (int)Double.parseDouble(input);
            } else {
                generator = generator.combine(new RandomFile(input));
                numWords = input.split("\\s").length;
            }
            
            System.out.println(generator.randomOutput(numWords));
        }
    }
    
    public static boolean allDigits(String input) {
        for (int i = 0; i < input.length(); i++) {
            if (!DIGITS.contains(input.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}
