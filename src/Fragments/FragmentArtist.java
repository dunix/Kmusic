package Fragments;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import com.echonest.api.v4.Artist;
import com.echonest.api.v4.EchoNestException;
import com.example.kmusic.R;

import APIS.EchonestAPI;
import APIS.LastfmAPI;
import Fragments.FragmentNewArtists.SearchAsyncParam;
import ObjectsAPIS.ObjectEchonest;
import ObjectsAPIS.ObjectInfo;
import ObjectsAPIS.ObjectLastFM;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;



public class FragmentArtist extends Fragment {
	EchonestAPI EchoAPI;
	
	
	Bitmap bitmap;
	

	
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
		View view=inflater.inflate(R.layout.result_tab_informacion_artista_, container, false);
		Intent intent=getActivity().getIntent();
		System.out.println("LA CONCHA" + intent.getStringExtra("artist"));
		TextView Artist= (TextView) view.findViewById(R.id.BioArtista);
		ImageView imag  = (ImageView)view.findViewById(R.id.imagenbio);
		Artist.setText(intent.getStringExtra("artist"));
		TextView c= (TextView) view.findViewById(R.id.Biografia);
    	SearchAsyncParam SearchAsyn = new SearchAsyncParam(imag,c);
    	SearchAsyn.execute(intent.getStringExtra("artist"));
		
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle state) {
		super.onActivityCreated(state);

		
	}
	
	
	
	public void ParseList(ObjectEchonest Echonest){
		ObjectInfo info = new ObjectInfo();
		info.artist = Echonest.getArtist();
		System.out.println(Echonest.getArtist());
		
	}
	
	
	
	public class SearchAsyncParam extends AsyncTask<String, Void, Boolean> {
		ArrayList<ObjectEchonest> List_Echonests = new ArrayList<ObjectEchonest>();
		ImageView Imagen;
		TextView bio;
		public  SearchAsyncParam(ImageView i,TextView b){
			Imagen  = i;
			bio=b;
		}
		
		protected void onPreExecute() {
		}
		
		@Override
		protected Boolean doInBackground(String... data) {
			
		  	  
			System.out.println("DATAAAAAAAAAAAAAAAAAAAAAAAA igual a -> "+data[0]);
			try {
				LastfmAPI image= new LastfmAPI();
			  	String url=image.getImagen(data[0]);
				
				EchoAPI = new EchonestAPI();
				List_Echonests = EchoAPI.searchArtistBiography(data[0]);
				List_Echonests.get(0).SetImagen(url);
				//System.out.println("holaaa"+List_Echonests.get(0).getImagen());
				bitmap=getBitmapImagen(List_Echonests.get(0).getImagen());
			} catch (EchoNestException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			

		    return true;
		}
		protected void onProgressUpdate(Integer... values) {
		}
		
		public Bitmap getBitmapImagen(String imagen){
			
			try{
				
				if (imagen!=""){
					//System.out.println(imagen);
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
			
			Imagen.setImageBitmap(bitmap);
			bio.setMovementMethod(new ScrollingMovementMethod());
			bio.setText(List_Echonests.get(0).getBiography());
		
		}	
	}
}	



