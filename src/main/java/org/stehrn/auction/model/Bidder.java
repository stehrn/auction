package org.stehrn.auction.model;

import java.util.Objects;

/**
 * Created by Nik on 05/08/2015.
 */
public class Bidder {

    private final String name;

    public Bidder(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Bidder bidder = (Bidder) o;
        return Objects.equals(name, bidder.name);
    }

    public String getName() {
        return name;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return name;
    }
}
