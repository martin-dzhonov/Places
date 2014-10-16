package com.garfield.places.places;

public class Place {
	private String name;
	private String content;
	private String id;
	private String imageData;
	private boolean hasOnlineReservation;
	private String website;
	private String openFromTo;
	private String phoneNumber;
	
	public Place(String name, String id, String imageData){
		this.name = name;
		this.id = id;
		this.imageData = imageData;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getId() {
		return id;
	}
	
	public String getImageBase64() {
		return imageData;
	}

	public boolean isHasOnlineReservation() {
		return hasOnlineReservation;
	}

	public void setHasOnlineReservation(boolean hasOnlineReservation) {
		this.hasOnlineReservation = hasOnlineReservation;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getOpenFromTo() {
		return openFromTo;
	}

	public void setOpenFromTo(String openFromTo) {
		this.openFromTo = openFromTo;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
}
