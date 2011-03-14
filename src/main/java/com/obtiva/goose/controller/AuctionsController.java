package com.obtiva.goose.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/auctions")
public class AuctionsController {

	@RequestMapping(value="/list", method=RequestMethod.GET)
	@ResponseBody
	public String list() {
		return "<h1>Joining Lost</h1>";
	}
	
	@RequestMapping(value="")
	public String index() {
		return "index";
	}

	@RequestMapping(value="/{auctionId}")
	public String show(HttpServletRequest request, @PathVariable String auctionId) {
		request.setAttribute("auctionId", auctionId);
		return "auction";
	}
	
}
