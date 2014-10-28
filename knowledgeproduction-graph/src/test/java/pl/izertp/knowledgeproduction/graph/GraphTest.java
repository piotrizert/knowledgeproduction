package pl.izertp.knowledgeproduction.graph;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class GraphTest {
    private static final int SIZE = 10;

    private static final int FROM1 = 0;

    private static final int TO1 = 1;

    private static final int TO2 = 2;

    Graph graph;

    @Before
    public void setUpGraph() {
        graph = new AdjacencyListGraph(SIZE);
    }

    @Test
    public void testAddEdge() {
        assertFalse("Adding new edge should return false", graph.addEdge(FROM1, TO1));
        assertTrue("Adding existing edge should return true", graph.addEdge(FROM1, TO1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddLoop() {
        graph.addEdge(FROM1, FROM1);
    }

    @Test
    public void testSymmetric() {
        graph.addEdge(FROM1, TO1);
        assertTrue(graph.getEdge(FROM1, TO1));
        assertTrue(graph.getEdge(TO1, FROM1));
    }

    @Test
    public void testNeighbors() {
        graph.addEdge(FROM1, TO1);
        graph.addEdge(FROM1, TO2);
        assertEquals(2, graph.getNeighbors(FROM1).size());
        assertEquals(1, graph.getNeighbors(TO1).size());
    }
}
