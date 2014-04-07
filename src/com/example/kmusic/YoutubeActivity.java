package com.example.kmusic;

import APIS.Search;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.Provider;
import com.google.android.youtube.player.YouTubePlayerView;

public class YoutubeActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

	String cancion;
	
	protected void onCreate(Bundle savedInstanceState) {
    	StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
    	StrictMode.setThreadPolicy(policy);
    	
    	Intent intent = getIntent();
    	cancion = intent.getStringExtra("cancion");
    	
    	
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.youtube_top);
                 
        YouTubePlayerView youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);
        youTubeView.initialize("AI39si4t1TOuQ5c_TtVD1aJRrdl2qARenjJqNMpttfmVTKnGe5G2MnQu86htSHjjIvAMHVCY3R3jml3zoz0Do-Huw5iWNHP99g", this);
		 
       // Log.w("MainActivity", "This is a warning");
        
        
    }
	  @Override
	  public void onInitializationFailure(Provider arg0,YouTubeInitializationResult arg1) {
	     Toast.makeText(this, "Error inicializando YouTube View", Toast.LENGTH_LONG).show(); 
	   
	  }

	  @Override
	  public void onInitializationSuccess(Provider provider, YouTubePlayer player, boolean wasRestored){
	     	   
	    if (!wasRestored) {
	    		
	    		Intent i=getIntent();
	    		String nombre=i.getStringExtra("cancion");
	    		try {
	    			Search s=new Search();
					player.loadVideo(s.busqueda(nombre));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	              //player.loadVideo("KPPoEPAbC5s");
	              
	            } 
	  }

}

