package pl.izertp.knowledgeproduction.core;

import java.io.IOException;
import java.util.Random;

/**
 * Implements runnable method, which does the simulation.
 * 
 * @author Piotr Izert
 */
public class Simulation implements Runnable {

    private static final int KNOWLEDGE_SIZE = 100;

    private static final int KNOWLEDGE_BASE_SIZE = 8;

    private static final int KNOWLEDGE_CONNECTIONS = 1;

    private static final int NUMBER_OF_AGENTS = 1000;

    private static final double TRADE_PROBABILITY = 0.5;

    private static final int AVARAGE_AGENT_CONNECTIONS = 5;

    private static final double KNOWLEDGE_ELEMENT_PROBABILITY = 0.1;

    private static final double PRODUCTION_PROBABILITY = 0.5;

    private static final int NUMBER_OF_ITERATIONS = 1000;

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
     * Every knowledge element must be possessed by at least one agent.
     * If not, the initial knowledge distribution draw is repeated.
     */
    public Simulation() {
        knowledgeStructure = new KnowledgeStructure(KNOWLEDGE_BASE_SIZE, KNOWLEDGE_SIZE, KNOWLEDGE_CONNECTIONS);

        // knowledge draw is repeated, if there is a base element which is not obtained by any agent
        while (agents == null || SimulationStatistics.numberOfElements(agents) < KNOWLEDGE_BASE_SIZE) {
            if (agents != null)
                System.out.println("Re-drawing knowledge elements");

            agents = new Agent[NUMBER_OF_AGENTS];
            for (int i = 0; i < NUMBER_OF_AGENTS; i++) {
                agents[i] = new Agent(knowledgeStructure, KNOWLEDGE_ELEMENT_PROBABILITY, PRODUCTION_PROBABILITY, TRADE_PROBABILITY);
            }
        }
        // every edge is connected to two agents
        agentStructure = new AgentStructure(agents, (int) (NUMBER_OF_AGENTS / 2.) * AVARAGE_AGENT_CONNECTIONS);
    }

    /**
     * Runnable interface method - runs the simulation.
     */
    public void run() {
        try {
            SimulationStatistics.openFiles();
        } catch (IOException e) {
            System.out.println("Problem with opening output files, terminating application");
            return;
        }

        SimulationStatistics.writeDepthTotal(agents);
        writeStatistics(true);
        for (int i = 0; i < NUMBER_OF_ITERATIONS; i++) {
            for (int j = 0; j < agents.length; j++) {
                agentStructure.makeStep(randomAgentIndex());
            }
            writeStatistics(false);
        }

        SimulationStatistics.closeFiles();

    }

    private int randomAgentIndex() {
        return new Random().nextInt(agents.length);
    }

    private void writeStatistics(boolean stdout) {
        int totalKnowledge = SimulationStatistics.writeSumOfElements(agents);
        int numberOfDifferentKnowledge = SimulationStatistics.writeNumberOfElements(agents);
        int[] distribution = SimulationStatistics.writeDistributionOfKnowledgeCount(agents);
        int[] depthSums = SimulationStatistics.writeDepthSum(agents);
        int[] knowledgeSums = SimulationStatistics.writeSumOfEachElement(agents);

        // TODO: add depth sum to stdout (or log)
        if (stdout) {
            StringBuilder sb = new StringBuilder();
            sb.append("\nSTATISTICS:\n");
            sb.append(String.format("Total number of knowledge elements: %d\n", totalKnowledge));
            sb.append(String.format("Number of different knowledge elements: %d\n", numberOfDifferentKnowledge));
            sb.append("Distribution: \n");
            for (int i = 0; i < distribution.length; i++) {
                sb.append(String.format("%d\t", i));
            }
            sb.append("\n");
            for (int i = 0; i < distribution.length; i++) {
                sb.append(String.format("%d\t", distribution[i]));
            }
            sb.append("\n");

            sb.append("Knowledge sums: \n");
            for (int i = 0; i < knowledgeSums.length; i++) {
                sb.append(String.format("%d\t", i));
            }
            sb.append("\n");
            for (int i = 0; i < knowledgeSums.length; i++) {
                sb.append(String.format("%d\t", knowledgeSums[i]));
            }
            sb.append("\n");
            System.out.println(sb.toString());
        }
    }
}
