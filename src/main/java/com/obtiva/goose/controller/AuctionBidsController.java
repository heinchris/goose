package com.obtiva.goose.controller;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketServlet;

import com.obtiva.goose.acceptance.util.JmsUtils;

public class AuctionBidsController extends WebSocketServlet {

	private static final long serialVersionUID = 5481094225630379738L;

	private final Set<AuctionWebSocket> _members = new CopyOnWriteArraySet<AuctionWebSocket>();

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.sendRedirect("/auctions/");
	}

	protected WebSocket doWebSocketConnect(HttpServletRequest request,
			String protocol) {
		return new AuctionWebSocket();
	}

	class AuctionWebSocket implements WebSocket {
		Outbound _outbound;

		public void onConnect(Outbound outbound) {
			_outbound = outbound;
			_members.add(this);
		}

		public void onMessage(byte frame, byte[] data, int offset, int length) {
			//NoOp
		}

		public void onMessage(byte frame, String data) {
			// send message to auction server
			new JmsUtils().sendMessage("blah");
			// just dummy a reply of lost auction
			try {
				_outbound.sendMessage(String.format("Lost auction for item %1s", data));
			} catch (IOException e) {
				e.printStackTrace();
			}

//			for (AuctionWebSocket member : _members) {
//				try {
//					_outbound.sendMessage("Closed");
//					member._outbound.sendMessage(frame, data);
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
		}

		public void onDisconnect() {
			_members.remove(this);
		}

		@Override
		public void onFragment(boolean arg0, byte arg1, byte[] arg2, int arg3, int arg4) {
			//NoOp
		}
	}
}
