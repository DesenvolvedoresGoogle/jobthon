package com.github.cherobin.hackthon_2014.cliente;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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
import android.widget.TextView;
import android.widget.Toast;

import com.github.cherobin.hackthon_2014.cliente.domain.RetornoAnalise;
import com.github.cherobin.hackthon_2014.cliente.repositorio.VagaRepositorio;
import com.github.cherobin.hackthon_2014.cliente.util.AppPreferences;

public class ListarVagasActivity extends Activity {
	AppPreferences _appPrefs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_listarvaga);
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

		ListView listViewVagas = (ListView) findViewById(R.id.listviewvagas);

		VagaRepositorio vagaRep = new VagaRepositorio();
		List<RetornoAnalise> myVagas = vagaRep.BurcarVagas();

		if (myVagas == null) {
			Toast.makeText(this, "Ocorreu um erro. Tente mais tarde!",
					Toast.LENGTH_LONG).show();
			finish();
			return;
		} else if (myVagas.size() < 1) {
			Toast.makeText(this,
					"N‹o temos vagas pra voc no momento. Atualize seu CV!",
					Toast.LENGTH_LONG).show();
			finish();
			return;
		}

		listViewVagas.setAdapter(new VagaAdapter(ListarVagasActivity.this,
				null, myVagas));
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		_appPrefs = new AppPreferences(getApplicationContext());
	}

	public class VagaAdapter extends BaseAdapter {

		private Context context;
		private LayoutInflater inflater;
		List<RetornoAnalise> myVagas = new ArrayList<RetornoAnalise>();

		public VagaAdapter(Activity activity, Context context,
				List<RetornoAnalise> vagas) {
			super();
			this.context = context;
			inflater = LayoutInflater.from(activity);
			myVagas = vagas;
			Collections.sort(myVagas, new PorcentoComparator());

		}

		public class PorcentoComparator implements Comparator<RetornoAnalise> {
			@Override
			public int compare(RetornoAnalise o1, RetornoAnalise o2) {
				return o2.compatibilidade.compareTo(o1.compatibilidade);
			}
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return myVagas.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			View view = convertView;
			view = inflater.inflate(R.layout.vagas_adapter, parent, false);

			RetornoAnalise vagaAtual = myVagas.get(position);
			Log.d("luan", String.valueOf(position));

			// dependendo do valor muda o background do textView (verde, amarelo
			// e vermelho)

			TextView porcento = (TextView) view.findViewById(R.id.porcento);
			if (vagaAtual.compatibilidade != null
					&& !vagaAtual.compatibilidade.equals(""))
				porcento.setText(vagaAtual.compatibilidade.toString() + " %");

			TextView empresa = (TextView) view.findViewById(R.id.empresa);
			if (vagaAtual.nomeEmpresa != null
					&& !vagaAtual.nomeEmpresa.equals(""))
				empresa.setText(vagaAtual.nomeEmpresa);

			TextView titulo = (TextView) view.findViewById(R.id.titulo);
			if (vagaAtual.nomeVaga != null && !vagaAtual.nomeVaga.equals(""))
				titulo.setText(vagaAtual.nomeVaga);

			return view;
		}

	}

}
