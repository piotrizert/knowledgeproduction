package pl.izertp.knowledgeproduction.core;

public class ApplicationLauncher {

    public static void main(String[] args) {
        KnowledgeRepresentation representation = new KnowledgeRepresentation(4, 10, 2);
        System.out.println(representation.toString());
    }

}
