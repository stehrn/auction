package org.stehrn.auction.api;

import org.stehrn.auction.model.Bidder;
import org.stehrn.auction.model.Item;

/**
 * Created by Nik on 05/08/2015.
 */
public interface AuctionTracker extends AuctionApp {

    BidsForItem getBids(Item item);

    ItemsForBidder getItems(Bidder bidder);
}
