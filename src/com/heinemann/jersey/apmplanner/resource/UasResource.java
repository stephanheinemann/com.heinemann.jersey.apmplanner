package com.heinemann.jersey.apmplanner.resource;

import java.io.IOException;
import java.util.Iterator;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import com.heinemann.grpc.apmplanner.ApmPlanner;
import com.heinemann.grpc.apmplanner.ApmPlanner.UasSubscriber;
import com.heinemann.grpc.apmplanner.UasManagerClient;
import com.heinemann.jersey.apmplanner.model.EventSubscribers;
import com.heinemann.jersey.apmplanner.model.Uas;

@Path("/uas")
public class UasResource {
	
	public static final String SUBSCRIBER = "subscriber";
	
	//TODO: retrieve supported commands and modes
	//TODO: ArduPilotMegaMAV ApmCopter CustomMode
	public static final String COMMAND_URL = "../command.html";
	public static final String COMMAND = "command";
	public static final String COMMAND_GO = "go";
	public static final String COMMAND_HOME = "home";
	public static final String COMMAND_LAND = "land";
	public static final String COMMAND_LAUNCH = "launch";
	public static final String COMMAND_REBOOT = "reboot";
	public static final String COMMAND_SHUTDOWN = "shutdown";
	
	public static final String MODE = "mode";
	public static final int MODE_STABILIZE = 0;
	public static final int MODE_ACRO = 1;
	public static final int MODE_ALT_HOLD = 2;
	public static final int MODE_AUTO = 3;
	public static final int MODE_GUIDED = 4;
	public static final int MODE_LOITER = 5;
	public static final int MODE_RTL = 6;
	public static final int MODE_CIRCLE = 7;
	public static final int MODE_POSITION = 8;
	public static final int MODE_LAND = 9;
	public static final int MODE_OF_LOITER = 10;
	public static final int MODE_DRIFT = 11;
	public static final int MODE_SPORT = 12;
	public static final int MODE_FLIP = 13;
	public static final int MODE_AUTOTUNE = 14;
	public static final int MODE_POS_HOLD = 15;
	
	public static final String ARMED = "armed";
	
	@GET
	@Produces({
		MediaType.TEXT_PLAIN,
		MediaType.TEXT_HTML,
		MediaType.APPLICATION_XML,
		MediaType.APPLICATION_XHTML_XML,
		MediaType.APPLICATION_JSON })
	public Uas getUas() {
		UasManagerClient client = new UasManagerClient(UasManagerClient.HOST, UasManagerClient.PORT);
		int identifier = client.getActiveUas();
		ApmPlanner.Uas clientUas = client.getUas(identifier);
		
		Uas uas = new Uas();
		uas.setIdentifier(clientUas.getIdentifier());
		uas.setName(clientUas.getName());
		uas.setAutopilotTypeName(clientUas.getAutopilotTypeName());
		uas.setShortState(clientUas.getShortState());
		uas.setShortMode(clientUas.getShortMode());
		uas.setArmed(clientUas.getIsArmed());
		uas.setBatterySpecs(clientUas.getBatterySpecs());
		uas.setUptime(clientUas.getUptime());
		uas.setRoll(clientUas.getRoll());
		uas.setPitch(clientUas.getPitch());
		uas.setYaw(clientUas.getYaw());
		
		EventSubscribers subscribers = uas.getSubscribers();
		Iterator<UasSubscriber> uasSubscribers = client.getSubscribers();
		while (uasSubscribers.hasNext()) {
			UasSubscriber uasSubscriber = uasSubscribers.next();
			subscribers.addSubscriber(UriBuilder.fromUri(uasSubscriber.getSubscriber()).build());
		}
		
		return uas;
	}
	
	@PUT
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.TEXT_HTML)
	public Response setUas(
			@FormParam(MODE) int mode,
			@FormParam(ARMED) boolean armed,
			@FormParam(SUBSCRIBER) String subscriber,
			@Context HttpServletResponse servletResponse) throws IOException {
		Response response = Response.accepted().build();
		UasManagerClient client = new UasManagerClient(UasManagerClient.HOST, UasManagerClient.PORT);
		
		//client.setMode(mode);
		//client.setArmed(armed);
		client.addSubscriber(subscriber);
		
		servletResponse.sendRedirect(COMMAND_URL);
		
		return response;
	}
	
	@DELETE
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response removeSubscriber(@FormParam(SUBSCRIBER) String subscriber) {
		Response response = Response.accepted().build();
		UasManagerClient client = new UasManagerClient(UasManagerClient.HOST, UasManagerClient.PORT);
	
		client.removeSubscriber(subscriber);
		
		return response;
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.TEXT_HTML)
	public Response command(
			@FormParam(COMMAND) String command,
			@Context HttpServletResponse servletResponse) throws IOException {
		Response response = Response.accepted().build();
		UasManagerClient client = new UasManagerClient(UasManagerClient.HOST, UasManagerClient.PORT);
		
		switch (command) {
		case COMMAND_GO:
			client.go();
			break;
		case COMMAND_HOME:
			client.home();
			break;
		case COMMAND_LAND:
			client.land();
			break;
		case COMMAND_LAUNCH:
			client.launch();
			break;
		case COMMAND_REBOOT:
			client.reboot();
			break;
		case COMMAND_SHUTDOWN:
			client.shutdown();
			break;
		default:
			response = Response.serverError().build();
		}
		
		servletResponse.sendRedirect(COMMAND_URL);
		
		return response;
	}

}
