package fr.gsbcr.android;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.DatePickerDialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;

public class CustomDatePicker {

	protected static List<Integer> result = new ArrayList<Integer>();
	protected DatePickerDialog createDialogWithoutDateField(String title,
			Context context) {
		final Calendar c = Calendar.getInstance();
		int mYear = c.get(Calendar.YEAR);
		int mMonth = c.get(Calendar.MONTH);
		int mDay = c.get(Calendar.DAY_OF_MONTH);

		DatePickerDialog dpd = new DatePickerDialog(context,
				new DatePickerDialog.OnDateSetListener() {

			@Override
			public void onDateSet(DatePicker view, int year,
					int monthOfYear, int dayOfMonth) {	
				result.clear();
				result.add(year);
				result.add(monthOfYear);
				
			}
		}, mYear, mMonth, mDay);
		java.lang.reflect.Field[] datePickerDialogFields = dpd.getClass()
				.getDeclaredFields();
		for (java.lang.reflect.Field datePickerDialogField : datePickerDialogFields) {
			if (datePickerDialogField.getName().equals("mDatePicker")) {
				datePickerDialogField.setAccessible(true);

				DatePicker datePicker = null;
				try {
					datePicker = (DatePicker) datePickerDialogField.get(dpd);
				} catch (IllegalAccessException | IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				java.lang.reflect.Field[] datePickerFields = datePickerDialogField
						.getType().getDeclaredFields();
				for (java.lang.reflect.Field datePickerField : datePickerFields) {
					Log.i("test", datePickerField.getName());
					if ("mDaySpinner".equals(datePickerField.getName())) {
						datePickerField.setAccessible(true);
						Object dayPicker = new Object();
						try {
							dayPicker = datePickerField.get(datePicker);
						} catch (IllegalAccessException
								| IllegalArgumentException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						((View) dayPicker).setVisibility(View.GONE);

					}
				}
			}
		}
		return dpd;

	}

	protected void show(DatePickerDialog dpd) {
		dpd.show();

	}
}
