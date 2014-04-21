package Fragments;

//Fragment que permite buscar nuevos artista por diversos tipos de busqueda

import java.util.ArrayList;



import com.echonest.api.v4.EchoNestException;
import com.example.kmusic.R;
import com.example.kmusic.ResultActivity;


import APIS.EchonestAPI;
import APIS.LastfmAPI;

import ObjectsAPIS.ObjectEchonest;
import ObjectsAPIS.ObjectInfo;
import ObjectsAPIS.ObjectLastFM;

import Seguridad.InternetStatus;
import android.app.Activity;
import android.app.Fragment;

import android.content.Intent;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
	private RadioGroup opciones;
	private SearchAsyncParam SearchAsyn;
	private ListView lstListado=null;
	private Fragment fragment;
	
	 
	public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
		View view=inflater.inflate(R.layout.fragment_new_artist,  container, false);
		fragment = this;
		
		
		lstListado = (ListView)fragment.getActivity().findViewById(R.id.busqueda);
		Button b=(Button) view.findViewById(R.id.button1); 
		opciones = (RadioGroup) view.findViewById(R.id.radioGroup1);
		b.setOnClickListener(this);
		
		if (! new InternetStatus().haveNetworkConnection(this.getActivity())){
			Toast.makeText(this.getActivity(), "Advertencia no posee conexi�n a Internet", Toast.LENGTH_SHORT).show();
		}
		
		
		return view;
	}
	
	/// Metodo del boton de busqueda 
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		// get selected radio button from radioGroup
        int selectedId = opciones.getCheckedRadioButtonId();
        RadioButton opcion= (RadioButton) this.getActivity().findViewById(selectedId);
        EditText buscar= (EditText) this.getActivity().findViewById(R.id.obtenerartista);
        SearchAsyn= new SearchAsyncParam();
        Result=new ArrayList<ObjectInfo>();
        if (new InternetStatus().haveNetworkConnection(this.getActivity())){
			
        	
		
	        if (opcion.getText().equals("Artista similar")){
	        	SearchAsyn.execute("SimilarArtist", buscar.getText().toString());
	        	
	        }
	        else if (opcion.getText().equals("Pa�s")){
	        	SearchAsyn.execute("Geographic",buscar.getText().toString());
	        }
	        
	        else if (opcion.getText().equals("G�nero")){
	        	SearchAsyn.execute("Genre",buscar.getText().toString());
	        }
       
        }
        else{
        	Toast.makeText(this.getActivity(), "Advertencia no posee conexi�n a Internet", Toast.LENGTH_SHORT).show();
        	
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
			info.artist=ObjecFM.getArtista();
			Result.add(info);	
		}
		for (ObjectEchonest e: List_Echo){
			System.out.println("esta es echooo   "+e.getArtist());
		}
		boolean flag=false;
		
		while(List_Echo.size()!=0){
			int cont=0;
			while(cont!=Result.size()-1){
				if(Result.get(cont).artist.toUpperCase().equals(List_Echo.get(0).getArtist().toUpperCase())){
					
					flag=true;
					
					break;
				}
				cont++;
			}
			if (flag==false){
				ObjectInfo info=new ObjectInfo();
				info.artist=List_Echo.get(0).getArtist();
				Result.add(info);
			}
			flag=false;
			List_Echo.remove(0);
		}
		
	}

	//Metodo que parse la informacion de los APIS 
	
	public void ParseList(ObjectLastFM Lastfm){
		ObjectInfo info=new ObjectInfo();
		info.cancion=Lastfm.getCancion();
		info.artist=Lastfm.getArtista();
		info.album=Lastfm.getAlbum();
		Result.add(info);
		
	}
	
	// Clase asincrona que realiza la conexion con los APIS y obtiene los datos
	
	public class SearchAsyncParam extends AsyncTask<String, Void, Boolean> {
		ArrayList<ObjectLastFM> List_LastFM;
		ArrayList<ObjectEchonest> List_Echonests;

		public  SearchAsyncParam(){
		}
		
		protected void onPreExecute() {
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
					for (ObjectInfo o:Result){
						System.out.println("Pone play!!! "+o.artist+"  "+o.album+"  "+o.cancion);
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
			}
			else{
				Toast.makeText(getActivity(), "No se encontro la informaci�n solicitada o no tiene acceso a internet", Toast.LENGTH_SHORT).show();
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
			
				}
				
								
				return (item);
				
			}
		
		
	}
}
