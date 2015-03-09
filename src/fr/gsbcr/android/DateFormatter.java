package fr.gsbcr.android;

import java.text.SimpleDateFormat;
import java.util.Date;
import android.annotation.SuppressLint;
import android.net.ParseException;

public class DateFormatter {
	@SuppressLint("SimpleDateFormat")
	static public Date convertStringToDate(String dateString) throws java.text.ParseException {
		String startDateString = dateString;
		SimpleDateFormat df = new SimpleDateFormat("dd-mm-yyyy");
		Date startDate = null;
		try {
			startDate = df.parse(startDateString);
			String newDateString = df.format(startDate);
			System.out.println(newDateString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return startDate;
	}
}
