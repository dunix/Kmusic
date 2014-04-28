package Fragments;

//Fragment que permite buscar nuevos artista por diversos tipos de busqueda

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;



import com.echonest.api.v4.EchoNestException;
import com.example.kmusic.R;
import com.example.kmusic.ResultActivity;


import APIS.EchonestAPI;
import APIS.LastfmAPI;
import Fragments.FragmentTopTracks.Adaptador.AsyncronaSetImage;

import ObjectsAPIS.ObjectEchonest;
import ObjectsAPIS.ObjectInfo;
import ObjectsAPIS.ObjectLastFM;

import Seguridad.InternetStatus;
import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class FragmentNewArtists extends Fragment implements OnClickListener {
	
	//Atributos de la clasee
	private	EchonestAPI EchoAPI;
	private LastfmAPI LastFMAPI;
	private ArrayList<ObjectInfo> Result;
	ArrayList<ObjectLastFM> List_LastFM;
	private RadioGroup opciones;
	private SearchAsyncParam SearchAsyn;
	private ListView lstListado=null;
	private Fragment fragment;
	Button busca;
	private static ProgressDialog pDialog;
	 
	public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
		View view=inflater.inflate(R.layout.fragment_new_artist,  container, false);
		fragment = this;
		
		
		lstListado = (ListView)fragment.getActivity().findViewById(R.id.busqueda);
		busca=(Button) view.findViewById(R.id.button1); 
		
		opciones = (RadioGroup) view.findViewById(R.id.radioGroup1);
		busca.setOnClickListener(this);
		
		if (! new InternetStatus().haveNetworkConnection(this.getActivity())){
			Toast.makeText(this.getActivity(), "Advertencia no posee conexión a Internet", Toast.LENGTH_SHORT).show();
		}
		
		
		return view;
	}
	
	/// Metodo del boton de busqueda 
	@SuppressWarnings("static-access")
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		pDialog = new ProgressDialog(this.getActivity());
	    pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	    pDialog.setMessage("Procesando...");
	   	pDialog.setCancelable(true);
	   	pDialog.setMax(100);
		
        int selectedId = opciones.getCheckedRadioButtonId();
        RadioButton opcion= (RadioButton) this.getActivity().findViewById(selectedId);
        EditText buscar= (EditText) this.getActivity().findViewById(R.id.obtenerartista);
       
        
        InputMethodManager inputManager = (InputMethodManager)this.getActivity().getSystemService(this.getActivity().INPUT_METHOD_SERVICE); 

        inputManager.hideSoftInputFromWindow(this.getActivity().getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
        
  
        SearchAsyn= new SearchAsyncParam();
        Result=new ArrayList<ObjectInfo>();
        
        if (new InternetStatus().haveNetworkConnection(this.getActivity())){
			
        	
		
	        if (opcion.getText().equals("Artista similar")){
	        	SearchAsyn.execute("SimilarArtist", buscar.getText().toString());
	        	
	        }
	        else if (opcion.getText().equals("País")){
	        	SearchAsyn.execute("Geographic",buscar.getText().toString());
	        }
	        
	        else if (opcion.getText().equals("Género")){
	        	SearchAsyn.execute("Genre",buscar.getText().toString());
	        }
       
        }
        else{
        	Toast.makeText(this.getActivity(), "Advertencia no posee conexión a Internet", Toast.LENGTH_SHORT).show();
        	
        }
		
	}
	
	@Override
	public void onActivityCreated(Bundle state) {
		super.onActivityCreated(state);
		fragment = this;
		
	}
	
	//--- Metodo que realiza el merge de diferentes datos de los APIS

	public void Merge(ArrayList<ObjectEchonest> List_Echo,ArrayList<ObjectLastFM> List_Lastfm){
		
		for(ObjectLastFM ObjecFM: List_Lastfm){
			ObjectInfo info=new ObjectInfo();
			info.setArtista(ObjecFM.getArtista());
			info.setImagen( ObjecFM.getImage());
			Result.add(info);	
		}
		
		boolean flag=false;
		
		while(List_Echo.size()!=0){
			int cont=0;
			while(cont!=Result.size()-1){
				if(Result.get(cont).getArtista().toUpperCase().equals(List_Echo.get(0).getArtist().toUpperCase())){
					
					flag=true;
					
					break;
				}
				cont++;
			}
			if (flag==false){
				ObjectInfo info=new ObjectInfo();
				LastFMAPI=new LastfmAPI();
				info.setArtista(List_Echo.get(0).getArtist());
				info.setImagen(LastFMAPI.getImagen2(List_Echo.get(0).getArtist()));
				Result.add(info);
			}
			flag=false;
			List_Echo.remove(0);
		}
		
	}

	//Metodo que parse la informacion de los APIS 
	
	public void ParseList(ObjectLastFM Lastfm){
		ObjectInfo info=new ObjectInfo();
		info.setCancion(Lastfm.getCancion());
		info.setArtista(Lastfm.getArtista());
		info.setAlbum(Lastfm.getAlbum());
		info.setImagen(Lastfm.getImage());
		Result.add(info);
		
	}
	
	// Clase asincrona que realiza la conexion con los APIS y obtiene los datos
	
	public class SearchAsyncParam extends AsyncTask<String, Void, Boolean> {

		ArrayList<ObjectEchonest> List_Echonests;

		public  SearchAsyncParam(){
		}
		
		protected void onPreExecute() {
			pDialog.setProgress(0);
			pDialog.show();
		}
		
		@Override
		protected Boolean doInBackground(String... data) {
			try{
				//busqueda por genero
				if(data[0].equals("Genre")){
					LastFMAPI=new LastfmAPI();
					List_LastFM=LastFMAPI.getTopGenreArtist(data[1]);
					for (ObjectLastFM o:List_LastFM){
						ParseList(o);
					}
					
				}
				
				//busqueda por artista similares
				else if(data[0].equals("SimilarArtist")){
					LastFMAPI=new LastfmAPI();
					List_LastFM=LastFMAPI.getSimilarArtist(data[1]);
					try {
						EchoAPI=new EchonestAPI();
						List_Echonests=EchoAPI.SearchArtistSimiliar(data[1]);
						Merge(List_Echonests,List_LastFM);
						
					} 
					catch (EchoNestException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						return false;
					}
				}
				//busquea por pais o zona geographica
				else if(data[0].equals("Geographic")){
					LastFMAPI=new LastfmAPI();
					List_LastFM=LastFMAPI.getTopGeographic(data[1]);
					for (ObjectLastFM o:List_LastFM){
						ParseList(o);
					}
					
				}
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
				lstListado.setAdapter(new Adaptador(fragment));
				lstListado.setOnItemClickListener(new OnItemClickListener() {
		            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
		            	
		            	
	
		            	String selectedArtist =((TextView)v.findViewById(R.id.Artista)).getText().toString();
		            	String selectedAlbum =((TextView)v.findViewById(R.id.Album)).getText().toString();
		            	String selectedTrack =((TextView)v.findViewById(R.id.Cancion)).getText().toString();
		            	Intent i = new Intent(fragment.getActivity(), ResultActivity.class);
		        		i.putExtra("artist", selectedArtist);
		        		i.putExtra("album", selectedAlbum);
		        		i.putExtra("track", selectedTrack);
		        		//required to launch from non-activity
		        		startActivity(i);
		            	
		            }
		        });
				pDialog.dismiss();
			}
			else{
				pDialog.dismiss();
				Toast.makeText(getActivity(), "No se encontro la información solicitada o no tiene acceso a internet", Toast.LENGTH_SHORT).show();
			}
				
		}	
	
	}
	
	
	//Clase adaptador que carga la informacion al listview

	class Adaptador extends ArrayAdapter<ObjectInfo>{
		Activity context;
		
		Adaptador(Fragment context){
			super(context.getActivity(), R.layout.componentes_busqueda_nuevosartist, Result);
			this.context = context.getActivity();			
		}
		
		public View getView(int position,View convertView, ViewGroup parent){
				
			
				LayoutInflater inflater = context.getLayoutInflater();
				View item = inflater.inflate(R.layout.componentes_busqueda_nuevosartist, null);
				if (Result.size()>0){
					TextView LblArtista  = (TextView)item.findViewById(R.id.Artista);
					LblArtista.setText(Result.get(position).getArtista());
					
					TextView LblAlbum  = (TextView)item.findViewById(R.id.Album);
					LblAlbum.setText(Result.get(position).getAlbum());
					
					TextView LblCancionTop  = (TextView)item.findViewById(R.id.Cancion);
					LblCancionTop.setText(Result.get(position).getCancion());
			
					ImageView imagen  = (ImageView)item.findViewById(R.id.imageViewResultados);
					AsyncronaSetImage image=new AsyncronaSetImage(Result.get(position).getImage(),imagen);
					image.execute();
					
				}
				
								
				return (item);
				
			}
		
	
	// Clase asincronica para poder realizar el set de imagenes	
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
}
