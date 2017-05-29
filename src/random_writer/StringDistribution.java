package random_writer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

// Represents a probability distribution among English words used in documents
public class StringDistribution implements Distribution<String> {
    
    private final Map<String, Integer> probabilities = new HashMap<>();
    private final int total;
    
    /*
     * Rep invariant:
     *      none of the keys in probabilities contain whitespace
     *      values in probabilities sum to total
     */
    
    public StringDistribution() {
        total = 0;
    }
    
    /**
     * create a new probability distribution
     * @param entries list of String outcomes in sample space
     */
    public StringDistribution(List<String> entries) {
        total = entries.size();
        for (String outcome: entries) {
            int currentValue = probabilities.getOrDefault(entries, 0);
            probabilities.put(outcome, currentValue + 1);
        }
    }
    
    /**
     * create a new probability distribution
     * @param entries list of String outcomes in sample space
     */
    public StringDistribution(String[] entries) {
        total = entries.length;
        for (String outcome: entries) {
            int currentValue = probabilities.getOrDefault(entries, 0);
            probabilities.put(outcome, currentValue + 1);
        }
    }
    
    /**
     * create a new probability distribution
     * @param results mapping of elements to the number of times they have occurred
     */
    public StringDistribution(Map<String, Integer> results) {
        int totalResults = 0;
        for (String key: results.keySet()) {
            int outcomes = results.get(key);
            totalResults += outcomes;
            probabilities.put(key, outcomes);
        }
        total = totalResults;
    }
    
    /**
     * create a new probability distribution
     * @param frequencies mapping of elements to their frequencies of occurrence
     * @param total number of observations
     * @throws IllegalArgumentException if frequency values do not sum to 1
     */
    public StringDistribution(Map<String, Double> frequencies, int total) throws IllegalArgumentException {
        int freqTotal = 0;
        this.total = total;
        for (String key: frequencies.keySet()) {
            double value = frequencies.get(key);
            int count = (int)Math.round(value * total);
            probabilities.put(key, count);
            freqTotal += value;
        }
        if (freqTotal != 1) {
            throw new IllegalArgumentException("Total probability of sample space does not sum to 1.");
        }
    }

    @Override
    public double getProbability(String element) {
        return getCount(element) / (double)total;
    }

    @Override
    public StringDistribution combine(Distribution<String> that) {
        Map<String, Integer> newFreqMap = new HashMap<>();
        Set<String> allOutcomes = this.sampleSpace();
        allOutcomes.addAll(that.sampleSpace());
        for (String key: allOutcomes) {
            int thisCount = this.getCount(key);
            int thatCount = that.getCount(key);
            newFreqMap.put(key, thisCount + thatCount);
        }
        return new StringDistribution(newFreqMap);
    }

    @Override
    public StringDistribution addSingle(String result) {
        Map<String, Integer> newCountMap = new HashMap<>();
        for (String key: this.sampleSpace()) {
            int count = this.getCount(key);
            newCountMap.put(key, count);
        }
        newCountMap.put(result, newCountMap.getOrDefault(result, 0) + 1);
        return new StringDistribution(newCountMap);
    }

    @Override
    public StringDistribution remove(String outcome) {
        Map<String, Integer> newDist = new HashMap<>();
        for (String key: this.sampleSpace()) {
            if (!key.equals(outcome)) {
                Integer count = this.getCount(key);
                newDist.put(key, count);
            }
        }
        return new StringDistribution(newDist);
    }

    @Override
    public StringDistribution joint(Distribution<String> that) {
        Map<String, Double> newKeys = new HashMap<>();
        for (String thisKey: this.sampleSpace()) {
            for (String thatKey: that.sampleSpace()) {
                String newKey = thisKey + " " + thatKey;
                Double newValue = this.getProbability(thisKey) * that.getProbability(thatKey);
                newKeys.put(newKey, newValue);
            }
        }
        int newTotal = this.getTotal() + that.getTotal();
        return new StringDistribution(newKeys, newTotal);
    }

    @Override
    public int getTotal() {
        return total;
    }

    @Override
    public Set<String> sampleSpace() {
        return probabilities.keySet();
    }
    
    @Override
    public String toString() {
        return probabilities.toString();
    }

    /**
     * @return "well that's a problem" if an error has occurred
     */
    @Override
    public String randomSelect() {
        double random = Math.random() * this.getTotal();
        Set<String> samples = this.sampleSpace();
        List<String> orderedSamples = new ArrayList<>();
        for (String elt: samples) {
            orderedSamples.add(elt);
        }
        Collections.sort(orderedSamples);
        String output = "well that's a problem";
        for (String nextElt: orderedSamples) {
            int nextValue = this.getCount(nextElt);
            random -= nextValue;
            if (random <= 0) {
                output = nextElt;
                break;
            }
        }
        return output;
    }

    @Override
    public int getCount(String element) {
        return probabilities.getOrDefault(element, 0);
    }
}
