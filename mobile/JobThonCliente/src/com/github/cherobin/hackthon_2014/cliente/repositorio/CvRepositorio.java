package com.github.cherobin.hackthon_2014.cliente.repositorio;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import com.google.gson.Gson;

public class CvRepositorio {

	public boolean Salvar(String gsonCv) {

		Gson gson = new Gson();

		HttpResponse response = null;
		HttpPost postMethod = new HttpPost(
				"http://gdgjobthom.appspot.com/curriculo");

		try {
			StringEntity se = new StringEntity(gsonCv);

			postMethod.setEntity(se);
			postMethod.setHeader("Accept", "application/json");
			postMethod.setHeader("Content-type", "application/json");

			HttpClient httpClient = new DefaultHttpClient();
			response = httpClient.execute(postMethod);

			return response.getStatusLine().getStatusCode() == 200;

		} catch (Exception e) {
			return false;
			
		}

	}

}
