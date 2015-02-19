package fr.gsbcr.android;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

@SuppressLint("NewApi")
public class LoginActivity extends Activity {

	EditText etId,etPassword;
	Context context ;
	CharSequence text ;
	int duration = Toast.LENGTH_SHORT;
	String ida = "dany";
	String password = "azerty";
	Toolbar mToolbar ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);	
		etId = (EditText)findViewById(fr.gsbcr.android.R.id.etId);
		etPassword = (EditText)findViewById(fr.gsbcr.android.R.id.etPassword);

	}
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
 
		  MenuInflater inflater = getMenuInflater();
		    inflater.inflate(R.menu.main, menu);
		    return true;
    }
	 @SuppressWarnings("unused")
	private void hideSystemUI() {
		    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
		            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
		            | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
		            // remove the following flag for version < API 19
		            | View.SYSTEM_UI_FLAG_IMMERSIVE); 
		  } 

}
