package edu.csula.datascience.examples;

import com.google.gson.Gson;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.elasticsearch.action.bulk.*;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.node.Node;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;

import static org.elasticsearch.node.NodeBuilder.nodeBuilder;

/**
 * Quiz elastic search app to see Salaries.csv file better
 *
 * gradle command to run this app `gradle esQuiz`
 *
 * Before you send data, please run the following to update mapping first:
 *
 * ```
 PUT /quiz-data
 {
     "mappings" : {
         "salaries" : {
             "properties" : {
                 "name" : {
                     "type" : "string",
                     "index" : "not_analyzed"
                 },
                 "jobTitle" : {
                     "type" : "string",
                     "index" : "not_analyzed"
                 },
                 "agency" : {
                     "type" : "string",
                     "index" : "not_analyzed"
                 },
                 "year": {
                     "type": "date"
                 }
             }
         }
     }
 }
 ```
 */
public class QuizESApp {
    private final static String indexName = "quiz-data";
    private final static String typeName = "salaries";

    public static void main(String[] args) throws URISyntaxException, IOException {
        Node node = nodeBuilder().settings(Settings.builder()
            .put("cluster.name", "krunalpatel78")
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
            ClassLoader.getSystemResource("result (1).csv")
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
                Salary salary = new Salary(
               
                    record.get("Name"),
                    record.get("Rating"),
                   
                   Integer.parseInt(record.get("Year").isEmpty() ? "1979" : record.get("Year"))
                 
                );
                System.out.println(record.get("Year"));
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

    static class Salary {
    
        private final String name;
        private final String rating;
        private final int year;

  

        public Salary(String name,String rating,int year) {
      
            this.name = name;
            this.rating = rating;
  
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

       

     
    
    }
}
