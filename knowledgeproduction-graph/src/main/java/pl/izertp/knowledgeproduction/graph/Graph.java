package pl.izertp.knowledgeproduction.graph;

import java.util.List;

public interface Graph {

    /**
     * @return number of graph vertices
     */
    public int getSize();

    /**
     * Adds an edge to the graph.
     * 
     * @param from start vertex
     * @param to end vertex
     * @return if the edge was already present or not
     */
    public boolean addEdge(int from, int to);

    /**
     * Checks if the edge is present.
     * 
     * @param from start vertex
     * @param to end vertex
     * @return if the edge is present or not
     */
    public boolean getEdge(int from, int to);
    
    /**
     * Returns a list of all neighbors of the vertex. 
     * 
     * @param v vertex
     * @return list of neighbors
     */
    public List<Integer> getNeighbors(int v);

}
