package com.github.cherobin.hackthon_2014.cliente;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.github.cherobin.hackthon_2014.cliente.util.AppPreferences;

public class HomeActivity extends Activity {

	AppPreferences _appPrefs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		_appPrefs = new AppPreferences(getApplicationContext());
		tratarVisibilidadeBotoes();
	}

	private void tratarVisibilidadeBotoes() {
		final Button cadastrarCv = (Button) findViewById(R.id.btnCadastrarCv);
		final Button editarCv = (Button) findViewById(R.id.btnEditarCv);
		cadastrarCv
				.setVisibility(_appPrefs.getCvCadastrado().equals("") ? View.VISIBLE
						: View.GONE);
		editarCv.setVisibility(_appPrefs.getCvCadastrado().equals("") ? View.GONE
				: View.VISIBLE);

	}

	public void onClickBtnCadastrarCv(View v) {
		Intent myIntent = new Intent(HomeActivity.this, FormCvActivity.class);
		HomeActivity.this.startActivity(myIntent);
	}

	public void onClickBtnEditarCv(View v) {
		Intent myIntent = new Intent(HomeActivity.this, FormCvActivity.class);
		myIntent.putExtra("action", "edit");
		HomeActivity.this.startActivity(myIntent);
	}

	public void onClickBtnVagas(View v) {
		if (_appPrefs.getCvCadastrado().equals("")) {
			Toast.makeText(this, "Cadastre seu CV para ver suas vagas!",
					Toast.LENGTH_LONG).show();
		}

		Intent myIntent = new Intent(HomeActivity.this, ListarVagasActivity.class);
		HomeActivity.this.startActivity(myIntent);
	}

}
