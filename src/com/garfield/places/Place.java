package com.garfield.places;

public class Place {
	private String name;
	private String content;
	private String id;
	private String imageData;

	
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
}
