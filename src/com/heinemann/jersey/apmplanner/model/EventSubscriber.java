package com.heinemann.jersey.apmplanner.model;

import java.net.URI;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class EventSubscriber {
	
	private URI uri;
	
	public EventSubscriber() {
		this.uri = null;
	}
	
	public EventSubscriber(URI uri) {
		this.uri = uri;
	}

	public URI getUri() {
		return uri;
	}

	public void setUri(URI uri) {
		this.uri = uri;
	}
	
}
