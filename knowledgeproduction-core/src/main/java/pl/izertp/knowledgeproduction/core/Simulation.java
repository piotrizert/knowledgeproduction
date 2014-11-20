package pl.izertp.knowledgeproduction.core;

import java.util.Random;

public class Simulation implements Runnable {

    private static final double KNOWLEDGE_ELEMENT_PROBABILITY = 0.75;

    private static final double PRODUCTION_PROBABILITY = 0.5;

    private static final int NUMBER_OF_ITERATIONS = 10;

    private static final int NUMBER_OF_AGENTS = 5;

    private static final int NUMBER_OF_CONNECTIONS = 10;

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
        knowledgeStructure = new KnowledgeStructure(4, 10, 1);
        agents = new Agent[NUMBER_OF_AGENTS];
        for (int i = 0; i < NUMBER_OF_AGENTS; i++) {
            agents[i] = new Agent(knowledgeStructure, KNOWLEDGE_ELEMENT_PROBABILITY, PRODUCTION_PROBABILITY);
        }
        agentStructure = new AgentStructure(agents, NUMBER_OF_CONNECTIONS);
    }

    /**
     * Runnable interface method - runs the simulation.
     */
    public void run() {
        System.out.println("Example agent generated:");
        System.out.println(agents[0].toString());
        for (int i = 0; i < NUMBER_OF_ITERATIONS; i++) {
            for (int j = 0; j < agents.length; j++) {
                agentStructure.makeStep(randomAgentIndex());
            }
        }
    }

    private int randomAgentIndex() {
        return new Random().nextInt(agents.length);
    }
}
