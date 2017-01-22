package it.espr.ielts.avails;

import java.net.URLEncoder;

import javax.inject.Inject;

import it.espr.gae.Http;

public class HttpDataProvider implements DataProvider {

	public static final String URL;

	@Inject
	private Http http;

	static {
		StringBuilder sb = new StringBuilder("https://my.ieltsessentials.com/Candidate/Booking/FindTestSession?");
		sb.append("TestSessionDate=").append("{{date}}");
		sb.append("&TestModuleId=").append("{{module}}");
		sb.append("&TestCentreId=").append("{{centre}}");
		sb.append("&TestVenueId=").append("{{venue}}");
		sb.append("&TestCentreLocationId=").append("{{location}}");

		URL = sb.toString();
	}

	@Override
	public String getData(String date, String module, String centre, String venue, String location) {
		String url = URL.replace("{{date}}", URLEncoder.encode(date)).replace("{{module}}", module).replace("{{centre}}", centre).replace("{{venue}}", venue).replace("{{location}}", location);
		return http.get(url);
	}
}
