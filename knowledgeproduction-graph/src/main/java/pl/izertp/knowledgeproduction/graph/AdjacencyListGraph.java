package pl.izertp.knowledgeproduction.graph;

import java.util.ArrayList;
import java.util.List;

public class AdjacencyListGraph implements Graph {

    /**
     * Number of graph vertices.
     */
    private int vertexNumber;

    /**
     * Array of lists of edges. Should be kept symetric (graph is undirected).
     */
    private List<Integer>[] edges;

    // array of lists
    @SuppressWarnings("unchecked")
    public AdjacencyListGraph(int n) {
        vertexNumber = n;
        edges = new List[n];
        for (int i = 0; i < n; i++) {
            edges[i] = new ArrayList<Integer>();
        }
    }

    public int getSize() {
        return vertexNumber;
    }

    public boolean addEdge(int from, int to) {
        checkArgs(from, to);
        if (edges[from].contains(to)) {
            return true;
        }
        edges[from].add(to);
        edges[to].add(from);
        return false;
    }

    public boolean getEdge(int from, int to) {
        return edges[from].contains(to);
    }

    public List<Integer> getNeighbors(int v) {
        return edges[v];
    }

    private void checkArgs(int from, int to) {
        if (from == to) {
            throw new IllegalArgumentException("Edge cant start and end in the same vertex");
        }
    }

}
