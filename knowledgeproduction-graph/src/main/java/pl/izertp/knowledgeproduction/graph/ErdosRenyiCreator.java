package pl.izertp.knowledgeproduction.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Helper class which fills an empty graph with edges using Erdos-Renyi construction.
 * In every step an edge is added between random vertices which are not already connected by an edge.
 * The total number of edges is given as a parameter.
 * 
 * @author Piotr Izert
 */
public class ErdosRenyiCreator {
    
    /**
     * Fills the graph with edges using Erdos-Renyi algorithm.
     * 
     * @param graph empty graph to fill with edges
     * @param edgeNumber number of edges to put
     */
    public static void InitErdosRenyiGraph(Graph graph, int edgeNumber) {
        if (edgeNumber > (graph.getSize() * (graph.getSize() - 1)) / 2) {
            throw new IllegalArgumentException("Number of edges is higher than the number of all possible edges");
        }

        ErdosRenyiCreator creator = new ErdosRenyiCreator();

        List<VertexPair> vertexPairs = new ArrayList<VertexPair>();

        for (int i = 0; i < graph.getSize(); i++) {
            for (int j = i + 1; j < graph.getSize(); j++) {
                vertexPairs.add(creator.new VertexPair(i, j));
            }
        }

        for (int i = 0; i < edgeNumber; i++) {
            Collections.shuffle(vertexPairs);
            VertexPair randomPair = vertexPairs.get(0);
            graph.addEdge(randomPair.vertexA, randomPair.vertexB);
            vertexPairs.remove(0);
        }
    }

    /**
     * Class used for holding a pair of vertices, needed for Erdos-Renyi construction.
     * 
     * @author Piotr Izert
     */
    private class VertexPair {

        private int vertexA;

        private int vertexB;

        public VertexPair(int a, int b) {
            vertexA = a;
            vertexB = b;
        }

    };

}
