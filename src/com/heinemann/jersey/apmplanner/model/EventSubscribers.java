package com.heinemann.jersey.apmplanner.model;

import java.net.URI;
import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="subscribers")
public class EventSubscribers {
	
	@XmlElement(name="subscriber")
	Set<URI> subscribers;
	
	public EventSubscribers() {
		this.subscribers = new HashSet<URI>();
	}
	
	public Set<URI> getSubscribers() {
		return subscribers;
	}

	public void addSubscriber(URI subscriber) {
		this.subscribers.add(subscriber);
	}
	
	public void removeSubscriber(URI subscriber) {
		this.subscribers.remove(subscriber);
	}
	
}
