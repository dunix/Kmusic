package APIS;
import java.util.ArrayList;

import org.json.JSONException;

import ObjectsAPIS.ObjectMusicXmatch;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class MusixMatchAPI {
	
	private ArrayList<ObjectMusicXmatch> List_musicX=new ArrayList<ObjectMusicXmatch>();
	
	public String SearchLyric(String Artist,String Track){
		String lyric=new String();
			HttpResponse<JsonNode> request = null;
			
		try {			
			request = Unirest.get("https://musixmatchcom-musixmatch.p.mashape.com/wsr/1.1/matcher.lyrics.get?q_track="+Track+"&q_artist="+Artist)
					  .header("X-Mashape-Authorization", "eyYErFoYcZbfabUORW6w1It9UMhqFnGK")
					  .asJson();
			lyric=request.getBody().getObject().getString("lyrics_body");
		} catch (Exception e) {
			// TODO Auto-generated catch block.z4
			lyric="Vacio";
			e.printStackTrace();
		}
		return lyric;
	}
	
public ArrayList<ObjectMusicXmatch> SearchTrack(String Track) {
		
		HttpResponse<JsonNode> request = null;
		try {
			int cont=0;
			
			request = Unirest.get("https://musixmatchcom-musixmatch.p.mashape.com/wsr/1.1/track.search?q_track="+Track+"&f_has_lyrics=1&s_track_rating=desc&page=1&page_size=20")
					  .header("X-Mashape-Authorization", "eyYErFoYcZbfabUORW6w1It9UMhqFnGK")
					  .asJson();
		    	try {
		    		
		    		
					while(cont!=10 && cont!=request.getBody().getArray().length())
					{
						
					ObjectMusicXmatch newObjectMusicXmatch = new ObjectMusicXmatch();
					newObjectMusicXmatch.nombre_cancion=request.getBody().getArray().getJSONObject(cont).get("track_name").toString();
					newObjectMusicXmatch.nombre_album=request.getBody().getArray().getJSONObject(cont).get("album_name").toString();
					newObjectMusicXmatch.nombre_artista=request.getBody().getArray().getJSONObject(cont).get("artist_name").toString();
					List_musicX.add(newObjectMusicXmatch);
					cont++;
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
				}	
		    
		} catch (UnirestException e) {
			// TODO Auto-generated catch block.z
			//e.printStackTrace();
		}
		
		return List_musicX;
		
	}
	


public ArrayList<ObjectMusicXmatch> SearchArtistTrack(String Artist,String Track) {
	
	HttpResponse<JsonNode> request = null;
	try {
		int cont=0;
		
		request = Unirest.get("https://musixmatchcom-musixmatch.p.mashape.com/wsr/1.1/track.search?q_track="+Track+"&q_artist="+Artist+"+&f_has_lyrics=1&s_track_rating=desc&page=1&page_size=20")
				  .header("X-Mashape-Authorization", "eyYErFoYcZbfabUORW6w1It9UMhqFnGK")
				  .asJson();
	    	try {
	    		
	    		
				while(cont!=10 && cont!=request.getBody().getArray().length())
				{
					
					ObjectMusicXmatch newObjectMusicXmatch = new ObjectMusicXmatch();
					newObjectMusicXmatch.nombre_cancion=request.getBody().getArray().getJSONObject(cont).get("track_name").toString();
					newObjectMusicXmatch.nombre_album=request.getBody().getArray().getJSONObject(cont).get("album_name").toString();
					newObjectMusicXmatch.nombre_artista=request.getBody().getArray().getJSONObject(cont).get("artist_name").toString();
					List_musicX.add(newObjectMusicXmatch);
					cont++;
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}	
	    
	} catch (UnirestException e) {
		// TODO Auto-generated catch block.z
		//e.printStackTrace();
	}
	
	return List_musicX;
	
}

}
