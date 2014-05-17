package com.github.cherobin.hackthon_2014.empresa;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import com.github.cherobin.hackthon_2014.empresa.util.AppPreferences;
import com.google.gson.Gson;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class CreateCompanyActivity extends Activity {

	private Spinner state;
	private EditText companyName;
	private EditText companyAbout;
	private EditText companyCity;
	private EditText companyPhone;
	private EditText companyEMail;
	private Button btnSend;
	private String selectState;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_company);

		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

		state = (Spinner) findViewById(R.id.spinnerCompanyState);
		companyName = (EditText) findViewById(R.id.editTextCompanyName);
		companyAbout = (EditText) findViewById(R.id.editTextCompanyAbout);
		companyCity = (EditText) findViewById(R.id.editTextCompanyCity);
		companyPhone = (EditText) findViewById(R.id.editTextCompanyPhone);
		companyEMail = (EditText) findViewById(R.id.editTextCompanyEmail);

		btnSend = (Button) findViewById(R.id.buttonCompanyCadastro);

		setStateSpinner();

		state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {
				selectState = (String) parent.getItemAtPosition(pos);
			}

			public void onNothingSelected(AdapterView<?> parent) {
			}
		});

		btnSend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				createCompany();
			
			}
		});

	}

	public void createCompany() {
		Company company = new Company(companyName.getText().toString(),
				companyCity.getText().toString(), selectState, companyAbout
						.getText().toString(), companyPhone.getText()
						.toString(), companyEMail.getText().toString());

		Gson gjob = new Gson();
		String gjobText = gjob.toJson(company);
		sendCompany(gjobText);
	}

	public void sendCompany(String gjobText) {

		HttpURLConnection connection;
		OutputStreamWriter request = null;

		URL url = null;
		String response = null;
		String parameters = gjobText;
		try {
			url = new URL("http://gdgjobthom.appspot.com/empresa");
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
				Toast.makeText(getBaseContext(), "Criado Com Sucesso",
						Toast.LENGTH_LONG).show();
				finish();;
			} else {
				Toast.makeText(getApplicationContext(), "Error ao cadastrar",
						Toast.LENGTH_LONG).show();
			}

			isr.close();
			reader.close();

		} catch (IOException e) {
			// Error
		}

		AppPreferences preferences = new AppPreferences(getApplicationContext());
		preferences.setEmpresaCadastrado(gjobText);

	}

	public void setStateSpinner() {

		ArrayAdapter<String> adaptadorSpinner = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, new String[] { "ESTADO",
						"AC", "AL", "AM", "AP", "BA", "CE", "DF", "ES", "GO",
						"MA", "MG", "MS", "MT", "PA", "PB", "PE", "PI", "PR",
						"RJ", "RN", "RO", "RS", "SC", "SE", "SP", "TO" });

		state.setAdapter(adaptadorSpinner);

	}

}
