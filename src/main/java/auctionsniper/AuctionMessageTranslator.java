package auctionsniper;

import javax.jms.Message;
import javax.jms.MessageListener;

import com.obtiva.goose.controller.AuctionWebSocket;

public class AuctionMessageTranslator implements MessageListener {

	private final AuctionWebSocket auctionWebSocket;

	public AuctionMessageTranslator(AuctionWebSocket auctionWebSocket) {
		this.auctionWebSocket = auctionWebSocket;
	}

	@Override
	public void onMessage(Message message) {
		// TODO Auto-generated method stub
		
	}

}
