package com.github.cherobin.hackthon_2014.cliente;

import java.util.Arrays;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.github.cherobin.hackthon_2014.cliente.domain.CV;
import com.github.cherobin.hackthon_2014.cliente.repositorio.CvRepositorio;
import com.github.cherobin.hackthon_2014.cliente.util.AppPreferences;
import com.google.gson.Gson;

public class FormCvActivity extends Activity {

	AppPreferences _appPrefs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_formcv);

		popularSpinner();

		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

		Intent intent = getIntent();
		String action = intent.getStringExtra("action");

		if (action != null && action.equals("edit")) {
			popularCamposDeTela();
			travarEmail();
		}

	}

	private void travarEmail() {
		EditText email = ((EditText) findViewById(R.id.editTextEmail));
		email.setEnabled(false);
		email.setInputType(InputType.TYPE_NULL);
	}

	private void popularCamposDeTela() {
		_appPrefs = new AppPreferences(getApplicationContext());
		String strCv = _appPrefs.getCvCadastrado();
		Gson gson = new Gson();
		CV cvFromShared = gson.fromJson(strCv, CV.class);

		EditText nome = ((EditText) findViewById(R.id.editTextName));
		nome.setText(cvFromShared.nome);

		EditText idade = ((EditText) findViewById(R.id.editTextIdade));
		idade.setText(String.valueOf(cvFromShared.idade));

		EditText habilidade = ((EditText) findViewById(R.id.editTextHabilidades));
		habilidade.setText(Arrays.asList(cvFromShared.habilidades).toString()
				.replace("[", "").replace("]", ""));

		EditText telefone = ((EditText) findViewById(R.id.editTextTelefone));
		telefone.setText(cvFromShared.telefone);

		EditText email = ((EditText) findViewById(R.id.editTextEmail));
		email.setText(cvFromShared.email);

		EditText cidade = ((EditText) findViewById(R.id.editTextCity));
		cidade.setText(cvFromShared.cidade);

		setSpinner(cvFromShared);

	}

	private void setSpinner(CV cvFromShared) {
		Spinner estado = (Spinner) findViewById(R.id.spinner_state);

		String[] states = new String[] { "ESTADO", "AC", "AL", "AM", "AP",
				"BA", "CE", "DF", "ES", "GO", "MA", "MG", "MS", "MT", "PA",
				"PB", "PE", "PI", "PR", "RJ", "RN", "RO", "RS", "SC", "SE",
				"SP", "TO" };

		for (int i = 0; i < states.length; i++) {
			if (cvFromShared.estado.equals(states[i])) {
				estado.setSelection(i);
				break;
			}
		}
	}

	private void popularSpinner() {
		final Spinner spinner = (Spinner) findViewById(R.id.spinner_state);
		ArrayAdapter<String> adaptadorSpinner = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, new String[] { "ESTADO",
						"AC", "AL", "AM", "AP", "BA", "CE", "DF", "ES", "GO",
						"MA", "MG", "MS", "MT", "PA", "PB", "PE", "PI", "PR",
						"RJ", "RN", "RO", "RS", "SC", "SE", "SP", "TO" });
		spinner.setAdapter(adaptadorSpinner);
	}

	public void onClickBtnSalvarCv(View v) {

		String nome = ((EditText) findViewById(R.id.editTextName)).getText()
				.toString();
		String idade = ((EditText) findViewById(R.id.editTextIdade)).getText()
				.toString();
		String habilidades = ((EditText) findViewById(R.id.editTextHabilidades))
				.getText().toString();
		String cidade = ((EditText) findViewById(R.id.editTextCity)).getText()
				.toString();
		String email = ((EditText) findViewById(R.id.editTextEmail)).getText()
				.toString();
		String telefone = ((EditText) findViewById(R.id.editTextTelefone))
				.getText().toString();
		String estado = ((Spinner) findViewById(R.id.spinner_state))
				.getSelectedItem().toString();

		if (nome.equals("") || idade.equals("") || habilidades.equals("")
				|| cidade.equals("") || email.equals("") || telefone.equals("")
				|| estado.equals("") || estado.equalsIgnoreCase("ESTADO")) {
			Toast.makeText(FormCvActivity.this,
					"Todos os campos s‹o obrigat—rios.", Toast.LENGTH_SHORT)
					.show();
			return;
		}

		// montar objeto
		CV cv = montarCv(nome, idade, habilidades, cidade, email, telefone,
				estado);

		// salvar dados.
		Gson gson = new Gson();
		String gsonCv = gson.toJson(cv);

		CvRepositorio cvRepositorio = new CvRepositorio();

		if (!cvRepositorio.Salvar(gsonCv)) {
			Toast.makeText(FormCvActivity.this,
					"Erro de conex‹o. Tente mais tarde.", Toast.LENGTH_SHORT)
					.show();
			return;
		}

		Toast.makeText(FormCvActivity.this,
				"Seus dados foram salvos com sucesso!", Toast.LENGTH_SHORT)
				.show();

		_appPrefs = new AppPreferences(getApplicationContext());
		_appPrefs.saveCvCadastrado(gsonCv);

		finish();
	}

	private CV montarCv(String nome, String idade, String habilidades,
			String cidade, String email, String telefone, String estado) {
		CV cv = new CV();
		cv.nome = nome;
		cv.idade = Integer.parseInt(idade);
		cv.cidade = cidade;
		cv.habilidades = habilidades.split(",");
		cv.email = email;
		cv.telefone = telefone;
		cv.estado = estado;
		return cv;
	}

}
