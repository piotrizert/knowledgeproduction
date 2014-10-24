package pl.izertp.knowledgeproduction.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import pl.izertp.knowledgeproduction.hypergraph.HyperGraph;
import pl.izertp.knowledgeproduction.hypergraph.MixedHyperGraph;
import lombok.Getter;

public class KnowledgeRepresentation {

    /**
     * Number of basic knowledge elements, from which all other elements can be developed.
     */
    @Getter
    private int baseSize;

    /**
     * Size of the whole knowledge graph.
     */
    @Getter
    private int size;
    
    /**
     * HyperGraph containing knowledge structure.
     */
    private HyperGraph graph;
    
    /**
     * Creates a new KnowledgeRepresentation object with random-generated knowledge HyperGraph.
     * 
     * @param baseSize number of basic knowledge elements, from which all other elements can be developed
     * @param size size of the whole knowledge graph
     * @param connectionNumber maximum number of connections to a non-base element
     */
    public KnowledgeRepresentation(int baseSize, int size, int connectionNumber) {
        if (baseSize > size) {
            throw new IllegalArgumentException("Size of base in KnowledgeRepresentation cant be greater than size of the graph");
        }
        this.baseSize = baseSize;
        this.size = size;
        this.graph = new MixedHyperGraph(size);
        
        for(int i=baseSize; i<size; i++) {
            for(int j=0; j<connectionNumber; j++) {
                int[] randomPair = getRandomPair(i);
                graph.addEdge(randomPair[0], randomPair[1], i);
            }
        }
    }
    
    /**
     * Creates a new KnowledgeRepresentation object basing on an existing HyperGraph.
     * 
     * @param graph hypergraph representing knowledge structure
     */
    public KnowledgeRepresentation(HyperGraph graph) {
        this.graph = graph;
    }
    
    /**
     * Returns a pair of random ints from 0 to max-1
     * @param max maximum number - 1
     * @return two-element array of random ints
     */
    private int[] getRandomPair(int max) {
        List<Integer> list = new ArrayList<Integer>();
        for(int i=0; i<max; i++) {
            list.add(i);
        }
        Collections.shuffle(list);
        return new int[]{list.get(0), list.get(1)};
    }
    
    @Override
    public String toString(){
        return graph.toString();
    }

}
