package org.stehrn.auction.mongo;

import org.stehrn.auction.AuctionHouseForBidder;
import org.stehrn.auction.api.AuctionHouse;
import org.stehrn.auction.api.AuctionTracker;
import org.stehrn.auction.api.BidsForItem;
import org.stehrn.auction.api.ItemsForBidder;
import org.stehrn.auction.model.Bid;
import org.stehrn.auction.model.Bidder;
import org.stehrn.auction.model.Item;

import java.util.List;
import java.util.Optional;

import static java.util.Comparator.comparing;

/**
 * Created by Nik on 06/08/2015.
 */
class MongoAuctionTracker implements AuctionTracker {

    private final MongoItemRepository itemRepository;

    MongoAuctionTracker(MongoItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Override
    public AuctionHouse join(Bidder bidder) {
        AuctionHouseForBidder auctionHouse = new AuctionHouseForBidder(bidder);
        auctionHouse.addListener((item, bid) -> itemRepository.bid(item.getId(), bid.amount(), bid.source().getName()));
        return auctionHouse;
    }

    @Override
    public BidsForItem getBids(Item item) {
        // this is essentially a live view of data
        return new BidsForItem() {
            @Override
            public List<Bid> history() {
                return itemRepository.findBids(item.getId());
            }

            @Override
            public Optional<Bid> currentWinningBid() {
                return history().stream().max(comparing(Bid::amount));
            }
        };
    }

    @Override
    public ItemsForBidder getItems(Bidder bidder) {
        throw new UnsupportedOperationException("not implemented yet!");
    }
}
