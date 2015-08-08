package org.stehrn.auction.mongo;

import org.stehrn.auction.api.*;
import org.stehrn.auction.model.Bidder;
import org.stehrn.auction.model.Item;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

/**
 * Created by Nik on 06/08/2015.
 */
public class TestApp {

    public static void main(String[] args) throws IOException, InterruptedException {
        if (args.length == 0) {
            new TestApp("mongodb://localhost");
        } else {
            new TestApp(args[0]);
        }
    }

    public TestApp(String mongoURIString) throws IOException, InterruptedException {

        Consumer<String> log = System.out::println;

        MongoApp mongoApp = new MongoApp(mongoURIString);
        ItemRepository itemRepository = mongoApp.createItemRepository();

        // createItem item
        Item fridge = itemRepository.createItem("TV", "Sony TV, plasma");
        log.accept("created: " + fridge);
        log.accept("trying to findItem with id: " + fridge.getId());
        Optional<Item> fridgeItem = itemRepository.findItem(fridge.getId());
        log.accept("found: " + fridgeItem.orElse(null));

        // place bids
        CompletableFuture.allOf(
                bid(mongoURIString, "nik", fridge, 100, 200, 101, 102, 104, 105, 107),
                bid(mongoURIString, "james", fridge, 300, 400, 401, 403, 444, 666, 678),
                bid(mongoURIString, "nerys", fridge, 50, 350, 600, 601, 901, 902, 903, 904)
                ).join();

        // check result
        AuctionTracker auctionTracker = mongoApp.createAuctionTracker();
        BidsForItem bidsForItem = auctionTracker.getBids(fridgeItem.get());
        log.accept("bids for fridge: " + bidsForItem.history());
        // nerys 904 ?
        log.accept("current winning bid: " + bidsForItem.currentWinningBid());
    }

    // notice how we start separate apps
    private CompletableFuture<Void> bid(String mongoURIString, String user, Item item, long... amounts) {
        return CompletableFuture.runAsync(() -> {
                    try (MongoApp mongoApp = new MongoApp(mongoURIString)) {

                        AuctionTracker auctionTracker = mongoApp.createAuctionTracker();
                        Bidder bidder = new Bidder(user);
                        AuctionHouse auctionHouse = auctionTracker.join(bidder);
                        Auction auction = auctionHouse.auctionFor(item);
                        for (long amount : amounts) {
                            auction.bid(amount);
                        }
                    }
                }
        );
    }
}
