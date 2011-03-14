package com.obtiva.goose.acceptance;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.obtiva.goose.acceptance.util.WebApplication;

public class AuctionSniperEndToEndTest {

	private FakeAuctionServer auction = new FakeAuctionServer("item-54321");
	private ApplicationRunner application = new ApplicationRunner();

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
		auction.hasReceivedJoinRequestFromSniper();
		auction.announceClosed();
		application.showsSniperHasLost(auction);
	}

}
