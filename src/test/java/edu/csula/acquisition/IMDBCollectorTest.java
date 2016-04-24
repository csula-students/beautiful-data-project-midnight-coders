package edu.csula.acquisition;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.mongodb.BasicDBObject;

public class IMDBCollectorTest {

	IMDBCollector collector;

	@Before
	public void Setup() {
		collector = new IMDBCollector();
	}
	
	@Test
	public void testIMDBJson() throws Exception {
		 String current = new java.io.File( "." ).getCanonicalPath();
		 FileInputStream fis;
		 List<IMDB_Model> list = new ArrayList<IMDB_Model>();
			try {
				fis = new FileInputStream(current+"\\Data\\test_imdb.json");
				String StringFromInputStream = IOUtils.toString(fis, "UTF-8");
				// System.out.println(StringFromInputStream);
				InputStream stream = new ByteArrayInputStream(StringFromInputStream.getBytes(StandardCharsets.UTF_8));

				for (Iterator it = new ObjectMapper().readValues(new JsonFactory().createJsonParser(stream), Map.class); it
						.hasNext();) {

					@SuppressWarnings("unchecked")
					LinkedHashMap<String, String> keyValue = (LinkedHashMap<String, String>) it.next();
					//System.out.println(keyValue.get("Rating"));
				list.add(new IMDB_Model(keyValue.get("Name"), keyValue.get("Plot"), keyValue.get("Genre"), keyValue.get("Rating"), keyValue.get("Vote")));
					
				}
				
				Assert.assertEquals(list.size(), 3);

				
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
			
	}
	
	@Test
	public void testIMDB() throws Exception {
		 String current = new java.io.File( "." ).getCanonicalPath();
		collector.save(current+"\\Data\\test_imdb.json", "testIMDB");
	}
	
	
}
