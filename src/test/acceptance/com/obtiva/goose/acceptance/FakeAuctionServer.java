package com.obtiva.goose.acceptance;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hamcrest.Matcher;

import com.obtiva.goose.acceptance.util.JmsUtils;
import com.obtiva.goose.controller.AuctionConstants;

public class FakeAuctionServer {
	
	private final static Log log = LogFactory.getLog(FakeAuctionServer.class);

	private final SingleMessageListener messageListener = new SingleMessageListener();
	private JmsUtils jmsUtils = new JmsUtils();
	private String itemId;

	public FakeAuctionServer(String itemId) {
		this.itemId = itemId;
	}

	public void stop() {
		jmsUtils.removeMessageListener(messageListener);
	}

	public String getItemId() {
		return itemId;
	}

	/* actions */
	public void startSellingItem() {
		jmsUtils.addMessageListener(messageListener);
	}
	
	public void reportPrice(int price, int increment, String bidder) {
		jmsUtils.sendMessage(String.format("SOLVersion: 1.1; Event: PRICE; CurrentPrice: %d; Increment: %d; Bidder: %s;", price, increment, bidder));
	}

	public void announceClosed() {
		jmsUtils.sendMessage(AuctionConstants.EVENT_CLOSE);
	}

	/* assertions */
	public void hasReceivedJoinRequestFromSniper(String sniperId) throws Exception {
		receivesAMessageMatching(sniperId, equalTo(String.format(AuctionConstants.COMMAND_JOIN)));
	}

	public void hasRecievedBid(int bid, String sniperId) throws Exception {
		receivesAMessageMatching(sniperId, equalTo(String.format(AuctionConstants.COMMAND_BID, bid)));
	}
	
	private void receivesAMessageMatching(String sniperId, Matcher<String> messageMatcher) throws Exception {
		// assert correct sniper id
		messageListener.receivesAMessage(messageMatcher);
	}

	public class SingleMessageListener implements MessageListener {
		private final ArrayBlockingQueue<TextMessage> messages = new ArrayBlockingQueue<TextMessage>(1);
		
		@Override
		public void onMessage(Message message) {
			try {
				String text = ((TextMessage) message).getText();
				if (text.contains("Command")) {
					log.info("\n>>>> " + text + "\n");
					messages.add((TextMessage) message);
				}
			} catch (JMSException e) {
				throw new RuntimeException(e);
			}
		}
		
		public void receivesAMessage(Matcher<? super String> messageMatcher) throws Exception {
			TextMessage message = messages.poll(5, TimeUnit.SECONDS);
			assertThat("Message", message, is(notNullValue()));
			assertThat(message.getText(), messageMatcher);
		}
	}

}
