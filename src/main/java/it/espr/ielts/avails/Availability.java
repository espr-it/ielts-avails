package it.espr.ielts.avails;

import java.util.Date;

public class Availability {

	String center;

	String venue;

	Date date;

	boolean available;

	public String toString() {
		return (available ? "Available" : "Not Available") + " - " + date + " (" + center + (venue != null ? " - " + venue : "") + ")<br>";
	}
}
