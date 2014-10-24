package pl.izertp.knowledgeproduction.core;

import static org.junit.Assert.assertArrayEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import pl.izertp.knowledgeproduction.hypergraph.HyperGraph;
import pl.izertp.knowledgeproduction.hypergraph.MixedHyperGraph;

public class KnowledgeStructureTest {

    private static final int SIZE = 10;

    private static final int FROM1a = 1;

    private static final int FROM1b = 2;

    private static final int TO1a = 5;

    private static final int TO1b = 6;

    private static final Integer[] RESULT1 = new Integer[] { TO1a, TO1b };

    private static final int FROM2a = 3;

    private static final int FROM2b = 4;

    private static final int TO2a = 7;

    private static final Integer[] RESULT2 = new Integer[] { TO2a };

    KnowledgeStructure knowledgeStructure;

    @Before
    public void setUp() {
        HyperGraph graph = new MixedHyperGraph(SIZE);
        graph.addEdge(FROM1a, FROM1b, TO1a);
        graph.addEdge(FROM1a, FROM1b, TO1b);
        graph.addEdge(FROM2a, FROM2b, TO2a);
        knowledgeStructure = new KnowledgeStructure(graph);
    }

    @Test
    public void testGetResultElements() {
        List<Integer> resultList = knowledgeStructure.getResultElements(FROM1a, FROM1b);
        assertArrayEquals("Wrong result elements", resultList.toArray(), RESULT1);

        resultList = knowledgeStructure.getResultElements(FROM2a, FROM2b);
        assertArrayEquals("Wrong result elements", resultList.toArray(), RESULT2);
    }

    @Test
    public void testEmptyResultElements() {
        List<Integer> resultList = knowledgeStructure.getResultElements(FROM1a, FROM2b);
        assertArrayEquals("Wrong result elements", resultList.toArray(), new Integer[] {});

    }

}
