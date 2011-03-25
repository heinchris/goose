package auctionsniper;

public interface AuctionEventListener {

	void auctionCLosed();
	void currentPrice(int currentPrice, int increment);
	
}
