package org.stehrn.auction;

import org.stehrn.auction.api.Auction;
import org.stehrn.auction.api.AuctionHouse;
import org.stehrn.auction.api.BidsForItem;
import org.stehrn.auction.model.Bidder;
import org.stehrn.auction.model.Item;

import java.util.function.Consumer;

/**
 * Created by Nik on 05/08/2015.
 */
public class TestApp {

    public static void main(String[] args) {
        Item item1 = new Item("Boring book about C++");
        Item item2 = new Item("Effective Java");

        Bidder nerys = new Bidder("Nerys");
        Bidder nik = new Bidder("Nik");

        Consumer<String> log = System.out::println;

        SimpleAuctionTracker auctionTracker = new SimpleAuctionTracker(log);
        AuctionHouse auctionHouseForNik = auctionTracker.join(nik);
        Auction item1AuctionForNik = auctionHouseForNik.auctionFor(item1);
        Auction item2AuctionForNik = auctionHouseForNik.auctionFor(item2);

        AuctionHouse auctionHouseForNerys = auctionTracker.join(nerys);
        Auction item1AuctionForNerys = auctionHouseForNerys.auctionFor(item1);

        item1AuctionForNik.bid(100);
        item1AuctionForNerys.bid(150);
        item1AuctionForNik.bid(101);

        item2AuctionForNik.bid(40);
        item2AuctionForNik.bid(20);

        BidsForItem item1Bids = auctionTracker.getBids(item1);
        // Get all the bids for an item
        log.accept("item1 history: " + item1Bids.history());
        // Get the current winning bid for an item
        log.accept("item1 winning bid: " + item1Bids.currentWinningBid());

        BidsForItem item2Bids = auctionTracker.getBids(item2);
        log.accept("item2 history: " + item2Bids.history());
        log.accept("item2 winning bid: " + item2Bids.currentWinningBid());

        // Get all the items on which a user has bid
        log.accept("Nerys items: " + auctionTracker.getItems(nerys).items());
        log.accept("Niks items: " + auctionTracker.getItems(nik).items());
    }
}
