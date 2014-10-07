package com.gerfield.places;

public class Place {
	private String name;
	private String content;

	public Place() {
		this(null);
	}

	public Place(String name) {
		this.name = name;
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

}
