package com.itao.starlite;

import org.mortbay.jetty.Server;
import org.mortbay.jetty.webapp.WebAppContext;

public class RunApp {
	public static void main(String[] args) throws Exception {
		Server server = new Server(9090);

	    WebAppContext webContext = new WebAppContext("./src/main/webapp/", "/starlite");
	    
	    server.setHandler(webContext);

	    server.start();
	    server.join();
	
	}
    

}
