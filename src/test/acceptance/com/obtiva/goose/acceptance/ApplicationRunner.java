package com.obtiva.goose.acceptance;

/**
 * In the book, this class would start a new Swing application. In this
 * implementation, it more closely represents a single browser session.
 */
public class ApplicationRunner {

	private static final String STATUS_JOINING = "Joining auction for item %1s";
	public static final String STATUS_LOST = "Lost auction for item %1s";
	private static final String STATUS_BIDDING = "Bidding for item %1s";

	private AuctionSniperDriver driver;

	/* actions */
	public void startBiddingIn(final FakeAuctionServer auction) {
		driver = new AuctionSniperDriver(auction.getItemId());
	}

	/* assertions */
	public void showsSniperIsJoining(FakeAuctionServer auction) {
		driver.showsSniperStatus(String.format(STATUS_JOINING,
				auction.getItemId()));
	}

	public void showsSniperHasLost(FakeAuctionServer auction) {
		driver.showsSniperStatus(String.format(STATUS_LOST, auction.getItemId()));
	}

	public void hasShownSniperIsBiddingIn(FakeAuctionServer auction) {
		driver.showsSniperStatus(String.format(STATUS_BIDDING,
				auction.getItemId()));
	}

	public void closeBrowser() {
		driver.closeBrowser();
	}

}
