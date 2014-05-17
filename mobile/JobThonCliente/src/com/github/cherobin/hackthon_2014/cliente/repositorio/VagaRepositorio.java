package com.github.cherobin.hackthon_2014.cliente.repositorio;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.content.Context;

import com.github.cherobin.hackthon_2014.cliente.domain.CV;
import com.github.cherobin.hackthon_2014.cliente.domain.RetornoAnalise;
import com.github.cherobin.hackthon_2014.cliente.util.AppPreferences;
import com.google.gson.Gson;

public class VagaRepositorio {
	Context myContext = null;

	public List<RetornoAnalise> BurcarVagas(Context context) {
		myContext = context;
		Gson gson = new Gson();
		List<RetornoAnalise> vagasTratadas = null;
		String email = "couto2@gmail.com";

		email = GetEmailLastCv();

		HttpGet getMethod = new HttpGet(
				"http://gdgjobthom.appspot.com/analises/curriculo/" + email);

		try {
			getMethod.setHeader("Accept", "application/json");
			getMethod.setHeader("Content-type", "application/json");

			HttpClient httpClient = new DefaultHttpClient();
			HttpResponse response = null;
			response = httpClient.execute(getMethod);

			// CONVERT RESPONSE TO STRING
			String result = EntityUtils.toString(response.getEntity());
			if (result == null || result.equals("null"))
				return new ArrayList<RetornoAnalise>();
			
			RetornoAnalise[] temp = gson.fromJson(result,
					RetornoAnalise[].class);
			List<RetornoAnalise> todas = Arrays.asList(temp);
			vagasTratadas = new ArrayList<RetornoAnalise>();

			for (RetornoAnalise retornoAnalise : todas) {
				if (retornoAnalise.compatibilidade > 0) {
					vagasTratadas.add(retornoAnalise);
				}
			}

			return vagasTratadas;

		} catch (Exception e) {
			return null;
		}
	}

	private String GetEmailLastCv() {
		AppPreferences _appPrefs = new AppPreferences(myContext);
		String strCv = _appPrefs.getCvCadastrado();
		Gson gson = new Gson();
		CV cvFromShared = gson.fromJson(strCv, CV.class);
		return cvFromShared.email;
	}
}
