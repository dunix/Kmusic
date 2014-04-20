package com.example.kmusic;

import java.io.IOException;

import APIS.MusixMatchAPI;
import APIS.Search;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import android.widget.Toast;

//import com.facebook.widget.FacebookDialog;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.Provider;
import com.google.android.youtube.player.YouTubePlayerView;

public class YoutubeActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

	private String cancion;
	private String URL;
	private ShareActionProvider mShareActionProvider;
	String Artist;
	String Track;
	String Album;
	
	protected void onCreate(Bundle savedInstanceState) {
		
    	StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
    	StrictMode.setThreadPolicy(policy);
    	
    	Intent intent = getIntent();
    	
    	if(intent.getStringExtra("artista")!=null){
    		Artist=intent.getStringExtra("artista");
    		Track=intent.getStringExtra("cancion");
    		Album="";
    		cancion = Artist+" "+Track;
    	}
    	else{
    		Artist=intent.getStringExtra("artist");
    		Track=intent.getStringExtra("track");
    		Album=intent.getStringExtra("album");
    	cancion = Artist+" "+Album+" "+Track;
    	}
    	
    	System.out.println(cancion+"    Youtube");
    	
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.youtube_top);
                 
        YouTubePlayerView youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);
        youTubeView.initialize("AI39si4t1TOuQ5c_TtVD1aJRrdl2qARenjJqNMpttfmVTKnGe5G2MnQu86htSHjjIvAMHVCY3R3jml3zoz0Do-Huw5iWNHP99g", this);
        
        TextView title= (TextView) findViewById(R.id.titulo);
        title.setText(cancion);
        
        TextView letra= (TextView) findViewById(R.id.letra);
        Asyncrona lyrics= new Asyncrona(Artist, Track, letra);
        lyrics.execute();
    }
	
	
	  @Override
	  public void onInitializationFailure(Provider arg0,YouTubeInitializationResult arg1) {
	     Toast.makeText(this, "Error inicializando YouTube View", Toast.LENGTH_LONG).show(); 
	   
	  }
	  
	  @Override
	  public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
	    	this.getMenuInflater().inflate(R.menu.menu_superior, menu);
	    	
	    	Search s1 =new Search();
			try {
				URL="https://www.youtube.com/watch?v="+s1.busqueda(Artist+" "+Track);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	////////////////////////////////////////////////////////////// AQUIIIIIIIIIIIIIIII //////////////////
	    	MenuItem item = (MenuItem) menu.findItem(R.id.menu_share1);

	        // Fetch and store ShareActionProvider
	    	ShareActionProvider mShareActionProvider = (ShareActionProvider) item.getActionProvider();
	        
	        Intent shareIntent = new Intent(Intent.ACTION_SEND);
	        shareIntent.setAction(Intent.ACTION_SEND);
	        shareIntent.setType("text/plain");
	        shareIntent.putExtra(Intent.EXTRA_TEXT, "-KMusic App- Estoy escuchando "+ Artist + " " + Track + " " + URL + " ");

	        mShareActionProvider.setShareIntent(shareIntent);
			return true;
		}
	   
	  @Override
	  public boolean onOptionsItemSelected(MenuItem item) {
			switch (item.getItemId()) {
		        case R.id.menu_search:
		        	Intent i = new Intent(getApplicationContext(), Main.class);
	                startActivity(i);
		            return true;
		        
		        default:
		            return super.onOptionsItemSelected(item);
			}
	    
	    
	    }


	  @Override
	  public void onInitializationSuccess(Provider provider, YouTubePlayer player, boolean wasRestored){
	     	   
	    if (!wasRestored) {
	    		
	    		String nombre=Artist+" "+Album+" "+Track;
	    		try {
	    			Search s=new Search();
					player.loadVideo(s.busqueda(nombre));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	              
	            } 
	  }
	  
	  
	  
	  public class Asyncrona extends AsyncTask<String , Void, Boolean> {
			String Artist;
			String Song;
			String result;
			TextView Lyric;
			
			
			public Asyncrona(String Artista, String Cancion, TextView letra){
				Artist=Artista;
				Song=Cancion;
				Lyric=letra;
				
				
			}
			@Override
			protected Boolean doInBackground(String... params) {
				// TODO Auto-generated method stub
				MusixMatchAPI letra=new MusixMatchAPI();
				result=letra.SearchLyric(Artist, Song);
				return true;
			}
			
			protected void onPostExecute( Boolean  response) {
				//System.out.println(result);
				Lyric.setMovementMethod(new ScrollingMovementMethod());
				Lyric.setText(result);
				
				
			}
		}

}

