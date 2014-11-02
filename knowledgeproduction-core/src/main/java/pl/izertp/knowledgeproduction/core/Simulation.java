package pl.izertp.knowledgeproduction.core;

public class Simulation implements Runnable {

    private static final double KNOWLEDGE_ELEMENT_PROBABILITY = 0.75;

    private static final double PRODUCTION_PROBABILITY = 0.5;

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
     * Creates new simulation.
     * 
     * @param n number of agents in simulation
     */
    public Simulation(int n) {
        knowledgeStructure = new KnowledgeStructure(4, 10, 1);
        agents = new Agent[n];
        for (int i = 0; i < n; i++) {
            agents[i] = new Agent(knowledgeStructure, KNOWLEDGE_ELEMENT_PROBABILITY, PRODUCTION_PROBABILITY);
        }
        agentStructure = new AgentStructure(agents);
    }

    public void run() {
        System.out.println(agents[0].toString());
        for(int i=0; i<10; i++){
            agentStructure.makeStep(0);
        }
    }
}
