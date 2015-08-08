package org.stehrn.auction.mongo;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;
import org.stehrn.auction.api.AuctionTracker;
import org.stehrn.auction.api.ItemRepository;

import java.io.Closeable;

/**
 * Created by Nik on 06/08/2015.
 */
public class MongoApp implements Closeable {

    private final MongoClient mongoClient;
    private final MongoDatabase auctionDatabase;

    public MongoApp() {
        this("mongodb://localhost");
    }

    public MongoApp(String mongoURIString) {
        mongoClient = new MongoClient(new MongoClientURI(mongoURIString));
        auctionDatabase = mongoClient.getDatabase("auction");
    }

    public AuctionTracker createAuctionTracker()
    {
        return new MongoAuctionTracker(createItemRepository());
    }

    public MongoItemRepository createItemRepository() {
        return new MongoItemRepository(new ItemDAO(auctionDatabase));
    }

    @Override
    public void close()
    {
        mongoClient.close();
    }
}
