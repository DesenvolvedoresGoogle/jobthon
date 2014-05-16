package com.github.cherobin.hackthon_2014.cliente;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.github.cherobin.hackthon_2014.cliente.util.AppPreferences;

public class HomeActivity extends Activity {

	AppPreferences _appPrefs = new AppPreferences(getApplicationContext());

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		if (_appPrefs.cvCadastrado()) {
			final Button cadastrarCv = (Button) findViewById(R.id.btnCadastrarCv);
			//ou visible=true em outro botao (pr evitar 
			cadastrarCv.setText(R.string.btnEditarCv);
		} else {
			
		}

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_home, container,
					false);
			return rootView;
		}
	}

	public void onClickBtnCadastrarCv(View v) {
		Intent myIntent = new Intent(HomeActivity.this, FormCvActivity.class);
		myIntent.putExtra("key", "luan teste");
		HomeActivity.this.startActivity(myIntent);

		// Toast.makeText(this, "Clicked on Button - Cadastrar CV",
		// Toast.LENGTH_LONG).show();
	}

	public void onClickBtnVagas(View v) {
		Toast.makeText(this, "Clicked on Button - Minhas Vagas",
				Toast.LENGTH_LONG).show();
	}

}
