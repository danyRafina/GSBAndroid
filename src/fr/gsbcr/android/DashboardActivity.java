package fr.gsbcr.android;

import java.io.IOException;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.TextView;

public class DashboardActivity extends Activity {
	TextView tvFullName;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dashboard);
		tvFullName = (TextView) findViewById(fr.gsbcr.android.R.id.tvFullName);
		tvFullName.setText("Bienvenue "+Modele.getVisiteur().getsColNom()+" "+Modele.getVisiteur().getsColPrenom());

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			//DO NOTHING
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		switch(item.getItemId()){
		case R.id.logout :
			Builder al = new AlertDialog.Builder(DashboardActivity.this);
			al.setTitle("Déconnexion")
			.setMessage(
					"Voulez-vous vraiment vous déconnecter ?")
					.setNegativeButton("Non",
							new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog,
								int which) {

						}})
						.setPositiveButton("Oui",
								new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								try {
									RequestTask.onCloseConnection();
									Modele.clear();
								} catch (IllegalStateException
										| IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								Intent intent = new Intent(
										getApplicationContext(),
										LoginActivity.class);
								intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							
								startActivity(intent);

							}
						}).setIcon(android.R.drawable.ic_dialog_alert)
						.show();
			
			break;
		case R.id.add_cr :
			startActivity(new Intent(DashboardActivity.this,
					NouvCompteRenduActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
			break;
		case R.id.tableME :
			startActivity(new Intent(DashboardActivity.this,
					AboutActivity.class));
			break;
		case R.id.quit :
			new AlertDialog.Builder(this)
			.setTitle("Quitter l'application")
			.setMessage("Voulez-vous vraiment quitter cette application ?")
			.setNegativeButton("Non", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) { 
					// do nothing
				}
			})
			.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					try {
						Modele.setVisiteur(null);
						RequestTask.onCloseConnection();
					} catch (IllegalStateException | IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					intent.putExtra("EXIT", true);
					startActivity(intent);
				}
			})
			.setIcon(android.R.drawable.ic_dialog_alert)
			.show();

			break;
		case R.id.list_cr :			        
			final Calendar c = Calendar.getInstance();
			int mYear = c.get(Calendar.YEAR);
			int mMonth = c.get(Calendar.MONTH);
			int mDay = c.get(Calendar.DAY_OF_MONTH);

			DatePickerDialog dpd = new DatePickerDialog(this,
					new DatePickerDialog.OnDateSetListener() {

				@Override
				public void onDateSet(DatePicker view, int year,
						int monthOfYear, int dayOfMonth) {	
				
						Intent intent = new Intent(DashboardActivity.this,CompteRenduListeActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
						intent.putExtra("Year",(int)year);
						intent.putExtra("Month",monthOfYear);
						startActivity(intent);
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
			dpd.setTitle("Mois et année de la rédaction du compte-rendu");
			dpd.setCancelable(false);
			dpd.setButton(
					DatePickerDialog.BUTTON_POSITIVE, "Valider",dpd);
			dpd.show();
		break;




	}return super.onOptionsItemSelected(item);

}



}
