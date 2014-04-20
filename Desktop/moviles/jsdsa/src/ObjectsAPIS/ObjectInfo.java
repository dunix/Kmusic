package ObjectsAPIS;

public class ObjectInfo {
	public String artist="";
	public String album="";
	public String cancion="";
	
	public String getCancion(){
		
		return cancion;
		}

		public String getArtista(){
			
		return artist;
		}

		

		public String getAlbum(){
			
		return album;
		}

		
		//-----------------Metodos Sets------------------------------
		
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
