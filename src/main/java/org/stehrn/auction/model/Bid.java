package org.stehrn.auction.model;

/**
 * Created by Nik on 04/08/2015.
 */
public class Bid {

    private final Bidder source;
    private final long amount;

    private Bid(Bidder bidder, long amount) {
        this.source = bidder;
        this.amount = amount;
    }

    public static Bid with(Bidder bidder, long amount) {
        return new Bid(bidder, amount);
    }

    public Bidder source() {
        return source;
    }

    public long amount() {
        return amount;
    }

    public Bid highest(Bid other) {
        return isHigherThan(other) ? this : other;
    }

    private boolean isHigherThan(Bid bid) {
        return bid == null || amount() > bid.amount();
    }

    @Override
    public String toString() {
        return "Bid{" + "source=" + source + ", amount=" + amount + '}';
    }
}
