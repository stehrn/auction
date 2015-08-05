package org.stehrn.auction.api;

import org.stehrn.auction.model.Bid;

import java.util.List;
import java.util.Optional;

/**
 * Created by Nik on 05/08/2015.
 */
public interface BidsForItem {

    List<Bid> history();

    Optional<Bid> currentWinningBid();
}
