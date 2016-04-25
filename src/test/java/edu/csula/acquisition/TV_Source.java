package edu.csula.acquisition;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
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

import com.google.common.collect.Lists;

public class TV_Source implements Source<TV_Model>{
	 int index = 0;
	public boolean hasNext() {
		 return index < 1;
	}

	public Collection<TV_Model> next() {
	/*	return Lists.newArrayList(
	            new TV_Model("1", "FRIENDS",null),
	            new TV_Model("2", "GAME OF THRONES","content2"),
	            new TV_Model("3", "SUITS","content3")
	        );*/
		try {
			return Lists.newArrayList(TV_Source.getSource());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}
	
	public static List<TV_Model> getSource() throws IOException{
		 String current = new java.io.File( "." ).getCanonicalPath();
		 FileInputStream fis;
		 List<TV_Model> list = new ArrayList<TV_Model>();
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
					
					String ct = Integer.toString(list.size() +1);
					
				list.add(new TV_Model(ct, keyValue.get("Name"), keyValue.get("Rating")));
					
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
			return list;
			
	}

}
