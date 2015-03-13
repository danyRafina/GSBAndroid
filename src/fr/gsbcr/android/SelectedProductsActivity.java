package fr.gsbcr.android;

import java.io.IOException;
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
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.Toast;

public class SelectedProductsActivity extends Activity implements AsyncInterface , OnClickListener{
	private List<Medicament> medicaments = new ArrayList<Medicament>();
	private String[] medicamentS ;
	private List<Medicament> medicamentSelected = new ArrayList<Medicament>();
	protected Button selectMedButton;
	private ProgressDialog prog;

	Spinner spinner;
	RequestTask task = null;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.selected_products);
		if (task != null) {
			RequestTask.toGetStatus(task);
		}
		task = new RequestTask();
		medicamentSelected.clear();
		task.delegation = this;
		selectMedButton = (Button) findViewById(R.id.select_med);
		selectMedButton.setText("Aucun produit sélectionné");

		selectMedButton.setOnClickListener(this);
		String post = "http://"+Modele.getAddressAndPort()+"/listMedicament/"
				+ Modele.getVisiteur().getsColMatricule() + "/"
				+ Modele.getVisiteur().getsColMdp();
		if(isNetworkAvailable() == true){
			task.execute(post);
			this.preProcess();
		}
		else {
			String text = "Oups ! Vous avez désactiver votre WIFI ou votre réseau cellulaire . \n  Veuillez l'activer SVP !";
			Context context = getApplicationContext();
			Toast.makeText(context, text,Toast.LENGTH_LONG).show();
		}
		Button buttonC = (Button) findViewById(fr.gsbcr.android.R.id.cancel);
		buttonC.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				new AlertDialog.Builder(SelectedProductsActivity.this)
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
									finish();
									startActivity(new Intent(
											SelectedProductsActivity.this,
											DashboardActivity.class));
								} catch (IllegalStateException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}).show();

			}
		});
	}

	protected boolean isNetworkAvailable(){
		ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			new AlertDialog.Builder(SelectedProductsActivity.this)
			.setTitle("Quitter le compte-rendu")
			.setCancelable(false)
			.setMessage(
					"Voulez-vous vraiment annuler la saisie de ce compte-rendu ?")
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
								finish();
								startActivity(new Intent(
										SelectedProductsActivity.this,
										DashboardActivity.class));
							} catch (IllegalStateException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}).show();
			return true;}
		return super.onKeyDown(keyCode, event);
	}

	protected void showSelectMedDialog() {

		boolean[] checkedMed = new boolean[medicamentS.length];

		int count = medicamentS.length;

		for(int i = 0; i < count; i++)

			checkedMed[i] = medicamentSelected.contains(onSearch(medicamentS[i]));

		DialogInterface.OnMultiChoiceClickListener coloursDialogListener = new DialogInterface.OnMultiChoiceClickListener() {

			@Override

			public void onClick(DialogInterface dialog, int which, boolean isChecked) {

				if(isChecked) 

					medicamentSelected.add(onSearch(medicamentS[which]));

				else

					medicamentSelected.remove(onSearch(medicamentS[which]));

				onChangeSelectedMed();

			}

		};


		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		builder.setTitle("Sélectionnez le(s) médicament(s)").setCancelable(false);
		builder.setPositiveButton("Valider",
				new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog,
					int which) {
				try {

				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});


		builder.setMultiChoiceItems(medicamentS, checkedMed, coloursDialogListener);

		AlertDialog dialog = builder.create();

		dialog.show();

	}
	public void onBack(View view){
		new AlertDialog.Builder(SelectedProductsActivity.this)
		.setTitle("Quitter le compte-rendu")
		.setCancelable(false)
		.setMessage(
				"Voulez-vous vraiment annuler la saisie de ce du compte-rendu ?")
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
									SelectedProductsActivity.this,
									DashboardActivity.class));
						} catch (IllegalStateException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}).show();

	}

	public void onBack(){
		new AlertDialog.Builder(SelectedProductsActivity.this)
		.setTitle("Quitter le compte-rendu")
		.setCancelable(false)
		.setMessage(
				"Voulez-vous vraiment annuler la saisie de ce du compte-rendu ?")
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
							finish();
							startActivity(new Intent(
									SelectedProductsActivity.this,
									DashboardActivity.class));
						} catch (IllegalStateException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}).show();

	}
	protected void onChangeSelectedMed() {

		StringBuilder stringBuilder = new StringBuilder();

		for(int i =0;i<medicamentSelected.size();i++){
			stringBuilder.append(medicamentSelected.get(i).toString()+"\n");
		}
		if(medicamentSelected.isEmpty()){
			stringBuilder.append("Aucun produit sélectionné");
		}


		selectMedButton.setText(stringBuilder.toString());

	}

	@Override
	public void processFinish(String output) throws JSONException {
		prog.dismiss();
		if(output != null){
			medicaments.clear();
			medicamentSelected.clear();
			JSONArray jsonMedicament = new JSONArray(output);
			medicamentS = new String[jsonMedicament.length()];
			for (int i = 0; i < jsonMedicament.length();i++) {
				JSONObject row = jsonMedicament.getJSONObject(i);
				Medicament med = new Medicament(row.getString(
						"DEPOTLEGAL").toString(), row.getString("NOMCOMMERCIAL").toString(),i);
				medicaments.add(med);
				medicamentS[i] = row.getString("NOMCOMMERCIAL").toString();
			}
		}
		else {
			new AlertDialog.Builder(SelectedProductsActivity.this)
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
										SelectedProductsActivity.this,
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

	@Override

	public void onClick(View view) {

		switch(view.getId()) {

		case R.id.select_med:
			showSelectMedDialog();

			break;

		default:

			break;

		}

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
			new AlertDialog.Builder(SelectedProductsActivity.this)
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
								Builder al = new AlertDialog.Builder(SelectedProductsActivity.this);
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
														Modele.setVisiteur(null);
													} catch (IllegalStateException
															| IOException e) {
														// TODO Auto-generated catch block
														e.printStackTrace();
													}
													finish();
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
			new AlertDialog.Builder(SelectedProductsActivity.this)
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
								Builder al = new AlertDialog.Builder(SelectedProductsActivity.this);
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
														Modele.clear();
													} catch (IllegalStateException
															| IOException e) {
														// TODO Auto-generated catch block
														e.printStackTrace();
													}
													finish();
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
			new AlertDialog.Builder(SelectedProductsActivity.this)
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

							DatePickerDialog dpd = new DatePickerDialog(SelectedProductsActivity.this,
									new DatePickerDialog.OnDateSetListener() {

								@Override
								public void onDateSet(DatePicker view, int year,
										int monthOfYear, int dayOfMonth) {	
									finish();
									Intent intent = new Intent(SelectedProductsActivity.this,CompteRenduListeActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
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

	public void onNext(View view){
		if(!medicamentSelected.isEmpty()){
			Modele.setMedicaments(medicamentSelected);
			Intent intent = new Intent(SelectedProductsActivity.this,EchantillonsActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
			startActivity(intent);
		}
		else {
			new AlertDialog.Builder(SelectedProductsActivity.this)
			.setTitle("Attention")
			.setCancelable(false)
			.setMessage(
					"Vous n'avez pas sélectionné de médicament !")
					.setNegativeButton("Annuler le compte-rendu",
							new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog,
								int which) {
							onBack();
						}
					})
					.setPositiveButton("Je sélectionne",
							new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog,
								int which) {

						}
					}).show();
		}
	}

	protected Medicament onSearch(String search){
		Medicament med = null;
		for(int i = 0;i<medicaments.size();i++){
			if(medicaments.get(i).getNomCommercial() == search){
				med = medicaments.get(i);
			}
		}
		return med;


	}


	public void clearList(View view){
		medicamentSelected.clear();
		onChangeSelectedMed();

	}

	public void onShowDialog(final Class<?> class1){
		new AlertDialog.Builder(SelectedProductsActivity.this)
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
						finish();
						startActivity(new Intent(SelectedProductsActivity.this,
								class1).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
					}
				}).show();
	}
	public void preProcess() {
		prog = new ProgressDialog(SelectedProductsActivity.this);
		prog.setTitle("Chargement de la liste des produits . Veuillez patienter !");
		prog.setCancelable(false);
		prog.show();

	}




}


