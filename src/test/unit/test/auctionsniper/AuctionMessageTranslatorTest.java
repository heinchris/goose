package test.auctionsniper;

import javax.jms.TextMessage;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.junit.Test;
import org.junit.runner.RunWith;

import auctionsniper.AuctionEventListener;
import auctionsniper.AuctionMessageTranslator;


@RunWith(JMock.class)
public class AuctionMessageTranslatorTest {

	private final Mockery context = new Mockery();
	private final AuctionEventListener auctionEventListener = context.mock(AuctionEventListener.class);
	private final AuctionMessageTranslator translator = new AuctionMessageTranslator(auctionEventListener);

	@Test
	public void notifiesAuctionClosedWhenClosedMessageReceived() throws Exception {
		context.checking(new Expectations() {{
			oneOf(auctionEventListener).auctionCLosed();
		}});
		TextMessage message = new MockTextMessage("SOLVersion: 1.1; Event: CLOSE");
		translator.onMessage(message);
	}
	
	@Test
	public void notifiesBidDetailWhenCurrentPriceMessageReceived() {
		context.checking(new Expectations() {{
			exactly(1).of(auctionEventListener).currentPrice(192, 7);
		}});
		TextMessage message = new MockTextMessage("SOLVersion: 1.1; Event: PRICE; CurrentPrice: 192; Increment: 7; Bidder: Someone else;");
		translator.onMessage(message);
	}
	
}
