package com.github.cherobin.hackthon_2014.empresa;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;

public class CreateJobActivity extends Activity {

	private Spinner state;
	private EditText jobNome;
	private EditText jobAbout;
	private EditText jobAbility;
	private Spinner jobArea;
	private EditText jobCity;
	private CheckBox jobCLT;
	private CheckBox jobPJ;
	private CheckBox jobEstagio;
	private Button btnSend;
	private String selectState;
	private String selectArea;
	private String email;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_createjobs);

		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

		state = (Spinner) findViewById(R.id.spinner_state);

		jobNome = (EditText) findViewById(R.id.editTextJobName);
		jobAbout = (EditText) findViewById(R.id.editTextAbout);
		jobArea = (Spinner) findViewById(R.id.spinnerArea);
		jobCity = (EditText) findViewById(R.id.editTextCity);
		jobCLT = (CheckBox) findViewById(R.id.checkBoxCLT);
		jobPJ = (CheckBox) findViewById(R.id.checkBoxPJ);
		jobEstagio = (CheckBox) findViewById(R.id.checkBoxEstagio);
		jobAbility = (EditText) findViewById(R.id.editTextAbility);
		btnSend = (Button) findViewById(R.id.btnSendJob);
		setStateSpinner();
		setJobAreaSpinner();

		state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {
				selectState = (String) parent.getItemAtPosition(pos);
			}

			public void onNothingSelected(AdapterView<?> parent) {
			}
		});

		jobArea.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {
				selectArea = (String) parent.getItemAtPosition(pos);
			}

			public void onNothingSelected(AdapterView<?> parent) {
			}
		});

		if (getIntent().getExtras() != null) {
			if (getIntent().getExtras().getSerializable("job") != null) {// editajob

				Job myObject = (Job) getIntent().getExtras().getSerializable(
						"job");

				if (myObject != null) { // edit job

					editJob(myObject);
				}

			}
			if (getIntent().getExtras().getSerializable("email") != null) {
				email = (String) getIntent().getExtras().getSerializable(
						"email");
			}

		}
		btnSend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				createJob();
			}
		});

	}

	public void createJob() {
		ArrayList<String> typeJob = new ArrayList<String>();
		ArrayList<String> abilityJob = new ArrayList<String>();

		if (jobCLT.isChecked()) {
			typeJob.add("CLT");
		}

		if (jobPJ.isChecked()) {
			typeJob.add("PJ");
		}

		if (jobEstagio.isChecked()) {
			typeJob.add("Estágio");
		}

		String[] result = jobAbility.getText().toString().split(",");

		for (String s : result) {
			abilityJob.add(s);
		}

		Job job = new Job(jobNome.getText().toString(), jobAbout.getText()
				.toString(), abilityJob, selectArea, jobCity.getText()
				.toString(), selectState, true, typeJob, email);

		Gson gjob = new Gson();
		String gjobText = gjob.toJson(job);
		sendJob(gjobText);
	}

	public void sendJob(String gjobText) {

		HttpURLConnection connection;
		OutputStreamWriter request = null;

		URL url = null;
		String response = null;
		String parameters = gjobText;
		try {
			url = new URL("http://gdgjobthom.appspot.com/vaga");
			connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			connection.setRequestMethod("POST");

			request = new OutputStreamWriter(connection.getOutputStream());
			request.write(parameters);
			request.flush();
			request.close();
			String line = "";
			InputStreamReader isr = new InputStreamReader(
					connection.getInputStream());
			BufferedReader reader = new BufferedReader(isr);
			StringBuilder sb = new StringBuilder();
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}

			response = sb.toString();

			if (response.contains("success")) {
				Toast.makeText(getApplicationContext(),
						"Cadastrado Com Sucesso", Toast.LENGTH_LONG).show();

				finish();
			} else {
				Toast.makeText(getApplicationContext(), "Error ao cadastrar",
						Toast.LENGTH_LONG).show();

			}

			isr.close();
			reader.close();

		} catch (IOException e) {
			// Error
		}

	}

	private void editJob(Job myObject) {
		jobNome.setText(myObject.titulo);
		jobAbout.setText(myObject.sobre);
		jobCity.setText(myObject.cidade);
		jobAbility.setText(myObject.habilidades.toString().replace("[", "")
				.replace("]", ""));

		for (int i = 0; i < myObject.contratacao.size(); i++) {
			if (myObject.contratacao.get(i).equalsIgnoreCase("CLT")) {
				jobCLT.setChecked(true);
			}

			if (myObject.contratacao.get(i).equalsIgnoreCase("PJ")) {
				jobPJ.setChecked(true);
			}

			if (myObject.contratacao.get(i).equalsIgnoreCase("Estágio")) {
				jobEstagio.setChecked(true);
			}
		}

		String[] states = new String[] { "ESTADO", "AC", "AL", "AM", "AP",
				"BA", "CE", "DF", "ES", "GO", "MA", "MG", "MS", "MT", "PA",
				"PB", "PE", "PI", "PR", "RJ", "RN", "RO", "RS", "SC", "SE",
				"SP", "TO" };

		for (int i = 0; i < states.length; i++) {
			if (myObject.estado.equals(states[i])) {
				state.setSelection(i);
				break;
			}
		}

		String[] areas = new String[] { "Área", "Programador", "Design",
				"Vendas", "Administrativo" };

		for (int i = 0; i < areas.length; i++) {
			if (myObject.area.equals(areas[i])) {
				jobArea.setSelection(i);
				break;
			}
		}

	}

	public void setStateSpinner() {

		ArrayAdapter<String> adaptadorSpinner = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, new String[] { "ESTADO",
						"AC", "AL", "AM", "AP", "BA", "CE", "DF", "ES", "GO",
						"MA", "MG", "MS", "MT", "PA", "PB", "PE", "PI", "PR",
						"RJ", "RN", "RO", "RS", "SC", "SE", "SP", "TO" });

		state.setAdapter(adaptadorSpinner);

	}

	private void setJobAreaSpinner() {
		ArrayAdapter<String> adaptadorSpinner = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, new String[] { "Área",
						"Programador", "Design", "Vendas", "Administrativo" });

		jobArea.setAdapter(adaptadorSpinner);

	}

}
