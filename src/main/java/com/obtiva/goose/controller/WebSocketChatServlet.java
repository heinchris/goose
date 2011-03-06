package com.obtiva.goose.controller;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocket.Outbound;
import org.eclipse.jetty.websocket.WebSocketServlet;

public class WebSocketChatServlet extends WebSocketServlet {

	private final Set<ChatWebSocket> _members = new CopyOnWriteArraySet();

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		getServletContext().getNamedDispatcher("default").forward(request,
				response);
	}

	protected WebSocket doWebSocketConnect(HttpServletRequest request,
			String protocol) {
		return new ChatWebSocket();
	}

	class ChatWebSocket implements WebSocket {
		Outbound _outbound;

		public void onConnect(Outbound outbound) {
			_outbound = outbound;
			_members.add(this);
		}

		public void onMessage(byte frame, byte[] data, int offset, int length) {
			System.out.println("********** message ************");
		}

		public void onMessage(byte frame, String data) {
			System.out.println("********** onMessage active ************");
			for (ChatWebSocket member : _members) {
				System.out.println("********** members active ************");
				try {
					member._outbound.sendMessage(frame, data);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		public void onDisconnect() {
			_members.remove(this);
		}

		@Override
		public void onFragment(boolean arg0, byte arg1, byte[] arg2, int arg3,
				int arg4) {
			System.out.println("********** fragment ************");
		}
	}
}
