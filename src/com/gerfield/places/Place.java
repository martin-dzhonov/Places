package com.gerfield.places;

public class Place {
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Place() {
		this(null);
	}

	public Place(String name) {
		this.name = name;
	}
}
