package com.example.kmusic;

import java.util.ArrayList;

import APIS.LastAsync;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class adaptador_listviewCanciones_Top extends BaseAdapter{
	
	private final Activity _actividad;

	protected ArrayList<ObjectLastFM> list_resultados_Fmtop= new ArrayList<ObjectLastFM>();
	
	
	
	public adaptador_listviewCanciones_Top(Activity actividad){
		super();
		this._actividad = actividad;
		list_resultados_Fmtop=new LastAsync().getLista_result_FM();
	}
	
	


	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list_resultados_Fmtop.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return list_resultados_Fmtop.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
	
	LayoutInflater inflater = _actividad.getLayoutInflater();
	View view  = inflater.inflate(R.layout.componenteslist_viewtop_canciones, null, true);
	
	TextView textview1 = (TextView)view.findViewById(R.id.LblArtistaTop);
	textview1.setText(list_resultados_Fmtop.get(arg0).getArtista());
	
	TextView textview2 = (TextView)view.findViewById(R.id.LblCancionTop);
	textview2.setText(list_resultados_Fmtop.get(arg0).getCancion());
	
		return view;
	}
	
}