package it.espr.ielts.avails;

import it.espr.mvc.Configuration;

public class WebConfig extends Configuration {

	@Override
	protected void configureMvc() {
		route().get("/(:date)").to(Parser.class, "getAllAvails");
		route().get("/(:date)/(:city)").to(Parser.class, "getAvails");
		this.bind(DataProvider.class).to(HttpDataProvider.class);
	}
}
