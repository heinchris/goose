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

	private final AuctionEventListener auctionEventListener;

	public AuctionMessageTranslator(AuctionEventListener auctionEventListener) {
		this.auctionEventListener = auctionEventListener;
	}

	@Override
	public void onMessage(Message message) {
		AuctionEvent event = AuctionEvent.from(message);
		String type = event.type();
		if ("CLOSE".equals(type)) {
			auctionEventListener.auctionCLosed();
		} else if ("PRICE".equals(type)) {
			auctionEventListener.currentPrice(event.price(), event.increment());
		}
	}

}

class AuctionEvent {
	
	private final static Log log = LogFactory.getLog(AuctionEvent.class);
	private Map<String, String> events = new HashMap<String, String>();
	
	public String type() { return events.get("Event"); }
	public int price() { return parseInt(events.get("CurrentPrice")); }
	public int increment() { return parseInt(events.get("Increment")); }

	private AuctionEvent(Message message) {
		unpackEventFromMessage(message);
		if (isEvent(type())) {
			log.info("\n<<<< " + getMessageData(message) + "\n");
		}
	}
	
	private int parseInt(String value) {
		return Integer.parseInt(value);
	}
	
	private void unpackEventFromMessage(Message message) {
		for (String element : getMessageData(message).split(";")) {
			String[] pair = element.split(":");
			events.put(pair[0].trim(), pair[1].trim());
		}
	}

	private String getMessageData(Message message) {
		try {
			return ((TextMessage)message).getText();
		} catch (JMSException e) {
			throw new RuntimeException(e);
		}
	}

	private boolean isEvent(String type) {
		return type != null;
	}

	public static AuctionEvent from(Message message) {
		return new AuctionEvent(message);
	}

}

