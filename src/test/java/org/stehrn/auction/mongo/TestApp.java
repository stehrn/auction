package org.stehrn.auction.mongo;

import org.stehrn.auction.api.Auction;
import org.stehrn.auction.api.AuctionHouse;
import org.stehrn.auction.api.BidsForItem;
import org.stehrn.auction.model.Bidder;
import org.stehrn.auction.model.Item;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;

/**
 * Created by Nik on 06/08/2015.
 */
public class TestApp {

    private ItemRepository itemRepository;

    public static void main(String[] args) throws IOException, InterruptedException {
        if (args.length == 0) {
            new TestApp("mongodb://localhost");
        } else {
            new TestApp(args[0]);
        }
    }

    public TestApp(String mongoURIString) throws IOException, InterruptedException {

        Consumer<String> log = System.out::println;

        itemRepository = new MongoApp(mongoURIString).createItemRepository();

        // create item
        Item fridge = itemRepository.create("Fridge", "As new Samsumg American style fridge");
        log.accept("created: " + fridge);
        log.accept("trying to findItem fridge with id: " + fridge.getId());
        Optional<Item> fridgeItem = itemRepository.findItem(fridge.getId());
        log.accept("found: " + fridgeItem.orElse(null));

        // place bids
        CountDownLatch latch = new CountDownLatch(3);
        bid(latch, mongoURIString, "nik", fridge, 100, 200, 101, 102, 104, 105, 107);
        bid(latch, mongoURIString, "james", fridge, 300, 400, 401, 403, 444, 666, 678);
        bid(latch, mongoURIString, "nerys", fridge, 50, 350, 600, 601, 901, 902, 903, 904);
        latch.await();

        // check result
        MongoAuctionTracker auctionTracker = new MongoAuctionTracker(itemRepository);
        BidsForItem bidsForItem = auctionTracker.getBids(fridgeItem.get());
        log.accept("bids for fridge: " + bidsForItem.history());
        // nerys 904 ?
        log.accept("current winning bid for fridge: " + bidsForItem.currentWinningBid());
    }

    // notice how we start separate apps
    private CompletableFuture<Void> bid(CountDownLatch latch, String mongoURIString, String user, Item item, long... amounts) {
        return CompletableFuture.runAsync(() -> {
                    MongoApp mongoApp = new MongoApp(mongoURIString);
                    ItemRepository itemRepository = mongoApp.createItemRepository();
                    MongoAuctionTracker auctionTracker = new MongoAuctionTracker(itemRepository);

                    Bidder bidder = new Bidder(user);
                    AuctionHouse auctionHouse = auctionTracker.join(bidder);
                    Auction auction = auctionHouse.auctionFor(item);
                    for (long amount : amounts) {
                        auction.bid(amount);
                    }
                    mongoApp.close();
                    latch.countDown();
                }
        );
    }
}
