package APIS;

import ObjectsAPIS.ObjectEchonest;


import com.echonest.api.v4.*;

import java.util.ArrayList;

public class EchonestAPI {
//Atributos de la clase echonest
    public EchoNestAPI en;
    private  boolean trace = false;

    public EchonestAPI() throws EchoNestException {
        en = new EchoNestAPI("EHY4JJEGIOFA1RCJP");
        en.setTraceSends(trace);
        en.setTraceRecvs(trace);
    }

    
    //------ Metodo de busqueda de artistas
    public ArrayList<ObjectEchonest> searchArtistByName(String name, int results) throws EchoNestException {
    	  ArrayList<ObjectEchonest> List_Echonest=new ArrayList<ObjectEchonest>();
    	  ArrayList<Artist> artists = (ArrayList<Artist>) en.searchArtists(name,results);
    	  for(Artist artist:artists){
    		  ObjectEchonest newEcho=new ObjectEchonest();
    		  newEcho.SetArtists(artist.getName());
    		  newEcho.SetBiography("");
    		  List_Echonest.add(newEcho);    		  
    	  }
    	  return List_Echonest;
    }
    
    
    //----------Metodo de busqueda de artistas similares
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
    	return List_Echonest;
    	
    }
    
// Metodo ques busca la biografia de un artista

    public ArrayList<ObjectEchonest> searchArtistBiography(String name) throws EchoNestException {
  	  int results=15;
  
  	  	
	  	  ArrayList<ObjectEchonest> List_Echonest = new ArrayList<ObjectEchonest>();
	  	  ArrayList<Artist> artists = (ArrayList<Artist>) en.searchArtists(name, results);
	  	  
	  	  for(Artist artist : artists){
	  		  
	  		  if (artist.getName().equals(name)){
		  		  ObjectEchonest newEcho = new ObjectEchonest();
		  		  newEcho.SetArtists(artist.getName());
		  		  for (Biography bio:artist.getBiographies() ){
		  			  if (bio.getText().length()>600){
		  				newEcho.SetBiography(bio.getText());
		  				  
		  			  }
		  			  
		  		  }
		  		  
		  		  List_Echonest.add(newEcho);
	  		  }
	  	  }
	  	  
	  	  if(List_Echonest.size()==0){
	  		  ObjectEchonest newEcho=new ObjectEchonest();
	  		  newEcho.SetArtists(name);
	  		  newEcho.SetBiography("No se encontro ninguna biografia correspondiente a este artista");
	  		  List_Echonest.add(newEcho);
	  	  }
	  	  return List_Echonest;
    }

}
