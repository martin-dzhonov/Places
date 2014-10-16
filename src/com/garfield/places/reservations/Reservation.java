package com.garfield.places.reservations;

import java.util.Date;

import com.orm.SugarRecord;

public class Reservation extends SugarRecord<Reservation> {
	private Date date;
	private int numberOfPeople;
	private String name;
	private String placeId;
	private String placeName;
	private String description;
	private String image;
	private String placeWebsite;
	private String openFromTo;
	private String placePhoneNumber;
	
	public Reservation() {
	}
	
	public Reservation(String name, Date date, int numberOfPeople, String placeId, 
			String placeName, String description, String image, String placeWebsite, 
			String openFromTo, String placePhoneNumber) {
		this.setName(name);
		this.setDate(date);
		this.setNumberOfPeople(numberOfPeople);
		this.setPlaceId(placeId);
		this.setPlaceName(placeName);
		this.setDescription(description);
		this.setImage(image);
		this.setPlaceWebsite(placeWebsite);
		this.setOpenFromTo(openFromTo);
		this.setPlacePhoneNumber(placePhoneNumber);
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public int getNumberOfPeople() {
		return numberOfPeople;
	}
	
	public void setNumberOfPeople(int numberOfPeople) {
		this.numberOfPeople = numberOfPeople;
	}
	
	public Date getDate() {
		return date;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}

	public String getPlaceId() {
		return placeId;
	}

	public void setPlaceId(String placeId) {
		this.placeId = placeId;
	}

	public String getPlaceName() {
		return placeName;
	}

	public void setPlaceName(String placeName) {
		this.placeName = placeName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getPlaceWebsite() {
		return placeWebsite;
	}

	public void setPlaceWebsite(String placeWebsite) {
		this.placeWebsite = placeWebsite;
	}

	public String getOpenFromTo() {
		return openFromTo;
	}

	public void setOpenFromTo(String openFromTo) {
		this.openFromTo = openFromTo;
	}

	public String getPlacePhoneNumber() {
		return placePhoneNumber;
	}

	public void setPlacePhoneNumber(String placePhoneNumber) {
		this.placePhoneNumber = placePhoneNumber;
	}
}