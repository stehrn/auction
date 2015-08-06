package org.stehrn.auction;

import org.stehrn.auction.api.BidListener;
import org.stehrn.auction.api.BidsForItem;
import org.stehrn.auction.model.Bid;
import org.stehrn.auction.model.Item;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by Nik on 05/08/2015.
 */
class SimpleBidsForItem implements BidListener {

    private final Map<Item, Bids> bidsForItem = new ConcurrentHashMap<>();

    @Override
    public void bid(Item item, Bid bid) {
        getBidsCreateIfAbsent(item).add(bid);
    }

    BidsForItem getBids(Item item) {
        return getBidsCreateIfAbsent(item);
    }

    private Bids getBidsCreateIfAbsent(Item item) {
        return bidsForItem.computeIfAbsent(item, k -> new Bids());
    }

    private class Bids implements BidsForItem {
        private final List<Bid> bids = Collections.synchronizedList(new ArrayList<>());
        private final AtomicReference<Bid> winningPrice = new AtomicReference<>();

        void add(Bid bid) {
            winningPrice.getAndAccumulate(bid, (previous, update) -> update.highest(previous));
            bids.add(bid);
        }

        @Override
        public List<Bid> history() {
            return Collections.unmodifiableList(bids);
        }

        @Override
        public Optional<Bid> currentWinningBid() {
            return Optional.ofNullable(winningPrice.get());
        }
    }
}
