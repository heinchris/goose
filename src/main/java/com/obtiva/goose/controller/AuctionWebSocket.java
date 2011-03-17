package com.obtiva.goose.controller;

import java.io.IOException;

import org.eclipse.jetty.websocket.WebSocket;

import auctionsniper.Auction;
import auctionsniper.AuctionMessageTranslator;
import auctionsniper.AuctionSniper;
import auctionsniper.SniperListener;

import com.obtiva.goose.acceptance.util.JmsUtils;

public class AuctionWebSocket implements WebSocket, SniperListener {

	Outbound _outbound;
	private AuctionMessageTranslator listener;
	private final String itemId;

	public AuctionWebSocket(String itemId) {
		this.itemId = itemId;
	}

	public void onConnect(Outbound outbound) {
		_outbound = outbound;
		
		Auction auction = new Auction() {
			public void bid(int amount) {
				new JmsUtils().sendMessage(String.format(AuctionConstants.COMMAND_BID, amount));
			}

			@Override
			public void join() {
				new JmsUtils().sendMessage(AuctionConstants.COMMAND_JOIN);
				
			}
		};
		auction.join();
		
		listener = new AuctionMessageTranslator(new AuctionSniper(auction, this));
		new JmsUtils().addMessageListener(listener);
	}

	public void onMessage(byte frame, byte[] data, int offset, int length) {
		// NoOp
	}

	public void onMessage(byte frame, String data) {
		// send message to auction server
		new JmsUtils().sendMessage(data);
	}

	public void sendMessage(String data) {
		try {
			_outbound.sendMessage(data);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void onDisconnect() {
		new JmsUtils().removeMessageListener(listener);
	}

	@Override
	public void onFragment(boolean arg0, byte arg1, byte[] arg2, int arg3,
			int arg4) {
		// NoOp
	}

	@Override
	public void sniperLost() {
		sendMessage(String.format(AuctionConstants.STATUS_LOST, this.itemId));
	}

	@Override
	public void sniperBidding() {
		sendMessage(String.format(AuctionConstants.STATUS_BIDDING, this.itemId));
	}

}
