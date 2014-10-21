package pl.izertp.knowledgeproduction.hypergraph;

import java.util.ArrayList;
import java.util.List;

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
     * Creates a new AdjacencyMatrixHyperGraph.
     * 
     * @param n number of vertices
     */
    @SuppressWarnings("unchecked") //array of lists
    public MixedHyperGraph(int n) {
        vertexNumber = n;
        edges = new List[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                edges[i][j] = new ArrayList<Integer>();
            }
        }
    }

    public boolean addEdge(int from1, int from2, int to) {
        checkArgs(from1, from2, to);
        
        if(edges[from1][from2].contains(to)){
            return true;
        }
        edges[from1][from2].add(to);
        edges[from2][from1].add(to);
        return false;
    }
    //TODO: list contains int/Integer
    public boolean getEdge(int from1, int from2, int to) {
        return edges[from1][from2].contains(to);
    }

    public int getSize() {
        return vertexNumber;
    }
    
    private void checkArgs(int from1, int from2, int to) {
        if(from1 == from2) {
            throw new IllegalArgumentException("Source vertices have to be different");
        }
        if( from1 == to || from2 == to) {
            throw new IllegalArgumentException("Source cant be the same as destination");
        }
    }

}
