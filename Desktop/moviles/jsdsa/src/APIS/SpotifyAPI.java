package APIS;

import java.util.ArrayList;

import org.json.JSONException;

import ObjectsAPIS.ObjectSpotify;
import android.os.AsyncTask;
import android.widget.TextView;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class SpotifyAPI  {
	private ArrayList<ObjectSpotify> List_spotify=new ArrayList<ObjectSpotify>();
	
	public ArrayList<ObjectSpotify> SearchTrack(String Track) {
		
		HttpResponse<JsonNode> request = null;
		try {
			int cont=0;
			
			request = Unirest.get("https://mager-spotify-web.p.mashape.com/search/1/track.json?q="+Track)
					  .header("X-Mashape-Authorization", "eyYErFoYcZbfabUORW6w1It9UMhqFnGK")
					  .asJson();
		    	try {
					while(cont!=20 && cont!=Integer.parseInt(request.getBody().getObject().getJSONObject("info").get("num_results").toString()))
					{
					ObjectSpotify newObjectSpotify=new ObjectSpotify();
					newObjectSpotify.nombre_album=request.getBody().getObject().getJSONArray("tracks").getJSONObject(cont).getJSONObject("album").get("name").toString();
					newObjectSpotify.nombre_artista=request.getBody().getObject().getJSONArray("tracks").getJSONObject(cont).getJSONArray("artists").getJSONObject(0).get("name").toString();
					newObjectSpotify.nombre_cancion=request.getBody().getObject().getJSONArray("tracks").getJSONObject(cont).get("name").toString();
				//	System.out.println("concha sera este_ "+ request.getBody().getObject().getJSONArray("tracks").getJSONObject(cont).get("name").toString());
					
					List_spotify.add(newObjectSpotify);
					cont++;
					
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
				}	
		    
		} catch (UnirestException e) {
			// TODO Auto-generated catch block.z
			//e.printStackTrace();
		}
		
		return List_spotify;
		
	}
	
	
	
}

	
