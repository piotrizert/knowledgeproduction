package pl.izertp.knowledgeproduction.core;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class SimulationStatisticsTest {

    private static double EPS = 1E-10;

    private static final int KNOWLEDGE_SIZE_EMPTY = 10;

    private static final int KNOWLEDGE_SIZE_HALF = 8;

    private static final int KNOWLEDGE_SIZE_FULL = 5;

    private Agent[] agents;

    @Before
    public void setUpAgents() {
        Agent agentEmpty = new Agent();
        Agent agentHalf = new Agent();
        Agent agentFull = new Agent();
        // empty set
        boolean[] knowledgeSetEmpty = new boolean[KNOWLEDGE_SIZE_EMPTY];
        // half-full set
        boolean[] knowledgeSetHalf = new boolean[KNOWLEDGE_SIZE_HALF];
        for (int i = 0; i < KNOWLEDGE_SIZE_HALF / 2; i++) {
            knowledgeSetHalf[i] = true;
        }
        // full set
        boolean[] knowledgeSetFull = new boolean[KNOWLEDGE_SIZE_FULL];
        for (int i = 0; i < KNOWLEDGE_SIZE_FULL; i++) {
            knowledgeSetFull[i] = true;
        }
        agentEmpty.setKnowledgeSet(knowledgeSetEmpty);
        agentHalf.setKnowledgeSet(knowledgeSetHalf);
        agentFull.setKnowledgeSet(knowledgeSetFull);

        agents = new Agent[] { agentEmpty, agentHalf, agentFull };
    }

    @Test
    public void testSumAllElements() {
        int expectedTotal = KNOWLEDGE_SIZE_HALF / 2 + KNOWLEDGE_SIZE_FULL;
        int statisticsTotal = SimulationStatistics.sumOfElements(agents);
        assertEquals("Sum of elements is wrong", expectedTotal, statisticsTotal);
    }

    @Test
    public void testNumberOfDifferentElements() {
        int expectedNumber = 5; // this is counted manually, should be recalculated when parameters are changed
        int statisticsNumber = SimulationStatistics.numberOfElements(agents);
        assertEquals("Number of different elements is wrong", expectedNumber, statisticsNumber);
    }

    @Test
    public void testSumEachElement() {
        int[] expectedSumOfEachElement = new int[] { 2, 2, 2, 2, 1, 0, 0, 0, 0, 0 }; // counted manually
        int[] statisticsSumOfEachElement = SimulationStatistics.sumOfEachElement(agents);
        assertArrayEquals("Knowledge sum for elements is wrong", expectedSumOfEachElement, statisticsSumOfEachElement);
    }

    @Test
    public void testVariance() {
        int[] constDistribution = new int[] { 0, 0, 5, 0 };
        double variance = SimulationStatistics.variance(constDistribution);
        assertTrue("Variance of constant distribution should be equal 0", variance < EPS);

        int[] variance1Distribution = new int[] { 0, 0, 0, 17, 0, 17 };
        variance = SimulationStatistics.variance(variance1Distribution);
        assertTrue("Variance calculated wrong", Math.abs(variance - 1) < EPS);
    }

}
