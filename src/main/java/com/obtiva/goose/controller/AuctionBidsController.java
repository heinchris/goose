package com.obtiva.goose.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketServlet;

public class AuctionBidsController extends WebSocketServlet {

	private static final long serialVersionUID = 5481094225630379738L;

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.sendRedirect("/auctions/");
	}

	protected WebSocket doWebSocketConnect(HttpServletRequest request,
			String protocol) {
		return new AuctionWebSocket();
	}

}
