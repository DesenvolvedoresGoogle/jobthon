package com.github.cherobin.hackthon_2014.cliente.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class AppPreferences {
	public static final String CV_CADASTRADO = "cv_cadastrado";
	private static final String APP_SHARED_PREFS = AppPreferences.class
			.getSimpleName();
	private SharedPreferences _sharedPrefs;
	private Editor _prefsEditor;

	public AppPreferences(Context context) {
		this._sharedPrefs = context.getSharedPreferences(APP_SHARED_PREFS,
				Activity.MODE_PRIVATE);
		this._prefsEditor = _sharedPrefs.edit();
	}

	public String getCvCadastrado() {
		return _sharedPrefs.getString(CV_CADASTRADO, "");
	}

	public void saveCvCadastrado(String cadastrado) {
		_prefsEditor.putString(CV_CADASTRADO, cadastrado);
		_prefsEditor.commit();
	}
}