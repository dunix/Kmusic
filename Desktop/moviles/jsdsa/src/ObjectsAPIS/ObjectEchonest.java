package ObjectsAPIS;

public class ObjectEchonest {
	String Artists="";
	String Biography="";
	String Imagen="";
	
	
	public void SetArtists(String artista){
		
		Artists=artista;
	}
	public void SetBiography(String bio){
		
		Biography=bio;
	}
	public void SetImagen(String image){
		
		Imagen=image;
	}
	
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
