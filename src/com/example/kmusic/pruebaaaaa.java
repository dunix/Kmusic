package com.example.kmusic;

import java.util.ArrayList;

import com.echonest.api.v4.EchoNestException;

import APIS.EchonestAPI;
import APIS.LastfmAPI;
import ObjectsAPIS.ObjectEchonest;
import ObjectsAPIS.ObjectInfo;
import ObjectsAPIS.ObjectLastFM;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class pruebaaaaa extends Fragment implements OnClickListener {
	EchonestAPI EchoAPI;
	LastfmAPI LastFMAPI;
	ArrayList<ObjectInfo> Result=new ArrayList<ObjectInfo>();
	
	public View onCreateView(LayoutInflater inflater,ViewGroup container,
			Bundle savedInstanceState) {

  /*  	SearchAsyncParam SearchAsyn=new SearchAsyncParam();
    	SearchAsyn.execute("SimilarArtist","Audioslave");*/
		System.out.println("mierdaaaaaaa");
		View view=inflater.inflate(R.layout.fragment_new_artist,  container, false);
		Button b=(Button) view.findViewById(R.id.button1); 
		b.setOnClickListener(this);
		
		return view;
	}
	
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		System.out.println("sasdasdasda");
		
	}

	public void Merge(ArrayList<ObjectEchonest> List_Echo,ArrayList<ObjectLastFM> List_Lastfm){
		
		for(ObjectLastFM ObjecFM: List_Lastfm){
			ObjectInfo info=new ObjectInfo();
			info.artist=ObjecFM.getArtista();
			System.out.println("esta es lastfm    "+ info.artist);
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
					System.out.println("Lastfm "+ Result.get(cont).artist.toUpperCase()+ " son iguale Echonest: "+List_Echo.get(0).getArtist().toUpperCase());
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

	
	
	
	public void ParseList(ObjectLastFM Lastfm){
		ObjectInfo info=new ObjectInfo();
		info.cancion=Lastfm.getCancion();
		info.artist=Lastfm.getArtista();
		info.album=Lastfm.getAlbum();
		Result.add(info);
		
	}
	
	public class SearchAsyncParam extends AsyncTask<String, Void, Boolean> {
		ArrayList<ObjectLastFM> List_LastFM;
		ArrayList<ObjectEchonest> List_Echonests;

		public  SearchAsyncParam(){
		}
		
		protected void onPreExecute() {
		}
		
		@Override
		protected Boolean doInBackground(String... data) {
			
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
			
			else if(data[0].equals("SimilarArtist")){
				
				
				LastFMAPI=new LastfmAPI();
				List_LastFM=LastFMAPI.getSimilarArtist(data[1]);
				try {
					EchoAPI=new EchonestAPI();
					List_Echonests=EchoAPI.SearchArtistSimiliar(data[1]);
					Merge(List_Echonests,List_LastFM);
					

					for (ObjectInfo o:Result){
						System.out.println("Pone play!!! "+o.artist+"  "+o.album+"  "+o.cancion);
						
					}
					
				} catch (EchoNestException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			 
				
			}
			
			else if(data[0].equals("Geographic")){
				LastFMAPI=new LastfmAPI();
				LastFMAPI.getTopGeographic(data[1]);
				for (ObjectLastFM o:List_LastFM){
					ParseList(o);
					
				}
				for (ObjectInfo o:Result){
					System.out.println("Pone play!!! "+o.artist+"  "+o.album+"  "+o.cancion);
					
				}
				
			}
			
			

		    return true;
		}
		
		protected void onProgressUpdate(Integer... values) {
		}
		
		
		
		protected void onPostExecute( Boolean  response) {
		
		}	
	}

	

	


}



