package auctionsniper;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.obtiva.goose.acceptance.FakeAuctionServer;
import com.obtiva.goose.controller.AuctionWebSocket;

public class AuctionMessageTranslator implements MessageListener {

	private final static Log log = LogFactory.getLog(AuctionMessageTranslator.class);
	private final AuctionWebSocket auctionWebSocket;

	public AuctionMessageTranslator(AuctionWebSocket auctionWebSocket) {
		this.auctionWebSocket = auctionWebSocket;
	}

	@Override
	public void onMessage(Message message) {
		try {
			String text = ((TextMessage)message).getText();
			log.info("\n<<<< " + text + "\n");
			auctionWebSocket.sendMessage(text);
		} catch (JMSException e) {
			throw new RuntimeException(e);
		}
	}

}
