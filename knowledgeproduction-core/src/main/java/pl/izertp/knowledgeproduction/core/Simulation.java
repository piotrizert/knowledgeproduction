package pl.izertp.knowledgeproduction.core;

import java.util.Random;

/**
 * Implements runnable method, which does the simulation.
 * 
 * @author Piotr Izert
 */
public class Simulation implements Runnable {

    private static final int KNOWLEDGE_SIZE = 10;

    private static final int KNOWLEDGE_BASE_SIZE = 4;

    private static final int KNOWLEDGE_CONNECTIONS = 1;

    private static final int NUMBER_OF_AGENTS = 5;

    private static final int NUMBER_OF_CONNECTIONS = 10;

    private static final double KNOWLEDGE_ELEMENT_PROBABILITY = 0.1;

    private static final double PRODUCTION_PROBABILITY = 0.5;

    private static final int NUMBER_OF_ITERATIONS = 10;

    /**
     * Structure of the knowledge for all the agents.
     */
    private KnowledgeStructure knowledgeStructure;

    /**
     * Network of connections between all the agents.
     */
    private AgentStructure agentStructure;

    /**
     * Array of agents.
     */
    private Agent[] agents;

    /**
     * Creates new simulation using parameters given as final static fields.
     */
    public Simulation() {
        knowledgeStructure = new KnowledgeStructure(KNOWLEDGE_BASE_SIZE, KNOWLEDGE_SIZE, KNOWLEDGE_CONNECTIONS);
        
        // knowledge draw is repeated, if there is a base element which is not obtained by any agent
        while (agents == null || SimulationStatistics.numberOfElements(agents) < KNOWLEDGE_BASE_SIZE) {
            if (agents != null)
                System.out.println("Re-drawing knowledge elements");
            
            agents = new Agent[NUMBER_OF_AGENTS];
            for (int i = 0; i < NUMBER_OF_AGENTS; i++) {
                agents[i] = new Agent(knowledgeStructure, KNOWLEDGE_ELEMENT_PROBABILITY, PRODUCTION_PROBABILITY);
            }
        }
        agentStructure = new AgentStructure(agents, NUMBER_OF_CONNECTIONS);
    }

    /**
     * Runnable interface method - runs the simulation.
     */
    public void run() {
        System.out.println("Example agent generated:");
        System.out.println(agents[0].toString());
        writeStatistics();
        for (int i = 0; i < NUMBER_OF_ITERATIONS; i++) {
            for (int j = 0; j < agents.length; j++) {
                agentStructure.makeStep(randomAgentIndex());
            }
            writeStatistics();
        }
    }

    private int randomAgentIndex() {
        return new Random().nextInt(agents.length);
    }

    private void writeStatistics() {
        int totalKnowledge = SimulationStatistics.sumOfElements(agents);
        int numberOfDifferentKnowledge = SimulationStatistics.numberOfElements(agents);
        int[] knowledgeDistribution = SimulationStatistics.elementDistribution(agents);

        StringBuilder sb = new StringBuilder();
        sb.append("\nSTATISTICS:\n");
        sb.append(String.format("Total number of knowledge elements: %d\n", totalKnowledge));
        sb.append(String.format("Number of different knowledge elements: %d\n", numberOfDifferentKnowledge));
        sb.append("Knowledge distribution: \n");
        for (int i = 0; i < knowledgeDistribution.length; i++) {
            sb.append(String.format("%d\t", i));
        }
        sb.append("\n");
        for (int i = 0; i < knowledgeDistribution.length; i++) {
            sb.append(String.format("%d\t", knowledgeDistribution[i]));
        }
        System.out.println(sb.toString());
    }
}
