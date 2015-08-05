package org.stehrn.auction.api;

import org.stehrn.auction.model.Item;

import java.util.Set;

/**
 * Created by Nik on 05/08/2015.
 */
@FunctionalInterface
public interface ItemsForBidder {

    Set<Item> items();
}
