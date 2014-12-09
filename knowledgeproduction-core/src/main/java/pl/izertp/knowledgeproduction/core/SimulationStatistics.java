package pl.izertp.knowledgeproduction.core;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * A helper class, which counts all the necessary simulation statistics.
 * 
 * @author Piotr Izert
 */
public class SimulationStatistics {

    private static final String OUTPUT_DIRECTORY = "output";

    private static final String SUM_OF_ALL_ELEMENTS_FILENAME = "sumofallelements.txt";

    private static final String NUMBER_OF_ELEMENTS_FILENAME = "numberofelements.txt";

    private static final String DISTRIBUTION_FILENAME = "distribution.txt";

    private static final String DEPTH_SUM_FILENAME = "depthsum.txt";

    private static final String SUMS_OF_EACH_ELEMENT_FILENAME = "sumsofeachelement.txt";

    private static BufferedWriter sumAllWriter;

    private static BufferedWriter numberWriter;

    private static BufferedWriter distributionWriter;

    private static BufferedWriter depthWriter;

    private static BufferedWriter sumEachWriter;

    /**
     * Counts the sum of all the elements in the simulation.
     * 
     * @param agents array of agents
     * @return sum of all elements possessed by the agents
     */
    public static int sumOfElements(Agent[] agents) {
        int sum = 0;
        for (Agent a : agents) {
            sum += a.getKnowledgeTotalCount();
        }
        return sum;
    }

    /**
     * Writes the sum of the elements to a file.
     * 
     * @param agents array of agents
     * @return sum of all elements possessed by the agents
     */
    public static int writeSumOfElements(Agent[] agents) {
        int sum = SimulationStatistics.sumOfElements(agents);
        try {
            sumAllWriter.write(String.format("%d%n", sum));
        } catch (IOException e) {
            System.out.println("Problem with writing sum of elements to a file");
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
     * Writes the number of distinct knowledge elements to a file.
     * 
     * @param agents array of agents
     * @return number of distinct knowledge elements
     */
    public static int writeNumberOfElements(Agent[] agents) {
        int number = SimulationStatistics.numberOfElements(agents);
        try {
            numberWriter.write(String.format("%d%n", number));
        } catch (IOException e) {
            System.out.println("Problem with writing number of elements to a file");
        }
        return number;
    }

    /**
     * Returns the distribution of knowledge total count of all the agents.
     * Can be interpreted as a histogram of total elements count.
     * 
     * @param agents array of agents
     * @return array of counts of the knowledge sum
     */
    public static int[] distributionOfKnowledgeCount(Agent[] agents) {
        int maxSize = knowledgeMaxSize(agents);
        int[] distribution = new int[maxSize + 1];
        for (Agent a : agents) {
            distribution[a.getKnowledgeTotalCount()]++;
        }
        return distribution;
    }

    /**
     * Writes the distribution of knowledge total count of all the agents.
     * 
     * @param agents array of agents
     * @return array of counts of the knowledge sum
     */
    public static int[] writeDistributionOfKnowledgeCount(Agent[] agents) {
        int[] distribution = distributionOfKnowledgeCount(agents);

        try {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < distribution.length; i++) {
                sb.append(String.format("%d\t", distribution[i]));
            }
            sb.append(String.format("%n"));
            distributionWriter.write(sb.toString());
        } catch (IOException e) {
            System.out.println("Problem with writing distribution of elements to a file");
        }
        return distribution;
    }

    /**
     * Counts the sum of elements grouped by element depth.
     * 
     * @param agents array of agents
     * @return sum of elements grouped by element depth
     */
    public static int[] depthSum(Agent[] agents) {
        int maxDepth = 0;
        for (Agent a : agents) {
            if (a.getKnowledgeStructure().getMaxDepth() > maxDepth) {
                maxDepth = a.getKnowledgeStructure().getMaxDepth();
            }
        }
        int[] depthSum = new int[maxDepth + 1];
        for (Agent a : agents) {
            for (int i = 0; i < a.getKnowledgeSize(); i++) {
                if (a.hasKnowledgeElement(i)) {
                    KnowledgeStructure structure = a.getKnowledgeStructure();
                    depthSum[structure.getElementDepth(i)]++;
                }
            }
        }
        return depthSum;
    }

    /**
     * Writes the sum of elements grouped by element depth.
     * 
     * @param agents array of agents
     * @return sum of elements grouped by element depth
     */
    public static int[] writeDepthSum(Agent[] agents) {
        int[] depthSums = SimulationStatistics.depthSum(agents);
        try {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < depthSums.length; i++) {
                sb.append(String.format("%d\t", depthSums[i]));
            }
            sb.append(String.format("%n"));
            depthWriter.write(sb.toString());
        } catch (IOException e) {
            System.out.println("Problem with writing sums of elements by depth to a file");
        }
        return depthSums;
    }
    
    public static int[] depthTotal(Agent[] agents) {
        int maxDepth = 0;
        for (Agent a : agents) {
            if (a.getKnowledgeStructure().getMaxDepth() > maxDepth) {
                maxDepth = a.getKnowledgeStructure().getMaxDepth();
            }
        }
        int[] depthTotal = new int[maxDepth + 1];
        for(Agent a : agents) {
            KnowledgeStructure structure = a.getKnowledgeStructure();
            for(int i=0; i<structure.getSize(); i++) {
                depthTotal[structure.getElementDepth(i)]++;
            }
        }        
        return depthTotal;
    }
    
    public static int[] writeDepthTotal(Agent[] agents) {
        int[] depthTotal = depthTotal(agents);
        try {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < depthTotal.length; i++) {
                sb.append(String.format("%d\t", depthTotal[i]));
            }
            sb.append(String.format("%n"));
            depthWriter.write(sb.toString());
        } catch (IOException e) {
            System.out.println("Problem with writing total elements by depth to a file");
        }
        return depthTotal;        
    }

    /**
     * Returns the sum of each knowledge element.
     * 
     * @param agents array of agents
     * @return array of numbers of knowledge element occurrences indexed by knowledge element number
     */
    public static int[] sumOfEachElement(Agent[] agents) {
        int maxSize = knowledgeMaxSize(agents);

        int[] sums = new int[maxSize];

        for (Agent a : agents) {
            for (int i = 0; i < a.getKnowledgeSize(); i++) {
                if (a.hasKnowledgeElement(i)) {
                    sums[i]++;
                }
            }
        }
        return sums;
    }

    /**
     * Writes the sum of each knowledge element to a file.
     * 
     * @param agents array of agents
     * @return array of numbers of knowledge element occurrences indexed by knowledge elements indices
     */
    public static int[] writeSumOfEachElement(Agent[] agents) {
        int[] sumOfEachElement = SimulationStatistics.sumOfEachElement(agents);
        try {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < sumOfEachElement.length; i++) {
                sb.append(String.format("%d\t", sumOfEachElement[i]));
            }
            sb.append(String.format("%n"));
            sumEachWriter.write(sb.toString());
        } catch (IOException e) {
            System.out.println("Problem with writing sum of each element to a file");
        }
        return sumOfEachElement;
    }

    /**
     * Opens all the output files and writes their initial content.
     * 
     * @throws IOException when an exception during creating fileWriters occurs
     */
    public static void openFiles() throws IOException {
        new File(OUTPUT_DIRECTORY).mkdirs();
        File sumFile = new File(OUTPUT_DIRECTORY + "/" + SUM_OF_ALL_ELEMENTS_FILENAME);
        File numberFile = new File(OUTPUT_DIRECTORY + "/" + NUMBER_OF_ELEMENTS_FILENAME);
        File distributionFile = new File(OUTPUT_DIRECTORY + "/" + DISTRIBUTION_FILENAME);
        File depthFile = new File(OUTPUT_DIRECTORY + "/" + DEPTH_SUM_FILENAME);
        File sumOfEachFile = new File(OUTPUT_DIRECTORY + "/" + SUMS_OF_EACH_ELEMENT_FILENAME);

        sumAllWriter = new BufferedWriter(new FileWriter(sumFile));
        numberWriter = new BufferedWriter(new FileWriter(numberFile));
        distributionWriter = new BufferedWriter(new FileWriter(distributionFile));
        depthWriter = new BufferedWriter(new FileWriter(depthFile));
        sumEachWriter = new BufferedWriter(new FileWriter(sumOfEachFile));

        try {
            sumAllWriter.write(String.format("Sum of elements%n"));
            numberWriter.write(String.format("Number of elements%n"));
            distributionWriter.write(String.format("Distribution of elements%n"));
            depthWriter.write(String.format("Sum of elements by depth%n"));
            sumEachWriter.write(String.format("Sums of each element%n"));
        } catch (IOException exc) {
            System.out.println("Problem with writing initial content to a file");
        }
    }

    /**
     * Closes all the output files.
     * IOExceptions are caught and logged.
     */
    public static void closeFiles() {
        try {
            sumAllWriter.close();
            numberWriter.close();
            distributionWriter.close();
            depthWriter.close();
            sumEachWriter.close();
        } catch (IOException exc) {
            System.out.println("Problem with closing output files");
        }
    }

    /**
     * Returns the maximal knowledge set size of all the agents.
     */
    private static int knowledgeMaxSize(Agent[] agents) {
        int maxSize = 0;
        for (Agent a : agents) {
            if (a.getKnowledgeSize() > maxSize) {
                maxSize = a.getKnowledgeSize();
            }
        }
        return maxSize;
    }

}
