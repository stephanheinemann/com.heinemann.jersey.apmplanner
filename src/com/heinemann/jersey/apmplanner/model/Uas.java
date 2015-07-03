package com.heinemann.jersey.apmplanner.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Uas {
	private int identifier;
	private String name;
	private String systemTypeName;
	private String autopilotTypeName;
	private String shortState;
	private String shortMode;
	private boolean isArmed;
	private String batterySpecs;
	private long uptime;
	private double roll;
	private double pitch;
	private double yaw;
	private EventSubscribers subscribers;

	public Uas() {
		this.subscribers = new EventSubscribers();
	}
	
	public int getIdentifier() {
		return identifier;
	}

	public void setIdentifier(int identifier) {
		this.identifier = identifier;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSystemTypeName() {
		return systemTypeName;
	}

	public void setSystemTypeName(String systemTypeName) {
		this.systemTypeName = systemTypeName;
	}

	public String getAutopilotTypeName() {
		return autopilotTypeName;
	}

	public void setAutopilotTypeName(String autopilotTypeName) {
		this.autopilotTypeName = autopilotTypeName;
	}

	public String getShortState() {
		return shortState;
	}

	public void setShortState(String shortState) {
		this.shortState = shortState;
	}

	public String getShortMode() {
		return shortMode;
	}

	public void setShortMode(String shortMode) {
		this.shortMode = shortMode;
	}

	public boolean isArmed() {
		return isArmed;
	}

	public void setArmed(boolean isArmed) {
		this.isArmed = isArmed;
	}

	public String getBatterySpecs() {
		return batterySpecs;
	}

	public void setBatterySpecs(String batterySpecs) {
		this.batterySpecs = batterySpecs;
	}

	public long getUptime() {
		return uptime;
	}

	public void setUptime(long uptime) {
		this.uptime = uptime;
	}

	public double getRoll() {
		return roll;
	}

	public void setRoll(double roll) {
		this.roll = roll;
	}

	public double getPitch() {
		return pitch;
	}

	public void setPitch(double pitch) {
		this.pitch = pitch;
	}

	public double getYaw() {
		return yaw;
	}

	public void setYaw(double yaw) {
		this.yaw = yaw;
	}

	public EventSubscribers getSubscribers() {
		return subscribers;
	}

	public void setSubscribers(EventSubscribers subscribers) {
		this.subscribers = subscribers;
	}
}
