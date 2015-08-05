package org.stehrn.auction.api;

import org.stehrn.auction.model.Bid;
import org.stehrn.auction.model.Item;

/**
 * Created by Nik on 05/08/2015.
 */
@FunctionalInterface
public interface BidListener {
    void bid(Item item, Bid bid);
}
