package edu.csula.acquisition;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

public class TVCollector implements Collector<List<String>, List<String>> {
	MongoClient mongoClient;
	DB mongoDB;
	public TVCollector(){
		
		mongoClient = new MongoClient("localhost",
				27017);
		
		 mongoDB = mongoClient.getDB("tvshows");
	}

	public Collection<List<String>> mungee(Collection<List<String>> src) {
		// TODO Auto-generated method stub
		return null;
	}

	public void save(String Path, String mongoCollection) {
		// get all files in the data directory
				File[] files = new File(Path).listFiles();
				//int id = 1;
				

				// Let's store the standard data in regular collection
				DBCollection collection = mongoDB
						.getCollection(mongoCollection);
				for (File file : files) {
					if (!file.isDirectory() && !file.isHidden() && file.exists()
							&& file.canRead()) {
						/* && filter.accept(file) */
						// /indexFile(file);
						String filepath = file.getPath();
						// filepath = filepath.replaceAll("\\/", "\\\\/");
						//System.out.println(file.getCanonicalPath());
						if (filepath != null && filepath != "") {
							String pattern = Pattern.quote(System
									.getProperty("file.separator"));
							String[] SeriesData = filepath.split(pattern);
							System.out
									.println(SeriesData[5] + "      " + SeriesData[6]);
							
							String str = null;
							FileInputStream fis;
							try {
								fis = new FileInputStream(file.getPath());
								byte[] data = new byte[(int) file.length()];
								fis.read(data);
								fis.close();
								str = new String(data, "UTF-8");

							} catch (FileNotFoundException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
								// Build our document and add all the fields
								BasicDBObject document = new BasicDBObject();
								//document.append("_id", id);
								document.append("SeriesName", SeriesData[5]);
								document.append("Season", SeriesData[6]);
								document.append("SubtitleData", str);

								// insert the document into the collection
								collection.insert(document);

						}
					} else {
						save(file.toString(), mongoCollection);
					}
				}
		
	}


}
