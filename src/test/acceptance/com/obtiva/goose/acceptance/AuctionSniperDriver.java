package com.obtiva.goose.acceptance;

import com.obtiva.goose.acceptance.util.WebDriverFacade;
import com.obtiva.goose.acceptance.util.WebSteps;


public class AuctionSniperDriver {

	private WebDriverFacade webDriverFacade = new WebDriverFacade();
	private WebSteps steps;

	public AuctionSniperDriver(String itemId) {
		try {
			steps = new WebSteps(webDriverFacade.getWebDriver());
			steps.visit("http://localhost:8080/auctions/" + itemId);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/* assertions */
	public void showsSniperStatus(String statusText) {
		try {
			// TODO find better way to manage
			// the test may try to assert a change in the browser
			// before the asynch jms messages have been processed
			// allow for some slack - 
			Thread.sleep(500);
			steps.shouldSee(statusText);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	public void closeBrowser() {
		try {
			webDriverFacade.closeBrowser();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
}
