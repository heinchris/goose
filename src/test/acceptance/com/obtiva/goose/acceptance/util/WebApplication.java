package com.obtiva.goose.acceptance.util;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

public class WebApplication {

	private static WebApplication instance;

	private Server server;
	private WebAppContext context;
	private Boolean running = Boolean.FALSE;

	private WebApplication() {
		// singleton
	}

	public static WebApplication getInstance() {
		if (instance == null) {
			instance = new WebApplication();
			instance.start();
		}
		return instance;
	}

	public void start() {
		if (running) {
			return;
		}
		synchronized (instance) {
			server = new Server(8080);

			context = new WebAppContext();
			context.setDescriptor("/WEB-INF/web.xml");
			context.setResourceBase("./src/main/webapp");
			context.setContextPath("/");
			context.setParentLoaderPriority(true);

			server.setHandler(context);

			try {
				server.start();
//				server.join();
				running = Boolean.TRUE;
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

	public void stop() throws Exception {
		if (!running) {
			return;
		}
		synchronized (instance) {
			server.stop();
			running = Boolean.FALSE;
		}
	}

}
