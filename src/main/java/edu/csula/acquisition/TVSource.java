package edu.csula.acquisition;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
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

	/*-----Fetch Subtitles from TV-Subs.com----------*/
	public static void getSubtitlesByTVSub() throws IOException {
		Document doc = null;
		for (int i = 100; i < 101; i++) {
			String url = "http://www.tv-subs.com/browse/page-" + i;
			doc = Jsoup.connect(url).timeout(100000 * 1000000).get();
			Element rows = doc.select("ul").get(0);
			Elements li = rows.select("li");
			for (int j = 1; j < li.size() + 1; j++) {
				Element row = li.get(j - 1);
				Element link = row.select("a[href]").first();
				Document docNew = null;
				docNew = Jsoup.connect(
						"http://www.tv-subs.com" + link.attr("href")).get();
				Element rowSeason = docNew.select("ul.season-list").get(0);
				Elements seasonli = rowSeason.select("li");
				System.out.println(link.attr("href") + "		");

				for (int k = 1; k < seasonli.size(); k++) {
					Element getseason = seasonli.get(k - 1);
					Element seasonlink = getseason.select("a[href]").first();
					String[] SeriesName = seasonlink.attr("href").split("/");

					System.out.println("			" + seasonlink.attr("href"));

					Document docepisode = Jsoup.connect(
							"http://www.tv-subs.com" + seasonlink.attr("href"))
							.get();
					Element rowEpisode = docepisode.select("ul.episode-list")
							.get(0);
					Elements episodeli = rowEpisode.select("li");

					for (int x = 1; x < episodeli.size(); x++) {
						Element getepisode = episodeli.get(x - 1);
						Element episodelink = getepisode.select("a[href]")
								.first();

						System.out.println("					" + episodelink.attr("href"));

						Document docEpisodelang = Jsoup.connect(
								"http://www.tv-subs.com"
										+ episodelink.attr("href")).get();
						Element table = docEpisodelang.select("table").get(0);
						Elements rowsEpisodeLang = table.select("tr");
						for (int y = 1; y < rowsEpisodeLang.size(); y++) {
							Element rowLang = rowsEpisodeLang.get(y);
							String language = rowLang.select("a[href]").first()
									.text();
							if (language.equals("English")) {
								System.out.println("							"
										+ rowLang.select("a[href]").first()
												.text());
								System.out.println("							"
										+ rowLang.select("a[href]").first()
												.attr("href"));

								System.out.println("											"
										+ "http://www.tv-subs.com"
										+ rowLang.select("a[href]").first()
												.attr("href") + ".zip");
								String saveTo = "D:\\TVSeriesData\\Data\\";
								String filepath = rowLang.select("a[href]")
										.first().attr("href");
								String[] filename = filepath.split("/");
								try {
									URL dwnldurl = new URL(
											"http://www.tv-subs.com//subtitle//"
													+ filename[2] + ".zip");
									HttpURLConnection conn = (HttpURLConnection) dwnldurl
											.openConnection();

									conn.setRequestProperty(
											"User-Agent",
											"Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");

									InputStream in = conn.getInputStream();
									FileOutputStream out = new FileOutputStream(
											saveTo + filename[2] + ".zip");
									byte[] b = new byte[1024];
									int count;
									while ((count = in.read(b)) >= 0) {
										out.write(b, 0, count);
									}
									out.flush();
									out.close();
									in.close();

								} catch (IOException e) {
									e.printStackTrace();
								}

								String zipFilePath = "D:\\TVSeriesData\\Data\\"
										+ filename[2] + ".zip";
								String destDirectory = "D:\\TVSeriesData\\Extracted";
								UnzipUtility unzipper = new UnzipUtility();
								try {
									unzipper.unzip(zipFilePath, destDirectory,
											SeriesName[2], SeriesName[3]);
									// TVShowsApp.unzip(zipFilePath,
									// destDirectory);
								} catch (Exception ex) {
									// some errors occurred
									ex.printStackTrace();
								}
								break;
							}

						}

					}

				}
				System.out.println();
			}
		}
	}
}
