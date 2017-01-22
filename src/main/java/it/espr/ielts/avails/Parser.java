package it.espr.ielts.avails;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Parser {

	private DataProvider dataProvider;

	// centreId|VenueId|LocationId
	private static Map<String, List<String>> centers = new LinkedHashMap<>();

	static {
		centers.put("auckland", new ArrayList<String>());
		centers.get("auckland").add("52|53|1145");
		centers.get("auckland").add("52|587|1145");
		centers.get("auckland").add("52|586|1145");
		centers.get("auckland").add("47|48|1144");
		centers.get("auckland").add("44|44|1143");

		centers.put("christchurch", new ArrayList<String>());
		centers.get("christchurch").add("48|49|15");

		centers.put("dunedin", new ArrayList<String>());
		centers.get("dunedin").add("43|43|21");

		centers.put("hamilton", new ArrayList<String>());
		centers.get("hamilton").add("49|50|25");

		centers.put("nelson", new ArrayList<String>());
		centers.get("nelson").add("42|296|425");

		centers.put("new-plymouth", new ArrayList<String>());
		centers.get("new-plymouth").add("50|605|746");

		centers.put("palmerston-north", new ArrayList<String>());
		centers.get("palmerston-north").add("50|51|53");

		centers.put("queenstown", new ArrayList<String>());
		centers.get("queenstown").add("43|730|736");

		centers.put("rotorua", new ArrayList<String>());
		centers.get("rotorua").add("51|52|60");

		centers.put("wellington", new ArrayList<String>());
		centers.get("wellington").add("50|604|745");
		centers.get("wellington").add("42|42|78");

		centers.put("samoa", new ArrayList<String>());
		centers.get("samoa").add("47|842|966");

	}

	public Parser(DataProvider dataProvider) {
		this.dataProvider = dataProvider;
	}

	private static final Logger log = LoggerFactory.getLogger(Parser.class);

	static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

	public List<Availability> getAllAvails(String date) throws ParseException {
		List<Availability> avails = new ArrayList<>();
		for (String city : centers.keySet()) {
			avails.addAll(this.getAvails(date, city));
		}
		return sort(avails);
	}

	public List<Availability> getAvails(String date, String city) {
		String module = "2";
		List<Availability> avails = new ArrayList<>();
		if (centers.containsKey(city)) {
			for (String center : centers.get(city)) {
				String[] tokens = center.split("\\|");
				String page = this.dataProvider.getData(date, module, tokens[0], tokens[1], tokens[2]);
				Document document = Jsoup.parse(page);
				for (Element session : document.select("#test-session-table .row")) {
					if (session.select(".test-session-date").isEmpty()) {
						continue;
					}
					try {
						Availability availability = new Availability();
						availability.center = session.select(".col-md-3.centreTitle").get(0).ownText();
						availability.venue = session.select(".col-md-4.centreTitle").get(0).ownText();
						availability.date = sdf.parse(session.select(".sessionDate").attr("data-dateorder"));
						String avail = session.select(".test-session-radio").text();
						if (avail != null && avail.toLowerCase().contains("available")) {
							availability.available = true;
						}
						avails.add(availability);
					} catch (Exception e) {
						log.error("Can't parse availability for {} at {}", date, center);
					}
				}
			}

		}
		return sort(avails);
	}

	private List<Availability> sort(List<Availability> avails) {
		java.util.Collections.sort(avails, new Comparator<Availability>() {
			@Override
			public int compare(Availability o1, Availability o2) {
				return o1.date.compareTo(o2.date);
			}
		});
		return avails;
	}
}
