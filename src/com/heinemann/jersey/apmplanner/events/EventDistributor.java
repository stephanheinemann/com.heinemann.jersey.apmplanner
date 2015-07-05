package com.heinemann.jersey.apmplanner.events;

import java.net.URI;
import java.util.Iterator;

import javax.ws.rs.core.Form;
import javax.ws.rs.core.UriBuilder;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.JerseyClient;
import org.glassfish.jersey.client.JerseyClientBuilder;
import org.glassfish.jersey.client.JerseyWebTarget;

import io.grpc.stub.StreamObserver;

import com.heinemann.grpc.apmplanner.UasManagerClient;
import com.heinemann.grpc.apmplanner.ApmPlanner.UasSubscriber;
import com.heinemann.grpc.apmplanner.events.ApmEvents.Null;
import com.heinemann.grpc.apmplanner.events.ApmEvents.UasEvent;
import com.heinemann.grpc.apmplanner.events.UasEventDistributionGrpc;

public class EventDistributor implements UasEventDistributionGrpc.UasEventDistribution {

	public static final String IDENTIFIER = "identifier";
	public static final String SOURCE = "source";
	public static final String PARAMETERS = "parameters";
	
	@Override
	public void fire(UasEvent request, StreamObserver<Null> responseObserver) {
		distribute(request);
		responseObserver.onValue(Null.newBuilder().build());
		responseObserver.onCompleted();
	}

	private void distribute(UasEvent uasEvent) {
		UasManagerClient client = new UasManagerClient(UasManagerClient.HOST, UasManagerClient.PORT);
		Iterator<UasSubscriber> uasSubscribers = client.getSubscribers();
		
		System.out.println("***** distributing event *****");
		
		while (uasSubscribers.hasNext()) {
			UasSubscriber uasSubscriber = uasSubscribers.next();
			URI subscriberUri = UriBuilder.fromUri(uasSubscriber.getSubscriber()).build();
			
			ClientConfig config = new ClientConfig();
			JerseyClient distributor = (JerseyClient) JerseyClientBuilder.newClient(config);
			JerseyWebTarget service = distributor.target(subscriberUri);
			
			Form eventForm = new Form();
			eventForm.param(IDENTIFIER, uasEvent.getIdentifier());
			eventForm.param(SOURCE, uasEvent.getSource());
			eventForm.param(PARAMETERS, uasEvent.getParameters());
			//service.request().post(Entity.entity(eventForm, MediaType.APPLICATION_FORM_URLENCODED), Response.class);
			System.out.println("***** notification ****");
			System.out.println(subscriberUri);
			System.out.println(eventForm);
		}
	}
}
