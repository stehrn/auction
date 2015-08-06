package org.stehrn.auction.mongo;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;

/**
 * Created by Nik on 06/08/2015.
 */
public class MongoApp {

    private final MongoClient mongoClient;
    private final MongoDatabase auctionDatabase;

    public MongoApp() {
        this("mongodb://localhost");
    }

    public MongoApp(String mongoURIString) {
        mongoClient = new MongoClient(new MongoClientURI(mongoURIString));
        auctionDatabase = mongoClient.getDatabase("auction");
    }

    public ItemRepository createItemRepository() {
        return new ItemRepository(new ItemDAO(auctionDatabase));
    }

    public void close()
    {
        mongoClient.close();
    }
}
