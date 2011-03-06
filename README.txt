Requirements

============================
OpenJms
============================
1. Download from http://openjms.sourceforge.net/downloads.html
2. Extract the zip or gzip'd file. It contains a root directory, which at the time I write this is "openjms-0.7.7-beta-1"
3. Modify openjms-0.7.7-beta-1/config/openjms.xml and add a new topic destination named "auctions"
	
	    <AdministeredTopic name="auctions" />
	
4. Open a terminal and cd to newly extracted directory.
5. Start JMS server;
	Windows
		bin\startup.bat
	Unix
		bin/startup.sh
		

============================
Code
============================
Even though I have changed the implementation from a Swing to Servlet application communicating over JMS instead of Chat, 
I have tried to stay as close as possible to the original book code. This means some of the implementation choices might
not be the best for a web application, but it makes working through the book easier.

