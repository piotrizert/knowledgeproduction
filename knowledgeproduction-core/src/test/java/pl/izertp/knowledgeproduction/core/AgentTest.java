package pl.izertp.knowledgeproduction.core;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import pl.izertp.knowledgeproduction.hypergraph.HyperGraph;
import pl.izertp.knowledgeproduction.hypergraph.MixedHyperGraph;

public class AgentTest {

    private static final int SIZE = 10;

    private static final int BASESIZE = 4;

    private static Integer[] BASEARRAY;

    private static Integer[] NONBASEARRAY;

    private static final int FROM1a = 0;

    private static final int FROM1b = 1;

    private static final int TO1a = BASESIZE;

    private static final int TO1b = BASESIZE + 1;

    private Agent agent;

    private KnowledgeStructure knowledgeStructure;

    @BeforeClass
    public static void prepareArrays() {
        BASEARRAY = new Integer[BASESIZE];
        for (int i = 0; i < BASESIZE; i++) {
            BASEARRAY[i] = i;
        }
        NONBASEARRAY = new Integer[SIZE - BASESIZE];
        for (int i = BASESIZE; i < SIZE; i++) {
            NONBASEARRAY[i - BASESIZE] = i;
        }
    }

    @Before
    public void setUpAgent() {
        HyperGraph graph = new MixedHyperGraph(SIZE);
        graph.addEdge(FROM1a, FROM1b, TO1a);
        graph.addEdge(FROM1a, FROM1b, TO1b);
        knowledgeStructure = new KnowledgeStructure(graph, BASESIZE);
        agent = new Agent(knowledgeStructure, 1, 1); // note, that now agent has all the base knowledge elements [0, BASESIZE-1]
    }

    @Test
    public void testAddElement() {
        assertTrue("Adding existing knowledge element to an agent should return true", agent.addKnowledgeElement(0));
        assertFalse("Adding non-existing knowledge element to an agent should return false", agent.addKnowledgeElement(BASESIZE)); // first element not from the base
        assertTrue("Adding existing knowledge element to an agent should return true", agent.addKnowledgeElement(BASESIZE));
    }

    @Test
    public void testProduceKnowledge() {
        assertEquals(BASESIZE, agent.getHaveKnowledge().size());
        agent.produceKnowledge();
        assertEquals(BASESIZE + 1, agent.getHaveKnowledge().size());
        agent.produceKnowledge();
        assertEquals(BASESIZE + 2, agent.getHaveKnowledge().size());
        agent.produceKnowledge();
        assertEquals(BASESIZE + 2, agent.getHaveKnowledge().size()); // size of knowledge should stop growing now, because agent can develop only 2 elements
    }

    @Test
    public void testGetHaveKnowledge() {
        assertArrayEquals("Test agent should have all the base knowledge", BASEARRAY, agent.getHaveKnowledge().toArray());
    }

    @Test
    public void testGetDoesntHaveKnowledge() {
        assertArrayEquals("Test agent shouldnt have any non-base knowledge", NONBASEARRAY, agent.getDoesntHaveKnowledge().toArray());
    }

}
