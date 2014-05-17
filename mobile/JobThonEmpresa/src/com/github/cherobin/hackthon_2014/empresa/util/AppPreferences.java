package com.github.cherobin.hackthon_2014.empresa.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class AppPreferences {
	public static final String EMPRESA_CADASTRADO = "empresa_cadastrado";
	private static final String APP_SHARED_PREFS = AppPreferences.class
			.getSimpleName();
	private SharedPreferences _sharedPrefs;
	private Editor _prefsEditor;

	public AppPreferences(Context context) {
		
		this._sharedPrefs = context.getSharedPreferences(APP_SHARED_PREFS,
				Activity.MODE_PRIVATE);
		
		this._prefsEditor = _sharedPrefs.edit();
	}

	public String getEmpresaCadastrado() {
		return _sharedPrefs.getString(EMPRESA_CADASTRADO,"");
	}

	public void setEmpresaCadastrado(String cadastrado) {
		_prefsEditor.putString(EMPRESA_CADASTRADO, cadastrado);
		_prefsEditor.commit();
	}
	
 
}