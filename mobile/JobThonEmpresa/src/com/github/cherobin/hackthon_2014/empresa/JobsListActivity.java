package com.github.cherobin.hackthon_2014.empresa;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.google.gson.Gson;

public class JobsListActivity extends Activity {

	private ListView listViewJobs;

	private String[] jobs;

	private Button btnCreateJob;

	List<Job> listJobs = new ArrayList<Job>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_listjobs);

		listViewJobs = (ListView) findViewById(R.id.listViewJobs);
		btnCreateJob = (Button) findViewById(R.id.btnCreateJob);

		// jobs =
		getJobs();
		//
		// final ArrayList<String> list = new ArrayList<String>();
		// for (int i = 0; i < jobs.length; ++i) {
		// list.add(jobs[i]);
		// }
		//
		// final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
		// android.R.layout.simple_list_item_1, list);
		// listViewJobs.setAdapter(adapter);

		btnCreateJob.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				startActivity(new Intent(getBaseContext(),
						CreateJobActivity.class));
			}
		});

		// //para remover a vaga
		// listViewJobs
		// .setOnItemClickListener(new AdapterView.OnItemClickListener() {
		// @Override
		// public void onItemClick(AdapterView<?> parent,
		// final View view, int position, long id) {
		// final String item = (String) parent
		// .getItemAtPosition(position);
		//
		// AlertDialog alertDialog = new AlertDialog.Builder(
		// JobsListActivity.this).create();
		//
		// alertDialog.setTitle(item);
		//
		// // alertDialog.setMessage("This is a three-button dialog!");
		//
		// alertDialog.setButton(AlertDialog.BUTTON_POSITIVE,
		// "Deletar",
		// new DialogInterface.OnClickListener() {
		//
		// public void onClick(DialogInterface dialog,
		// int id) {
		//
		// view.animate().setDuration(2000)
		// .alpha(0)
		// .withEndAction(new Runnable() {
		// @Override
		// public void run() {
		// list.remove(item);
		//
		// adapter.notifyDataSetChanged();
		// view.setAlpha(1);
		// }
		// });
		// }
		// });
		//
		// alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL,
		// "Editar",
		// new DialogInterface.OnClickListener() {
		//
		// public void onClick(DialogInterface dialog,
		// int id) {
		//
		// // ...
		//
		// }
		// });
		//
		// alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE,
		// "Visualizar",
		// new DialogInterface.OnClickListener() {
		//
		// public void onClick(DialogInterface dialog,
		// int id) {
		// visualizar(item);
		//
		// }
		//
		// });
		// alertDialog.show();
		// }
		//
		// });
	}

	private void visualizar(String item) {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(
				JobsListActivity.this);

		alertDialog.setTitle(item);

		alertDialog
				.setMessage("This is a three-button dialog!This is a three-button dialog!This is a three-button dialog!This is a three-button dialog!This is a three-button dialog!This is a three-button dialog!This is a three-button dialog!This is a three-button dialog!!!!!");

		alertDialog.setPositiveButton("ok", null);
		alertDialog.show();
	}

	private void getJobs() {

		
		Gson gjob = new Gson();
 
        HttpResponse response = null;
        HttpGet getMethod = new HttpGet("http://gdgjobthom.appspot.com/vagas");
        try {
            HttpClient httpClient = new DefaultHttpClient();
			response = httpClient.execute(getMethod);

       
            String result = EntityUtils.toString(response.getEntity());
            Log.e("aaaaaaaaaaaa", result);
        }catch (Exception e) {
			// TODO: handle exception
		}
        
        
		// Job[] ajob = gjob.fromJson(bf.readLine(), Job[].class);
		// List<Job> myjobs = Arrays.asList(ajob);
		// for (int i = 0; i < myjobs.size(); i++) {
		//
		// Log.e("aaaaaaaaaaaaaaaaaaaaaaaaaaaaa", myjobs.get(i).titulo);
		// }

		// return new String[] { "Android", "iPhone", "WindowsMobile",
		// "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
		// "Linux", "OS/2", "Ubuntu", "Windows7", "Max OS X", "Linux",
		// "OS/2", "Ubuntu", "Windows7", "Max OS X", "Linux", "OS/2",
		// "Android", "iPhone", "WindowsMobile" };

	}

}
