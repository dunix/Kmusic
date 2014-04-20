package com.example.kmusic;

import java.util.ArrayList;

import APIS.LastAsync;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class adaptador extends BaseAdapter{
	
	private final Activity _actividad;

	protected ArrayList<ObjectLastFM> lista2= new ArrayList<ObjectLastFM>();
	
	
	
	public adaptador(Activity actividad){
		super();
		this._actividad = actividad;
		lista2=new LastAsync().lista;
	}
	
	


	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return lista2.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return lista2.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
	
	LayoutInflater inflater = _actividad.getLayoutInflater();
	View view  = inflater.inflate(R.layout.listopciones_principal, null, true);
	
	TextView textview1 = (TextView)view.findViewById(R.id.LblTitulo);
	//textview1.setText("sd");
	textview1.setText(lista2.get(arg0).artista);
	
	TextView textview2 = (TextView)view.findViewById(R.id.LblSubTitulo);
	//textview2.setText("d");
	textview2.setText(lista2.get(arg0).cancion);
	
		return view;
	}
	
}