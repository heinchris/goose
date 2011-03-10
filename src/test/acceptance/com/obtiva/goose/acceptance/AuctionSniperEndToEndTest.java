package com.obtiva.goose.acceptance;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.obtiva.goose.acceptance.util.WebApplication;

public class AuctionSniperEndToEndTest {
	
	private final String sniperId = "Joe Sniper";
	private final FakeAuctionServer auction = new FakeAuctionServer("item-54321");
	private final ApplicationRunner application = new ApplicationRunner();

	@BeforeClass
	public static void startWebServer() throws Exception {
		WebApplication.getInstance().start();
	}

	@AfterClass
	public static void stopWebServer() throws Exception {
		WebApplication.getInstance().stop();
	}
	
	@After
	public void closeBrowser() {
		application.closeBrowser();
	}

	@Test
	public void sniperJoinsAuctionUntilAuctionCloses() throws Exception {
		auction.startSellingItem();
		application.startBiddingIn(auction);
		// this next assertion was buried in the startBidding() method the book,
		// however I thought it should be surfaced in the test to show intent
		application.showsSniperIsJoining(auction);
		auction.hasReceivedJoinRequestFromSniper(sniperId);
		auction.announceClosed();
		application.showsSniperHasLost(auction);
	}
	
	@Test
	public void sniperMakesHigherBidButLoses() throws Exception {
		auction.startSellingItem();

		application.startBiddingIn(auction);
		application.showsSniperIsJoining(auction);
		auction.hasReceivedJoinRequestFromSniper(sniperId);

		auction.reportPrice(1000, 98, "other bidder");
		application.hasShownSniperIsBiddingIn(auction);
		
		auction.hasRecievedBid(1098, "sniper id");
		
		auction.announceClosed();
		application.showsSniperHasLost(auction);
		
	}

}
