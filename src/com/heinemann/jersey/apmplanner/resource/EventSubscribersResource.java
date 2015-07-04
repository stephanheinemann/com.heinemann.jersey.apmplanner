package com.heinemann.jersey.apmplanner.resource;

import java.io.IOException;
import java.net.URI;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import com.heinemann.grpc.apmplanner.ApmPlanner.UasSubscriber;
import com.heinemann.grpc.apmplanner.UasManagerClient;
import com.heinemann.jersey.apmplanner.model.EventSubscriber;

@Path("/subscribers")
public class EventSubscribersResource {

	public static final String SUBSCRIBER_URL = "../subscriber.html";
	public static final String SUBSCRIBER = "subscriber";
	
	@Context
	UriInfo uriInfo;
	
	@Context
	Request request;
	
	@GET
	@Produces({
		MediaType.TEXT_PLAIN,
		MediaType.TEXT_HTML,
		MediaType.TEXT_XML,
		MediaType.APPLICATION_XML,
		MediaType.APPLICATION_XHTML_XML,
		MediaType.APPLICATION_JSON })
	public Set<EventSubscriber> getEventSubscribers() {
		Set<EventSubscriber> eventSubscribers = new HashSet<EventSubscriber>();
		UasManagerClient client = new UasManagerClient(UasManagerClient.HOST, UasManagerClient.PORT);
		Iterator<UasSubscriber> uasSubscribers = client.getSubscribers();
		
		while (uasSubscribers.hasNext()) {
			UasSubscriber uasSubscriber = uasSubscribers.next();
			URI subscriberUri = UriBuilder.fromUri(uasSubscriber.getSubscriber()).build();
			eventSubscribers.add(new EventSubscriber(subscriberUri));
		}
		
		return eventSubscribers;
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.TEXT_HTML)
	public void addEventSubscriber(
			@FormParam(SUBSCRIBER) String subscriber,
			@Context HttpServletResponse servletResponse) throws IOException {
		UasManagerClient client = new UasManagerClient(UasManagerClient.HOST, UasManagerClient.PORT);
		client.addSubscriber(subscriber);
		servletResponse.sendRedirect(SUBSCRIBER_URL);
	}
	
	@Path("{" + SUBSCRIBER + "}")
	public EventSubscriberResource getEventSubscriber(
			@PathParam(SUBSCRIBER) String subscriber) {
		return new EventSubscriberResource(uriInfo, request, subscriber);
	}
	
}
