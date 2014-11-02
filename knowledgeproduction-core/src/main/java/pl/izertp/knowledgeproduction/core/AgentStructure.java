package pl.izertp.knowledgeproduction.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import pl.izertp.knowledgeproduction.graph.AdjacencyListGraph;
import pl.izertp.knowledgeproduction.graph.ErdosRenyiCreator;
import pl.izertp.knowledgeproduction.graph.Graph;

public class AgentStructure {

    private Graph agentsGraph;

    private Agent[] agents;

    private List<Agent>[] neighborList;

    private int size;

    // array of lists warning - type check
    @SuppressWarnings("unchecked")
    public AgentStructure(Agent[] agents) {
        this.agents = agents;
        size = agents.length;
        agentsGraph = new AdjacencyListGraph(size);
        ErdosRenyiCreator.InitErdosRenyiGraph(agentsGraph);
        neighborList = new List[size];
        for (int i = 0; i < size; i++) {
            neighborList[i] = getNeighbours(i);
        }
    }

    public boolean makeStep(int agentIndex) {
        double random = new Random().nextDouble();
        double productionChance = agents[agentIndex].getProductionChance();
        if (random < productionChance) {
            return produceKnowledge(agentIndex);
        } else {
            return propagateKnowledge(agentIndex);
        }
    }

    private boolean produceKnowledge(int agentIndex) {
        boolean effect = agents[agentIndex].produceKnowledge();
        if (effect) {
            System.out.println(String.format("Agent %d produced some knowledge", agentIndex));
        } else {
            System.out.println(String.format("Agent %d didn't produce knowledge", agentIndex));
        }
        return effect;
    }

    private boolean propagateKnowledge(int agentIndex) {
        Random random = new Random();
        int neighborCount = neighborList[agentIndex].size();
        if (neighborCount == 0) {
            System.out.println(String.format("Agent %d doesn't have any neighbors, so he doesn't propagate knowledge", agentIndex));
            return false;
        }
        int randomAgentIndex = random.nextInt(neighborCount);
        Agent randomAgent = neighborList[agentIndex].get(randomAgentIndex);
        List<Integer> knowledgeToPropagate = randomAgent.getHaveKnowledge();
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

    private List<Agent> getNeighbours(int index) {
        List<Agent> agentList = new ArrayList<Agent>();
        List<Integer> indexList = agentsGraph.getNeighbors(index);
        for (Integer i : indexList) {
            agentList.add(agents[i]);
        }
        return agentList;
    }

}
