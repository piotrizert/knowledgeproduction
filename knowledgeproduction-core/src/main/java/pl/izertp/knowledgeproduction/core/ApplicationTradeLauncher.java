package pl.izertp.knowledgeproduction.core;

public class ApplicationTradeLauncher {

    public static void main(String[] args) {

        for (double tradeProbability = 0; tradeProbability <= 1; tradeProbability += 0.1) {
            Simulation simulation = new Simulation(tradeProbability);
            simulation.run();
            int totalKnowledge = SimulationStatistics.sumOfElements(simulation.getAgents());
            System.out.println(String.format("Trade: %f Number: %d", tradeProbability, totalKnowledge));
        }

    }

}
