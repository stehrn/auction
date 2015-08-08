package org.stehrn.auction.api;

import org.stehrn.auction.model.Bidder;

/**
 * Created by Nik on 08/08/2015.
 */
@FunctionalInterface
public interface AuctionApp {
    AuctionHouse join(Bidder bidder);
}
