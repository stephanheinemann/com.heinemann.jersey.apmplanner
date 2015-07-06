package com.heinemann.jersey.apmplanner.events;

import java.net.URI;
import java.util.Iterator;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import org.glassfish.jersey.client.ClientConfig;

import io.grpc.stub.StreamObserver;

import com.heinemann.grpc.apmplanner.UasManagerClient;
import com.heinemann.grpc.apmplanner.ApmPlanner.UasSubscriber;
import com.heinemann.grpc.apmplanner.events.ApmEvents.Null;
import com.heinemann.grpc.apmplanner.events.ApmEvents.UasEvent;
import com.heinemann.grpc.apmplanner.events.UasEventDistributionGrpc;

public class EventDistributor implements UasEventDistributionGrpc.UasEventDistribution {
	
	@Override
	public void fire(UasEvent request, StreamObserver<Null> responseObserver) {
		distribute(request);
		responseObserver.onValue(Null.newBuilder().build());
		responseObserver.onCompleted();
	}

	private void distribute(UasEvent uasEvent) {
		UasManagerClient client = new UasManagerClient(UasManagerClient.HOST, UasManagerClient.PORT);
		Iterator<UasSubscriber> uasSubscribers = client.getSubscribers();
		
		while (uasSubscribers.hasNext()) {
			UasSubscriber uasSubscriber = uasSubscribers.next();
			URI subscriberUri = UriBuilder.fromUri(uasSubscriber.getSubscriber()).build();
			
			ClientConfig config = new ClientConfig();
			Client distributor = ClientBuilder.newClient(config);
			WebTarget service = distributor.target(subscriberUri);
			
			QName qname = new QName("http://www.heinemann.com", "uas-event");
			JAXBElement<UasEvent> event = new JAXBElement<UasEvent>(qname, UasEvent.class, uasEvent);
			//TODO: check if not collecting responses (or registering listeners) creates an issue
			service.request().async().post(Entity.entity(event, MediaType.APPLICATION_XML), Response.class);
		}
	}
}
