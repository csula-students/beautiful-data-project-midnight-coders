package edu.csula.acquisition;

import java.io.IOException;

public class TVCollectorApp {
	public static void main(String[] args) throws IOException {
		
		 
	        //System.out.println("Current dir:"+current);
		TVSource t = new TVSource(Long.MAX_VALUE);
		while(t.hasNext()){
			String current = new java.io.File( "." ).getCanonicalPath();
		TVSource.getSubtitlesByTVSub();
		t.getTVShowsByTVSubtitles("http://www.tvsubtitles.net/tvshows.html");
	
		TVSource.getDataFromIMDB();
		
		
		TVCollector tv  =new TVCollector();
		tv.save(current+"\\src\\main\\resources\\TvSeries_Data\\Tv-Subs\\Extracted", "TVSubtitlesData");
		tv.save(current+"\\src\\main\\resources\\TvSeries_Data\\Tv-Subtitles\\Extracted", "TVSubData");
		IMDBCollector im = new IMDBCollector();
		im.save(current+"\\Data\\IMDB_Ratings.json", "IMDB");
		}
	}
}
