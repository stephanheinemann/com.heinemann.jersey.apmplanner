package com.heinemann.jersey.apmplanner.resource;

import java.io.IOException;
import java.net.URI;
import java.util.Iterator;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import com.heinemann.grpc.apmplanner.ApmPlanner.UasSubscriber;
import com.heinemann.grpc.apmplanner.UasManagerClient;
import com.heinemann.jersey.apmplanner.model.EventSubscriber;

public class EventSubscriberResource {
	
	public static final String SUBSCRIBER_NOT_FOUND = "subscriber not found";
	
	@Context
	UriInfo uriInfo;
	
	@Context
	Request request;
	
	String subscriber;
	
	public EventSubscriberResource(
			UriInfo uriInfo,
			Request request,
			String subscriber) {
		this.uriInfo = uriInfo;
		this.request = request;
		this.subscriber = subscriber;
	}

	@GET
	@Produces({
		MediaType.TEXT_PLAIN,
		MediaType.TEXT_HTML,
		MediaType.APPLICATION_XML,
		MediaType.APPLICATION_XHTML_XML,
		MediaType.APPLICATION_JSON })
	public EventSubscriber getSubscriber() {
		EventSubscriber eventSubscriber = null;
		UasManagerClient client = new UasManagerClient(UasManagerClient.HOST, UasManagerClient.PORT);
		Iterator<UasSubscriber> uasSubscribers = client.getSubscribers();
		
		while (uasSubscribers.hasNext()) {
			UasSubscriber uasSubscriber = uasSubscribers.next();
			if (uasSubscriber.getSubscriber().equals(subscriber)) {
				URI subscriberUri = UriBuilder.fromUri(subscriber).build();
				eventSubscriber = new EventSubscriber(subscriberUri);
				break;
			}
		}
		
		if (null == eventSubscriber) {
			throw new RuntimeException(SUBSCRIBER_NOT_FOUND);
		}
		
		return eventSubscriber;
	}
	
	@DELETE
	@Produces(MediaType.TEXT_HTML)
	public Response removeSubscriber(
			@Context HttpServletResponse servletResponse) throws IOException {
		Response response = Response.accepted().build();
		UasManagerClient client = new UasManagerClient(UasManagerClient.HOST, UasManagerClient.PORT);
		
		client.removeSubscriber(subscriber);
		servletResponse.sendRedirect(EventSubscribersResource.SUBSCRIBER_URL);
		
		return response;
	}
}
