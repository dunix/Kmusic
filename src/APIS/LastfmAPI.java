package APIS;

import java.util.ArrayList;
import java.util.Collection;

import de.umass.lastfm.Album;
import de.umass.lastfm.Caller;
import de.umass.lastfm.Chart;
import de.umass.lastfm.Geo;
import de.umass.lastfm.ImageSize;
import de.umass.lastfm.PaginatedResult;
import de.umass.lastfm.Tag;
import de.umass.lastfm.Track;
import ObjectsAPIS.ObjectLastFM;
import de.umass.lastfm.Artist;

public class LastfmAPI {
	
	//Atributos de las clases
	
	private ArrayList<ObjectLastFM> ListFM=new ArrayList<ObjectLastFM>();
	private static final String key = "2e321ea1e5efd535b9a261c211cefe78";
	
//--------------------------------------------------------Constructor de la clase--------------------------------------------------------------	
	
	public LastfmAPI(){
		//Caller.getInstance().setCache(null);
		
	}
	
	
//---------------------------------------------------- Metodo que busca la Imagen de un artista-------------------------------------------------
	
	public String getImagen(String Artista){
		String Imagen="";
		Artist artista=Artist.getInfo(Artista, key);
		Imagen=artista.getImageURL(ImageSize.EXTRALARGE);
		if(Imagen!=null){
			return Imagen;
		}
		return Imagen;
		
	}
	
//-----------------------------------------------------Metodo que imagen con distinta dimension--------------------------------------------------	
	public String getImagen2(String Artista){
		String Imagen="";
		Artist artista=Artist.getInfo(Artista, key);
		Imagen=artista.getImageURL(ImageSize.MEDIUM);
		if(Imagen!=null){
			return Imagen;
		}
		return Imagen;
		
	}
	
//---------------------------------------------------------Metodo con sobrecarga para imagenes del albums------------------------------------------
	public String getImagen(String Artista,String album){
		String Imagen="";
		Album artista=Album.getInfo(Artista, album, key);
		Imagen=artista.getImageURL(ImageSize.MEDIUM);
		if(Imagen!=null){
			return Imagen;
		}
		return Imagen;
		
	}
//--------------------------Metodo de busqueda por top de canciones-----------------------------------------	
	public ArrayList<ObjectLastFM> getTopTracks(){
		PaginatedResult<Track> response = Chart.getTopTracks(1, key);
		Collection<Track> TopTracks = response.getPageResults();
		 
	     for (Track track : TopTracks) {
	    	 ObjectLastFM objectfm=new ObjectLastFM();
	    	 objectfm.setCancion(track.getName());
	    	 objectfm.setArtista(track.getArtist());
	    	 if(track.getImageURL(ImageSize.MEDIUM)!=null){
	    		 objectfm.setImagen(track.getImageURL(ImageSize.MEDIUM));
	    		 //System.out.println(objectfm.getImage());
	    	 }
	    	 ListFM.add(objectfm);
	    	 
	       }
		return ListFM;
	}
//--------------------------Metodo de busqueda por top de artistas-----------------------------------------
	public ArrayList<ObjectLastFM> getTopArtists(){
		PaginatedResult<Artist> response = Chart.getTopArtists(1, key);
		Collection<Artist> TopArtists = response.getPageResults();
		 
	     for (Artist artista : TopArtists) {
	 
	    	 ObjectLastFM objectfm=new ObjectLastFM();
	    	 objectfm.setArtista(artista.getName());
	    	 ListFM.add(objectfm);
	     }
		return ListFM;
	}
//--------------------------Metodo de busqueda por Artista y cancion-----------------------------------------
	public ArrayList<ObjectLastFM> getArtistsTracks(String artista, String cancion){
	
		Track response = Track.getInfo(artista, cancion, key);
		System.out.println(response.getWikiText());
		ObjectLastFM newObjectLastFM= new ObjectLastFM();
		newObjectLastFM.setCancion(response.getName());
		newObjectLastFM.setAlbum(response.getAlbum());
		newObjectLastFM.setArtista(response.getArtist());
		ListFM.add(newObjectLastFM);
		
		
		return ListFM;
	}
	
//--------------------------Metodo de busqueda por Artista y album-----------------------------------------
	public ArrayList<ObjectLastFM> getArtistsAlbum(String artista, String album){
		Album response = Album.getInfo(artista, album, key);
		//System.out.println(response.getTracks().size());
		for(Track song:response.getTracks()){
			ObjectLastFM newObjectLastFM= new ObjectLastFM();
			newObjectLastFM.setCancion(song.getName());
			newObjectLastFM.setAlbum(album);
			newObjectLastFM.setArtista(song.getArtist());
			ListFM.add(newObjectLastFM);
		}
		
		System.out.println(ListFM.size());
		return ListFM;
	}
	
//--------------------------Metodo de busqueda por genero artistas-----------------------------------------	
	public ArrayList<ObjectLastFM> getTopGenreArtist(String genero){
		
		Collection<Artist> tags=Tag.getTopArtists(genero,key);
		//System.out.println(response.getTracks().size());
		for(Artist artist:tags){
			ObjectLastFM newObjectLastFM= new ObjectLastFM();
			newObjectLastFM.setArtista(artist.getName());
			newObjectLastFM.setImagen(getImagen2(artist.getName()));
			ListFM.add(newObjectLastFM);
		}
	
		return ListFM;
	}

//--------------------------Metodo de busqueda por pais artistas-----------------------------------------	
	public ArrayList<ObjectLastFM> getTopGeographic(String pais){
		Collection<Artist> GeoArtists=Geo.getTopArtists(pais, key);
		for(Artist artist:GeoArtists){
		
			ObjectLastFM newObjectLastFM= new ObjectLastFM();
			newObjectLastFM.setArtista(artist.getName());
			newObjectLastFM.setImagen(getImagen2(artist.getName()));
			ListFM.add(newObjectLastFM);
			
		}
		
		
		return ListFM;
	}
	

	//--------------------------Metodo de busqueda por  artistas similares-----------------------------------------		
	public ArrayList<ObjectLastFM> getSimilarArtist(String artista){
		int limit=15;
		Collection<Artist> SimilarArtists=Artist.getSimilar(artista, limit, key);
		for(Artist artist:SimilarArtists){
			ObjectLastFM newObjectLastFM= new ObjectLastFM();
			newObjectLastFM.setArtista(artist.getName());
			newObjectLastFM.setImagen(getImagen2(artist.getName()));
			ListFM.add(newObjectLastFM);
		}
	
		return ListFM;
	}
	
	//-------------Busqueda de Albums de un artista-----------------------------------------------------------

	
	public ArrayList<ObjectLastFM> getAlbums(String nameArtist){
		
		
		Collection<Album> Albums=Artist.getTopAlbums(nameArtist, key);
		for(Album newAlbum:Albums){
			ObjectLastFM newObjectLastFM= new ObjectLastFM();
			newObjectLastFM.setAlbum(newAlbum.getName());
			if(newAlbum.getImageURL(ImageSize.MEDIUM)!=null){
				newObjectLastFM.setImagen(newAlbum.getImageURL(ImageSize.MEDIUM));
				System.out.println(newAlbum.getImageURL(ImageSize.MEDIUM));
			}
			ListFM.add(newObjectLastFM);
			
		}
		return ListFM;
	}
	
	
	
	//----------------------------------------- Metodo de busqueda de canciones--------------------------------------------
	public  ArrayList<ObjectLastFM> getTracks(String nameArtist,String nameAlbum ){
		Album newAlbum=Album.getInfo(nameArtist, nameAlbum, key);
		
		//Track.g
		for(Track newTrack:newAlbum.getTracks()){
			ObjectLastFM newObjectLastFM= new ObjectLastFM();
			newObjectLastFM.setCancion(newTrack.getName());
			ListFM.add(newObjectLastFM);
		
		
	}
		return ListFM;
	}

//------------------------------------ M�todo de biograf�a--------------------------------------------
	
	public  ArrayList<ObjectLastFM> geBiography(String name ){
		
		Artist artist = Artist.getInfo(name, key);
		ObjectLastFM newObjectLastFM= new ObjectLastFM();
		String bio = artist.getWikiText();
		newObjectLastFM.setOther(bio);
		newObjectLastFM.setImagen(getImagen(name));
		ListFM.add(newObjectLastFM);
		
		return ListFM;
	}
	
	
	
//--------------------------------------Metodo para la busqueda de canciones-------------------------------------------------------
	public ArrayList<ObjectLastFM> searchTracks(String Song){
		Collection<Track> track= Track.search(Song,key);
		int cont=0;

		for(Track newTrack:track){
			if (cont==15) break;
			ObjectLastFM newObjectLastFM= new ObjectLastFM();
			newObjectLastFM.setArtista(newTrack.getArtist());
			newObjectLastFM.setCancion(newTrack.getName());
			Track a1= Track.getInfo(newObjectLastFM.getArtista(), newTrack.getName(), key);
			newObjectLastFM.setAlbum(a1.getAlbum());
			ListFM.add(newObjectLastFM);
			
			cont++;
	
		
	}

		return ListFM;
		
	}
	
}
