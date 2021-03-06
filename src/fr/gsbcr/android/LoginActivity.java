package fr.gsbcr.android;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

@SuppressLint("NewApi")
public class LoginActivity extends Activity implements AsyncInterface {

	EditText etId, etPassword;
	Context context;
	CharSequence text;
	int duration = Toast.LENGTH_SHORT;
	Toolbar mToolbar;
	RequestTask task = new RequestTask();
	private ProgressDialog prog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		if (getIntent().getBooleanExtra("EXIT", false)) {
			finish();  
		}
		task.delegation = this;
		Modele.setAddressAndPort("192.168.0.17:8080");
		etId = (EditText) findViewById(fr.gsbcr.android.R.id.etId);
		etPassword = (EditText) findViewById(fr.gsbcr.android.R.id.etPassword);
		etId.setText("");
		etPassword.setText("");

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.login_menu, menu);
		return true;
	}

	protected boolean isNetworkAvailable(){
		ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		switch(item.getItemId()){
		case R.id.about_login :
			startActivity(new Intent(LoginActivity.this,
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
					//CompteRenduListeActivity.this.finish();
					finish();
				}
			})
			.setIcon(android.R.drawable.ic_dialog_alert)
			.show();

			break;


		}
		return super.onOptionsItemSelected(item);
	}

	@SuppressWarnings("unused")
	private void hideSystemUI() {
		getWindow().getDecorView().setSystemUiVisibility(
				View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
				| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
				| View.SYSTEM_UI_FLAG_FULLSCREEN
				| View.SYSTEM_UI_FLAG_IMMERSIVE);
	}

	public void onReset(View vue) {
		etId.setText("");
		etPassword.setText("");
	}

	public void onValidate(View vue) throws InterruptedException,
	ExecutionException, TimeoutException {

		if (etId.getText().toString().trim().equals("")
				|| etPassword.getText().toString().trim().equals("")) {
			this.task = null;
			text = "L'un des champs est vide !";
			context = getApplicationContext();
			Toast.makeText(context, text, duration).show();
		} else {
			String post = "http://"+Modele.getAddressAndPort()+"/connection/"
					+ etId.getText().toString() + "/"
					+ etPassword.getText().toString();
			if(this.isNetworkAvailable() == true) {
				task = new RequestTask();
				task.delegation = this;
				this.preProcess();
				this.task.execute(post);
			}
			else {
				text = "Oups ! Vous avez désactiver votre WIFI ou votre réseau cellulaire . \n  Veuillez l'activer SVP !";
				context = getApplicationContext();
				Toast.makeText(context, text, duration).show();
			}
		}
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
	public void processFinish(String output) throws JSONException {
		
		prog.dismiss();
		context = getApplicationContext();
		if(output != null){
			JSONObject json = new JSONObject(output);
			if (json.getString("RESULT").equals("ACCEPT")) {
				Collaborateur visiteur = new Collaborateur(json.getString("MATRICULE"),json.getString("NOM"),json.getString("PRENOM"),json.getString("MDP"));
				Modele.setVisiteur(visiteur);
				startActivity(new Intent(LoginActivity.this,
						DashboardActivity.class));
			} else {
				this.task = null;
				etId.setText("");
				etPassword.setText("");
				text = "Votre identifiant ou mot de passe est incorrect";
				Toast.makeText(context, text, duration).show();



			}
		}
		else {
			text = "Un problème est survenue avec votre connexion !";
			Toast.makeText(context, text, duration).show();
		}
	}

	public void preProcess() {
		prog = new ProgressDialog(LoginActivity.this);
		prog.setTitle("Connexion en cours . Veuillez patienter !");
		prog.setCancelable(false);
		prog.show();

	}



}
