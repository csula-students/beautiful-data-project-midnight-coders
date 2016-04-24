package edu.csula.acquisition;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;

public class TVCollectorTest {
	TVCollector collector;

	@Before
	public void Setup() {
		collector = new TVCollector();
	}

	@Test
	public void testSubtitles() throws Exception {
		String current = new java.io.File(".").getCanonicalPath();
		collector.save(current+"\\Data\\ExtractedTest", "testTV");
	}

	@Test
	public void testSubtitlesList() throws Exception {
		String current = new java.io.File(".").getCanonicalPath();
		fetchRecords(current+"\\Data\\ExtractedTest");
		//Assert.assertEquals(tv.size(), 11);
		assert tv.size() > 0;

		}
	
	List<TV_Model> tv = new ArrayList<TV_Model>();
	
	public void fetchRecords(String path){
		File[] files = new File(path).listFiles();
		
		for (File file : files) {
			if (!file.isDirectory() && !file.isHidden() && file.exists() && file.canRead()) {

				String filepath = file.getPath();

				if (filepath != null && filepath != "") {
					String pattern = Pattern.quote(System.getProperty("file.separator"));
					String[] SeriesData = filepath.split(pattern);
					System.out
							.println(SeriesData[SeriesData.length - 2] + "      " + SeriesData[SeriesData.length - 1]);

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

					/*
					 * document.append("SeriesName", SeriesData[5]);
					 * document.append("Season", SeriesData[6]);
					 * document.append("SubtitleData", str);
					 */
					tv.add(new TV_Model( SeriesData[SeriesData.length - 2],  SeriesData[SeriesData.length - 1]));
				}
			}else {
				fetchRecords(file.toString());
			}

		}

	}
}
