package ObjectsAPIS;

import java.net.URL;

public class ObjectLastFM {
	
	private String cancion="";
	private String artista="";
	private String genero="";
	private String album="";
	private Object other="";
	private String imagen="";
	public ObjectLastFM(){}
	
	//-----------------Metodos gets------------------------------
	
	public String getCancion(){
		
	return cancion;
	}

	public String getArtista(){
		
	return artista;
	}

	public String getGenero(){
		
	return genero;
	}

	public String getAlbum(){
		
	return album;
	}

	public Object getOther(){
		
	return other;
	}
	
	public String getImage(){
		
		return imagen;
		}

	//-----------------Metodos Sets------------------------------
	
	public void setArtista(String new_artista){
		
		artista=new_artista;
	}

	public void setGenero(String new_genero){
		
		genero=new_genero;
	}

	public void setAlbum(String new_album){
		
		album=new_album;
	}
	public void setCancion(String new_cancion){
		
		cancion=new_cancion;
	}

	public void setOther(Object new_other){
		
		other=new_other;
	}

	public void setImagen(String url ){
		
		imagen=url;
	}
}
