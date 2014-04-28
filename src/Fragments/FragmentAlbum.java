package Fragments;


import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


import com.example.kmusic.R;

import com.example.kmusic.TracksForAlbumActivity;



import APIS.LastfmAPI;
import Fragments.FragmentNewArtists.Adaptador.AsyncronaSetImage;

import ObjectsAPIS.ObjectLastFM;

import Seguridad.InternetStatus;
import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.os.AsyncTask;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

// Fragment que maneja los albums encontrados para un artista

public class FragmentAlbum extends Fragment {
	ArrayList<ObjectLastFM> List_LastFM;
	ListView lstListado=null;				// Lista que contiene los datos para el adaptor del listview
	Fragment fragment;
	Gallery gallery; 
	Activity activity;
	private static ProgressDialog pDialog;
	ImageView imageView; 
	View v1;

	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
		final View v = inflater.inflate(R.layout.result_tab_album, container, false);
		fragment=this;
		Intent intent=getActivity().getIntent();
		
		v1=v;
		
		TextView t= (TextView)v.findViewById(R.id.NombreArtistaAlbumTab);
		t.setText(intent.getStringExtra("artist")+"\n");
		
		lstListado = (ListView)v.findViewById(R.id.ListViewAlbumResult);
		
		activity=this.getActivity();
	/*	gallery= (Gallery) v.findViewById(R.id.gallery1);
		imageView = (ImageView) v.findViewById(R.id.imageView1);
		*/
		pDialog = new ProgressDialog(this.getActivity());
	    pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	    pDialog.setMessage("Procesando...");
	   	pDialog.setCancelable(true);
	   	pDialog.setMax(100);
		
	   	
	   	
	 /*  	gallery.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				//Toast.makeText(getApplicationContext(), "pic: " + arg2, Toast.LENGTH_SHORT).show();
				TextView t= (TextView) v.findViewById(R.id.NombreArtistaAlbumTab) ;
				t.setText(List_LastFM.get(arg2).getAlbum());
				
				// TODO Auto-generated method stub
				
			}
			
			
		});*/
		
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
	/*
	public class ImageAdapter extends BaseAdapter {

		private Context context;
		private int imageBackgroud;
		private Integer[] pics;
		
		
		public ImageAdapter (View context){
			
			this.context = context.getContext();
		
		}
		
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return List_LastFM.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			// TODO Auto-generated method stub
			ImageView imageView = new ImageView(context);
			System.out.println("oaaaaa      " +List_LastFM.get(arg0).getImage());
			AsyncronaSetImage image=new AsyncronaSetImage(List_LastFM.get(arg0).getImage(),imageView);
			image.execute();
			
			
			
			
			return imageView;
		}
	}

	*/
	
	
	
	
	
	 class SearchAsyncAlbums extends AsyncTask<String, Void, Boolean> {
			LastfmAPI LastFMAPI;
			private String Artist;

			public  SearchAsyncAlbums(){
			}
			
			protected void onPreExecute() {
				pDialog.setProgress(0);
				pDialog.show();
			}
			
			@Override
			protected Boolean doInBackground(String... data) {
				try{
					Artist=data[0];
					LastFMAPI=new LastfmAPI();
					if(data[1].equals("")){
						
						List_LastFM=LastFMAPI.getAlbums(Artist);	
					}
					
					else{
						
						List_LastFM=new ArrayList<ObjectLastFM>();
						ObjectLastFM newObjectLastfm=new ObjectLastFM();
						newObjectLastfm.setArtista(Artist);
						newObjectLastfm.setAlbum(data[1]);
						newObjectLastfm.setImagen(LastFMAPI.getImagen(Artist, data[1]));
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
				
				int progreso = values[0].intValue();
				
				pDialog.setProgress(progreso);
			}
			
			protected void onPostExecute( Boolean  response) {
				if (response){
		
					//gallery.setAdapter(new ImageAdapter(v1));
					
					
					pDialog.dismiss();
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
					pDialog.dismiss();
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
					
					ImageView imagen  = (ImageView)item.findViewById(R.id.imageViewAlbumTab);
					AsyncronaSetImage image=new AsyncronaSetImage(List_LastFM.get(position).getImage(),imagen);
					image.execute();
					
					
					return (item);
					
				}
			
			
		}
	 public class AsyncronaSetImage extends AsyncTask<String , Void, Boolean> {
			String imagen;
			ImageView imag;
			private Bitmap bitmap;
			
			public AsyncronaSetImage(String ima, ImageView i){
				imagen = ima;
				imag=i;
				
			}
			
			@Override
			protected Boolean doInBackground(String... params) {
				// TODO Auto-generated method stub

		
				try {
					bitmap=getBitmapImagen(imagen);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				return true;
			}
			
			protected void onPostExecute( Boolean  response) {
	
				imag.setImageBitmap(bitmap);
			}
			
			public Bitmap getBitmapImagen(String imagen){
				// Realiza el set de la imagen correspondiente a listview de top tracks
				try{
					
					if (imagen!=""){
						URL url = new URL(imagen);
						HttpURLConnection connection= (HttpURLConnection) url.openConnection();
						connection.setDoInput(true);
						connection.connect();
						InputStream input =connection.getInputStream();
						Bitmap bitM= BitmapFactory.decodeStream(input);
						return bitM;
					}
					
					URL url = new URL("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSy5__ZrEZMLWSnGvyQPZDHBiJH8WzL-qIIvq8kIdJqBL2IVhThYQ");
					HttpURLConnection connection= (HttpURLConnection) url.openConnection();
					connection.setDoInput(true);
					connection.connect();
					InputStream input =connection.getInputStream();
					Bitmap bitM= BitmapFactory.decodeStream(input);
					return bitM;
					
				}
				catch(Exception e){
					System.out.println("imagen vacia     "+ imagen);
					e.printStackTrace();
					
					
				}
				return null;
			}
			}
		
		
	 
}


