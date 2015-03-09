package fr.gsbcr.android;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

@SuppressLint("SimpleDateFormat")
public class CompteRenduDetailsActivity extends Activity {
	TextView tvPraFullName , tvBilan , tvMotif , tvDateVisite , tvRapDate , tvRapNum , tvRapLu , tvCoefConf;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.compte_rendu_details);
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		CompteRendu cr =  Modele.getCr();
		tvPraFullName = (TextView)findViewById(fr.gsbcr.android.R.id.tvPra);
		tvBilan = (TextView)findViewById(fr.gsbcr.android.R.id.tvBilan);
		tvMotif = (TextView)findViewById(fr.gsbcr.android.R.id.tvMotif);
		tvDateVisite= (TextView)findViewById(fr.gsbcr.android.R.id.tvDateVisite);
		tvRapDate = (TextView)findViewById(fr.gsbcr.android.R.id.tvRapDate);
		tvRapNum = (TextView)findViewById(fr.gsbcr.android.R.id.tvNumCR);
		tvRapLu = (TextView)findViewById(fr.gsbcr.android.R.id.tvRapLu);
		tvCoefConf = (TextView)findViewById(fr.gsbcr.android.R.id.tvCoefConf);
		tvPraFullName.setText(cr.getPraticien().getPraNom()+" "+cr.getPraticien().getPraPrenom());
		tvBilan.setText(cr.getsBilanCR());
		tvMotif.setText(cr.getsMotifCR().getLabelMotif());
		SimpleDateFormat df = new SimpleDateFormat("dd-mm-yyy");
		tvDateVisite.setText(df.format(cr.getdDateVisite()));
		tvRapDate.setText(df.format(cr.getdDateCR()));
		tvRapNum.setText(Integer.toString(cr.getiNumCR()));
		if(cr.getByEstLuCR().equals("1")){
			tvRapLu.setText("Oui");
		}
		else {
			tvRapLu.setText("Non");
		}
		tvCoefConf.setText(Integer.toString(cr.getiCoefConfCR()));

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		switch(item.getItemId()){
		case R.id.add_cr :
			startActivity(new Intent(CompteRenduDetailsActivity.this,NouvCompteRenduActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
			break;
		case R.id.logout :
			Builder al = new AlertDialog.Builder(CompteRenduDetailsActivity.this);
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
		case android.R.id.home:
			this.finish();
		break;
		case R.id.tableME :
			startActivity(new Intent(CompteRenduDetailsActivity.this,
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
				
						Intent intent = new Intent(CompteRenduDetailsActivity.this,CompteRenduListeActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
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

          
		}
		return super.onOptionsItemSelected(item);
	};


	
}
