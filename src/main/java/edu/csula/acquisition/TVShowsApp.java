package edu.csula.acquisition;

public class TVShowsApp {
	public static void main(String[] args){
		TVSource t = new TVSource();
		t.getTVShows("http://www.tvsubtitles.net/tvshows.html");
	}
}
