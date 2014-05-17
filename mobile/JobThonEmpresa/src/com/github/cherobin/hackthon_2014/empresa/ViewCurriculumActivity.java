package com.github.cherobin.hackthon_2014.empresa;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.cherobin.hackthon_2014.VO.Curriculum;
import com.github.cherobin.hackthon_2014.VO.JobId;
import com.github.cherobin.hackthon_2014.VO.JobView;
import com.google.gson.Gson;

public class ViewCurriculumActivity extends Activity {

	Curriculum curriculum;
	TextView nome;
	TextView idade;
	TextView habilidades;
	TextView cidade;
	TextView estado;
	TextView email;
	TextView telefone;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_curriculum);

		nome = (TextView) findViewById(R.id.textViewCName);
		idade = (TextView) findViewById(R.id.textViewCIdade);
		habilidades = (TextView) findViewById(R.id.textViewCHabilidades);
		cidade = (TextView) findViewById(R.id.textViewCCidade);
		estado = (TextView) findViewById(R.id.textViewCEstado);
		email = (TextView) findViewById(R.id.textViewCEMail);
		telefone = (TextView) findViewById(R.id.textViewCTelefone);

		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

		if (getIntent().getExtras() != null) {
			if (getIntent().getExtras().getSerializable("curriculum") != null) {// editajob

				JobView myObject = (JobView) getIntent().getExtras()
						.getSerializable("curriculum");

				if (myObject != null) { // edit job

					getCVView(myObject.cid);
				}

			}
		}

	}

	private void getCVView(String email) {
//		Toast.makeText(getBaseContext(), "Email!" + email,
//				Toast.LENGTH_LONG).show();
		Gson gson = new Gson();
		HttpResponse response = null;
		HttpGet getMethod = new HttpGet(
				"http://gdgjobthom.appspot.com/curriculo/" + email);
		try {
			HttpClient httpClient = new DefaultHttpClient();
			response = httpClient.execute(getMethod);
			String result = EntityUtils.toString(response.getEntity());
			curriculum = gson.fromJson(result, Curriculum.class);
			setCV(curriculum);

		} catch (Exception e) {
			Toast.makeText(getBaseContext(), "Nenhum CV Encontrado!",
					Toast.LENGTH_LONG).show();
		}

	}

	private void setCV(Curriculum cv) {		
		nome.setText(cv.nome.toString());
		idade.setText(""+cv.idade);
		String hab="";
		for (int i = 0; i < cv.habilidades.length; i++) {
			hab += cv.habilidades[i]+", ";
		}
 		habilidades.setText(hab);
		cidade.setText(cv.cidade.toString());
		estado.setText(cv.estado.toString());
		email.setText(cv.email.toString());
		telefone.setText(cv.telefone.toString());
	}

}
