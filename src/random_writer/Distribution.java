package random_writer;

import java.util.Set;

/*
 * Represents an independent probability distribution parameterized by the abstract type <R>
 * Distribution is an immutable type
 */
public interface Distribution<R> {
    
    /**
     * return the probability of the specified element
     * @param element one of the possible outcomes represented by the distribution
     * @return probability associated with element if found in distribution, zero otherwise
     */
    public double getProbability(R element);
    
    /**
     * combines two probability distributions
     * similar to performing two rounds of tests and combining results
     * @param dist valid Distribution instance
     * @return combination of this and dist
     */
    public Distribution<R> combine(Distribution<R> dist);
    
    /**
     * adds a single new outcome observation to the distribution
     * @param result outcome of a single new trial
     * @return new distribution instance with added result
     */
    public Distribution<R> addSingle(R result);
    
    /**
     * removes all instances of outcome from distribution
     * @param outcome possible occurrence in distribution
     * @return new distribution instance without outcome
     */
    public Distribution<R> remove(R outcome);
    
    /**
     * @return total number of observations observed
     */
    public int getTotal();
    
    /**
     * creates a joint probability distribution using the two independent distributions
     * @param dist independent distribution
     * @return new distribution instance
     */
    public Distribution<R> joint(Distribution<R> dist);
    
    /**
     * @return a set of all possible outcomes in sample space
     */
    public Set<R> sampleSpace();
    
    /**
     * selects an outcome from the sample space randomly according to the distribution
     * @return an element from the distribution
     */
    public R randomSelect();
    
    /**
     * @param element outcome found in the distribution
     * @return number of times element has occurred
     */
    public int getCount(R element);
}
