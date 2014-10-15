package com.garfield.places;

import java.util.Date;

import android.graphics.Bitmap;

import com.orm.SugarRecord;

public class Reservation extends SugarRecord<Reservation> {
	private Date date;
	private int numberOfPeople;
	private String name;
	private String placeId;
	private String placeName;
	private String capacity;
	private String description;
	private Bitmap image;
	
	public Reservation() {
	}
	
	public Reservation(String name, Date date, int numberOfPeople, String placeId, 
			String placeName, String capacity, String description, Bitmap image) {
		this.setName(name);
		this.setDate(date);
		this.setNumberOfPeople(numberOfPeople);
		this.setPlaceId(placeId);
		this.setPlaceName(placeName);
		this.setDescription(description);
		this.setCapacity(capacity);
		this.setImage(image);
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

	public String getCapacity() {
		return capacity;
	}

	public void setCapacity(String capacity) {
		this.capacity = capacity;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Bitmap getImage() {
		return image;
	}

	public void setImage(Bitmap image) {
		this.image = image;
	}
}