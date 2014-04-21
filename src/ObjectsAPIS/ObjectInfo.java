package ObjectsAPIS;
//Clase objeto del info utilizado para los adaptadores
public class ObjectInfo {
	public String artist="";
	public String album="";
	public String cancion="";
	
	/*
	 * Metodos gets de los atributos
	 */
	public String getCancion(){
		
		return cancion;
		}

		public String getArtista(){
			
		return artist;
		}

		

		public String getAlbum(){
			
		return album;
		}

		
		/*
		 * Metodos sets de los atributos
		 */
		
		public void setArtista(String new_artista){
			
			artist=new_artista;
		}

	
		public void setAlbum(String new_album){
			
			album=new_album;
		}
		public void setCancion(String new_cancion){
			
			cancion=new_cancion;
		}

		
	

}
