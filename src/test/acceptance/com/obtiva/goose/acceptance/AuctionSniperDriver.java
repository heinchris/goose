package com.obtiva.goose.acceptance;

import com.obtiva.goose.acceptance.util.WebDriverFacade;
import com.obtiva.goose.acceptance.util.WebSteps;


public class AuctionSniperDriver {

	private WebSteps steps;

	public AuctionSniperDriver(String itemId) {
		try {
			steps = new WebSteps(new WebDriverFacade());
			steps.visit("http://localhost:8080/auctions/" + itemId);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/* assertions */
	public void showsSniperStatus(String statusText) {
		try {
			steps.shouldSee(statusText);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
	
}
