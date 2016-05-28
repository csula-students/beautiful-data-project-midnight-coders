package edu.csula.acquisition;

import com.google.gson.Gson;
import com.mongodb.BasicDBObject;

import org.apache.commons.io.IOUtils;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.elasticsearch.action.bulk.*;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.node.Node;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;

import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;


import static org.elasticsearch.node.NodeBuilder.nodeBuilder;
public class ESImdb {
	
	 private final static String indexName = "imdb1-data";
	    private final static String typeName = "imdb";
	    static Client client;
	    static Node node;

	    public static void main(String[] args) throws URISyntaxException, IOException {
	    	 String current = new java.io.File( "." ).getCanonicalPath();
	    	node = nodeBuilder().settings(Settings.builder()
		            .put("cluster.name", "krunalpatel78")
		            .put("path.home", "elasticsearch-data")).node();
		        client = node.client();
		        save(current+"\\Data\\IMDB_Ratings.json");
	    }
	    public static void save(String path){
	        

	        /**
	         *
	         *
	         * INSERT data to elastic search
	         */

	        // as usual process to connect to data source, we will need to set up
	        // node and client// to read CSV file from the resource folder
	       /* File csv = new File(
	            ClassLoader.getSystemResource("GlobalLandTemperaturesByState.csv")
	                .toURI()
	        );*/

	        // create bulk processor
	        BulkProcessor bulkProcessor = BulkProcessor.builder(
	            client,
	            new BulkProcessor.Listener() {
	                @Override
	                public void beforeBulk(long executionId,
	                                       BulkRequest request) {
	                }

	                @Override
	                public void afterBulk(long executionId,
	                                      BulkRequest request,
	                                      BulkResponse response) {
	                }

	                @Override
	                public void afterBulk(long executionId,
	                                      BulkRequest request,
	                                      Throwable failure) {
	                    System.out.println("Facing error while importing data to elastic search");
	                    failure.printStackTrace();
	                }
	            })
	            .setBulkActions(100000)
	            .setBulkSize(new ByteSizeValue(1, ByteSizeUnit.GB))
	            .setFlushInterval(TimeValue.timeValueSeconds(5))
	            .setConcurrentRequests(1)
	            .setBackoffPolicy(
	                BackoffPolicy.exponentialBackoff(TimeValue.timeValueMillis(100), 3))
	            .build();

	        // Gson library for sending json to elastic search
	        Gson gson = new Gson();

	      /*  File[] files = new File(path).listFiles();
			//int id = 1;
			

			// Let's store the standard data in regular collection

			for (File file : files) {
				if (!file.isDirectory() && !file.isHidden() && file.exists()
						&& file.canRead()) {
					 && filter.accept(file) 
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
						

				        Date date = null;
				        String output = null;
				      
				        DateFormat outputformat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
				   
				         //Changing the format of date and storing it in String
				    	 output = outputformat.format(new Date());
				    	  try {
							date = outputformat.parse(output);
						} catch (ParseException e) {
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
							
							  bulkProcessor.add(new IndexRequest(indexName, typeName)
			                    .source(gson.toJson(new Temp(SeriesData[5], SeriesData[6], str, date))));

					}
				} else {
					save(file.toString());
				}
			}*/


	        FileInputStream fis;
			try {
				fis = new FileInputStream(path);
				String StringFromInputStream = IOUtils.toString(fis, "UTF-8");
				// System.out.println(StringFromInputStream);
				InputStream stream = new ByteArrayInputStream(StringFromInputStream.getBytes(StandardCharsets.UTF_8));

				for (Iterator it = new ObjectMapper().readValues(new JsonFactory().createJsonParser(stream), Map.class); it
						.hasNext();) {

					@SuppressWarnings("unchecked")
					LinkedHashMap<String, String> keyValue = (LinkedHashMap<String, String>) it.next();
					System.out.println(keyValue.get("Rating"));
				
					
					   
					   bulkProcessor.add(new IndexRequest(indexName, typeName)
			                    .source(gson.toJson(new Temp(keyValue.get("Name"), keyValue.get("Rating"), keyValue.get("Genre"), new Date()))));
				
					
				}
				
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JsonParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        
	        
	        
	       /* SearchResponse sr = node.client().prepareSearch(indexName)
	            .setTypes(typeName)
	            .setQuery(QueryBuilders.matchAllQuery())
	            .addAggregation(
	                AggregationBuilders.terms("stateAgg").field("state")
	                    .size(Integer.MAX_VALUE)
	            )
	            .execute().actionGet();

	        // Get your facet results
	        Terms agg1 = sr.getAggregations().get("stateAgg");

	        for (Terms.Bucket bucket: agg1.getBuckets()) {
	            System.out.println(bucket.getKey() + ": " + bucket.getDocCount());
	        }*/
	    }
	    
	    
}
class Temp {
	String seriesName;
	String rating;
	String genre;
	Date dt;
	
	public Temp(String seriesName, String rating, String genre, Date dt){
		this.seriesName = seriesName;
		this.rating = rating;
		this.genre = genre;
		this.dt = dt;
	}

	public String getSeriesName() {
		return seriesName;
	}

	public void setSeriesName(String seriesName) {
		this.seriesName = seriesName;
	}



	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public Date getDt() {
		return dt;
	}

	public void setDt(Date dt) {
		this.dt = dt;
	}
	

}
