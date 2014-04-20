package Fragments;

import java.util.ArrayList;

import com.echonest.api.v4.EchoNestException;
import com.example.kmusic.R;
import com.example.kmusic.ResultActivity;
import com.example.kmusic.TracksForAlbumActivity;

import APIS.EchonestAPI;
import APIS.LastfmAPI;
import Fragments.FragmentNewArtists.Adaptador;
import ObjectsAPIS.ObjectEchonest;
import ObjectsAPIS.ObjectInfo;
import ObjectsAPIS.ObjectLastFM;
import ObjectsAPIS.ObjectMusicXmatch;
import ObjectsAPIS.ObjectSpotify;
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
import android.widget.AdapterView.OnItemClickListener;

public class FragmentAlbum extends Fragment {
	ArrayList<ObjectLastFM> List_LastFM;
	ListView lstListado=null;
	Fragment fragment;
	
	

	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.result_tab_album, container, false);
		fragment=this;
		Intent intent=getActivity().getIntent();
		System.out.println(intent.getStringExtra("artist")+" "+intent.getStringExtra("album")+" "+intent.getStringExtra("track") +"  "+"Si es esteeee");
		TextView t= (TextView)v.findViewById(R.id.NombreArtistaAlbumTab);
		t.setText(intent.getStringExtra("artist"));//cambiar
		
		lstListado = (ListView)v.findViewById(R.id.ListViewAlbumResult);
		SearchAsyncAlbums AsynAlbums=new SearchAsyncAlbums();
		AsynAlbums.execute(intent.getStringExtra("artist"),intent.getStringExtra("album"));
        return v;
	}
	
	
	
	
	 class SearchAsyncAlbums extends AsyncTask<String, Void, Boolean> {
			LastfmAPI LastFMAPI;
			private String Artist;

			public  SearchAsyncAlbums(){
			}
			
			protected void onPreExecute() {
			}
			
			@Override
			protected Boolean doInBackground(String... data) {
				
				Artist=data[0];
				if(data[1].equals("")){
					LastFMAPI=new LastfmAPI();
					List_LastFM=LastFMAPI.getAlbums(data[0]);	
					System.out.println("Viene vacio");
				}
				
				else{
					///Sele manda el album que viene de una vez
					List_LastFM=new ArrayList<ObjectLastFM>();
					ObjectLastFM newObjectLastfm=new ObjectLastFM();
					newObjectLastfm.setArtista(data[0]);
					newObjectLastfm.setAlbum(data[1]);
					List_LastFM.add(newObjectLastfm);
				}
				
				
				
				
			    return true;
			}
			protected void onProgressUpdate(Integer... values) {
			}
			
			
			
			protected void onPostExecute( Boolean  response) {
				
				//lstListado = (ListView)fragment.getActivity().findViewById(R.id.busqueda);
				lstListado.setAdapter(new Adaptador(fragment));
				lstListado.setOnItemClickListener(new OnItemClickListener() {
		            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
		            	String selectedAlbum =((TextView)v.findViewById(R.id.NombreAlbum)).getText().toString();
		            	
		            	Intent i = new Intent(fragment.getActivity(), TracksForAlbumActivity.class);
		        		i.putExtra("artist", Artist);
		        		i.putExtra("album", selectedAlbum);
		        
		        		//required to launch from non-activity
		        		startActivity(i);
		            	
		            }
		        });
				
			}	
		}	
	 
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
