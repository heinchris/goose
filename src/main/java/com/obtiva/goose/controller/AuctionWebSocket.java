package com.obtiva.goose.controller;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import org.eclipse.jetty.websocket.WebSocket;

import com.obtiva.goose.acceptance.util.JmsUtils;

public class AuctionWebSocket implements WebSocket {

	private final Set<AuctionWebSocket> _members = new CopyOnWriteArraySet<AuctionWebSocket>();
	Outbound _outbound;

	public void onConnect(Outbound outbound) {
		_outbound = outbound;
		_members.add(this);
	}

	public void onMessage(byte frame, byte[] data, int offset, int length) {
		// NoOp
	}

	public void onMessage(byte frame, String data) {
		// send message to auction server
		new JmsUtils().sendMessage("blah");
		// just dummy a reply of lost auction
		try {
			_outbound.sendMessage(String.format("Lost auction for item %1s",
					data));
		} catch (IOException e) {
			e.printStackTrace();
		}

		// for (AuctionWebSocket member : _members) {
		// try {
		// _outbound.sendMessage("Closed");
		// member._outbound.sendMessage(frame, data);
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		// }
	}

	public void onDisconnect() {
		_members.remove(this);
	}

	@Override
	public void onFragment(boolean arg0, byte arg1, byte[] arg2, int arg3,
			int arg4) {
		// NoOp
	}
}
