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
public class PraticienActivity extends Activity {
	TextView idPra , nom , prenom , adresse , cp ,ville , coefN, prof , lieu ; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.praticien_details);
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		Praticien pra = Modele.getPraticien();
		idPra = (TextView)findViewById(fr.gsbcr.android.R.id.numPra);
		nom = (TextView)findViewById(fr.gsbcr.android.R.id.nomPra);
		prenom = (TextView)findViewById(fr.gsbcr.android.R.id.prenomPra);
		adresse = (TextView)findViewById(fr.gsbcr.android.R.id.addPra);
		cp = (TextView)findViewById(fr.gsbcr.android.R.id.cp);
		ville = (TextView)findViewById(fr.gsbcr.android.R.id.villePra);
		coefN = (TextView)findViewById(fr.gsbcr.android.R.id.coefN);
		prof = (TextView)findViewById(fr.gsbcr.android.R.id.proPra);
		lieu = (TextView)findViewById(fr.gsbcr.android.R.id.local);
		idPra.setText(""+pra.getPraNum());
		nom.setText(pra.getPraNom());
		prenom.setText(pra.getPraPrenom());
		adresse.setText(pra.getPraAdresse());
		cp.setText(""+pra.getPraCP());
		ville.setText(pra.getPraVille());
		coefN.setText(""+pra.getPraCoefN());
		prof.setText(pra.getPraProfession());
		lieu.setText(pra.getPraLieuTravail());


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
			startActivity(new Intent(PraticienActivity.this,NouvCompteRenduActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
			break;
		case R.id.logout :
			Builder al = new AlertDialog.Builder(PraticienActivity.this);
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
			startActivity(new Intent(PraticienActivity.this,
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

					Intent intent = new Intent(PraticienActivity.this,CompteRenduListeActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
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
