package com.example.kmusic;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

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
	InputStream salidaXMLLyrics = null;
	TextView letra;
	String lyrics;
	protected void onCreate(Bundle savedInstanceState) {
		
    	StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
    	StrictMode.setThreadPolicy(policy);
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube);
    	context=this;
    	if (new InternetStatus().haveNetworkConnection(this)){
	    	Intent intent = getIntent();
	    	Artist=intent.getStringExtra("artist");
	    	Track=intent.getStringExtra("track");
	    	Album=intent.getStringExtra("album");
	    	cancion = Artist+" "+Album+" "+Track;
	    	try{	    	
		    	YouTubePlayerView youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);
		        youTubeView.initialize("AI39si4t1TOuQ5c_TtVD1aJRrdl2qARenjJqNMpttfmVTKnGe5G2MnQu86htSHjjIvAMHVCY3R3jml3zoz0Do-Huw5iWNHP99g", this);
		        
		        TextView title= (TextView) findViewById(R.id.titulo);
		        title.setText(cancion);
		        
		        letra= (TextView) findViewById(R.id.letra);
		        
		        MyTaskObtenerLyric lyric = new MyTaskObtenerLyric();
		        lyric.execute();
		        /*Asyncrona lyrics= new Asyncrona(Artist, Track, letra);
		        lyrics.execute();*/
	    	}
	    	catch(Exception e){
	    		
	    		
	    	}
    	}
    	
    	else{
    		TextView title= (TextView) findViewById(R.id.titulo);
	        title.setText(cancion);
	        Toast.makeText(context, "Por favor revise su conexi髇 a Internet", Toast.LENGTH_SHORT).show();
    		
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

	  
	  	private String XMLObtenerLyric(){
			DocumentBuilderFactory factory = 
					DocumentBuilderFactory.newInstance();

			try{
				DocumentBuilder builder = factory.newDocumentBuilder();
				//Lectura completa del XML
				Document dom = builder.parse(salidaXMLLyrics);
				//Obtener nodo root
				Element root = dom.getDocumentElement();


				NodeList items = root.
						getElementsByTagName("Lyric");

				Node item = items.item(0);
				lyrics =  item.getTextContent();

				return lyrics;

			}
			//Error en el XML
			catch (Exception ex) 
			{
				/* No se obtiene la letra
				Actualizar GUI con el valor del atributo lyrics
				que se setea en la siguiente linea*/
				lyrics = "No hay letra";  
				System.out.println("---No hay letra---"); 
				return lyrics;
				//throw new RuntimeException(ex);

			} 		


		}

		private class MyTaskObtenerLyric extends AsyncTask<Void, Void, Boolean> {

			@Override
			protected void onPreExecute() {

			}

			@Override
			protected Boolean doInBackground(Void... arg0) {

				try{
					URL link ;
					link = new URL("http://api.chartlyrics.com/apiv1."
							+ "asmx/SearchLyricDirect?"
							+ "artist=" + Artist
							+ "&"
							+ "song=" + Track
							);

					//Flujo de datos del API
					salidaXMLLyrics = link.openConnection().
							getInputStream();
					// Revisi贸n del resultado obtenido
					if (salidaXMLLyrics != null){ // Obtuv贸 datos
						System.out.println("Se conecto");
						return true;

					}else{ // Fallo obteniendo datos
						System.out.println("Contenido nulo");
						return false;
					}
				}catch(Exception e){ //Error de conexi贸n
					System.out.println("No se conecto");
					return false;
				}
			}


			@Override
			protected void onPostExecute(Boolean valor) {
				// La informaci贸n fue obtenida con 茅xito
				if (valor){ 
					/* caso de 茅xito
								//Se procede a procesar el documento 
								// XML consumido del API
								// llamada al m茅todo parser */
					letra.setText("\n"+ XMLObtenerLyric() +"\n");
					letra.setMovementMethod(new ScrollingMovementMethod());
				}
				else{ 
					/* No se obtiene la letra
								Actualizar GUI con el valor del atributo lyrics
								que se setea en la siguiente linea*/
					lyrics = "No hay letra";  
					letra.setText(lyrics);
					System.out.println("---No hay letra---");
				}


			}

		}			

}

