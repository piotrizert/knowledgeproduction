package pl.izertp.knowledgeproduction.hypergraph;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class HyperGraphTest {

    private static final int Vfrom1 = 0;

    private static final int Vfrom2 = 1;

    private static final int Vto1 = 2;

    private static final int Vto2 = 3;

    private static final int SIZE = 10;

    private HyperGraph graph;

    @Before
    public void setUpGraph() {
        graph = new MixedHyperGraph(SIZE);
    }

    @Test
    public void testAddEdge() {
        assertFalse("Adding non-existent edge should return false", graph.addEdge(Vfrom1, Vfrom2, Vto1));
        assertTrue("Adding existent edge should return true", graph.addEdge(Vfrom1, Vfrom2, Vto1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddEdgeTheSameStart() {
        graph.addEdge(Vfrom1, Vfrom1, Vto1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSameStartBeginning() {
        graph.addEdge(Vfrom1, Vfrom2, Vfrom1);
    }

    @Test
    public void testEdgeSymmetric() {
        graph.addEdge(Vfrom1, Vfrom2, Vto1);
        assertTrue("Edge should be present", graph.getEdge(Vfrom1, Vfrom2, Vto1));
        assertTrue("Edges should be symmetric", graph.getEdge(Vfrom2, Vfrom1, Vto1));
    }

    @Test
    public void testToVertices() {
        graph.addEdge(Vfrom1, Vfrom2, Vto1);
        graph.addEdge(Vfrom1, Vfrom2, Vto2);
        assertArrayEquals("Wrong destination vertices list", graph.toVertices(Vfrom1, Vfrom2).toArray(), new Integer[] { Vto1, Vto2 });
    }

}
