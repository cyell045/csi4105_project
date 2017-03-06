/**
 * Created by celineyelle on 2017-03-04.
 */

import java.util.*;

public class Set {

    private int n;
    private boolean[] set;

    /**
     * This is the constructor for the Set.
     **/
    public Set(int size) {

        n = size;
        set = new boolean[n];
        for (int i = 0; i < n; i++)
            set[i] = false;
    }


    /**
     * Copy constructor for duplicating a set.
     **/
    public Set(Set s) {
        n = s.n;
        set = new boolean[n];
        for (int i = 0; i < n; i++)
            set[i] = s.set[i];
    }

    /**
     * This is the default constructor for the Set.
     * If no argument is provided, it will construct
     * an empty set that has the potential to hold 100 elements.
     **/
    public Set() {

        this(100);
    }


    /**
     * This inserts an element into the set.
     **/
    public void insert(int x) {
        if (x > 0 && x <= n)
            set[x - 1] = true;
    }

    /**
     * This removes an element from the set.
     **/
    public void delete(int x) {
        if (x > 0 && x <= n)
            set[x - 1] = false;
    }

    /**
     * Test an element for membership.
     **/
    public boolean isMember(int x) {
        if (x > 0 && x <= n)
            return set[x - 1];
        else
            return false;
    }

    /**
     * Test the set to see if it is empty.
     **/
    public boolean isEmpty() {
        boolean empty = true;
        for (int i = 0; i < n; i++)
            empty = empty && !set[i];

        return empty;
    }

    /**
     * Combine another set with this one using the union operation.
     **/
    public void union(Set s) {
        for (int i = 0; i < n; i++)
            set[i] = set[i] || s.set[i];

    }

    /**
     * Combine another set with this one using the intersection operation.
     **/
    public void intersection(Set s) {
        for (int i = 0; i < n; i++)
            set[i] = set[i] && s.set[i];
    }

    /**
     * Return the number of elements in the set.
     **/
    public int size() {
        int size = 0;
        for (int i = 0; i < n; i++)
            if (set[i])
                size++;

        return size;
    }

    /**
     * Print out all the elements in this set.
     **/
    public void print() {
        System.out.print("{");
        for (int i = 0; i < n; i++)
            if (set[i])
                System.out.print((i + 1) + ", ");

        System.out.println("}");
    }
}

