package org.stehrn.auction.api;

import org.stehrn.auction.model.Item;

/**
 * Created by Nik on 05/08/2015.
 */
@FunctionalInterface
public interface AuctionHouse {
    Auction auctionFor(Item item);
}
