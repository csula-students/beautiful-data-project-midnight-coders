package edu.csula.acquisition;

public class TV_Model {
	String name;
	String season;

	public TV_Model(String name, String season) {
		this.name = name;
		this.season = season;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSeason() {
		return season;
	}

	public void setSeason(String season) {
		this.season = season;
	}

}
