package org.stehrn.auction.mongo;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.types.ObjectId;

import static com.mongodb.client.model.Filters.eq;

/**
 * Created by Nik on 06/08/2015.
 * <p>
 * items
 *   name: String
 *   description: String
 *    bids [
 *    bid
 *     amount: long
 *     bidder: String
 *    ]
 */
class ItemDAO {

    private final MongoCollection<Document> itemsCollection;

    ItemDAO(final MongoDatabase auctionDatabase) {
        itemsCollection = auctionDatabase.getCollection("items");
    }

    Document create(String name, String description) {
        Document item = new Document("name", name).append("description", description);
        itemsCollection.insertOne(item);
        return item;
    }

    Document find(String _id) {
        return itemsCollection.find(eq("_id", new ObjectId(_id))).first();
    }

    boolean bid(String _id, long amount, String bidder) {
        UpdateResult updateResult = itemsCollection.updateOne(eq("_id", new ObjectId(_id)),
                new Document("$push", new Document("bids", new Document("amount", amount).append("bidder", bidder))));
        return updateResult.getModifiedCount() == 1;
    }
}
