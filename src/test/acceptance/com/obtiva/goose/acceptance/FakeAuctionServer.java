package com.obtiva.goose.acceptance;

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

import com.obtiva.goose.acceptance.util.JmsUtils;

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
	
	public void announceClosed() {
		jmsUtils.sendMessage("arbitrary text for closing");
	}

	/* assertions */
	public void hasReceivedJoinRequestFromSniper() throws InterruptedException {
		messageListener.receivesAMessage();
	}
	
	public class SingleMessageListener implements MessageListener {
		private final ArrayBlockingQueue<TextMessage> messages = new ArrayBlockingQueue<TextMessage>(1);
		
		@Override
		public void onMessage(Message message) {
			try {
				log.info("Message received: ('" + ((TextMessage) message).getText() + "')");
				messages.add((TextMessage) message);
			} catch (JMSException e) {
				throw new RuntimeException(e);
			}
		}
		
		public void receivesAMessage() throws InterruptedException {
			assertThat("Message", messages.poll(5, TimeUnit.SECONDS), is(notNullValue()));
		}
	}


}
