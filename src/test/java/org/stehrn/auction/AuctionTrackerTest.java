package org.stehrn.auction;

import org.junit.Before;
import org.junit.Test;
import org.stehrn.auction.model.Bid;
import org.stehrn.auction.model.Bidder;
import org.stehrn.auction.model.Item;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

/**
 * Created by Nik on 05/08/2015.
 * <p>
 * TODO:
 * what if you bid against yourself - throw exception?
 */
public class AuctionTrackerTest {

    private Bidder bidder1;
    private Bidder bidder2;
    private Item item1;
    private Item item2;

    private SimpleAuctionTracker tracker;

    @Before
    public void before() {
        bidder1 = new Bidder("Harry");
        bidder2 = new Bidder("Sally");
        item1 = new Item("Fridge");
        item2 = new Item("TV");
        tracker = new SimpleAuctionTracker(s -> {});
    }

    @Test
    public void emptyBidsForItemBeforeBiddingStarts() {
        assertThat(tracker.getBids(item1).history(), is(empty()));
    }

    @Test
    public void noWinningItemBeforeBiddingStarts() {
        Optional<Bid> currentWinningBid = tracker.getBids(item1).currentWinningBid();
        assertNotNull(currentWinningBid);
        assertFalse(currentWinningBid.isPresent());
    }

    @Test
    public void singleBidGeneratesExpectedBidsForItem() {

        bid(bidder1, item1, 50L);
        List<Bid> bids = tracker.getBids(item1).history();
        assertThat(bids, hasSize(1));
        doBidAsserts(bids.get(0), 50L, bidder1);
    }

    @Test
    public void multipleBidsOnSameItemGeneratesExpectedBidsForItem() {
        bid(bidder1, item1, 100);
        bid(bidder2, item1, 101);
        List<Bid> bids = tracker.getBids(item1).history();
        assertThat(bids, hasSize(2));
        doBidAsserts(bids.get(0), 100L, bidder1);
        doBidAsserts(bids.get(1), 101L, bidder2);
    }

    @Test
    public void multipleBidsOnSameItemGeneratesExpectedWinningBid() {
        bid(bidder1, item1, 100);
        doBidAsserts(tracker.getBids(item1).currentWinningBid(), 100L, bidder1);
        bid(bidder2, item1, 101);
        doBidAsserts(tracker.getBids(item1).currentWinningBid(), 101L, bidder2);
    }

    @Test
    public void lowerBidDoesNotWin() {
        bid(bidder1, item1, 100);
        doBidAsserts(tracker.getBids(item1).currentWinningBid(), 100L, bidder1);
        bid(bidder2, item1, 99);
        doBidAsserts(tracker.getBids(item1).currentWinningBid(), 100L, bidder1);
    }

    @Test
    public void multipleBidsOnDifferentItemsGeneratesExpectedWinningBid() {
        bid(bidder1, item1, 100);
        doBidAsserts(tracker.getBids(item1).currentWinningBid(), 100L, bidder1);
        bid(bidder2, item2, 101);
        doBidAsserts(tracker.getBids(item1).currentWinningBid(), 100L, bidder1);
        doBidAsserts(tracker.getBids(item2).currentWinningBid(), 101L, bidder2);
    }

    @Test
    public void emptyItemsForBidderBeforeBiddingStarts()
    {
        assertThat(tracker.getItems(bidder1).items(), is(empty()));
    }

    @Test
    public void expectedItemsForBidderAfterOneBid()
    {
        bid(bidder1, item1, 100);
        Set<Item> items = tracker.getItems(bidder1).items();
        assertThat(items, hasSize(1));
        assertThat(items.iterator().next(), is(item1));
    }

    @Test
    public void expectedItemsForBidderAfterTwoBidsOnSameItem()
    {
        bid(bidder1, item1, 100);
        bid(bidder1, item1, 101);
        Set<Item> items = tracker.getItems(bidder1).items();
        assertThat(items, hasSize(1));
        assertThat(items.iterator().next(), is(item1));
    }

    @Test
    public void expectedItemsForBidderAfterMultipleBids()
    {
        bid(bidder1, item1, 100);
        bid(bidder1, item2, 100);
        bid(bidder2, item2, 100); // should not have influence
        Set<Item> items = tracker.getItems(bidder1).items();
        assertThat(items, hasSize(2));
        assertThat(items, containsInAnyOrder(item1, item2));
    }

    private void doBidAsserts(Optional<Bid> bid, long expectedAmount, Bidder expectedBidder) {
        assertThat(bid.isPresent(), is(true));
        doBidAsserts(bid.get(), expectedAmount, expectedBidder);
    }

    private void doBidAsserts(Bid bid, long expectedAmount, Bidder expectedBidder) {
        assertThat(bid.amount(), is(expectedAmount));
        assertThat(bid.source(), is(expectedBidder));
    }

    private void bid(Bidder bidder, Item item, long amount) {
        tracker.join(bidder).auctionFor(item).bid(amount);
    }


}
