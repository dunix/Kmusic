package ObjectsAPIS;

// Clase objeto del API Echonest
public class ObjectEchonest {
	
	//Atributos del objeto
	
	String Artists="";
	String Biography="";
	String Imagen="";
	
/*
 * Metodos Sets de los atributos
 */
	
	public void SetArtists(String artista){
		
		Artists=artista;
	}
	public void SetBiography(String bio){
		
		Biography=bio;
	}
	public void SetImagen(String image){
		
		Imagen=image;
	}
	
	/*
	 * Metodos gets de los atributos
	 */
	
	public String getArtist(){
		
		return Artists;
		
	}
	
	public String getImagen(){
		
		return Imagen;
		
	}
	public String getBiography(){
		
		return Biography;
		
	}
	
	

	
	
	
	

}
