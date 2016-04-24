package edu.csula.acquisition;

public class IMDB_Model {
	String title;
	String plot;
	String genre;
	String rating;
	String vote;
	
	public IMDB_Model(String title,String plot,String genre,String rating,String vote) {
	this.title=title;
	this.plot=plot;
	this.genre=genre;
	this.rating=rating;
	this.vote=vote;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPlot() {
		return plot;
	}

	public void setPlot(String plot) {
		this.plot = plot;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	public String getVote() {
		return vote;
	}

	public void setVote(String vote) {
		this.vote = vote;
	}

}
