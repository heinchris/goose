package com.obtiva.goose.controller;

public interface AuctionConstants {

	public static final String STATUS_JOINING = "Joining auction for item %1s";
	public static final String STATUS_LOST = "Lost auction for item %1s";
	public static final String STATUS_BIDDING = "Bidding for item %1s";

	public static final String COMMAND_JOIN = "SOLVersion: 1.1; Command: JOIN;";
	public static final String COMMAND_BID = "SOLVersion: 1.1; Command: BID; price: %d;";
	public static final String EVENT_CLOSE = "SOLVersion: 1.1; Event: CLOSE;";
	public static final String EVENT_PRICE = "SOLVersion: 1.1; Event: PRICE; CurrentPrice: %d; Increment: %d; Bidder: %s;";
	
}
