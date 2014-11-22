package pl.izertp.knowledgeproduction.core;

import java.util.HashSet;
import java.util.Set;

/**
 * A helper class, which counts all the necessary simulation statistics.
 * 
 * @author Piotr Izert
 */
public class SimulationStatistics {

    /**
     * Conuts the sum of all the elements in the simulation.
     * 
     * @param agents array of agents
     * @return sum of all elements posessed by the agents
     */
    public static int sumOfElements(Agent[] agents) {
        int sum = 0;
        for (Agent a : agents) {
            sum += a.getKnowledgeTotalCount();
        }
        return sum;
    }

    /**
     * Counts the number of distinct knowledge elements in the simulation.
     * 
     * @param agents array of agents
     * @return number of distinct knowledge elements
     */
    public static int numberOfElements(Agent[] agents) {
        Set<Integer> elements = new HashSet<Integer>();
        for (Agent a : agents) {
            for (int i = 0; i < a.getKnowledgeSize(); i++) {
                if (a.hasKnowledgeElement(i)) {
                    elements.add(i);
                }
            }
        }
        return elements.size();
    }
    
    /**
     * Returns the distribution of knowledge elements amongst the agents.
     * 
     * @param agents array of agents
     * @return array of numbers of knowledge element occurrences indexed by knowledge elements indices
     */
    public static int[] elementDistribution(Agent[] agents) {
        int maxSize = 0;
        for (Agent a : agents) {
            if (a.getKnowledgeSize() > maxSize) {
                maxSize = a.getKnowledgeSize();
            }
        }

        int[] distribution = new int[maxSize];

        for (Agent a : agents) {
            for (int i = 0; i < a.getKnowledgeSize(); i++) {
                if (a.hasKnowledgeElement(i)) {
                    distribution[i]++;
                }
            }
        }
        return distribution;
    }

}
