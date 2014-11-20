package pl.izertp.knowledgeproduction.graph;

import java.util.ArrayList;
import java.util.List;

public class AdjacencyListGraph implements Graph {

    /**
     * Number of graph vertices.
     */
    private int vertexNumber;

    /**
     * Array of lists of vertex neighbors (adjacency lists). Should be kept symmetric
     * (graph is undirected).
     */
    private List<Integer>[] neighbors;

    /**
     * Initializes the graph by initialization of adjacency lists array.
     * 
     * @param n number of vertices
     */
    // array of lists warning - type match
    @SuppressWarnings("unchecked")
    public AdjacencyListGraph(int n) {
        vertexNumber = n;
        neighbors = new List[n];
        for (int i = 0; i < n; i++) {
            neighbors[i] = new ArrayList<Integer>();
        }
    }

    public int getSize() {
        return vertexNumber;
    }

    public boolean addEdge(int from, int to) {
        checkArgs(from, to);
        if (neighbors[from].contains(to)) {
            return true;
        }
        neighbors[from].add(to);
        neighbors[to].add(from);
        return false;
    }

    public boolean getEdge(int from, int to) {
        return neighbors[from].contains(to);
    }

    public List<Integer> getNeighbors(int v) {
        return neighbors[v];
    }

    private void checkArgs(int from, int to) {
        if (from == to) {
            throw new IllegalArgumentException("Loops are not allowed");
        }
    }

}
