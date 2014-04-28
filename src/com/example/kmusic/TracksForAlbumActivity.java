package com.example.kmusic;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import APIS.LastfmAPI;

import ObjectsAPIS.ObjectLastFM;
import Seguridad.InternetStatus;
import android.app.Activity;
import android.app.ProgressDialog;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;

import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

//Activity encargada de mostrar las canciones asociadas a un album
public class TracksForAlbumActivity extends Activity{
	ArrayList<ObjectLastFM> List_LastFM;
	ListView listViewAlmbumResult;
	Activity activity;
	Bitmap imagenAlbum;
	ImageView imagen;
	String Album;
	String Artista;
	Button Comprar;
	private static ProgressDialog pDialog;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_canciones_por_album);
        activity=this;
        
        Intent intent=getIntent();
        TextView Titulo= (TextView)this.findViewById(R.id.ArtistaAlbumCancionTab);
        Artista=intent.getStringExtra("artist");
        Album=intent.getStringExtra("album");
        Titulo.setText(Artista+" "+Album+"\n");
        Titulo.setMovementMethod(new ScrollingMovementMethod());
        
        pDialog = new ProgressDialog(this);
	    pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	    pDialog.setMessage("Procesando...");
	   	pDialog.setCancelable(true);
	   	pDialog.setMax(100);
        
        imagen  = (ImageView) findViewById(R.id.ImagenAlbumTab);
        
        final Button button = (Button) findViewById(R.id.buttonAmazon);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
            	BuyAlbum();
            }
        });

 
        
        listViewAlmbumResult = (ListView)this.findViewById(R.id.ListViewTrackForAlbum);
        
        if( new InternetStatus().haveNetworkConnection(this)){
        	SearchAsyncTracks AsynTracks=new SearchAsyncTracks();
        	AsynTracks.execute(intent.getStringExtra("artist"),intent.getStringExtra("album"));
        }
        else {
        	Toast.makeText(this, "Advertencia no tiene acceso a internet", Toast.LENGTH_SHORT).show();
        	
        }
        
	}
	
	// Método encargado de realizar el intent a amazon para la compra del album
	public void BuyAlbum(){
		
    	Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        Artista+="+";
        Album.replace(" ", "+");
        intent.setData(Uri.parse("http://www.amazon.com/s/ref=nb_sb_noss_2?url=search-alias%3Dpopular&field-keywords="+Artista+" "+Album));                
        startActivity(intent);
	}
	
	// Método encargado de realizar el set de la imagen del album
	public Bitmap getBitmapImagen(String imagen){
		
		try{
			
			if (imagen!=""){
				System.out.println(imagen);
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

	
	// clase asincronica la cual busca las canciones asociadas a un album especifico según LastFM
	 class SearchAsyncTracks extends AsyncTask<String, Void, Boolean> {
			LastfmAPI LastFMAPI;
			String Album;
			String Artist;

			public  SearchAsyncTracks(){
			}
			
			protected void onPreExecute() {
				pDialog.setProgress(0);
				pDialog.show();
			}
			
			
			
			@Override
			protected Boolean doInBackground(String... data) {
				try{
					Artist=data[0];
					Album=data[1];
					LastFMAPI=new LastfmAPI();
					List_LastFM=LastFMAPI.getTracks(Artist, Album);	
					imagenAlbum=getBitmapImagen(getIntent().getStringExtra("urlImagAlbum"));
				}
				catch(Exception e){
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
					pDialog.dismiss(); 
					imagen.setImageBitmap(imagenAlbum);
					listViewAlmbumResult.setAdapter(new Adaptador(activity));
					listViewAlmbumResult.setOnItemClickListener(new OnItemClickListener() {
		            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
		            	
		            	
		            	String selectedTrack =((TextView)v.findViewById(R.id.NombreTrack)).getText().toString();
		            
		            	Intent i = new Intent(activity, YoutubeActivity.class);
		        		i.putExtra("track", selectedTrack);
		        		i.putExtra("artist", Artist);
		        		i.putExtra("album", Album);
		        		//required to launch from non-activity
		        		startActivity(i);
		            	
		            }
		        });
				
					
				
				}
				else{
					pDialog.dismiss();
					Toast.makeText(activity, "No se encontro la información solicitada o no tiene acceso a internet", Toast.LENGTH_SHORT).show();
				
				}
			}	
		}
	 
	 
	 // Adaptador custom para el listview de las canciones por album
	 class Adaptador extends ArrayAdapter<ObjectLastFM>{
			Activity context;
			
			Adaptador(Activity context){
				super(context, R.layout.componentes_busqueda_nuevosartist, List_LastFM);
				this.context = context;
				System.out.println("length track for albums list"+ List_LastFM.size());
				
				
			}
			
			public View getView(int position,View convertView, ViewGroup parent){
					
				
					LayoutInflater inflater = context.getLayoutInflater();
					View item = inflater.inflate(R.layout.componentes_listview_tracks_for_album, null);
					if (List_LastFM.size()>0){
							TextView LblNombreTrack  = (TextView)item.findViewById(R.id.NombreTrack);
							LblNombreTrack.setText(List_LastFM.get(position).getCancion());
						}
					
									
					return (item);
					
				}
			
			
		}
	 

}
