package org.stehrn.auction.simple;

import org.stehrn.auction.api.BidListener;
import org.stehrn.auction.api.ItemsForBidder;
import org.stehrn.auction.model.Bid;
import org.stehrn.auction.model.Bidder;
import org.stehrn.auction.model.Item;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Nik on 05/08/2015.
 */
class SimpleItemsForBidder implements BidListener {

    private final Map<Bidder, ItemsForBidderImpl> itemsForBidder = new ConcurrentHashMap<>();

    @Override
    public void bid(Item item, Bid bid) {
        getItemsCreateIfAbsent(bid.source()).add(item);
    }

    ItemsForBidder getItems(Bidder bidder) {
        return getItemsCreateIfAbsent(bidder);
    }

    private ItemsForBidderImpl getItemsCreateIfAbsent(Bidder bidder) {
        return itemsForBidder.computeIfAbsent(bidder, k -> new ItemsForBidderImpl());
    }

    private class ItemsForBidderImpl implements ItemsForBidder {
        private final Set<Item> items = Collections.synchronizedSet(new HashSet<>());

        @Override
        public Set<Item> items() {
            return Collections.unmodifiableSet(items);
        }

        void add(Item item) {
            items.add(item);
        }
    }
}
