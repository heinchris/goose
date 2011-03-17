package auctionsniper;

public class AuctionSniper implements AuctionEventListener {

	private final SniperListener sniperListener;
	private final Auction auction;

	public AuctionSniper(Auction auction, SniperListener sniperListener) {
		this.auction = auction;
		this.sniperListener = sniperListener;
	}

	@Override
	public void auctionCLosed() {
		sniperListener.sniperLost();
	}

	@Override
	public void currentPrice(int currentPrice, int increment) {
		auction.bid(currentPrice + increment);
		sniperListener.sniperBidding();
	}

}
