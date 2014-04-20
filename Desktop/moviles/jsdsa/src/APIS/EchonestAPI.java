package APIS;

import ObjectsAPIS.ObjectEchonest;

import com.echonest.*;
import com.echonest.api.v4.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.echonest.api.v4.Track.AnalysisStatus;
import com.echonest.api.v4.util.Commander;
import com.echonest.api.v4.util.MQuery;
import com.echonest.api.v4.util.Utilities;

public class EchonestAPI {

    public EchoNestAPI en;
    private  boolean trace = false;

    public EchonestAPI() throws EchoNestException {
        en = new EchoNestAPI("EHY4JJEGIOFA1RCJP");
        //System.out.println(System.getProperty("ECHO_NEST_API_KEY"));
       en.setTraceSends(trace);
       en.setTraceRecvs(trace);
       
   
    }



    public ArrayList<ObjectEchonest> searchArtistByName(String name, int results) throws EchoNestException {
    	  ArrayList<ObjectEchonest> List_Echonest=new ArrayList<ObjectEchonest>();
    	  ArrayList<Artist> artists = (ArrayList<Artist>) en.searchArtists(name,results);
    	  for(Artist artist:artists){
    		  ObjectEchonest newEcho=new ObjectEchonest();
    		  newEcho.SetArtists(artist.getName());
    		  newEcho.SetBiography("");
    		  List_Echonest.add(newEcho);    		  
    	  }
    	  System.out.println("0000000000000000000000000000000000000"+artists.get(0).getName());
    	  System.out.println(List_Echonest.size());
    	  
    	  return List_Echonest;
    	  
    }
    
    public ArrayList<ObjectEchonest> SearchArtistSimiliar(String name){
    	Params p=new Params();
    	p.add("name",name);
    	p.add("results", 10);
    	 ArrayList<ObjectEchonest> List_Echonest=new ArrayList<ObjectEchonest>();
    	try {
			ArrayList<Artist> artists = (ArrayList<Artist>) en.getSimilarArtists(p);
			for(Artist artist:artists){
				 
	    		  ObjectEchonest newEcho=new ObjectEchonest();
	    		  newEcho.SetArtists(artist.getName());
	    		  newEcho.SetBiography("");
	    		  List_Echonest.add(newEcho); 
	    		 
	    	  }
		} catch (EchoNestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
    	System.out.println(List_Echonest.size());
    	return List_Echonest;
    	
    }
    
    // Comparar el string artista con la lista de artista para obtener la biografia
    

    public void stats() {
        en.showStats();
    }

    public ArrayList<ObjectEchonest> searchArtistBiography(String name) throws EchoNestException {
  	  int results=15;
  
  	  	
	  	  ArrayList<ObjectEchonest> List_Echonest = new ArrayList<ObjectEchonest>();
	  	  ArrayList<Artist> artists = (ArrayList<Artist>) en.searchArtists(name, results);
	  	  
	  	  for(Artist artist : artists){
	  		  
	  		  if (artist.getName().equals(name)){
	  			  //System.out.println("cosaaaaaaaa"+ artist.getImages().get(0).getURL());
		  		  ObjectEchonest newEcho = new ObjectEchonest();
		  		  newEcho.SetArtists(artist.getName());
		  		  newEcho.SetBiography(artist.getBiographies().get(7).getText());
		  		 
		  		 
		  		  List_Echonest.add(newEcho);
		  		// System.out.println("000000000    "+newEcho.getBiography());
	  		  }
	  	  }
	  	  
	  	  if(List_Echonest.size()==0){
	  		  ObjectEchonest newEcho=new ObjectEchonest();
	  		  newEcho.SetArtists(name);
	  		  newEcho.SetBiography("No se encontro ninguna biografia correspondiente a este artista");
	  		  List_Echonest.add(newEcho);
	  		 System.out.println("000000000    "+newEcho.getBiography());
	  		  
	  	  }
	  	  
	  	  
	  	  return List_Echonest;
	  
}

}
