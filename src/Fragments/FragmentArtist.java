package Fragments;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


import com.echonest.api.v4.EchoNestException;
import com.example.kmusic.R;

import APIS.EchonestAPI;
import APIS.LastfmAPI;

import ObjectsAPIS.ObjectEchonest;
import ObjectsAPIS.ObjectInfo;
import ObjectsAPIS.ObjectLastFM;

import Seguridad.InternetStatus;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

//Fragment que posee informacion del artista, como biografia...

public class FragmentArtist extends Fragment {

	Bitmap bitmap;
	private static ProgressDialog pDialog;
	

	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
		View view=inflater.inflate(R.layout.result_tab_informacion_artista_, container, false);
		Intent intent=getActivity().getIntent();
		
		TextView Artist= (TextView) view.findViewById(R.id.BioArtista);
		Artist.setMovementMethod(new ScrollingMovementMethod());
		ImageView imag  = (ImageView)view.findViewById(R.id.imagenbio);
		Artist.setText(intent.getStringExtra("artist")+"\n");
		TextView c= (TextView) view.findViewById(R.id.Biografia);
		
		pDialog = new ProgressDialog(this.getActivity());
	    pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	    pDialog.setMessage("Procesando...");
	   	pDialog.setCancelable(true);
	   	pDialog.setMax(100);
		
		if (new InternetStatus().haveNetworkConnection(this.getActivity())){
			SearchAsyncParam SearchAsyn = new SearchAsyncParam(imag,c);
			SearchAsyn.execute(intent.getStringExtra("artist"));
		}
		else{
			Toast.makeText(getActivity(), "Advertencia no tiene acceso a internet", Toast.LENGTH_SHORT).show();
			
		}
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle state) {
		super.onActivityCreated(state);

		
	}
	
	// --- Metodo que parse los objectos proveniente del API al un objectoinfo para enviarlo al adaptador
	public void ParseList(ObjectEchonest Echonest){
		ObjectInfo info = new ObjectInfo();
		info.setArtista(Echonest.getArtist());
		//System.out.println(Echonest.getArtist());
		
	}
	
	
	/// Clase asincrona que se conectado a los API para traer la informacion de un artista
	
	public class SearchAsyncParam extends AsyncTask<String, Void, Boolean> {
		ArrayList<ObjectLastFM> List_LastFM = new ArrayList<ObjectLastFM>();
		ImageView Imagen;
		TextView bio;
		public  SearchAsyncParam(ImageView i,TextView b){
			Imagen  = i;
			bio=b;
		}
		
		protected void onPreExecute() {
			pDialog.setProgress(0);
			pDialog.show();
		}
		
		@Override
		protected Boolean doInBackground(String... data) {
			
			try {
				LastfmAPI bioFM= new LastfmAPI();
				List_LastFM= bioFM.geBiography(data[0]);
				bitmap=getBitmapImagen(List_LastFM.get(0).getImage());
			} 
			
			catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
			
			return true;
		}
		protected void onProgressUpdate(Integer... values) {
			
			int progreso = values[0].intValue();
			
			pDialog.setProgress(progreso);
		}
		
		//Metodo que extrae la imagen desde un url y la carga al Imageview
		
		public Bitmap getBitmapImagen(String imagen){
			
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
		
		protected void onPostExecute( Boolean  response) {
			if (response){
				Imagen.setImageBitmap(bitmap);
				bio.setMovementMethod(new ScrollingMovementMethod());
				bio.setMovementMethod(LinkMovementMethod.getInstance());
				bio.setText(Html.fromHtml(List_LastFM.get(0).getOther().toString()));
				pDialog.dismiss();
			}
			else{
				pDialog.dismiss();
				Toast.makeText(getActivity(), "No se encontro la información solicitada o no tiene acceso a internet", Toast.LENGTH_SHORT).show();
				
			}
		}
		
	}
	
	
}	



