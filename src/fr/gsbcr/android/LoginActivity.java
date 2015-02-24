package fr.gsbcr.android;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.concurrent.ExecutionException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		task.delegation = this;
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
		        public void onClick(DialogInterface dialog, int which) { 
		            // do nothing
		        }
		     })
		     .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int which) { 
		        	  finish();
		              System.exit(0);
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
			ExecutionException {

		if (etId.getText().toString().trim().equals("")
				|| etPassword.getText().toString().trim().equals("")) {
			this.task = null;
			text = "L'un des champs est vide !";
			context = getApplicationContext();
			Toast.makeText(context, text, duration).show();
		} else {
			String post = "http://192.168.1.1:8080/connection/"
					+ etId.getText().toString() + "/"
					+ etPassword.getText().toString();
				task = new RequestTask();
				task.delegation = this;
				this.task.execute(post);
			}
	}

	public void processFinish(String output) throws JSONException {
		context = getApplicationContext();
		JSONObject json = new JSONObject(output);
		if (json.getString("RESULT").equals("ACCEPT")) {
			startActivity(new Intent(LoginActivity.this,
					CompteRenduActivity.class));
		} else {
				this.task = null;
				text = "Votre identifiant ou mot de passe est incorrect";
				Toast.makeText(context, text, duration).show();

			

		}
	}

}
