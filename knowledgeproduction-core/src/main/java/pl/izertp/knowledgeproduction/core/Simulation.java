package pl.izertp.knowledgeproduction.core;

public class Simulation implements Runnable {
    
    private KnowledgeRepresentation knowledgeRepresentation;
    
    public Simulation() {
        knowledgeRepresentation = new KnowledgeRepresentation(4, 10, 3);
    }
    
    public void run() {
        System.out.println(knowledgeRepresentation);
        System.out.println(knowledgeRepresentation.getResultElements(2, 3));
    }

}
