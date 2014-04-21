package com.example.kmusic;

import java.io.IOException;

import APIS.MusixMatchAPI;
import APIS.Search;
import Seguridad.InternetStatus;
import android.app.Activity;
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


import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.Provider;
import com.google.android.youtube.player.YouTubePlayerView;

// Activity de youtube la cual reproduce el video de una cancion con su respestiva letra
public class YoutubeActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

	String cancion="";
	String URL;
	ShareActionProvider mShareActionProvider;
	String Artist;
	String Track;
	String Album;
	Activity context;
	
	protected void onCreate(Bundle savedInstanceState) {
		
    	StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
    	StrictMode.setThreadPolicy(policy);
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube);
    	context=this;
    	if (new InternetStatus().haveNetworkConnection(this)){
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
	    	
	    	
	    	YouTubePlayerView youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);
	        youTubeView.initialize("AI39si4t1TOuQ5c_TtVD1aJRrdl2qARenjJqNMpttfmVTKnGe5G2MnQu86htSHjjIvAMHVCY3R3jml3zoz0Do-Huw5iWNHP99g", this);
	        
	        TextView title= (TextView) findViewById(R.id.titulo);
	        title.setText(cancion);
	        
	        TextView letra= (TextView) findViewById(R.id.letra);
	        Asyncrona lyrics= new Asyncrona(Artist, Track, letra);
	        lyrics.execute();
    	}
    	
    	else{
    		TextView title= (TextView) findViewById(R.id.titulo);
	        title.setText(cancion);
	        Toast.makeText(context, "Por favor revise su conexión a Internet", Toast.LENGTH_SHORT).show();
    		
    	}
    }
	
	
	  @Override
	  public void onInitializationFailure(Provider arg0,YouTubeInitializationResult arg1) {
	     Toast.makeText(this, "Error inicializando YouTube View", Toast.LENGTH_LONG).show(); 
	   
	  }
	
	  // Menu de para el envio de datos en las redes sociales
	  @Override
	  public boolean onCreateOptionsMenu(Menu menu) {
		  
		
	    	this.getMenuInflater().inflate(R.menu.menu_superior, menu);
	    	
	    	Search s1 =new Search();
			try {
				URL="https://www.youtube.com/watch?v="+s1.busqueda(Artist+" "+Track);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	
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
	  
	  // Define lo que realiza  los items del menu 
	  @Override
	  public boolean onOptionsItemSelected(MenuItem item) {
			switch (item.getItemId()) {
		        case R.id.menu_search:
		        	Intent i = new Intent(getApplicationContext(), SearchActivity.class);
	                startActivity(i);
		            return true;
		        
		        default:
		            return super.onOptionsItemSelected(item);
			}
	    
	    
	    }
	  
	  // Inicializa el reproductor de youtube con su respectivo ID
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
	  
	  // Clase asincronica encarga de realizar la consulta al api de MusicXMatch para obtener las letras de la canciones
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
				try{
					MusixMatchAPI letra=new MusixMatchAPI();
					result=letra.SearchLyric(Artist, Song);
					
					System.out.println(" Probando la letraaaaa");
					return true;
				}
				catch(OutOfMemoryError exception){
					exception.printStackTrace();
					return false;
				}
				catch(Exception e){
					e.printStackTrace();
					return false;	
					
				}
			}
			
			protected void onPostExecute( Boolean  response) {
				//System.out.println(result);
				System.out.println(response);
				if (response && result!=null){
					Lyric.setMovementMethod(new ScrollingMovementMethod());
					if(result.equals("Vacio")){
						result="No se encontro la letra";
					}
						
					
					Lyric.setText(result);
				}
				else {
					Toast.makeText(context, "No se encontro la información solicitada o no tiene acceso a internet", Toast.LENGTH_SHORT).show();
					Lyric.setText("No se encontro la letra");
				}
				
				
			}
		}

}

