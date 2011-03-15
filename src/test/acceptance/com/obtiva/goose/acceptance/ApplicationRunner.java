package com.obtiva.goose.acceptance;

import com.obtiva.goose.controller.AuctionConstants;

/**
 * In the book, this class would start a new Swing application. In this
 * implementation, it more closely represents a single browser session.
 */
public class ApplicationRunner {
	private AuctionSniperDriver driver;

	/* actions */
	public void startBiddingIn(final FakeAuctionServer auction) {
		driver = new AuctionSniperDriver(auction.getItemId());
	}

	/* assertions */
	public void showsSniperIsJoining(FakeAuctionServer auction) {
		driver.showsSniperStatus(String.format(AuctionConstants.STATUS_JOINING, auction.getItemId()));
	}

	public void showsSniperHasLost(FakeAuctionServer auction) {
		driver.showsSniperStatus(String.format(AuctionConstants.STATUS_LOST, auction.getItemId()));
	}

	public void hasShownSniperIsBiddingIn(FakeAuctionServer auction) {
		driver.showsSniperStatus(String.format(AuctionConstants.STATUS_BIDDING, auction.getItemId()));
	}

	public void closeBrowser() {
		driver.closeBrowser();
	}

}
