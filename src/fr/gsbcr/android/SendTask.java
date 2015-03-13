package fr.gsbcr.android;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

class SendTask extends AsyncTask<JSONObject,JSONArray,String>{
	public AsyncInterface delegation ;
	private static HttpClient httpclient;
	private static HttpResponse response ;

	@Override
	protected String doInBackground(JSONObject... uri) {
		httpclient = new DefaultHttpClient();
		String responseString = null;
		HttpPost post = null;
		try {
			post = new HttpPost(uri[0].getString("url").toString());
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			Log.d("url",uri[0].getString("url").toString());
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add(new BasicNameValuePair("CR",uri[1].toString()));
			nameValuePairs.add(new BasicNameValuePair("Echantillons", uri[2].toString()));
			Log.d("Ech",uri[2].toString());
			Log.d("CR", uri[1].toString());
			post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			response = httpclient.execute(post);
			StatusLine statusLine = response.getStatusLine();
			if(statusLine.getStatusCode() == HttpStatus.SC_OK){
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				response.getEntity().writeTo(out);
				responseString = out.toString();
				out.close();

			} else{
				//Closes the connection.
				response.getEntity().getContent().close();
				throw new IOException(statusLine.getReasonPhrase());
			}


		} catch (ClientProtocolException e) {
			//TODO Handle problems..
		} catch (IOException e) {
			//TODO Handle problems..
		}
		return responseString;
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		try {

			delegation.processFinish(result);

		} catch (Exception e) {

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

	protected static int toGetStatus(SendTask task){
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