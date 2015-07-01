package com.heinemann.jersey.apmplanner.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.heinemann.grpc.apmplanner.ApmPlanner;
import com.heinemann.grpc.apmplanner.UasManagerClient;
import com.heinemann.jersey.apmplanner.model.Uas;

@Path("/uas")
public class UasResource {
	
	public static final String COMMAND = "command";
	public static final String COMMAND_GO = "go";
	public static final String COMMAND_HOME = "home";
	public static final String COMMAND_LAND = "land";
	public static final String COMMAND_LAUNCH = "launch";
	public static final String COMMAND_REBOOT = "reboot";
	public static final String COMMAND_SHUTDOWN = "shutdown";
	
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
		
		return uas;
	}
	
	@PUT
	public Response setMode() {
		return null;
	}
	
	@POST
	//@Produces(MediaType.TEXT_HTML) (redirect to command form)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response command(@FormParam(COMMAND) String command) {
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
		
		return response;
	}

}
