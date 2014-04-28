package Fragments;



import com.echonest.api.v4.EchoNestException;
import com.example.kmusic.R;
import com.example.kmusic.ResultActivity;
import java.util.ArrayList;
import APIS.EchonestAPI;
import APIS.LastfmAPI;
import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import APIS.*;
import ObjectsAPIS.ObjectEchonest;
import ObjectsAPIS.ObjectInfo;
import ObjectsAPIS.ObjectLastFM;
import ObjectsAPIS.ObjectSpotify;
import Seguridad.InternetStatus;


/*
 * Fragment que posee los diversos tipo de busqueda personalizadas 
 */
public class FragmentCustomSearch extends Fragment implements OnClickListener {
	EchonestAPI EchoAPI;
	LastfmAPI LastFMAPI;
	SpotifyAPI SpotyAPI;
	ArrayList<ObjectInfo> Result;
	SearchAsyncParam1 SearchParam1;
	SearchAsyncParam2  SearchParam2;
	ListView lstListado=null;
	Fragment fragment;
	private static ProgressDialog pDialog;
	
	
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
		
		View view=inflater.inflate(R.layout.fragment_custom_search, container, false);
		fragment = this;
		Button b=(Button) view.findViewById(R.id.buscarN); 
		lstListado = (ListView)fragment.getActivity().findViewById(R.id.busqueda);
		b.setOnClickListener(this);
		
		
		
		if (! new InternetStatus().haveNetworkConnection(this.getActivity())){
			Toast.makeText(this.getActivity(), "Advertencia no posee conexión a Internet", Toast.LENGTH_SHORT).show();
		}
		
		return view;
		
	}
	
	
//-------------------------------------------------------------------- Metodo del boton buscar en donde se valida el tipo de busqueda a realizar--------------------------------------------------------------------
	@Override
	public void onClick(View v) {
	
		EditText artista= (EditText)this.getActivity().findViewById(R.id.getArtista);
		EditText album= (EditText)this.getActivity().findViewById(R.id.getAlbum);
		EditText cancion= (EditText)this.getActivity().findViewById(R.id.getSong);
		Result = new ArrayList<ObjectInfo>();
		
		@SuppressWarnings("static-access")
		InputMethodManager inputManager = (InputMethodManager)this.getActivity().getSystemService(this.getActivity().INPUT_METHOD_SERVICE); 
		inputManager.hideSoftInputFromWindow(this.getActivity().getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
	    
		pDialog = new ProgressDialog(this.getActivity());
	    pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	    pDialog.setMessage("Procesando...");
	   	pDialog.setCancelable(true);
	   	pDialog.setMax(100);
		
		if (new InternetStatus().haveNetworkConnection(this.getActivity())){
			if ((artista.getText().toString().compareTo("")==0)&& (album.getText().toString().compareTo("")==0) && ((cancion.getText().toString().compareTo("")!=0))){
				SearchParam1=new SearchAsyncParam1();
				SearchParam1.execute("Track",cancion.getText().toString());
			}
			else if ((artista.getText().toString().compareTo("")!=0)&& (album.getText().toString().compareTo("")==0) && ((cancion.getText().toString().compareTo("")==0))){
				SearchParam1=new SearchAsyncParam1();
				SearchParam1.execute("Artist",artista.getText().toString());
			}
			
			else if ((artista.getText().toString().compareTo("")!=0)&& (album.getText().toString().compareTo("")==0) && ((cancion.getText().toString().compareTo("")!=0))){
				SearchParam2=new SearchAsyncParam2();
				SearchParam2.execute("ArtistTrack",artista.getText().toString(), cancion.getText().toString());
			}
			
			else if ((artista.getText().toString().compareTo("")!=0)&& (album.getText().toString().compareTo("")!=0) && ((cancion.getText().toString().compareTo("")==0))){
				SearchParam2=new SearchAsyncParam2();
				SearchParam2.execute("ArtistAlbum",artista.getText().toString(), album.getText().toString());
			}
			else{
				Toast.makeText(this.getActivity(), "Su parámetro de búsqueda no es válido", Toast.LENGTH_SHORT).show();
				
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

	
//--------------------------------------- Metodo que realiza el merge de los datos obtenidos los APIS--------------------------------------------------------------------
	
	public ArrayList<ObjectInfo> Merge(ArrayList<ObjectLastFM> L1, ArrayList<ObjectSpotify> L2){
		
		for(ObjectLastFM a : L1){
			System.out.println(a.getArtista()+a.getAlbum()+a.getCancion());
			ObjectInfo info = new ObjectInfo();
			info.setCancion(a.getCancion());
			info.setAlbum( a.getAlbum());
			info.setArtista(  a.getArtista());
			Result.add(info);
		}
	
		boolean flag=false;
		while(L2.size()!=0){
			
			int cont=0;
			while(cont!=Result.size()-1){
				if(Result.get(cont).getArtista().toUpperCase().equals(L2.get(0).getNombreArtista().toUpperCase())&& Result.get(cont).getCancion().toUpperCase().equals(L2.get(0).getNombreCancion().toUpperCase())){
					flag=true;
					break;
					}
				cont++;
		}
		if (flag==false){
			ObjectInfo info=new ObjectInfo();
			info.setCancion(L2.get(0).getNombreCancion());
			info.setAlbum(L2.get(0).getNombreAlbum());
			info.setArtista(L2.get(0).getNombreArtista());
			Result.add(info);
		}
		flag=false;
		L2.remove(0);
		}

		return Result;
	}
	
	
//--------------------------------Metodo que parse los objetos obtenidos de los API a objetos objectinfo para el adaptador--------------------------------------
	

	
	public void ParseList(ObjectEchonest Echonest){
		ObjectInfo info = new ObjectInfo();
		info.setArtista(Echonest.getArtist());
		System.out.println(Echonest.getArtist());
		Result.add(info);
	}
	
	public void ParseList(ObjectLastFM Lastfm){
		ObjectInfo info = new ObjectInfo();
		info.setCancion(Lastfm.getCancion());
		info.setArtista( Lastfm.getArtista());
		info.setAlbum( Lastfm.getAlbum());
		Result.add(info);
		
	}
	
//------------------------------------Clase Asincrona de Busqueda por un parametro---------------------------------------------------------------------------------
	 
	class SearchAsyncParam1 extends AsyncTask<String, Void, Boolean> {
		ArrayList<ObjectLastFM> List_LastFM;
		ArrayList<ObjectSpotify> List_Spotify;
		ArrayList<ObjectEchonest> List_Echonest;

		public  SearchAsyncParam1(){
		}
		
		protected void onPreExecute() {
			pDialog.setProgress(0);
			pDialog.show();
		}
		
		@Override
		protected Boolean doInBackground(String... data) {
			try{
				if(data[0].equals("Track")){
					Result = new ArrayList<ObjectInfo>();;
					SpotyAPI = new SpotifyAPI();
					LastFMAPI = new LastfmAPI();
					List_LastFM = LastFMAPI.searchTracks(data[1]);
					List_Spotify = SpotyAPI.SearchTrack(data[1]);
					Merge(List_LastFM, List_Spotify);
				}
				
				else if(data[0].equals("Artist")){
					try {
						Result = new ArrayList<ObjectInfo>();;
						EchoAPI = new EchonestAPI();
						List_Echonest = EchoAPI.searchArtistByName(data[1],15);
						for(ObjectEchonest Echo : List_Echonest){
							ParseList(Echo);
						}
					} 
					catch (EchoNestException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						return false;
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
				lstListado = (ListView)fragment.getActivity().findViewById(R.id.busqueda);
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

//------------------------------------Clase Asincrona de Busqueda por dos parametros---------------------------------------------------------------------------------
	
	public class SearchAsyncParam2 extends AsyncTask<String, Void, Boolean> {
		ArrayList<ObjectLastFM> List_LastFM;

		public  SearchAsyncParam2(){
			List_LastFM = new ArrayList<ObjectLastFM>();
		}
		
		protected void onPreExecute() {
			pDialog.setProgress(0);
			pDialog.show();
		}
		
		@Override
		protected Boolean doInBackground(String... data) {
			try{
				if(data[0].equals("ArtistTrack")){
					LastFMAPI = new LastfmAPI();
					List_LastFM = LastFMAPI.getArtistsTracks(data[1], data[2]);
					for(ObjectLastFM newFM:List_LastFM){
						ParseList(newFM);
					}	
					
				}
				
				else if(data[0].equals("ArtistAlbum")){
					LastFMAPI = new LastfmAPI();
					List_LastFM = LastFMAPI.getArtistsAlbum(data[1], data[2]);
					for(ObjectLastFM newFM:List_LastFM){
						ParseList(newFM);
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
	
	
//--------------------------------Clase adaptador realiza el set del listview de la busqueda con objetos genericos independiente de las busquedas--------------------------------
	
	class Adaptador extends ArrayAdapter<ObjectInfo>{
		Activity context;
		
		Adaptador(Fragment context){
			super(context.getActivity(), R.layout.componentes_busqueda_nuevosartist, Result);
			this.context = context.getActivity();
			System.out.println("length ssss"+ Result.size());
			
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
