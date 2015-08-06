package org.stehrn.auction;

import org.stehrn.auction.api.AuctionHouse;
import org.stehrn.auction.api.AuctionTracker;
import org.stehrn.auction.api.BidsForItem;
import org.stehrn.auction.api.ItemsForBidder;
import org.stehrn.auction.model.Bidder;
import org.stehrn.auction.model.Item;

import java.util.function.Consumer;

/**
 * Created by Nik on 05/08/2015.
 */
public class SimpleAuctionTracker implements AuctionTracker {

    private final SimpleBidsForItem bidsForItem = new SimpleBidsForItem();
    private final SimpleItemsForBidder itemsForBidder = new SimpleItemsForBidder();

    private final Consumer<String> logger;

    public SimpleAuctionTracker(Consumer<String> logger) {
        this.logger = logger;
    }

    public AuctionHouse join(Bidder bidder) {
        AuctionHouseForBidder auctionHouse = new AuctionHouseForBidder(bidder);
        auctionHouse.addListener(bidsForItem);
        auctionHouse.addListener(itemsForBidder);
        auctionHouse.addListener((item, bid) -> logger.accept((String.format("Bid for item: %s, bid: %s", item, bid))));
        return auctionHouse;
    }

    @Override
    public BidsForItem getBids(Item item) {
        return bidsForItem.getBids(item);
    }

    @Override
    public ItemsForBidder getItems(Bidder bidder) {
        return itemsForBidder.getItems(bidder);
    }
}
