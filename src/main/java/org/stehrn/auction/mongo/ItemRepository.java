package org.stehrn.auction.mongo;

import org.bson.Document;
import org.stehrn.auction.model.Bid;
import org.stehrn.auction.model.Bidder;
import org.stehrn.auction.model.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by Nik on 06/08/2015.
 */
class ItemRepository {

    private final ItemDAO itemDAO;

    ItemRepository(ItemDAO itemDAO) {
        this.itemDAO = itemDAO;
    }

    Item create(String name, String description) {
        Document doc = itemDAO.create(name, description);
        return getItem(doc);
    }

    Optional<Item> findItem(String itemId) {
        Document doc = itemDAO.find(itemId);
        Item item = doc != null ? getItem(doc) : null;
        return Optional.ofNullable(item);
    }

    boolean bid(String itemId, long amount, String bidder) {
        return itemDAO.bid(itemId, amount, bidder);
    }

    private Item getItem(Document doc) {
        return new Item(doc.getObjectId("_id").toHexString(), doc.getString("name"), doc.getString("description"));
    }

    public List<Bid> findBids(String itemId) {
        List<Bid> bids = new ArrayList<>();
        Document doc = itemDAO.find(itemId);
        if(doc != null && doc.containsKey("bids"))
        {
            List<Document> bidsDoc = (List<Document>) doc.get("bids");
            bidsDoc.forEach(bid -> bids.add(toBid(bid)));
        }
        return bids;
    }

    private Bid toBid(Document doc)
    {
        Bidder bidder = getBidder("bidder");
        long amount = doc.getLong("amount");
        return Bid.with(bidder, amount);
    }

    private Bidder getBidder(String id)
    {
        return new Bidder(id);
    }
}
