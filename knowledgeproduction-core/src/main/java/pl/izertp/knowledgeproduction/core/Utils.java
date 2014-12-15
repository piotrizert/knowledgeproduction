package pl.izertp.knowledgeproduction.core;

import java.util.Random;
import java.util.Set;

public class Utils {

    /**
     * Returns a randomly chosen element from a set.
     * 
     * @param set a set from which to choose
     * @return chosen element, null if set is empty
     */
    public static <T> T getRandomSetElement(Set<T> set) {
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
        throw new IllegalStateException("This piece of code should never be reached");
    }

}
