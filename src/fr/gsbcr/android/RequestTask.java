package fr.gsbcr.android;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

class RequestTask extends AsyncTask<String,String, String>{
	public AsyncInterface delegation ;
	private static HttpClient httpclient;
	private static HttpResponse response ;

	@Override
	protected String doInBackground(String... uri) {
		HttpParams httpParameters = new BasicHttpParams();
		int timeoutConnection = 20;
		HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
		httpclient = new DefaultHttpClient(httpParameters);

		String responseString = "NONE";
		try {
			Log.i("Connection","Connection Successful");

			response = httpclient.execute(new HttpGet(uri[0]));
			StatusLine statusLine = response.getStatusLine();
			if(statusLine.getStatusCode() == HttpStatus.SC_OK){
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				response.getEntity().writeTo(out);
				responseString = out.toString();
				out.close();

			} else{
				//Closes the connection.
				response.getEntity().getContent().close();
				responseString = null;
				throw new IOException(statusLine.getReasonPhrase());
			}


		} catch (ClientProtocolException e) {
			responseString = null;
		} catch (IOException e) {
			responseString = null;
		}
		return responseString;
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		try {
			this.delegation.processFinish(result);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}



	}
	
	

	protected static void onCloseConnection() throws IllegalStateException, IOException{
		//response.getEntity().getContent().close();
		/*if(response != null ){
    		response.getEntity().getContent().close();
	    	httpclient.getConnectionManager().shutdown();
	    	response = null;
	    	httpclient = null;
    	}*/
	}

	protected static int toGetStatus(RequestTask task){
		int statusCode = 0;
		if(task.getStatus() == AsyncTask.Status.RUNNING ) {
			task.cancel(true);
		}
		else if (task.getStatus() == AsyncTask.Status.FINISHED) {
			statusCode = 1;

		}
		return statusCode ;
	}

}