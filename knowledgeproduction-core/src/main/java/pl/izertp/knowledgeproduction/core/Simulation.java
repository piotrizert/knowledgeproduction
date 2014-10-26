package pl.izertp.knowledgeproduction.core;

public class Simulation implements Runnable {
    
    private KnowledgeStructure knowledgeStructure;
    
    public Simulation() {
        knowledgeStructure = new KnowledgeStructure(4, 10, 1);
    }
    
    public void run() {
        Agent agent = new Agent(knowledgeStructure, 0.75);
        System.out.println(agent);
    }

}
