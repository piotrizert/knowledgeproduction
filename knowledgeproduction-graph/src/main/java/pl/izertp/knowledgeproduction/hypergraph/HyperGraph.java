package pl.izertp.knowledgeproduction.hypergraph;

import java.util.List;

public interface HyperGraph {

    /**
     * @return number of graph vertices
     */
    public int getSize();

    /**
     * Adds an edge to the graph.
     * 
     * @param from1 start vertex 1
     * @param from2 start vertex 2
     * @param to end vertex
     * @return if the edge was already present or not
     */
    public boolean addEdge(int from1, int from2, int to);
    
    /**
     * Checks if the edge is present.
     * 
     * @param from1 start vertex 1
     * @param from2 start vertex 2
     * @param to end vertex
     * @return if the edge is present or not
     */
    public boolean getEdge(int from1, int from2, int to);
    
    /**
     * Returns a list of vertices, to which there is an edge from vertices v1 and v2.
     * 
     * @param v1 vertex 1
     * @param e2 vertex 2
     * @return list of vertices, to which given pair is connected
     */
    public List<Integer> toVertices(int v1, int v2);

}
