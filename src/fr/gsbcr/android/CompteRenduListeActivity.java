package fr.gsbcr.android;

import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Toast;

public class CompteRenduListeActivity extends Activity implements
AsyncInterface {
	ListView lvCR;
	private List<CompteRendu> lesCR = new ArrayList<CompteRendu>();
	RequestTask task = null;
	private NewAdapter adapter = null;
	private ProgressDialog prog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.compte_rendu_liste);
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		if (task != null) {
			RequestTask.toGetStatus(task);
		}
		lesCR.clear();
		task = new RequestTask();
		task.delegation = this;
		Bundle bundle = getIntent().getExtras();
		System.out.println(bundle.getInt("Year"));
		System.out.println(bundle.getInt("Month"));
		String post = "http://"+Modele.getAddressAndPort()+"/listCR/" + bundle.getInt("Year")
				+ "/" + (bundle.getInt("Month") + 1) + "/"
				+ Modele.getVisiteur().getsColMatricule() + "/"
				+ Modele.getVisiteur().getsColMdp();
		task.execute(post);
		this.preProcess();

		this.adapter = new NewAdapter(this, lesCR);
		lvCR = (ListView) findViewById(fr.gsbcr.android.R.id.listCompteRendu);
		lvCR.setAdapter(adapter);
		lvCR.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Modele.setCr(lesCR.get(position));
				startNewActivity(position);

			}
		});
	}

	protected void startNewActivity(int position) {
		Intent intent = new Intent(getBaseContext(),
				CompteRenduDetailsActivity.class);
		startActivity(intent);
	}

	public void onDialogShow(String title){
		new AlertDialog.Builder(CompteRenduListeActivity.this)
		.setTitle("Erreur")
		.setCancelable(false)
		.setMessage(title)
		.setPositiveButton("OK",
				new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog,
					int which) {
			finish();
				startActivity(new Intent(CompteRenduListeActivity.this,DashboardActivity.class));
			}
		}).setIcon(android.R.drawable.ic_dialog_alert)
		.show();
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.add_cr :
			startActivity(new Intent(CompteRenduListeActivity.this,NouvCompteRenduActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
			break;
		case R.id.logout:
			Builder al = new AlertDialog.Builder(CompteRenduListeActivity.this);
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
		case R.id.tableME:
			startActivity(new Intent(CompteRenduListeActivity.this,
					AboutActivity.class));
			break;
		case R.id.quit:
			new AlertDialog.Builder(this)
			.setTitle("Quitter l'application")
			.setMessage(
					"Voulez-vous vraiment quitter cette application ?")
					.setNegativeButton("Non",
							new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog,
								int which) {
							// do nothing
						}
					})
					.setPositiveButton("Oui",
							new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog,
								int which) {
							try {
								RequestTask.onCloseConnection();
								Modele.setVisiteur(null);
							} catch (IllegalStateException
									| IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							Intent intent = new Intent(
									getApplicationContext(),
									LoginActivity.class);
							intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							intent.putExtra("EXIT", true);
							startActivity(intent);

						}
					}).setIcon(android.R.drawable.ic_dialog_alert)
					.show();

			break;
		case R.id.list_cr:
			final Calendar c = Calendar.getInstance();
			int mYear = c.get(Calendar.YEAR);
			int mMonth = c.get(Calendar.MONTH);
			int mDay = c.get(Calendar.DAY_OF_MONTH);

			DatePickerDialog dpd = new DatePickerDialog(this,
					new DatePickerDialog.OnDateSetListener() {

				@Override
				public void onDateSet(DatePicker view, int year,
						int monthOfYear, int dayOfMonth) {	
				
						Intent intent = new Intent(CompteRenduListeActivity.this,CompteRenduListeActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
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

	@Override
	public void processFinish(String output) throws JSONException {
		lesCR.clear();
		prog.dismiss();
		if(output != null){
		JSONArray json = new JSONArray(output);
		if (!json.getJSONObject(0).has("RESULT")) {
			for (int i = 0; i < json.length(); i++) {
				JSONObject row = json.getJSONObject(i);
				Praticien praticien = new Praticien(Integer.parseInt(row
						.getString("PRA_NUM").toString()), row.getString(
								"PRA_NOM").toString(), row.getString("PRA_PRENOM")
								.toString());
				try {
					lesCR.add(new CompteRendu(Modele.getVisiteur(), Integer
							.parseInt(row.getString("NUM").toString()),
							praticien, DateFormatter.convertStringToDate(row
									.getString("RAP_DATE").toString()),
									DateFormatter.convertStringToDate(row.getString(
											"DATE_VISITE").toString()), row
											.getString("BILAN"), new Motif(row.getInt("NUMMOTIF"),row.getString(
													"LABELMOTIF").toString()), Integer
													.parseInt(row.getString("COEFCONF")
															.toString()), row
															.getString("RAPLU").toString()));
					if(this.adapter != null){
						this.adapter.notifyDataSetChanged();
					}
				} catch (NumberFormatException | ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		else {
			this.onDialogShow("Il n'y a pas de compte-rendu à cette date ");
		}
		}
		else {
			this.onDialogShow("Oups ! Vérifiez votre connexion internet !");
		}
	}

	public void preProcess() {
		prog = new ProgressDialog(CompteRenduListeActivity.this);
		prog.setTitle("Chargement de la liste . Veuillez patienter !");
		prog.setCancelable(false);
		prog.show();
	}


}