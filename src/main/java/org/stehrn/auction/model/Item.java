package org.stehrn.auction.model;

import java.util.Objects;

/**
 * Created by Nik on 04/08/2015.
 */
public class Item {

    private final String description;

    public Item(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return Objects.equals(description, item.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description);
    }

    @Override
    public String toString() {
        return description;
    }
}
