package edu.csula.acquisition;

import static org.elasticsearch.node.NodeBuilder.nodeBuilder;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.elasticsearch.action.bulk.BackoffPolicy;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.node.Node;

import com.google.gson.Gson;

/*
 * 
 * 
 PUT /series1-data
 {
     "mappings" : {
         "series" : {
             "properties" : {
                 "name" : {
                     "type" : "string",
                     "index" : "analyzed"
                 },
                 "rating" : {
                     "type" : "double",
                     "index" : "analyzed"
                 },
                 "vote" : {
                     "type" : "double",
                     "index" : "analyzed"
                 },
				  "genre" : {
                     "type" : "string",
                     "index" : "analyzed"
                 },
                 "year": {
                     "type": "date"
                 }
             }
         }
     }
 }

 */

public class TVSeriesES {
	private final static String indexName = "series1-data";
    private final static String typeName = "series";

    public static void main(String[] args) throws URISyntaxException, IOException {
        Node node = nodeBuilder().settings(Settings.builder()
            .put("cluster.name", "vshah-es")
            .put("path.home", "elasticsearch-data")).node();
        Client client = node.client();

        /**
         *
         *
         * INSERT data to elastic search
         */

        // as usual process to connect to data source, we will need to set up
        // node and client// to read CSV file from the resource folder
        File csv = new File(
            ClassLoader.getSystemResource("TVData.csv")
                .toURI()
        );

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
            .setBulkActions(10000)
            .setBulkSize(new ByteSizeValue(1, ByteSizeUnit.GB))
            .setFlushInterval(TimeValue.timeValueSeconds(5))
            .setConcurrentRequests(1)
            .setBackoffPolicy(
                BackoffPolicy.exponentialBackoff(TimeValue.timeValueMillis(100), 3))
            .build();

        // Gson library for sending json to elastic search
        Gson gson = new Gson();

        try {
            // after reading the csv file, we will use CSVParser to parse through
            // the csv files
            CSVParser parser = CSVParser.parse(
                csv,
                Charset.defaultCharset(),
                CSVFormat.EXCEL.withHeader()
            );

            // for each record, we will insert data into Elastic Search
            parser.forEach(record -> {
                Data salary = new Data(
                    record.get("Name").replaceAll(" ", ""),
                    record.get("Rating"),
                   Long.parseLong( record.get("Vote").replaceAll(",", "")),
                    record.get("Genre"),
                   Integer.parseInt(record.get("Year").isEmpty() ? "1979" : record.get("Year")) 
                );
                //System.out.println( record.get("Genre").replaceAll(" ", ""));
                bulkProcessor.add(new IndexRequest(indexName, typeName)
                    .source(gson.toJson(salary))
                );
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Double parseSafe(String value) {
        return Double.parseDouble(value.isEmpty() || value.equals("Not Provided") ? "0" : value);
    }

    static class Data {
    
        private final String name;
        private final String rating;
        private final long vote;
        private final String genre;
        private final int year;

  

        public Data(String name,String rating,long vote,String genre,int year) {
      
            this.name = name;
            this.rating = rating;
            this.vote=vote;
            this.genre=genre;
          this.year=year;
        }

       

        public int getYear() {
			return year;
		}



		public String getName() {
            return name;
        }



		public String getRating() {
			return rating;
		}



		public long getVote() {
			return vote;
		}



		public String getGenre() {
			return genre;
		}    
    }

}
