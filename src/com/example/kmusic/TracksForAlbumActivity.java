package com.example.kmusic;

import java.util.ArrayList;

import APIS.LastfmAPI;
import ObjectsAPIS.ObjectInfo;
import ObjectsAPIS.ObjectLastFM;
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

public class TracksForAlbumActivity extends Activity {
	ArrayList<ObjectLastFM> List_LastFM;
	ListView listViewAlmbumResult;
	Activity activity;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_canciones_por_album);
        activity=this;
        Intent intent=getIntent();
        TextView Titulo= (TextView)this.findViewById(R.id.ArtistaAlbumCancionTab);
        Titulo.setText(intent.getStringExtra("artist"));
        listViewAlmbumResult = (ListView)this.findViewById(R.id.ListViewAlbumResult);
        SearchAsyncTracks AsynTracks=new SearchAsyncTracks();
		AsynTracks.execute(intent.getStringExtra("artist"),intent.getStringExtra("album"));
        
	}
	
	
	 class SearchAsyncTracks extends AsyncTask<String, Void, Boolean> {
			LastfmAPI LastFMAPI;
			String Album;
			String Artist;

			public  SearchAsyncTracks(){
			}
			
			protected void onPreExecute() {
			}
			
			@Override
			protected Boolean doInBackground(String... data) {
				Artist=data[0];
				Album=data[1];
				LastFMAPI=new LastfmAPI();
				List_LastFM=LastFMAPI.getTracks(data[0],data[1]);	
							
			    return true;
			}
			protected void onProgressUpdate(Integer... values) {
			}
			
			
			
			protected void onPostExecute( Boolean  response) {
				
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
		}
	 
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
