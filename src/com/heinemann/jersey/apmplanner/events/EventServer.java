package com.heinemann.jersey.apmplanner.events;

import java.io.IOException;
import java.net.InetSocketAddress;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.heinemann.grpc.apmplanner.events.UasEventDistributionGrpc;

import io.grpc.Server;
import io.grpc.netty.NettyServerBuilder;

public class EventServer implements ServletContextListener {

	public static final String HOST = "rigi-lab-03.cs.uvic.ca";
	public static final int PORT = 50052;
	private Server server;

	private void start() throws IOException {
		server = NettyServerBuilder.forAddress(new InetSocketAddress(HOST, PORT))
				.addService((new EventDistributor()).bindService()).build()
				.start();
		
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				EventServer.this.stop();
			}
		});
		System.out.println("***** event server started *****");
	}

	private void stop() {
		if (server != null) {
			server.shutdown();
		}
		System.out.println("***** event server stopped *****");
	}

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		stop();
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		try {
			start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
