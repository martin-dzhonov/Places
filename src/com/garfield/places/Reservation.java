package com.garfield.places;

import java.util.Date;

import com.orm.SugarRecord;

public class Reservation extends SugarRecord<Reservation> {
	Date date;
	int numberOfPeople;
	String name;
	String placeId;
	
	public Reservation() {
	}
	
	public Reservation(String name, Date date, int numberOfPeople, String placeId) {
		this.setName(name);
		this.setDate(date);
		this.setNumberOfPeople(numberOfPeople);
		this.setPlaceId(placeId);
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
}