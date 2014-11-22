package pl.izertp.knowledgeproduction.hypergraph;

import java.util.ArrayList;
import java.util.List;

/**
 * Implemetation of HyperGraph based partially on vertex matrix and partially on adjacency lists.
 * All the pairs of vertices are kept in a two-dimenstional array. Each element of the array
 * contains a list of vertices, to which there is an edge from the given pair of vertices.
 * Provides fast - O(1) - access to the "child" element list needed in knowledge production
 * algorithm (method toVertices).
 * 
 * @author Piotr Izert
 */
public class MixedHyperGraph implements HyperGraph {

    /**
     * Two dimensional array of lists of edges.
     * Has to be kept symmetric (edges[from1][from2] equals edges[from2][from1])
     */
    private List<Integer>[][] edges;

    /**
     * Number of graph vertices.
     */
    private int vertexNumber;

    /**
     * Creates a new, empty MixedHyperGraph.
     * 
     * @param n number of vertices
     */
    // array of lists warning - type check
    @SuppressWarnings("unchecked")
    public MixedHyperGraph(int n) {
        vertexNumber = n;
        edges = new List[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                edges[i][j] = new ArrayList<Integer>();
            }
        }
    }

    public int getSize() {
        return vertexNumber;
    }

    public boolean addEdge(int from1, int from2, int to) {
        checkArgs(from1, from2, to);
        if (edges[from1][from2].contains(to)) {
            return true;
        }
        edges[from1][from2].add(to);
        edges[from2][from1].add(to);
        return false;
    }

    public boolean getEdge(int from1, int from2, int to) {
        return edges[from1][from2].contains(to);
    }

    public List<Integer> toVertices(int v1, int v2) {
        return edges[v1][v2];
    }

    private void checkArgs(int from1, int from2, int to) {
        if (from1 == from2) {
            throw new IllegalArgumentException("Source vertices have to be different");
        }
        if (from1 == to || from2 == to) {
            throw new IllegalArgumentException("Source cant be the same as destination");
        }
    }

    // TODO: this is not the optimal implementation, can be done in O(n^2) for sparse graphs
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < vertexNumber; i++) {
            for (int j = i + 1; j < vertexNumber; j++) {
                for (int k = 0; k < vertexNumber; k++) {
                    if (getEdge(i, j, k)) {
                        sb.append(String.format("[{%d} {%d} -> {%d}\n", i, j, k));
                    }
                }
            }
        }
        return sb.toString();
    }

}
