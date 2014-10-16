package com.garfield.places.places;

// TODO: Add website, phone number and open time in backend
public class Place {
	private String name;
	private String content;
	private String id;
	private String imageData;
	private boolean hasOnlineReservation;

	
	public Place(String name, String id, String imageData, boolean hasOnlineReservation){
		this.name = name;
		this.id = id;
		this.imageData = imageData;
		this.hasOnlineReservation = hasOnlineReservation;
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
}
