package com.obtiva.goose.controller;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.jms.Message;

import org.eclipse.jetty.websocket.WebSocket;

import auctionsniper.AuctionMessageTranslator;

import com.obtiva.goose.acceptance.util.JmsUtils;

public class AuctionWebSocket implements WebSocket {

	Outbound _outbound;
	private AuctionMessageTranslator listener;

	public void onConnect(Outbound outbound) {
		_outbound = outbound;
		listener = new AuctionMessageTranslator(this);
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

}
