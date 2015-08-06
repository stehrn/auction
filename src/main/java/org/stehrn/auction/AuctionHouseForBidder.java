package org.stehrn.auction;

import org.stehrn.auction.api.Auction;
import org.stehrn.auction.api.AuctionHouse;
import org.stehrn.auction.api.BidListener;
import org.stehrn.auction.model.Bid;
import org.stehrn.auction.model.Bidder;
import org.stehrn.auction.model.Item;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Nik on 05/08/2015.
 */
public class AuctionHouseForBidder implements AuctionHouse {

    private final Bidder bidder;
    private final List<BidListener> listeners = new CopyOnWriteArrayList<>();

    public AuctionHouseForBidder(Bidder bidder) {
        this.bidder = bidder;
    }

    // TODO - nothing stopping same bidder calling this multiple times for same item, although I dont think there is any impact to end result its something we may want to guard against
    @Override
    public Auction auctionFor(Item item) {
        return amount -> notifyListeners(item, Bid.with(bidder, amount));
    }

    public void addListener(BidListener listener) {
        listeners.add(listener);
    }

    private void notifyListeners(Item item, Bid bid) {
        listeners.forEach(l -> l.bid(item, bid));
    }
}
