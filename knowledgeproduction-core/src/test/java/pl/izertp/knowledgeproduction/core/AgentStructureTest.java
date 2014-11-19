package pl.izertp.knowledgeproduction.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import pl.izertp.knowledgeproduction.graph.AdjacencyListGraph;
import pl.izertp.knowledgeproduction.graph.Graph;
import pl.izertp.knowledgeproduction.hypergraph.HyperGraph;
import pl.izertp.knowledgeproduction.hypergraph.MixedHyperGraph;

public class AgentStructureTest {

    private Agent agentProduce;

    private Agent agentPropagate;

    private static KnowledgeStructure knowledgeStructure;

    private AgentStructure testAgentStructure;

    private Graph agentsGraph;

    private static boolean[] fullSet;

    private static boolean[] emptySet;

    @BeforeClass
    public static void initClass() {
        // configuration:
        // graph's base elements: {0,1} -> baseSize = 2
        // elements possible to develop from base: {2, 3}
        HyperGraph knowledgeGraph = new MixedHyperGraph(5);
        knowledgeGraph.addEdge(0, 1, 2);
        knowledgeGraph.addEdge(0, 1, 3);

        knowledgeStructure = new KnowledgeStructure(knowledgeGraph, 2);

        fullSet = new boolean[] { true, true, false, false, false };
        emptySet = new boolean[] { false, false, false, false, false };
    }

    @Before
    public void initTest() {
        agentProduce = new Agent();
        agentProduce.setProductionChance(1);
        agentProduce.setKnowledgeStructure(knowledgeStructure);

        agentPropagate = new Agent();
        agentPropagate.setProductionChance(0);
        agentPropagate.setKnowledgeStructure(knowledgeStructure);

        agentsGraph = new AdjacencyListGraph(2);
        agentsGraph.addEdge(0, 1);

        testAgentStructure = new AgentStructure();
        testAgentStructure.setAgents(new Agent[] { agentProduce, agentPropagate });
        testAgentStructure.setAgentsGraph(agentsGraph);
        testAgentStructure.initNeighborList();
    }

    /**
     * In this test, agentProduce will produce the knowledge.
     * Due to initial conditions, exactly two elements should be produced.
     * AgentPropagate is not properly initialized, because it shouldn't be used.
     */
    @Test
    public void testAgentProduce() {
        agentProduce.setKnowledgeSet(fullSet);
        agentProduce.initAgent();
        assertTrue("Knowledge element should be produced", testAgentStructure.makeStep(0));
        assertTrue("Knowledge element should be produced", testAgentStructure.makeStep(0));
        assertFalse("Knowledge element shouldn't be produced", testAgentStructure.makeStep(0));
    }

    /**
     * In this test, agentPropagate will propagate knowledge to agentProduce.
     * Due to initial conditions, exactly two elements should be propagated.
     */
    @Test
    public void testAgentPropagate() {
        agentProduce.setKnowledgeSet(emptySet);
        agentProduce.initAgent();
        agentPropagate.setKnowledgeSet(fullSet);
        agentPropagate.initAgent();
        assertTrue("Knowledge element should be propagated", testAgentStructure.makeStep(1));

        assertEquals("AgentProduce should now have some propagated knowledge", 1, agentProduce.getHaveKnowledge().size());
    }

}
