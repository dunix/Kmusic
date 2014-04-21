package Fragments;


import java.util.ArrayList;


import com.example.kmusic.R;

import com.example.kmusic.TracksForAlbumActivity;


import APIS.LastfmAPI;

import ObjectsAPIS.ObjectLastFM;

import Seguridad.InternetStatus;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;

import android.os.AsyncTask;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

// Fragment que maneja los albums encontrados para un artista

public class FragmentAlbum extends Fragment {
	ArrayList<ObjectLastFM> List_LastFM;
	ListView lstListado=null;				// Lista que contiene los datos para el adaptor del listview
	Fragment fragment;
	
	

	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.result_tab_album, container, false);
		fragment=this;
		Intent intent=getActivity().getIntent();
		
		TextView t= (TextView)v.findViewById(R.id.NombreArtistaAlbumTab);
		t.setText(intent.getStringExtra("artist")+"\n");
		
		lstListado = (ListView)v.findViewById(R.id.ListViewAlbumResult);
		
		if (new InternetStatus().haveNetworkConnection(getActivity())){
			SearchAsyncAlbums AsynAlbums=new SearchAsyncAlbums();
			AsynAlbums.execute(intent.getStringExtra("artist"),intent.getStringExtra("album"));
		}
		else{
			Toast.makeText(getActivity(), "Advertencia no tiene acceso a internet", Toast.LENGTH_SHORT).show();
			
		}
        return v;
	}
	
	
	/*
	 	Clase asincrona encargada de conectarse con el API  y obtener los datos de los albums que se le cargan al adaptador
	 	
	*/
	
	 class SearchAsyncAlbums extends AsyncTask<String, Void, Boolean> {
			LastfmAPI LastFMAPI;
			private String Artist;

			public  SearchAsyncAlbums(){
			}
			
			protected void onPreExecute() {
			}
			
			@Override
			protected Boolean doInBackground(String... data) {
				try{
					Artist=data[0];
					if(data[1].equals("")){
						LastFMAPI=new LastfmAPI();
						List_LastFM=LastFMAPI.getAlbums(data[0]);	
					}
					
					else{
						
						List_LastFM=new ArrayList<ObjectLastFM>();
						ObjectLastFM newObjectLastfm=new ObjectLastFM();
						newObjectLastfm.setArtista(data[0]);
						newObjectLastfm.setAlbum(data[1]);
						List_LastFM.add(newObjectLastfm);
					}
				}
				catch (Exception e){
					e.printStackTrace();
					return false;
					
				}
			return true;
			
			}
			
			protected void onProgressUpdate(Integer... values) {
			}
			
			protected void onPostExecute( Boolean  response) {
				if (response){
					lstListado.setAdapter(new Adaptador(fragment));
					lstListado.setOnItemClickListener(new OnItemClickListener() {
		            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
		            	String selectedAlbum =((TextView)v.findViewById(R.id.NombreAlbum)).getText().toString();
		            	
		            	Intent i = new Intent(fragment.getActivity(), TracksForAlbumActivity.class);
		        		i.putExtra("artist", Artist);
		        		i.putExtra("album", selectedAlbum);
		        		i.putExtra("urlImagAlbum", List_LastFM.get(position).getImage());
		        		startActivity(i);
		            	
		            }
		        });
				}
				else{
					Toast.makeText(getActivity(), "No se encontro la información solicitada o no tiene acceso a internet", Toast.LENGTH_SHORT).show();
				}
			}	
		
	 }	
	 
	 /*
	  * 	Clase adaptador que carga los datos al listview en el tab de albums
	  * 
	  */
	 
	 class Adaptador extends ArrayAdapter<ObjectLastFM>{
			Activity context;
			
			Adaptador(Fragment context){
				super(context.getActivity(), R.layout.components_listresult_album, List_LastFM);
				this.context = context.getActivity();
				System.out.println("length ssss"+ List_LastFM.size());
				
				
			}
			
			public View getView(int position,View convertView, ViewGroup parent){
					
				
					LayoutInflater inflater = context.getLayoutInflater();
					View item = inflater.inflate(R.layout.components_listresult_album, null);
					
					TextView LblArtista  = (TextView)item.findViewById(R.id.NombreAlbum);
					LblArtista.setText(List_LastFM.get(position).getAlbum());
					
					
					
					return (item);
					
				}
			
			
		}
	 
	 
}


