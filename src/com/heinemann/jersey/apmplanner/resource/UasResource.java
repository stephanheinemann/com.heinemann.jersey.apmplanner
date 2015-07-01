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

	@GET
	@Produces({
		MediaType.TEXT_PLAIN,
		MediaType.TEXT_HTML,
		MediaType.APPLICATION_XML,
		MediaType.APPLICATION_XHTML_XML,
		MediaType.APPLICATION_JSON })
	public Uas getUas() {
		UasManagerClient client = new UasManagerClient("rigi-lab-03.cs.uvic.ca", 50051);
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
	public Response command(@FormParam("command") String command) {
		UasManagerClient client = new UasManagerClient("rigi-lab-03.cs.uvic.ca", 50051);
		client.reboot();
		return Response.accepted().build();
	}

}
