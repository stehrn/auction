package org.stehrn.auction.model;

import java.util.Objects;

/**
 * Created by Nik on 04/08/2015.
 */
public class Item {

    private final String _id;
    private final String name;
    private final String description;

    public Item(String _id, String name) {
        this(_id, name, null);
    }

    public Item(String _id, String name, String description) {
        this._id = _id;
        this.name = name;
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return Objects.equals(_id, item._id);
    }

    public String getId() {
        return _id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(_id);
    }

    @Override
    public String toString() {
        return name + " [" + description + "]";
    }
}
