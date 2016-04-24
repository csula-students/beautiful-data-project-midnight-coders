package edu.csula.acquisition;

public class TVShowsApp {
	public static void main(String[] args){
		TVSource t = new TVSource();
		t.getTVShowsByTVSubtitles("http://www.tvsubtitles.net/tvshows.html");
	}
}