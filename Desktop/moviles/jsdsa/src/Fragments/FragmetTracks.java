package Fragments;

import java.util.ArrayList;

import com.example.kmusic.R;

import APIS.LastfmAPI;
import Fragments.FragmentAlbum.SearchAsyncAlbums;
import ObjectsAPIS.ObjectLastFM;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmetTracks extends Fragment {

	public View onCreateView(LayoutInflater inflater,ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_tracks, container, false);
		SearchAsyncTracks AsynTracks=new SearchAsyncTracks();
		AsynTracks.execute("Coldplay","Parachutes");
         return v;
	}
	
	
	
	
	 class SearchAsyncTracks extends AsyncTask<String, Void, Boolean> {
			LastfmAPI LastFMAPI;

			public  SearchAsyncTracks(){
			}
			
			protected void onPreExecute() {
			}
			
			@Override
			protected Boolean doInBackground(String... data) {
				LastFMAPI=new LastfmAPI();
				ArrayList<ObjectLastFM> List_LastFM;
				List_LastFM=LastFMAPI.getTracks(data[0],data[1]);	
							
			    return true;
			}
			protected void onProgressUpdate(Integer... values) {
			}
			
			
			
			protected void onPostExecute( Boolean  response) {
				
				
			}	
		}	

}
