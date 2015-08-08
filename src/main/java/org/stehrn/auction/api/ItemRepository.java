package org.stehrn.auction.api;

import org.stehrn.auction.model.Item;

import java.util.Optional;

/**
 * Created by Nik on 08/08/2015.
 */
public interface ItemRepository {

    Item createItem(String name, String description);

    Optional<Item> findItem(String itemId);
}
