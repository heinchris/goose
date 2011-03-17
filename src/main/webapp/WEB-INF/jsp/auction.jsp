<html>
<head>
	<title>WebSocket Chat</title>
	<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/jquery-1.4.3.js"></script>
	<script type='text/javascript'><!--
	if (!window.WebSocket) {
		alert("WebSocket not supported by this browser");
	}

	$(function() {
		auction.join('<%= request.getAttribute("auctionId") %>');
	});
	
	//function $F() {
	//	return document.getElementById(arguments[0]).value;
	//}

	function getKeyCode(ev) {
		if (window.event)
			return window.event.keyCode;
		return ev.keyCode;
	}

	var auction = {
		join : function(itemId) {
			this.itemId = itemId;
			var location = 'ws://localhost:8080/bids/' + itemId
			this._ws = new WebSocket(location);
			this._ws.onopen = this._onopen;
			this._ws.onmessage = this._onmessage;
			this._ws.onclose = this._onclose;
		},

		_onopen : function() {
			//auction._send('<%= com.obtiva.goose.controller.AuctionConstants.COMMAND_JOIN %>');
		},

		_send : function(command) {
			if (this._ws) {
				this._ws.send(command);
			}
		},

		//bid : function(text) {
		//	auction._send(auction._username, text);
		//},

		_onmessage : function(m) {
			if (m.data) {
				$("#status").html(m.data);
			}
		},

		_onclose : function(m) {
			this._ws = null;
			$('#status').html("");
		}

	};
	--></script>
	<style type='text/css'>
	div {
		border: 0px solid black;
	}
	
	div.hidden {
		display: none;
	}
	</style>
</head>
<body>
	<div id="status">Joining auction for item <%= request.getAttribute("auctionId") %>...</div>
</body>
</html>


