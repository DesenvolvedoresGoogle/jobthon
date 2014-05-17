package com.github.cherobin.hackthon_2014.empresa;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.gson.Gson;

public class JobsListActivity extends Activity {

	private ListView listViewJobs;
	private Button btnCreateJob;
	private List<Job> myJobsList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_listjobs);

		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

		listViewJobs = (ListView) findViewById(R.id.listViewJobs);
		btnCreateJob = (Button) findViewById(R.id.btnCreateJob);
		 	 
	
		getJobs();

		final ArrayList<String> list = new ArrayList<String>();
		for (int i = 0; i < myJobsList.size(); ++i) {
			list.add(myJobsList.get(i).titulo);
		}

		final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, list);
		listViewJobs.setAdapter(adapter);

		btnCreateJob.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				startActivity(new Intent(getBaseContext(),
						CreateJobActivity.class));
			}
		});

		// para remover a vaga
		listViewJobs
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent,
							final View view, final int position, long id) {
						final String item = (String) parent
								.getItemAtPosition(position);

						AlertDialog alertDialog = new AlertDialog.Builder(
								JobsListActivity.this).create();

						alertDialog.setTitle(item);

						// alertDialog.setMessage("This is a three-button dialog!");

						alertDialog.setButton(AlertDialog.BUTTON_POSITIVE,
								"Deletar",
								new DialogInterface.OnClickListener() {

									public void onClick(DialogInterface dialog,
											int id) {

										view.animate().setDuration(2000)
												.alpha(0)
												.withEndAction(new Runnable() {
													@Override
													public void run() {
														list.remove(item);

														adapter.notifyDataSetChanged();
														view.setAlpha(1);
													}
												});
									}
								});

						alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL,
								"Editar",
								new DialogInterface.OnClickListener() {

									public void onClick(DialogInterface dialog,
											int id) {
										Intent intent = new Intent(JobsListActivity.this, CreateJobActivity.class); 
										
										Job details = myJobsList.get(position);
							
										Bundle bundle = new Bundle();
										bundle.putSerializable("object", details);
										intent.putExtras(bundle);

										startActivity(intent);
									 
									}
								});

						alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE,
								"Visualizar",
								new DialogInterface.OnClickListener() {

									public void onClick(DialogInterface dialog,
											int id) {
										visualizar(item, position);

									}

								});
						alertDialog.show();
					}

				});
	}

	private void visualizar(String item, int position) {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(
				JobsListActivity.this);

		myJobsList.get(position);
		 
		 
	}

	private void getJobs() {

		Gson gson = new Gson();

		HttpResponse response = null;
		HttpGet getMethod = new HttpGet("http://gdgjobthom.appspot.com/vagas");
		try {
			HttpClient httpClient = new DefaultHttpClient();
			response = httpClient.execute(getMethod);

			String result = EntityUtils.toString(response.getEntity());

			Job[] myJobsArray = gson.fromJson(result, Job[].class);
			myJobsList = Arrays.asList(myJobsArray);

		} catch (Exception e) {
			// TODO: handle exception
		}
	}

}
