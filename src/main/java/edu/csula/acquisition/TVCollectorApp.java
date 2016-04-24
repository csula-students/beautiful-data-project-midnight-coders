package edu.csula.acquisition;

import java.io.IOException;

public class TVCollectorApp {
	public static void main(String[] args) throws IOException {
		
		 String current = new java.io.File( "." ).getCanonicalPath();
	        //System.out.println("Current dir:"+current);
		TVSource t = new TVSource();
		TVSource.getSubtitlesByTVSub();
		t.getTVShowsByTVSubtitles("http://www.tvsubtitles.net/tvshows.html");
	
		TVSource.getDataFromIMDB();
		
		
		TVCollector tv  =new TVCollector();
		tv.save("C:\\Users\\Ami\\CS594_data_workspace\\TvSeries_Data\\Tv-Subs\\Extracted", "TVSubtitlesData");
		tv.save("C:\\Users\\Ami\\CS594_data_workspace\\TvSeries_Data\\Tv-Subtitles\\extracted", "TVSubData");
		IMDBCollector im = new IMDBCollector();
		im.save(current+"\\Data\\IMDB_Ratings.json", "IMDB");
	}
}
