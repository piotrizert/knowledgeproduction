package pl.izertp.knowledgeproduction.core;

public class Simulation implements Runnable {
    
    private KnowledgeStructure knowledgeStructure;
    
    public Simulation() {
        knowledgeStructure = new KnowledgeStructure(4, 10, 3);
    }
    
    public void run() {
        System.out.println(knowledgeStructure);
        System.out.println(knowledgeStructure.getResultElements(2, 3));
    }

}
