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
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.github.cherobin.hackthon_2014.empresa.util.AppPreferences;
import com.google.gson.Gson;

public class JobsListActivity extends Activity {

	private ListView listViewJobs;
	private Button btnCreateJob;
	private List<Job_com_id> myJobsList;
	Company comp;
	AppPreferences preferenceCompany;
	String email = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_listjobs);
		
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		
		preferenceCompany = new AppPreferences(getApplicationContext());
		
		if(preferenceCompany.getEmpresaCadastrado().equalsIgnoreCase("")){
			startActivity(new Intent(getBaseContext(),
					CreateCompanyActivity.class));
		}else {
			String companyString = preferenceCompany.getEmpresaCadastrado();
			
			Gson gson = new Gson();
			Company company = gson.fromJson(companyString, Company.class);
			email =  company.email;	
			getJobs();
		}
		Log.e("email ----------------", email);
		
		listViewJobs = (ListView) findViewById(R.id.listViewJobs);
		btnCreateJob = (Button) findViewById(R.id.btnCreateJob);

		
		final ArrayList<String> list = new ArrayList<String>();
 		if(myJobsList!=null)
		for (int i = 0; i < myJobsList.size(); ++i) {
			list.add(myJobsList.get(i).titulo);
		}

		final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, list);
		listViewJobs.setAdapter(adapter);

		btnCreateJob.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				Intent intent = new Intent(
						JobsListActivity.this,
						CreateJobActivity.class);
				
				Bundle bundle = new Bundle();				
				String details = email;				
				bundle.putSerializable("email",
						details);
				intent.putExtras(bundle); 			
				startActivity(intent);
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

//						alertDialog.setButton(AlertDialog.BUTTON_POSITIVE,
//								"Deletar",
//								new DialogInterface.OnClickListener() {
//
//									public void onClick(DialogInterface dialog,
//											int id) {
//
//										view.animate().setDuration(2000)
//												.alpha(0)
//												.withEndAction(new Runnable() {
//													@Override
//													public void run() {
//														list.remove(item);
//
//														adapter.notifyDataSetChanged();
//														view.setAlpha(1);
//													}
//												});
//									}
//								});

//						alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL,
//								"Editar",
//								new DialogInterface.OnClickListener() {
//
//									public void onClick(DialogInterface dialog,
//											int id) {
//										Intent intent = new Intent(
//												JobsListActivity.this,
//												CreateJobActivity.class);
//
//										Job_com_id details = myJobsList.get(position);
//
//										Bundle bundle = new Bundle();
//										bundle.putSerializable("job",
//												details);
//										intent.putExtras(bundle);
//
//										startActivity(intent);
//
//									}
//								});

						alertDialog.setButton(AlertDialog.BUTTON_POSITIVE,
								"Visualizar",
								new DialogInterface.OnClickListener() {

									public void onClick(DialogInterface dialog,
											int id) {
										visualizar(item, position);

									}

								});
						
						alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE,
								"Sair",
								new DialogInterface.OnClickListener() {

									public void onClick(DialogInterface dialog,
											int id) {
									 

									}

								});
						alertDialog.show();
					}

				});
	}

	private void visualizar(String item, int position) {

		myJobsList.get(position);
		
		

	}

	private void getJobs() {
 
		Gson gson = new Gson();

		HttpResponse response = null;
		HttpGet getMethod = new HttpGet("http://gdgjobthom.appspot.com/vagas/"+email);		 
		try {
			HttpClient httpClient = new DefaultHttpClient();
			response = httpClient.execute(getMethod);
			String result = EntityUtils.toString(response.getEntity());
			Job_com_id[] myJobsArray = gson.fromJson(result, Job_com_id[].class);
			myJobsList = Arrays.asList(myJobsArray);			 
		} catch (Exception e) { 
		}
	}

	@Override
	public void onResume(){
		super.onResume();
		Gson gson = new Gson();

		HttpResponse response = null;
		HttpGet getMethod = new HttpGet("http://gdgjobthom.appspot.com/vagas/"+email);		 
		try {
			HttpClient httpClient = new DefaultHttpClient();
			response = httpClient.execute(getMethod);
			String result = EntityUtils.toString(response.getEntity());
			Job_com_id[] myJobsArray = gson.fromJson(result, Job_com_id[].class);
			myJobsList = Arrays.asList(myJobsArray);			 
		} catch (Exception e) { 
		}
		

		final ArrayList<String> list = new ArrayList<String>();
 		if(myJobsList!=null)
		for (int i = 0; i < myJobsList.size(); ++i) {
			list.add(myJobsList.get(i).titulo);
		}

		final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, list);
		listViewJobs.setAdapter(adapter);

	}

}
