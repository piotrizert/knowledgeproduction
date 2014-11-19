package pl.izertp.knowledgeproduction.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import lombok.Setter;
import pl.izertp.knowledgeproduction.graph.AdjacencyListGraph;
import pl.izertp.knowledgeproduction.graph.ErdosRenyiCreator;
import pl.izertp.knowledgeproduction.graph.Graph;

public class AgentStructure {

    /**
     * Graph of agents' connections.
     */
    @Setter
    private Graph agentsGraph;

    /**
     * Array of agents.
     */
    @Setter
    private Agent[] agents;

    /**
     * Array of lists of agents' neighbors.
     */
    private List<Agent>[] neighborList;

    /**
     * Number of agents.
     */
    private int size;

    /**
     * Initializes the object with given array of agents.
     * Agents' connection graph is initialized as Erdos-Renyi graph by helper class.
     * neighborList is also initialized.
     * 
     * @param agents array of agents
     */
    public AgentStructure(Agent[] agents) {
        this.agents = agents;
        size = agents.length;
        agentsGraph = new AdjacencyListGraph(size);
        ErdosRenyiCreator.InitErdosRenyiGraph(agentsGraph);
        initNeighborList();
    }

    /**
     * Empty constructor. Note, that after setting all the fields, initNeighborList must be called.
     */
    public AgentStructure() {

    }

    /**
     * Initializes the neighborList array - must be called after setting agents and agentsGraph.
     */
    // array of lists warning - type check
    @SuppressWarnings("unchecked")
    public void initNeighborList() {
        size = agents.length;
        neighborList = new List[size];
        for (int i = 0; i < size; i++) {
            neighborList[i] = getNeighbors(i);
        }
    }

    /**
     * A single action of the agent - produces or propagates knowledge accordingly to agent's
     * production chance.
     * 
     * @param agentIndex index of the agent, which will perform action
     * @return true, if action returned an effect (knowledge was actually propagated or produced)
     */
    public boolean makeStep(int agentIndex) {
        double random = new Random().nextDouble();
        double productionChance = agents[agentIndex].getProductionChance();
        if (random < productionChance) {
            return produceKnowledge(agentIndex);
        } else {
            return propagateKnowledge(agentIndex);
        }
    }

    /**
     * Simulates agent's knowledge production (uses Agent internal method).
     * 
     * @param agentIndex index of the agent which will produce knowledge
     * @return true, if the agent produced some knowledge
     */
    private boolean produceKnowledge(int agentIndex) {
        boolean effect = agents[agentIndex].produceKnowledge();
        if (effect) {
            System.out.println(String.format("Agent %d produced some knowledge", agentIndex));
        } else {
            System.out.println(String.format("Agent %d didn't produce knowledge", agentIndex));
        }
        return effect;
    }

    /**
     * Propagates the knowledge of selected agent to his randomly selected neighbor.
     * Knowledge element to share is selected randomly.
     * If the naighbor already has this element, nothing happens.
     * 
     * @param agentIndex index of agent, which will try to share his knowledge with a random neighbor
     * @return true, if the knowledge was passed
     */
    private boolean propagateKnowledge(int agentIndex) {
        Agent propagatingAgent = agents[agentIndex];
        Random random = new Random();
        int neighborCount = neighborList[agentIndex].size();
        if (neighborCount == 0) {
            System.out.println(String.format("Agent %d doesn't have any neighbors, so he doesn't propagate knowledge", agentIndex));
            return false;
        }
        int randomAgentIndex = random.nextInt(neighborCount);
        Agent randomAgent = neighborList[agentIndex].get(randomAgentIndex);
        List<Integer> knowledgeToPropagate = propagatingAgent.getHaveKnowledge();
        int randomIndex = random.nextInt(knowledgeToPropagate.size());
        int randomKnowledgeElement = knowledgeToPropagate.get(randomIndex);
        boolean effect = !(randomAgent.addKnowledgeElement(randomKnowledgeElement));
        if (effect) {
            System.out.println(String.format("Agent %d passed some knowledge to agent %d", agentIndex, randomAgentIndex));
        } else {
            System.out.println(String.format("Agent %d didn't pass any knowledge to agent %d", agentIndex, randomAgentIndex));
        }
        return effect;
    }

    /**
     * Helper method used to initialize neighborList.
     * 
     * @param index index of the agent
     * @return list of its neighbors
     */
    private List<Agent> getNeighbors(int index) {
        List<Agent> agentList = new ArrayList<Agent>();
        List<Integer> indexList = agentsGraph.getNeighbors(index);
        for (Integer i : indexList) {
            agentList.add(agents[i]);
        }
        return agentList;
    }

}
