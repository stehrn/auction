package org.stehrn.auction;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.stehrn.auction.simple.AuctionTrackerTest;

/**
 * Created by Nik on 05/08/2015.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        AuctionTrackerTest.class
})
public class TestSuite {
}
