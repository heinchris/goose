package auctionsniper;

import java.util.HashMap;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class AuctionMessageTranslator implements MessageListener {

	private final static Log log = LogFactory.getLog(AuctionMessageTranslator.class);
	private final String itemId;
	private final AuctionEventListener auctionEventListener;

	public AuctionMessageTranslator(AuctionEventListener auctionEventListener, String itemId) {
		this.auctionEventListener = auctionEventListener;
		this.itemId = itemId;
	}

	@Override
	public void onMessage(Message message) {
		Map<String, String> event = unpackEventFromMessage(message);
		String type = event.get("Event");
		if ("CLOSE".equals(type)) {
			auctionEventListener.auctionCLosed();
		} else if ("PRICE".equals(type)) {
			auctionEventListener.currentPrice(Integer.parseInt(event.get("CurrentPrice")), Integer.parseInt(event.get("Increment")));
		}
		if (isEvent(type)) {
			log.info("\n<<<< " + getMessageData(message) + "\n");
		}
	}

	private boolean isEvent(String type) {
		return type != null;
	}
	
	private Map<String, String> unpackEventFromMessage(Message message) {
		Map<String, String> event = new HashMap<String, String>();
		for (String element : getMessageData(message).split(";")) {
			String[] pair = element.split(":");
			event.put(pair[0].trim(), pair[1].trim());
		}
		return event;
	}
	
	private String getMessageData(Message message) {
		try {
			return ((TextMessage)message).getText();
		} catch (JMSException e) {
			throw new RuntimeException(e);
		}
	}

}
