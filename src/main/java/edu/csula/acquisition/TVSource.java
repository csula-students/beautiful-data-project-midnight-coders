package edu.csula.acquisition;

import java.io.IOException;

import java.util.Collection;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import org.jsoup.select.Elements;

public class TVSource implements Source<String> {

	Document doc;

	public boolean hasNext() {
		// TODO Auto-generated method stub
		return false;
	}

	public Collection<String> next() {
		// TODO Auto-generated method stub
		return null;
	}

	public void getTVShows(String path) {
		try {
			doc = Jsoup.connect(path).get();

			Element table = doc.select("table").get(2); // select the first
														// table.
			Elements rows = table.select("tr");

			for (int i = 1; i < rows.size(); i++) { // first row is the col
													// names so skip it.
				Element row = rows.get(i);
				Elements cols = row.select("td");
				Element link = row.select("a[href]").first();
				/*System.out.print(link.attr("href") + "	");
				System.out.print(cols.get(1).text() + "   ");
				System.out.println(cols.get(2).text());*/
				JsonWriter jw = new JsonWriter();
				jw.JsonWrite(link.attr("href"), cols.get(1).text(), cols.get(2).text());
			}

		} catch (IOException e) {

			e.printStackTrace();
		}

	}

}
