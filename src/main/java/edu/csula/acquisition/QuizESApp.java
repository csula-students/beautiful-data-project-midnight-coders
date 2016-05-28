package edu.csula.acquisition;

import com.google.gson.Gson;

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
import java.util.Date;

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
    private final static String indexName = "es-data";
    private final static String typeName = "es";

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

        
            // after reading the csv file, we will use CSVParser to parse through
            // the csv files
           
        Salary salary = new Salary(1, "krunal", new Date());
        
        bulkProcessor.add(new IndexRequest(indexName, typeName)
                .source(gson.toJson(salary))
            );
            // for each record, we will insert data into Elastic Search
       /*     parser.forEach(record -> {
                Salary salary = new Salary(
                    Long.parseLong(record.get("Id")),
                    record.get("EmployeeName"),
                    record.get("JobTitle"),
                    parseSafe(record.get("BasePay")),
                    parseSafe(record.get("OvertimePay")),
                    parseSafe(record.get("OtherPay")),
                    parseSafe(record.get("Benefits")),
                    parseSafe(record.get("TotalPay")),
                    parseSafe(record.get("TotalPayBenefits")),
                    Integer.parseInt(record.get("Year").isEmpty() ? "1979" : record.get("Year")),
                    record.get("Notes"),
                    record.get("Agency"),
                    record.get("Status")
                );
                bulkProcessor.add(new IndexRequest(indexName, typeName)
                    .source(gson.toJson(salary))
                );
            });*/
        
    }

  

    static class Salary {
        private long id;
        private String name;
        private Date dt;

        public long getId() {
			return id;
		}

		public String getName() {
			return name;
		}

		public Date getDt() {
			return dt;
		}

		public void setId(long id) {
			this.id = id;
		}

		public void setName(String name) {
			this.name = name;
		}

		public void setDt(Date dt) {
			this.dt = dt;
		}

		public Salary(long id, String name, Date dt) {
            this.id = id;
            this.name = name;
            this.dt=dt;
            
        }

     
    }
}