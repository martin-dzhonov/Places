package com.garfield.places;

public class Place {
	private String name;
	private String content;
	private String id;
	public Place() {
		this(null);
	}

	public Place(String name) {
		this.name = name;
	}
	
	public Place(String name, String id) {
		this.name = name;
		this.id = id;
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

}
