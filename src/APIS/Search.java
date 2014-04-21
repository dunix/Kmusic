package APIS;


import java.io.IOException;

import java.util.Iterator;
import java.util.List;


import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.ResourceId;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;

// Clase que busca el identificador de un video en youtube
public class Search {

	// Atributos de conexion y manejo de datos

	  /** Global instance of the HTTP transport. */
	  private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();

	  /** Global instance of the JSON factory. */
	  private static final JsonFactory JSON_FACTORY = new JacksonFactory();

	  /** Global instance of the max number of videos we want returned (50 = upper limit per page). */
	  private static final long NUMBER_OF_VIDEOS_RETURNED = 2;

	  /** Global instance of Youtube object to make all API requests. */
	  private static YouTube youtube;
	  
	  // Metodo que recibe el nombre de la cancion que desea buscar y devuelve el identificador de la misma
	  
	  public String busqueda(String param) throws IOException{
		  
	  youtube = new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, new HttpRequestInitializer() {
	        public void initialize(HttpRequest request) throws IOException {}
	      }).setApplicationName("youtube-cmdline-search-sample").build();

	      // Get query term from user.
	      String queryTerm = param;

	      YouTube.Search.List search = youtube.search().list("id,snippet");
	      /*
	       * It is important to set your developer key from the Google Developer Console for
	       * non-authenticated requests (found under the API Access tab at this link:
	       * code.google.com/apis/). This is good practice and increased your quota.
	       */
	      String apiKey = "AIzaSyCLcN-N5yUy1anSdO4aDptzfg90a7Pxtrk";
	      search.setKey(apiKey);
	      search.setQ(queryTerm);
	      /*
	       * We are only searching for videos (not playlists or channels). If we were searching for
	       * more, we would add them as a string like this: "video,playlist,channel".
	       */
	      search.setType("video");
	      /*
	       * This method reduces the info returned to only the fields we need and makes calls more
	       * efficient.
	       */
	      search.setFields("items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url)");
	      search.setMaxResults(NUMBER_OF_VIDEOS_RETURNED);
	      SearchListResponse searchResponse = search.execute();

	      List<SearchResult> searchResultList = searchResponse.getItems();

	      if (searchResultList != null) {
	       return prettyPrint(searchResultList.iterator(), queryTerm);
	        
	     
	      }
	      return queryTerm;
	    
	  }

	 
	  private static String prettyPrint(Iterator<SearchResult> iteratorSearchResults, String query) {

	    

	    if (!iteratorSearchResults.hasNext()) {
	      System.out.println(" There aren't any results for your query.");
	      return "";
	    }
	    SearchResult singleVideo = iteratorSearchResults.next();
	    ResourceId rId = singleVideo.getId();
	

	      

	      // Double checks the kind is video.
	      if (rId.getKind().equals("youtube#video")) {
	      

	        return rId.getVideoId().toString();
	        //System.out.println(" Title: " + singleVideo.getSnippet().getTitle());
	        //System.out.println(" Thumbnail: " + thumbnail.getUrl());
	        //System.out.println("\n-------------------------------------------------------------\n");
	      }
		return query;
	    
	    
	 
	  }
}
