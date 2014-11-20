package pl.izertp.knowledgeproduction.core;

import java.util.HashSet;
import java.util.Set;

public class SimulationStatistics {

    public static int sumOfElements(Agent[] agents) {
        int sum = 0;
        for (Agent a : agents) {
            sum += a.getKnowledgeTotalCount();
        }
        return sum;
    }

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

    public static int[] elementDistribution(Agent[] agents) {
        int maxSize = 0;
        for (Agent a : agents) {
            if (a.getKnowledgeSize() > maxSize) {
                maxSize = a.getKnowledgeSize();
            }
        }

        int[] distribution = new int[maxSize];

        for (Agent a : agents) {
            for(int i=0; i<a.getKnowledgeSize(); i++) {
                if(a.hasKnowledgeElement(i)) {
                    distribution[i]++;
                }
            }
        }
        return distribution;
    }

}
