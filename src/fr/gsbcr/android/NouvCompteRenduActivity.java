package fr.gsbcr.android;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@SuppressLint("SimpleDateFormat")
public class NouvCompteRenduActivity extends Activity implements AsyncInterface {
	private List<Praticien> praticiens = new ArrayList<Praticien>();
	private List<Motif> motifs = new ArrayList<Motif>();
	TextView tvDate;
	RequestTask task = null;
	Spinner spinnerPra;
	Spinner spinnerMotif;
	CompteRendu cr;
	EditText coefConf ;
	EditText bilan ;
	private int nextNum ;
	private ProgressDialog prog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_compte_rendu);
		tvDate = (TextView) findViewById(fr.gsbcr.android.R.id.tvDate);
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		String date = sdf.format(new Date());
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		tvDate.setText(date);
		if (task != null) {
			RequestTask.toGetStatus(task);
		}
		task = new RequestTask();
		task.delegation = this;
		String post = "http://"+Modele.getAddressAndPort()+"/listsForCR/"
				+ Modele.getVisiteur().getsColMatricule() + "/"
				+ Modele.getVisiteur().getsColMdp();
		this.preProcess();
		task.execute(post);

		Button buttonC = (Button) findViewById(fr.gsbcr.android.R.id.cancel);
		buttonC.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onBack();
			}
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			onBack();
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

	public void onBack(){
		new AlertDialog.Builder(NouvCompteRenduActivity.this)
		.setTitle("Quitter le compte-rendu")
		.setCancelable(false)
		.setMessage(
				"Voulez-vous vraiment annuler la création du compte-rendu ?")
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
							startActivity(new Intent(
									NouvCompteRenduActivity.this,
									DashboardActivity.class));
						} catch (IllegalStateException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}).show();
	}
	public void addItemsOnSpinner() {
		spinnerPra = (Spinner) findViewById(R.id.SpinnerPra);

		ArrayAdapter<Praticien> dataAdapterPra = new ArrayAdapter<Praticien>(
				this, android.R.layout.simple_spinner_item, this.praticiens);
		dataAdapterPra
		.setDropDownViewResource(android.R.layout.simple_selectable_list_item);
		spinnerPra.setAdapter(dataAdapterPra);
		spinnerPra.setPrompt("Praticiens");

		spinnerMotif = (Spinner) findViewById(R.id.spinnerMotif);
		ArrayAdapter<Motif> dataAdapterMotif = new ArrayAdapter<Motif>(this,
				android.R.layout.simple_spinner_item, motifs);
		dataAdapterMotif
		.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerMotif.setAdapter(dataAdapterMotif);
		spinnerMotif.setPrompt("Motifs");


	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.insert_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.logout :
			try {
				Modele.clear();
				RequestTask.onCloseConnection();

			} catch (IllegalStateException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			new AlertDialog.Builder(NouvCompteRenduActivity.this)
			.setTitle("Quitter le compte-rendu")
			.setCancelable(false)
			.setMessage(
					"Voulez-vous vraiment annuler la création du compte-rendu ?")
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
								Builder al = new AlertDialog.Builder(NouvCompteRenduActivity.this);
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

							} catch (IllegalStateException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}).show();

		
		break;
		case android.R.id.home:
			onBack();
			break;

		case R.id.add_cr :

			this.onShowDialog(NouvCompteRenduActivity.class);
			break;
		case R.id.quit :
			new AlertDialog.Builder(NouvCompteRenduActivity.this)
			.setTitle("Quitter le compte-rendu")
			.setCancelable(false)
			.setMessage(
					"Voulez-vous vraiment annuler la création du compte-rendu ?")
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
								Builder al = new AlertDialog.Builder(NouvCompteRenduActivity.this);
								al.setTitle("Quitter l'application")
								.setMessage(
										"Voulez-vous vraiment quitter cette application ?")
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

							} catch (IllegalStateException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}).show();

			break;
		case R.id.list_cr :	
			new AlertDialog.Builder(NouvCompteRenduActivity.this)
			.setTitle("Quitter le compte-rendu")
			.setCancelable(false)
			.setMessage(
					"Voulez-vous vraiment annuler la création du compte-rendu ?")
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
							final Calendar c = Calendar.getInstance();
							int mYear = c.get(Calendar.YEAR);
							int mMonth = c.get(Calendar.MONTH);
							int mDay = c.get(Calendar.DAY_OF_MONTH);

							DatePickerDialog dpd = new DatePickerDialog(NouvCompteRenduActivity.this,
									new DatePickerDialog.OnDateSetListener() {

								@Override
								public void onDateSet(DatePicker view, int year,
										int monthOfYear, int dayOfMonth) {	

									Intent intent = new Intent(NouvCompteRenduActivity.this,CompteRenduListeActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
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
						}
					}).show();

			break;
		}
		return super.onOptionsItemSelected(item);
	}

	public void changeDate(View vue) {
		final Calendar c = Calendar.getInstance();
		int mYear = c.get(Calendar.YEAR);
		int mMonth = c.get(Calendar.MONTH);
		int mDay = c.get(Calendar.DAY_OF_MONTH);

		DatePickerDialog dpd = new DatePickerDialog(this,
				new DatePickerDialog.OnDateSetListener() {

			@Override
			public void onDateSet(DatePicker view, int year,
					int monthOfYear, int dayOfMonth) {
				monthOfYear++;
				Calendar calendar = new GregorianCalendar(year,
						monthOfYear, dayOfMonth);
				SimpleDateFormat sdf = new SimpleDateFormat(
						"dd-MM-yyyy");
				String date = sdf.format(calendar.getTime());
				tvDate.setText(date);

			}
		}, mYear, mMonth, mDay);
		dpd.show();

	}

	@Override
	public void processFinish(String output) throws JSONException {
		prog.dismiss();
		praticiens.clear();
		motifs.clear();
		if(output != null){
			JSONObject json = new JSONObject(output);
			JSONArray jsonPraticiens = new JSONArray(json.getString("PRA_LIST"));
			JSONArray jsonMotifs = new JSONArray(json.getString("MOTIF_LIST"));
			this.nextNum = Integer.parseInt(json.getString("COUNT_RAP"));
			System.out.println(this.nextNum);
			for (int i = 0; i < jsonPraticiens.length(); i++) {
				JSONObject row = jsonPraticiens.getJSONObject(i);
				Praticien praticien = new Praticien(Integer.parseInt(row.getString(
						"NUM").toString()), row.getString("NOM").toString(), row
						.getString("PRENOM").toString());
				praticiens.add(praticien);
			}
			for (int i = 0; i < jsonMotifs.length(); i++) {
				JSONObject row = jsonMotifs.getJSONObject(i);
				Motif motif = new Motif(Integer.parseInt(row.getString("NUM")
						.toString()), row.getString("LABEL").toString());
				motifs.add(motif);
			}
			addItemsOnSpinner();
		}
		else {

			new AlertDialog.Builder(NouvCompteRenduActivity.this)
			.setTitle("Erreur")
			.setCancelable(false)
			.setMessage(
					"Vérifiez votre connexion internet !")
					.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog,
								int which) {
							try {
								startActivity(new Intent(
										NouvCompteRenduActivity.this,
										DashboardActivity.class));
							} catch (IllegalStateException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					})
					.setIcon(android.R.drawable.ic_dialog_alert)
					.show();
		}

	}

	public void onNext(View view) throws ParseException{
		tvDate = (TextView) findViewById(fr.gsbcr.android.R.id.tvDate);
		spinnerPra = (Spinner) findViewById(R.id.SpinnerPra);
		spinnerMotif = (Spinner) findViewById(R.id.spinnerMotif);
		coefConf = (EditText) findViewById(fr.gsbcr.android.R.id.coefConf);
		bilan = (EditText) findViewById(fr.gsbcr.android.R.id.bilanCR);
		Praticien praticien = (Praticien)spinnerPra.getSelectedItem();
		Motif motif = (Motif)spinnerMotif.getSelectedItem();
		if(bilan.getText().toString().matches("") || coefConf.getText().toString().matches("") || bilan.getText().toString().trim().length() == 0 || coefConf.getText().toString().trim().length() == 0) {
			new AlertDialog.Builder(NouvCompteRenduActivity.this)
			.setTitle("Erreur")
			.setCancelable(false)
			.setMessage(
					"Un des champs est vide !")
					.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog,
								int which) {
						}
					}).show();

		}
		else {
			CompteRendu compteRendu = new CompteRendu(Modele.getVisiteur(),this.nextNum, praticien,new Date(), DateFormatter.convertStringToDate(tvDate.getText().toString()),bilan.getText().toString(),motif,Integer.parseInt(coefConf.getText().toString()),"0");
			System.out.println(compteRendu);
			Intent intent = new Intent(NouvCompteRenduActivity.this,SelectedProductsActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
			Modele.setCr(compteRendu);
			startActivity(intent);
		}


	}

	public void onShowDialog(final Class<?> class1){
		new AlertDialog.Builder(NouvCompteRenduActivity.this)
		.setTitle("Quitter le compte-rendu")
		.setCancelable(false)
		.setMessage(
				"Voulez-vous vraiment annuler la création du compte-rendu ?")
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
						startActivity(new Intent(NouvCompteRenduActivity.this,
								class1).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
					}
				}).show();
	}

	public void preProcess() {
		prog = new ProgressDialog(NouvCompteRenduActivity.this);
		prog.setTitle("Chargement des listes . Veuillez patienter !");
		prog.setCancelable(false);
		prog.show();
		
	}


}
