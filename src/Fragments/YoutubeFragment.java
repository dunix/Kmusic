/*ITCR Curso IC8041
 * Aplicaciones móviles
 * Aplicación kmusic
 * Nombre de clase: FragmentTopTracks
 * Descripción: Fragment que se encarga de mostrar el top de canciones 
 * obtenidas mediante el API de LastFm 
 * */
package Fragments;

import java.io.IOException;

import APIS.Search;

import android.os.AsyncTask;
import android.os.Bundle;



import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.OnInitializedListener;
import com.google.android.youtube.player.YouTubePlayerFragment;

import com.google.android.youtube.player.YouTubePlayer.Provider;

public class YoutubeFragment extends YouTubePlayerFragment  {
private static String Artista;


public YoutubeFragment() { }

// Metodo que inicializa la clase con youtube con el artista selecionado
public static YoutubeFragment newInstance(String url) {
	YoutubeFragment f = new YoutubeFragment();
	Bundle b = new Bundle();
    b.putString("url", url);
    
  
	Artista= url+"live";
    f.setArguments(b);
    f.init();

    return f;
}


//Metodo encargado de inicializar el video de youtube dentro del fragment con el concierto asociado a la busqueda realizada
private void init() {

    initialize("AI39si4t1TOuQ5c_TtVD1aJRrdl2qARenjJqNMpttfmVTKnGe5G2MnQu86htSHjjIvAMHVCY3R3jml3zoz0Do-Huw5iWNHP99g", new OnInitializedListener() {

        @Override
        public void onInitializationFailure(Provider arg0, YouTubeInitializationResult arg1) { 
        	
        	
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

//Metodo de clase asincronica encargada de obtener el id del video para poder reproducir el video
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