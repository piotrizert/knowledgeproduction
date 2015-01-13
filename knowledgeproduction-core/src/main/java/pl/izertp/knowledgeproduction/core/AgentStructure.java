package pl.izertp.knowledgeproduction.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;

import lombok.Setter;
import pl.izertp.knowledgeproduction.graph.AdjacencyListGraph;
import pl.izertp.knowledgeproduction.graph.ErdosRenyiCreator;
import pl.izertp.knowledgeproduction.graph.Graph;

/**
 * Structure of agents. Holds the connections between agents,
 * contains implementation of knowledge propagation and a single step of simulation.
 * 
 * @author Piotr Izert
 */
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
    public AgentStructure(Agent[] agents, int connectionNumber) {
        this.agents = agents;
        size = agents.length;
        agentsGraph = new AdjacencyListGraph(size);
        ErdosRenyiCreator.InitErdosRenyiGraph(agentsGraph, connectionNumber);
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
            if (agents[agentIndex].isTrade())
                return tradeKnowledge(agentIndex);
            else
                return propagateKnowledge(agentIndex, true);
        }
    }

    /**
     * Simulates agent's knowledge production (uses Agent internal method).
     * 
     * @param agentIndex index of the agent which will produce knowledge
     * @return true, if some knowledge was produced, false otherwise
     */
    private boolean produceKnowledge(int agentIndex) {
        int producedKnowledge = agents[agentIndex].produceKnowledge();
        if (producedKnowledge < 0) {
            // System.out.println(String.format("Agent %d didn't produce knowledge", agentIndex));
            return false;
        } else {
            // System.out.println(String.format("Agent %d produced knowledge element %d", agentIndex, producedKnowledge));
            return true;
        }
    }

    /**
     * Propagates the knowledge of selected agent to his randomly selected neighbor.
     * Knowledge element to share is selected randomly.
     * If target is set to false, when the neighbor already has this element,
     * nothing happens.
     * If target is set to true, the element is selected from the set of elements possessed
     * by the agent and not possessed by the neighbor.
     * 
     * @param agentIndex index of agent, which will try to share his knowledge with a random neighbor
     * @param target if true, element is selected from the set of elements possessed
     *        by the agent and not possessed by the neighbor, else the element is selected
     *        randomly
     * @return true, if the knowledge was passed
     */
    private boolean propagateKnowledge(int agentIndex, boolean target) {
        Random random = new Random();

        Agent propagatingAgent = agents[agentIndex];
        int neighborCount = neighborList[agentIndex].size();
        if (neighborCount == 0) {
            // System.out.println(String.format("Agent %d doesn't have any neighbors, so he doesn't propagate knowledge", agentIndex));
            return false;
        }
        int randomNeighborIndex = random.nextInt(neighborCount);
        Agent randomNeighbor = neighborList[agentIndex].get(randomNeighborIndex);
        Set<Integer> knowledgeToPropagate = propagatingAgent.getHaveKnowledge();
        if (knowledgeToPropagate.size() == 0) {
            // System.out.println(String.format("Agent %d doesn't have any knowledge", agentIndex));
            return false;
        }

        int elementToPropagate;

        if (!target) {
            elementToPropagate = Utils.getRandomSetElement(knowledgeToPropagate);
        } else {
            Set<Integer> agentKnowledgeSet = propagatingAgent.getHaveKnowledge();
            Set<Integer> neighborDoesntHaveSet = randomNeighbor.getDoesntHaveKnowledge();
            List<Integer> possibleElements = new ArrayList<Integer>();
            for (int agentElement : agentKnowledgeSet) {
                if (neighborDoesntHaveSet.contains(agentElement))
                    possibleElements.add(agentElement);
            }
            if (possibleElements.size() == 0) {
                // TODO: some log here
                return false;
            }
            Collections.shuffle(possibleElements);
            elementToPropagate = possibleElements.get(0);
        }
        boolean effect = !(randomNeighbor.addKnowledgeElement(elementToPropagate));
        if (effect) {
            // System.out.println(String.format("Agent %d passed some knowledge to agent %d", agentIndex, Arrays.asList(agents).indexOf(randomNeighbor)));
        } else {
            // System.out.println(String.format("Agent %d didn't pass any knowledge to agent %d", agentIndex, Arrays.asList(agents).indexOf(randomNeighbor)));
        }
        return effect;
    }

    /**
     * Performs a trade process of given agent. Looks for neighboring trading agents,
     * picks a random one and checks, if there are any possible trades. If there are, picks a random trade and performs it.
     * 
     * @param agentIndex index of agent, which will trade
     * @return true, if trade took place
     * @throws IllegalStateException when given agent is not a trading agent
     */
    public boolean tradeKnowledge(int agentIndex) {
        Agent tradingAgent = agents[agentIndex];
        if (!tradingAgent.isTrade()) {
            throw new IllegalStateException(String.format("Agent %d is not a trading agent", agentIndex));
        }

        int neighborCount = neighborList[agentIndex].size();
        if (neighborCount == 0) {
            // System.out.println(String.format("Agent %d doesn't have any neighbors, so he doesn't trade knowledge", agentIndex));
            return false;
        }

        List<Agent> tradeNeighbors = new ArrayList<Agent>();
        for (Agent a : neighborList[agentIndex]) {
            if (a.isTrade())
                tradeNeighbors.add(a);
        }
        if (tradeNeighbors.size() == 0) {
            // System.out.println(String.format("Agent %d doesn't have any trade neighbors, so he doesn't trade knowledge", agentIndex));
            return false;
        }
        Collections.shuffle(tradeNeighbors);
        Agent partnerAgent = tradeNeighbors.get(0);

        int[] knowledgeToTrade = getRandomPossibleTrade(tradingAgent, partnerAgent);
        if (knowledgeToTrade == null) {
            // System.out.println(String.format("Agent %d doesn't trade knowledge, because he has nothing to trade with his neighbor", agentIndex));
            return false;
        }

        boolean tradeGot = tradingAgent.addKnowledgeElement(knowledgeToTrade[1]);
        boolean partnerGot = partnerAgent.addKnowledgeElement(knowledgeToTrade[0]);
        if (tradeGot || partnerGot)
            throw new IllegalStateException("Trade didnt take place");
        return true;
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

    /**
     * Returns a pair of elements, which can be traded. First element in returned array is
     * an element from 'trading' agent's knowledge set and second element of the array is
     * an element from its partner's set.
     * 
     * @param tradingAgent agent, which wants to trade his knowledge
     * @param partnerAgent agent, with which the knowledge is traded
     * @return 2-element array of indices
     */
    private int[] getRandomPossibleTrade(Agent tradingAgent, Agent partnerAgent) {
        List<Integer> possibleTrades = new ArrayList<Integer>();
        List<Integer> possiblePartner = new ArrayList<Integer>();
        Set<Integer> tradingSet = tradingAgent.getHaveKnowledge();
        Set<Integer> partnerSet = partnerAgent.getHaveKnowledge();
        for (int tradeElement : tradingSet) {
            if (!partnerSet.contains(tradeElement))
                possibleTrades.add(tradeElement);
        }
        for (int partnerElement : partnerSet) {
            if (!tradingSet.contains(partnerElement))
                possiblePartner.add(partnerElement);
        }
        if (possibleTrades.size() == 0 || possiblePartner.size() == 0) {
            return null;
        }
        Collections.shuffle(possibleTrades);
        Collections.shuffle(possiblePartner);
        return new int[] { possibleTrades.get(0), possiblePartner.get(0) };
    }

}
