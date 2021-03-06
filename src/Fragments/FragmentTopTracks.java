/*ITCR Curso IC8041
 * Aplicaciones móviles
 * Aplicación kmusic
 * Nombre de clase: FragmentTopTracks
 * Descripción: Fragment que se encarga de mostrar el top de canciones 
 * obtenidas mediante el API de LastFm 
 * */
package Fragments;



import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import com.example.kmusic.R;
import com.example.kmusic.YoutubeActivity;
//import com.facebook.UiLifecycleHelper;
//import com.facebook.widget.FacebookDialog;
import APIS.LastfmAPI;
import APIS.Search;
import ObjectsAPIS.ObjectLastFM;
import Seguridad.InternetStatus;
import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import de.umass.lastfm.Caller;

//Fragement encargado de consultar y visualiza el top de canciones seg�n LastFM

public class FragmentTopTracks  extends Fragment{
	//Atributos de clase
	
	private ListView lstListado;
	public  ArrayList<ObjectLastFM> lista_result_Fm = new ArrayList<ObjectLastFM>();
	public Fragment fragment;
	private static ProgressDialog pDialog;
	private InternetStatus Internet_Status=new InternetStatus();

	
		
	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
		
		pDialog = new ProgressDialog(this.getActivity());
    	pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    	pDialog.setMessage("Procesando...");
    	pDialog.setCancelable(true);
    	pDialog.setMax(100);
    	
    	
    	if (Internet_Status.haveNetworkConnection(getActivity())){
			LastAsync a = new LastAsync(this.getActivity(), pDialog);
	    	a.execute();
	    	
    	}
    	
    	else{
    		System.out.println("No hay internet");
    		Toast.makeText(getActivity(), "Por favor revisa tu conexi�n a Internet", Toast.LENGTH_SHORT).show();
    		pDialog.dismiss();
    	}
    	
		return inflater.inflate(R.layout.fragment_top_last_fm, container, false);
	}

	
	@Override
	public void onActivityCreated(Bundle state) {
		super.onActivityCreated(state);
		fragment = this;
		
	}
	
	
	public boolean onCreateOptionsMenu(Menu menu) {
		
		this.getActivity().getMenuInflater().inflate(R.menu.listing, menu);
		return true;
	}
	
	
	
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
	
// Adaptador custom encargado de llenar el listview con los tops de canciones seg�n lastfm	
	class Adaptador extends ArrayAdapter<ObjectLastFM>{
		Activity context;
		
		Adaptador(Fragment context){
			super(context.getActivity(), R.layout.componenteslist_viewtop_canciones, lista_result_Fm);
			this.context = context.getActivity();
			
			
		}
		
		public View getView(int position,View convertView, ViewGroup parent){
				
			
				LayoutInflater inflater = context.getLayoutInflater();
				View item = inflater.inflate(R.layout.componenteslist_viewtop_canciones, null);
				
				TextView LblArtistaTop  = (TextView)item.findViewById(R.id.LblArtistaTop);
				LblArtistaTop.setText(lista_result_Fm.get(position).getArtista());
				
			
				
				TextView LblCancionTop  = (TextView)item.findViewById(R.id.LblCancionTop);
				LblCancionTop.setText(lista_result_Fm.get(position).getCancion());
				
			
				
				ImageView imagen  = (ImageView)item.findViewById(R.id.imagenbio);
				AsyncronaSetImage image=new AsyncronaSetImage(lista_result_Fm.get(position).getImage(),imagen);
				image.execute();
								
				return (item);
				
		}
	
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//Clase asincronica para poder realizar el set de imagenes en el listview
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
				
	public class AsyncronaSetImage extends AsyncTask<String , Void, Boolean> {
		String imagen;
		ImageView imag;
		private Bitmap bitmap;
		
		public AsyncronaSetImage(String ima, ImageView i){
			imagen = ima;
			imag=i;
			
		}
		
		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub

	
			try {
				bitmap=getBitmapImagen(imagen);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			return true;
		}
		
		protected void onPostExecute( Boolean  response) {
			imag.setImageBitmap(bitmap);
		}
		
		public Bitmap getBitmapImagen(String imagen){
			// Realiza el set de la imagen correspondiente a listview de top tracks
			try{
				
				if (imagen!=""){
					URL url = new URL(imagen);
					HttpURLConnection connection= (HttpURLConnection) url.openConnection();
					connection.setDoInput(true);
					connection.connect();
					InputStream input =connection.getInputStream();
					Bitmap bitM= BitmapFactory.decodeStream(input);
					return bitM;
				}
				
				URL url = new URL("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSy5__ZrEZMLWSnGvyQPZDHBiJH8WzL-qIIvq8kIdJqBL2IVhThYQ");
				HttpURLConnection connection= (HttpURLConnection) url.openConnection();
				connection.setDoInput(true);
				connection.connect();
				InputStream input =connection.getInputStream();
				Bitmap bitM= BitmapFactory.decodeStream(input);
				return bitM;
				
			}
			catch(Exception e){
				System.out.println("imagen vacia     "+ imagen);
				e.printStackTrace();
				
				
			}
			return null;
		}
		}
	}	

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// Clase asincronica encargada de realizar la consulta al API de LastFM para obtener el top de canciones
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public class LastAsync extends AsyncTask<Void, Void, Boolean> {
		Context context;
		
		private ProgressDialog pDialog;
		
		public LastAsync(){
		}
		
		public LastAsync(Context context_Activity, ProgressDialog new_pDialog){
			pDialog=new_pDialog;
			context=context_Activity;
		}
		
		protected void onPreExecute() {
			pDialog.setProgress(0);
			pDialog.show();
		}
		
		@Override
		protected Boolean doInBackground(Void... arg0) {
			try{
				LastfmAPI LastFMAPI=new LastfmAPI();
				Caller.getInstance().setCache(null);
				lista_result_Fm=LastFMAPI.getTopTracks();
				return true;
			}
			catch(Exception e){
				e.printStackTrace();
				return false;	
			}
			
		}
		
		protected void onProgressUpdate(Integer... values) {
			
			int progreso = values[0].intValue();
			
			pDialog.setProgress(progreso);
		}
		
		protected void onPostExecute( Boolean  response) {
			if(response){
				lstListado = (ListView)getView().findViewById(R.id.Lst_canciones_top);
				registerForContextMenu(lstListado);
				lstListado.setAdapter(new Adaptador(fragment));
				lstListado.setOnItemClickListener(new OnItemClickListener() {
		            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
		            	
		            	String selectedArtist =((TextView)v.findViewById(R.id.LblArtistaTop)).getText().toString();
		            	String selectedTrack =((TextView)v.findViewById(R.id.LblCancionTop)).getText().toString();
		            	
		            	Intent i = new Intent(fragment.getActivity(), YoutubeActivity.class);
		        		i.putExtra("artist", selectedArtist);
		        		i.putExtra("track", selectedTrack);
		        		i.putExtra("album", "");
		        		//required to launch from non-activity
		        		startActivity(i);
		            	
		            }
		        });
				pDialog.dismiss();
				}
			else{
				pDialog.dismiss();
				Toast.makeText(getActivity(), "No se encontro la informaci�n solicitada o no tiene acceso a internet", Toast.LENGTH_SHORT).show();
			}
		}
		
		
	}	
	
////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// Clase asincronica para realizar la publicaci�n de facebook con el video de youtube
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public class Asyncrona extends AsyncTask<String , Void, Boolean> {
		String artist;
		String song;
		String Url;
		
		public Asyncrona(String Artista, String Cancion){
			artist=Artista;
			song=Cancion;
			
		}
		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			Search s=new Search();
	
			try {
				Url="https://www.youtube.com/watch?v="+s.busqueda(artist+" "+song);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			return true;
		}
		
		protected void onPostExecute( Boolean  response) {
	/*		FacebookDialog shareDialog = new FacebookDialog.ShareDialogBuilder(fragment.getActivity())
			.setLink(Url)
			.setName(artist+" "+song)
			.build();
			uiHelper.trackPendingDialogCall(shareDialog.present());*/
		}
	}






}

