package pl.izertp.knowledgeproduction.hypergraph;

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

}
