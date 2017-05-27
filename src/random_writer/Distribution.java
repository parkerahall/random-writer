package random_writer;

import java.util.HashMap;
import java.util.Map;

// Represents a probability distribution among English words used in documents
public class Distribution<R> {
    
    private final Map<R, Double> probabilities = new HashMap<>();
    
    public Distribution<R>() {
        
    }
}
