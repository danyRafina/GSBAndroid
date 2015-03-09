package fr.gsbcr.android;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.AlertDialog.Builder;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.NumberPicker.OnValueChangeListener;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static android.widget.LinearLayout.VERTICAL;



@SuppressLint("SimpleDateFormat")
public class EchantillonsActivity extends Activity implements AsyncInterface{
	private List<TextView> tvList = new ArrayList<TextView>();
	private Button bBack ;
	private Button bValidate ;
	TableLayout layout ;
	NumberPicker np;
	private Medicament med ;
	private List<Offrir> off = new ArrayList<Offrir>();
	private SendTask sendTask;
	private static ProgressDialog prog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.echantillons);
		layout = (TableLayout)findViewById(R.id.tableME);
		off.clear();
		for(int i=0;i < Modele.getMedicaments().size();i++){
			TableRow row= new TableRow(this);
			TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT);
			row.setLayoutParams(lp);
			System.out.println(Modele.getMedicaments().get(i).getIdentifier());
			row.setGravity(Gravity.CENTER);
			np = new NumberPicker(EchantillonsActivity.this);
			np.setId(i);
			np.setMaxValue(20);
			np.setMinValue(1);
			TextView v = new TextView(this);
			v.setTextSize(20);
			v.setText(Modele.getMedicaments().get(i).getNomCommercial());
			v.setPadding(0,0,40,0);
			v.setTypeface(Typeface.DEFAULT_BOLD);
			row.addView(v);
			row.addView(np);
			med = Modele.getMedicaments().get(i);
			off.add(new Offrir(Modele.getVisiteur(),Modele.getCr().getPraticien(),Modele.getCr(),0,Modele.getMedicaments().get(i),i));
			np.setOnValueChangedListener(new OnValueChangeListener() {

				@Override
				public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

					for(int i = 0;i<off.size();i++){
						if(i == picker.getId()){

							off.get(i).setEchantillon(newVal);
						}
					}



					System.out.println(picker.getId()+" FOR "+med+":");
				}
			});
			layout.addView(row);
		}

	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			new AlertDialog.Builder(EchantillonsActivity.this)
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
								startActivity(new Intent(
										EchantillonsActivity.this,
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

	public void onBack(View view){
		new AlertDialog.Builder(EchantillonsActivity.this)
		.setTitle("Quitter le compte-rendu")
		.setCancelable(false)
		.setMessage(
				"Voulez-vous vraiment annuler la saisie de ce du compte-rendu ?")
				.setNegativeButton("Non",
						new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog,
							int which) {

					}
				})
				.setPositiveButton("Oui",
						new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog,
							int which) {
						try {
							startActivity(new Intent(
									EchantillonsActivity.this,
									DashboardActivity.class));
						} catch (IllegalStateException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}).show();
	}


	public void onValid(View view){
		new AlertDialog.Builder(EchantillonsActivity.this)
		.setTitle("Compte-rendu")
		.setCancelable(false)
		.setMessage(
				"Êtes-vous sûr de vouloir enregistrer ce compte-rendu ?")
				.setNegativeButton("Non",
						new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog,
							int which) {

					}
				})
				.setPositiveButton("Oui",
						new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog,
							int which) {
						sendTask = new SendTask();
						sendTask.delegation = EchantillonsActivity.this;
						String url = "http://"+Modele.getAddressAndPort()+"/insertCR/"+Modele.getVisiteur().getsColMatricule()+"/"+Modele.getVisiteur().getsColMdp();
						JSONObject json = new JSONObject();
						JSONArray jsonA = new JSONArray();
						JSONObject json3 = new JSONObject();
						try {
							String DATE_FORMAT_NOW = "yyyy-MM-dd";

							SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
							String stringDate = sdf.format(Modele.getCr().getdDateVisite());
							json.put("rapNum", Modele.getCr().getiNumCR());
							json.put("praNum", Modele.getCr().getPraticien().getPraNum());
							json.put("coefConf", Modele.getCr().getiCoefConfCR());
							json.put("rapBilan", Modele.getCr().getsBilanCR());
							json.put("motif", Modele.getCr().getsMotifCR().getNumMotif());
							json.put("dateVisite",stringDate);
							json.put("rapLu",Modele.getCr().getByEstLuCR());



							for(Offrir oneOff : off){
								JSONObject json2 = new JSONObject();
								json2.put("matricule",oneOff.getColMatricule().getsColMatricule());
								json2.put("medicament",oneOff.getMed().getDepotLegal());
								json2.put("rapNum", oneOff.getCr().getiNumCR());
								json2.put("quantite",oneOff.getEchantillon());
								jsonA.put(json2);


							}
							json3.put("Echantillon", jsonA);
							JSONObject j = new JSONObject();
							j.put("url",url);
							prog = new ProgressDialog(EchantillonsActivity.this);
							prog.setTitle("Envoi en cours . Veuillez patienter !");
							prog.setCancelable(false);
							prog.show();
							sendTask.execute(j,json,json3);

						}
						catch(JSONException je){
							je.printStackTrace();
						}
					}
				}).show();
	}
	public void onBack(){
		new AlertDialog.Builder(EchantillonsActivity.this)
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
							Modele.setCr(null);
							Modele.setMedicaments(null);
							startActivity(new Intent(
									EchantillonsActivity.this,
									DashboardActivity.class));
						} catch (IllegalStateException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}).show();

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
			new AlertDialog.Builder(EchantillonsActivity.this)
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
								Builder al = new AlertDialog.Builder(EchantillonsActivity.this);
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
			new AlertDialog.Builder(EchantillonsActivity.this)
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
								Builder al = new AlertDialog.Builder(EchantillonsActivity.this);
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
													} catch (IllegalStateException e) {
														// TODO Auto-generated catch block
														e.printStackTrace();
													} catch (IOException e) {
														// TODO Auto-generated catch block
														e.printStackTrace();
													}
													Modele.clear();
													Log.d("PASS","AFTER MODELE.CLEAR");
													try{
														startActivity(new Intent(
																EchantillonsActivity.this,
																LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).putExtra("EXIT", true));
														Log.d("PASS","AFTER 4");
													}
													catch(Exception e){
														Log.d("PASS","AFTER E "+e.getMessage());
													}
													//finish();

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
			new AlertDialog.Builder(EchantillonsActivity.this)
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

							DatePickerDialog dpd = new DatePickerDialog(EchantillonsActivity.this,
									new DatePickerDialog.OnDateSetListener() {

								@Override
								public void onDateSet(DatePicker view, int year,
										int monthOfYear, int dayOfMonth) {	

									Intent intent = new Intent(EchantillonsActivity.this,CompteRenduListeActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
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
	@Override
	public void processFinish(String output) throws JSONException {
		prog.dismiss();
		if(output != null){
			try{
				System.out.println("RESPONSE : "+output);}
			catch(Exception e) {
				e.printStackTrace();
			}
			JSONArray json = new JSONArray(output);
			JSONObject jsonO = json.getJSONObject(0);
			if(jsonO.getString("RESULT").equals("SUCCESS")){
				new AlertDialog.Builder(EchantillonsActivity.this)
				.setTitle("Compte-rendu")
				.setCancelable(false)
				.setMessage(
						"Votre compte-rendu a été bien enregistré")
						.setPositiveButton("OK",
								new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								Modele.setCr(null);
								Modele.setMedicaments(null);
								startActivity(new Intent(
										EchantillonsActivity.this,
										DashboardActivity.class));
							}
						}).show();
			}
			else if (jsonO.getString("RESULT").equals("REFUSED")){
				Toast.makeText(getApplicationContext(), "Connexion refusée !", Toast.LENGTH_LONG).show();
			}
			else if(jsonO.getString("RESULT").equals("ERROR [COMPTE-RENDU]")){
				new AlertDialog.Builder(EchantillonsActivity.this)
				.setTitle("Compte-rendu")
				.setCancelable(false)
				.setMessage(
						"Une erreur s'est produite lors de l'enregistrement du compte-rendu")
						.setPositiveButton("Réésayer ?",
								new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {

							}
						})
						.setNegativeButton("Quitter le compte-rendu",
								new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								onBack();
							}
						}).show();

			}
			else {
				new AlertDialog.Builder(EchantillonsActivity.this)
				.setTitle("Compte-rendu")
				.setCancelable(false)
				.setMessage(
						"Une erreur s'est produite lors de l'enregistrement des échantillons \n Contactez l'administrateur !")

						.setNegativeButton("Quitter le compte-rendu",
								new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								onBack();
							}
						}).show();
			}
		}
		else {
			Toast.makeText(getApplicationContext(), "Vérifiez votre connexion", Toast.LENGTH_LONG).show();
		}
	}
	public void onShowDialog(final Class<?> class1){
		new AlertDialog.Builder(EchantillonsActivity.this)
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
						startActivity(new Intent(EchantillonsActivity.this,
								class1).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
					}
				}).show();
	}

}


