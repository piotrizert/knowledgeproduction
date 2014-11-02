package pl.izertp.knowledgeproduction.core;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class Agent {

    private int knowledgeSize;

    private KnowledgeStructure knowledgeStructure;

    /**
     * Represents current state of agent's knowledge.
     */
    private boolean[] knowledgeSet;

    /**
     * Elements of knowledge possible to develop with the present state of knowledge,
     * which arent already developed.
     */
    private Set<Integer> possibleElements;

    /**
     * Creates an Agent object.
     * The agent gets randomly selected elements from the set of base elements of given
     * KnowledgeStructure. A chance of selecting any of the base elements is given as
     * a parameter.
     * 
     * @param knowledgeStructure structure of knowledge
     * @param chance chance at which base elements will be selected (must be [0,1])
     */
    public Agent(KnowledgeStructure knowledgeStructure, double chance) {
        this.knowledgeStructure = knowledgeStructure;
        this.knowledgeSize = knowledgeStructure.getSize();
        knowledgeSet = new boolean[knowledgeSize];

        // set the initial knowledge
        Random random = new Random();
        for (int i = 0; i < knowledgeStructure.getBaseSize(); i++) {
            if (random.nextDouble() < chance) {
                knowledgeSet[i] = true;
            }
        }

        // initialise the set of elements possible to develop
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
        for (int i = 0; i < knowledgeSize; i++) {
            possibleElements.addAll(knowledgeStructure.getResultElements(n, i));
        }
        return false;
    }

    /**
     * Single step of knowledge development - adds a randomly chosen piece of knowledge
     * possible to develop basing on current state of knowledge.
     */
    public boolean produceKnowledge() {
        Integer element = getRandomSetElement(possibleElements);
        if (element != null) {
            this.addKnowledgeElement(element);
            possibleElements.remove(element);
            return true;
        }
        return false;
    }

    /**
     * Returns the list of knowledge elements this agent has.
     * 
     * @return list of knowledge elements this agent has
     */
    public List<Integer> getHaveKnowledge() {
        List<Integer> haveKnowledge = new ArrayList<Integer>();
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
    public List<Integer> getDoesntHaveKnowledge() {
        List<Integer> doesntHaveKnowledge = new ArrayList<Integer>();
        for (int i = 0; i < knowledgeSize; i++) {
            if (!knowledgeSet[i])
                doesntHaveKnowledge.add(i);
        }
        return doesntHaveKnowledge;
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

    /**
     * Returns a randomly chosen element from a set.
     * 
     * @param set a set from which to choose
     * @return chosen element, null if set is empty
     */
    private <T> T getRandomSetElement(Set<T> set) {
        int size = set.size();
        if (size == 0)
            return null;
        int index = new Random().nextInt(size);
        int i = 0;
        for (T obj : set) {
            if (i == index)
                return obj;
            i = i + 1;
        }
        return null; // never reached
    }
}
