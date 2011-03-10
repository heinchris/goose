package test.auctionsniper;

import javax.jms.TextMessage;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.obtiva.goose.controller.AuctionWebSocket;

import auctionsniper.AuctionMessageTranslator;

@RunWith(JMock.class)
public class AuctionMessageTranslatorTest {

	Mockery context = new Mockery();
	private final AuctionWebSocket auctionWebSocket = context.mock(AuctionWebSocket.class);
	private final AuctionMessageTranslator translator = new AuctionMessageTranslator(auctionWebSocket);
	
	@Test
	public void notifiesAuctionClosedWhenClosedMessageReceived() throws Exception {
		context.checking(new Expectations() {{
			//oneOf(listener).auctionCLosed();
		}});
		TextMessage message = new MockTextMessage("SOLVersion: 1.1; Event: CLOSE");
		translator.onMessage(message);
	}
	
}
