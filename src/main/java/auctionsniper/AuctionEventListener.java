package auctionsniper;

public interface AuctionEventListener {

	void sendMessage(String message);
	void auctionCLosed();
	void currentPrice(int currentPrice, int increment);
	
}
