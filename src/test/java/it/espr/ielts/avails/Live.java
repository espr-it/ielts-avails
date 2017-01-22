package it.espr.ielts.avails;

import java.text.ParseException;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.gson.Gson;

import it.espr.injector.Configuration;
import it.espr.injector.Injector;

@RunWith(MockitoJUnitRunner.class)
public class Live {

	private static final Injector injector;

	static {
		injector = Injector.injector(new Configuration() {

			@Override
			protected void configure() {
				this.bind(DataProvider.class).to(HttpDataProvider.class);
			}
		});
	}

	@Test
	public void parse() throws ParseException {
		List<Availability> availabilities = injector.get(Parser.class).getAllAvails("01 02 2017");
		System.out.println(new Gson().toJson(availabilities));
		System.out.println(availabilities);
	}
}
