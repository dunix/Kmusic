package Fragments;

import java.io.IOException;
import APIS.MusixMatchAPI;
import APIS.Search;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kmusic.R;
import com.example.kmusic.YoutubeActivity.Asyncrona;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.OnInitializedListener;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.android.youtube.player.YouTubePlayer.Provider;

public class YoutubeFragment extends YouTubePlayerFragment  {
private static String Artista;


public YoutubeFragment() { }

public static YoutubeFragment newInstance(String url) {

	YoutubeFragment f = new YoutubeFragment();

    Bundle b = new Bundle();
    b.putString("url", url);
    
  
	Artista= url+"live";
    f.setArguments(b);
    f.init();

    return f;
}

private void init() {

    initialize("AI39si4t1TOuQ5c_TtVD1aJRrdl2qARenjJqNMpttfmVTKnGe5G2MnQu86htSHjjIvAMHVCY3R3jml3zoz0Do-Huw5iWNHP99g", new OnInitializedListener() {

        @Override
        public void onInitializationFailure(Provider arg0, YouTubeInitializationResult arg1) { 
        	
        	System.out.println("xqqqqqq????");
        }

        @Override
        public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {
            if (!wasRestored) { 
            	YoutubeAsyn newAsyn=new YoutubeAsyn(player);
            	newAsyn.execute(Artista);
                
				
            }
        }
    });
}

public class YoutubeAsyn extends AsyncTask<String , Void, Boolean> {
	YouTubePlayer player;
	String idVideo;
	
	public YoutubeAsyn(YouTubePlayer newplayer){
		player=newplayer;
	}
	
	@Override
	protected Boolean doInBackground(String... data) {
		// TODO Auto-generated method stub
		Search busquedakey=new Search();
		try {
			idVideo=busquedakey.busqueda(data[0]);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
	
	protected void onPostExecute( Boolean  response) {
		
		player.cueVideo(idVideo);
		
	}
}




}