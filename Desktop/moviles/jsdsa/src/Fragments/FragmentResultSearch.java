package Fragments;

import java.util.Collection;

import com.echonest.api.v4.EchoNestException;
import com.example.kmusic.R;
import com.example.kmusic.ResultActivity;
import com.example.kmusic.YoutubeActivity;
import com.example.kmusic.R.layout;

import java.util.ArrayList;

import de.umass.lastfm.Caller;
import de.umass.lastfm.Chart;
import de.umass.lastfm.PaginatedResult;
import de.umass.lastfm.Track;
import APIS.EchonestAPI;
import android.app.Activity;
import android.app.Fragment;
import android.app.SearchableInfo;
import android.content.Intent;
import android.content.res.ObbInfo;
import android.location.Address;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import APIS.*;
import Fragments.FragmentTopArtists.LastAsync;
import Fragments.FragmentTopTracks.Adaptador;
import ObjectsAPIS.ObjectEchonest;
import ObjectsAPIS.ObjectInfo;
import ObjectsAPIS.ObjectLastFM;
import ObjectsAPIS.ObjectMusicXmatch;
import ObjectsAPIS.ObjectSpotify;

public class FragmentResultSearch extends Fragment implements OnClickListener {
	EchonestAPI EchoAPI;
	LastfmAPI LastFMAPI;
	MusixMatchAPI MusixAPI;
	SpotifyAPI SpotyAPI;
	ArrayList<ObjectInfo> Result;
	SearchAsyncParam1 SearchParam1;
	SearchAsyncParam2  SearchParam2;
	ListView lstListado=null;
	Fragment fragment;
	
	
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
		
		
		
		//SearchParam1.execute("Track","Paradise");
		
		View view=inflater.inflate(R.layout.tab_result_search, container, false);
		fragment = this;
		Button b=(Button) view.findViewById(R.id.buscarN); 
		lstListado = (ListView)fragment.getActivity().findViewById(R.id.busqueda);
		b.setOnClickListener(this);
		return view;
		
		}
	
	@Override
	public void onClick(View v) {
	
		EditText artista= (EditText)this.getActivity().findViewById(R.id.getArtista);
		EditText album= (EditText)this.getActivity().findViewById(R.id.getAlbum);
		EditText cancion= (EditText)this.getActivity().findViewById(R.id.getSong);
		Result = new ArrayList<ObjectInfo>();
		
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
		
	}
	
	@Override
	public void onActivityCreated(Bundle state) {
		super.onActivityCreated(state);
		fragment = this;
		
	}

	
	public ArrayList<ObjectInfo> Merge(ArrayList<ObjectMusicXmatch> L1, ArrayList<ObjectSpotify> L2){
		
		for(ObjectMusicXmatch a : L1){
			ObjectInfo info = new ObjectInfo();
			info.cancion = a.getNombreCancion();
			info.album = a.getNombreAlbum();
			info.artist = a.getNombreArtista();
			Result.add(info);
		}
		System.out.println(Result.size());
		boolean flag=false;
		while(L2.size()!=0){
			
			int cont=0;
			while(cont!=Result.size()-1){
				if(Result.get(cont).artist.toUpperCase().equals(L2.get(0).getNombreArtista().toUpperCase())&& Result.get(cont).cancion.toUpperCase().equals(L2.get(0).getNombreCancion().toUpperCase())){
					System.out.println("MusicX cancion: "+ Result.get(cont).cancion.toUpperCase()+ " son Diferentes Spotmierda: "+L2.get(0).getNombreCancion().toUpperCase()+"\n");
					System.out.println("MusicX album: "+ Result.get(cont).album.toUpperCase()+ " son Diferentes Spotmierda: "+L2.get(0).getNombreAlbum().toUpperCase()+"\n");
					System.out.println("MusicX artista: "+ Result.get(cont).artist+ " son Diferentes Spotmierda: "+L2.get(0).getNombreArtista().toUpperCase()+"\n");
					flag=true;
					break;
					}
				cont++;
		}
		if (flag==false){
			ObjectInfo info=new ObjectInfo();
			info.cancion=L2.get(0).getNombreCancion();
			info.album=L2.get(0).getNombreAlbum();
			info.artist=L2.get(0).getNombreArtista();
			Result.add(info);
		}
		flag=false;
		L2.remove(0);
		}
		System.out.println(Result.size());
		return Result;
	}
	
	public void ParseList(ObjectMusicXmatch MusicX){
		ObjectInfo info = new ObjectInfo();
		info.artist = MusicX.getNombreArtista();
		info.album = MusicX.getNombreAlbum();
		info.cancion = MusicX.getNombreCancion();
		Result.add(info);
	}
	
	public void ParseList(ObjectEchonest Echonest){
		ObjectInfo info = new ObjectInfo();
		info.artist = Echonest.getArtist();
		System.out.println(Echonest.getArtist());
		Result.add(info);
	}
	
	public void ParseList(ObjectLastFM Lastfm){
		ObjectInfo info = new ObjectInfo();
		info.cancion = Lastfm.getCancion();
		info.artist = Lastfm.getArtista();
		info.album = Lastfm.getAlbum();
		Result.add(info);
		
	}
	
	//----Clase Asincrona de Busqueda por un parametro-----------------
	 class SearchAsyncParam1 extends AsyncTask<String, Void, Boolean> {
		ArrayList<ObjectMusicXmatch> List_MusixMatch;
		ArrayList<ObjectSpotify> List_Spotify;
		ArrayList<ObjectEchonest> List_Echonest;

		public  SearchAsyncParam1(){
		}
		
		protected void onPreExecute() {
		}
		
		@Override
		protected Boolean doInBackground(String... data) {
			
			if(data[0].equals("Track")){
				Result = new ArrayList<ObjectInfo>();;
				SpotyAPI = new SpotifyAPI();
				MusixAPI = new MusixMatchAPI();
				List_MusixMatch = MusixAPI.SearchTrack(data[1]);
				List_Spotify = SpotyAPI.SearchTrack(data[1]);
				Merge(List_MusixMatch, List_Spotify);
				
			}
			
			else if(data[0].equals("Artist")){
				
			try {
				Result = new ArrayList<ObjectInfo>();;
				EchoAPI = new EchonestAPI();
				List_Echonest = EchoAPI.searchArtistByName(data[1],15);
				for(ObjectEchonest Echo : List_Echonest){
					ParseList(Echo);
				}
				System.out.println(Result.size());
				
			} catch (EchoNestException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				
			}
			
			
		    return true;
		}
		protected void onProgressUpdate(Integer... values) {
		}
		
		
		
		protected void onPostExecute( Boolean  response) {
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
			
		}	
	}	

	//----Clase Asincrona de Busqueda por dos parametros-----------------
	public class SearchAsyncParam2 extends AsyncTask<String, Void, Boolean> {
		ArrayList<ObjectMusicXmatch> List_MusixMatch;
		ArrayList<ObjectLastFM> List_LastFM;

		public  SearchAsyncParam2(){
			List_MusixMatch= new ArrayList<ObjectMusicXmatch>();
			List_LastFM = new ArrayList<ObjectLastFM>();
		}
		
		protected void onPreExecute() {
		}
		
		@Override
		protected Boolean doInBackground(String... data) {
			if(data[0].equals("ArtistTrack")){
				
				System.out.println("entraaaaaaaaaaaaaa"+ data[1]+ data[2]);	
			
				LastFMAPI = new LastfmAPI();
				List_LastFM = LastFMAPI.getArtistsTracks(data[1], data[2]);
				
				for(ObjectLastFM newFM:List_LastFM){
					ParseList(newFM);
				}
				
				MusixAPI = new MusixMatchAPI();
				List_MusixMatch = MusixAPI.SearchArtistTrack(data[1], data[2]);
			
				for(ObjectMusicXmatch newFM:List_MusixMatch){
					ParseList(newFM);
				}		
				
			}
			
			else if(data[0].equals("ArtistAlbum")){
				System.out.println("sssssss"+ data[1]+"  " +data[2]);
				LastFMAPI = new LastfmAPI();
				List_LastFM = LastFMAPI.getArtistsAlbum(data[1], data[2]);
				for(ObjectLastFM newFM:List_LastFM){
					ParseList(newFM);
				}
				
				}
		    return true;
		}
		protected void onProgressUpdate(Integer... values) {
		}
		
		
		
		protected void onPostExecute( Boolean  response) {
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
	}	
		


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
