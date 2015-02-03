package pl.izertp.knowledgeproduction.core;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;

/**
 * A class of a single agent - an unit which can develop knowledge and propagate knowledge
 * to other agents.
 * 
 * @author Piotr Izert
 */
public class Agent {

    /**
     * Size of the knowledge set.
     */
    @Getter
    private int knowledgeSize;

    /**
     * Structure of the knowledge (with the hypergraph).
     */
    @Getter
    private KnowledgeStructure knowledgeStructure;

    /**
     * KnowledgeStructure setter. It also sets knowledgeSize.
     * 
     * @param knowledgeStructure
     */
    public void setKnowledgeStructure(KnowledgeStructure knowledgeStructure) {
        this.knowledgeStructure = knowledgeStructure;
        this.knowledgeSize = knowledgeStructure.getSize();
    }

    /**
     * Chance of producing knowledge == (1 - propagationChance).
     */
    @Getter
    @Setter
    private double productionChance;

    /**
     * Represents current state of agent's knowledge.
     */
    private boolean[] knowledgeSet;

    public void setKnowledgeSet(boolean[] knowledgeSet) {
        this.knowledgeSet = knowledgeSet;
        this.knowledgeSize = knowledgeSet.length;
    }

    /**
     * Elements of knowledge possible to develop with the present state of knowledge,
     * which arent already developed.
     */
    private Set<Integer> possibleElements;

    /**
     * True, if the agent will only share his knowledge in a trade process, i.e.
     * it will only share his knowledge if it gets a new piece of knowledge in exchange.
     * False, if the agent will share its knowledge "for free".
     */
    @Getter
    @Setter
    private boolean trade;
    
    @Getter
    private int[] gotFrom;

    /**
     * Creates an Agent object.
     * The agent gets randomly selected elements from the set of base elements of given
     * KnowledgeStructure. A chance of selecting any of the base elements is given as
     * a parameter.
     * 
     * @param knowledgeStructure structure of knowledge
     * @param baseElementChance chance at which base elements will be selected ,must be [0,1]
     * @param productionChance chance of producing knowledge == (1 - propagationChance), must be [0,1]
     */
    public Agent(KnowledgeStructure knowledgeStructure, double baseElementChance, double productionChance, double tradeProbability) {
        // TODO: check args 0-1
        this.knowledgeStructure = knowledgeStructure;
        this.knowledgeSize = knowledgeStructure.getSize();
        this.productionChance = productionChance;
        this.trade = tradeProbability > new Random().nextDouble();
        this.gotFrom = new int[knowledgeSize];
        this.knowledgeSet = new boolean[knowledgeSize];

        // set the initial knowledge
        Random random = new Random();
        for (int i = 0; i < knowledgeStructure.getBaseSize(); i++) {
            if (random.nextDouble() < baseElementChance) {
                knowledgeSet[i] = true;
            }
        }

        // initialize the set of elements possible to develop
        initAgent();
    }

    /**
     * Empty constructor. Note, that knowledgeStructure, knowledgeSet and
     * productionChance must be set manually.
     * After setting initial knowledge set and graph, initAgent method must be called.
     */
    public Agent() {

    }

    /**
     * Method, which initializes agent's possibleElements set.
     */
    public void initAgent() {
        possibleElements = new HashSet<Integer>();
        for (int i = 0; i < knowledgeSize; i++) {
            if (!knowledgeSet[i])
                continue;
            for (int j = i + 1; j < knowledgeSize; j++) {
                if (!knowledgeSet[j])
                    continue;
                possibleElements.addAll(knowledgeStructure.getResultElements(i, j));
            }
        }
    }

    /**
     * Adds an element of knowledge to the agent's knowledge set.
     * 
     * @param n index of knowledge element
     * @return true if element was already present, false if not
     */
    public boolean addKnowledgeElement(int n) {
        if (n >= knowledgeSize) {
            throw new IndexOutOfBoundsException("Index of knowledge element to add out of bounds");
        }
        if (knowledgeSet[n]) {
            return true;
        }
        knowledgeSet[n] = true;
        for (int i : getHaveKnowledge()) {
            possibleElements.addAll(knowledgeStructure.getResultElements(n, i));
        }
        return false;
    }

    /**
     * Single step of knowledge development - adds a randomly chosen piece of knowledge
     * possible to develop basing on current state of knowledge.
     * 
     * @return index of produced element, -1, if nothing was produced
     */
    public int produceKnowledge() {
        Integer element = Utils.getRandomSetElement(possibleElements);
        if (element != null) {
            this.addKnowledgeElement(element);
            possibleElements.remove(element);
            this.gotFrom[element] = -1;
            return element;
        }
        return -1;
    }

    /**
     * Returns the list of knowledge elements this agent has.
     * 
     * @return list of knowledge elements this agent has
     */
    public Set<Integer> getHaveKnowledge() {
        Set<Integer> haveKnowledge = new HashSet<Integer>();
        for (int i = 0; i < knowledgeSize; i++) {
            if (knowledgeSet[i])
                haveKnowledge.add(i);
        }
        return haveKnowledge;
    }

    /**
     * Returns the list of knowledge elements this agent doesnt have.
     * 
     * @return list of knowledge elements this agent doesnt have
     */
    public Set<Integer> getDoesntHaveKnowledge() {
        Set<Integer> doesntHaveKnowledge = new HashSet<Integer>();
        for (int i = 0; i < knowledgeSize; i++) {
            if (!knowledgeSet[i])
                doesntHaveKnowledge.add(i);
        }
        return doesntHaveKnowledge;
    }

    /**
     * Checks, if this agent has knowledge element given as parameter.
     * 
     * @param i index of the element
     * @return true, if the agent has this element, false otherwise
     */
    public boolean hasKnowledgeElement(int i) {
        return knowledgeSet[i];
    }

    /**
     * Returns total number of agent's knowledge elements.
     * 
     * @return total number of knowledge elements
     */
    public int getKnowledgeTotalCount() {
        return getHaveKnowledge().size();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("--AGENT--\n");
        sb.append("KnowledgeStructue graph:\n");
        sb.append(this.knowledgeStructure);
        sb.append("Knowledge set:\n");
        for (int i = 0; i < knowledgeSize; i++) {
            sb.append(String.format("%d\t", i));
        }
        sb.append("\n");
        for (int i = 0; i < knowledgeSize; i++) {
            sb.append(String.format("%b\t", knowledgeSet[i]));
        }
        sb.append("\nElements possible to develop:\n");
        sb.append(possibleElements);
        return sb.toString();
    }

}
