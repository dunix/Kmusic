/*ITCR Curso IC8041
 * Aplicaciones móviles
 * Aplicación kmusic
 * Nombre de clase: FragmentTopTracks
 * Descripción: Fragment que se encarga de mostrar el top de canciones 
 * obtenidas mediante el API de LastFm 
 * */
package Fragments;


import java.util.ArrayList;

import com.example.kmusic.R;
import com.example.kmusic.R.id;
import com.example.kmusic.R.layout;

import APIS.LastfmAPI;
import ObjectsAPIS.ObjectLastFM;
import android.app.Activity;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;


public class FragmentTopArtists  extends Fragment{
	//Atributos de clase
	private LastfmAPI LastFM;
	private ListView lstListado;
	private  ArrayList<ObjectLastFM> lista_result_Fm = new ArrayList<ObjectLastFM>();
	Fragment fragment  ;
		
	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container,
			Bundle savedInstanceState) {
    	LastAsync a= new LastAsync();
    	a.execute();
		return inflater.inflate(R.layout.fragment_top_last_fm, 
				container, false);
	}
	
	@Override
	public void onActivityCreated(Bundle state) {
		super.onActivityCreated(state);
		fragment = this; 
		
	}
	class AdaptadorCorreos extends ArrayAdapter<ObjectLastFM>{
		Activity context;
		AdaptadorCorreos(Fragment context){
			super(context.getActivity(),R.layout.componenteslist_viewtop_artista,
					lista_result_Fm);
			this.context = context.getActivity();
		}
			public View getView(int position,View convertView,
					ViewGroup parent){
				LayoutInflater inflater = context.getLayoutInflater();
				View item = inflater.inflate(R.layout.componenteslist_viewtop_artista, null);
				
				TextView LblArtistaTop2  = 
						(TextView)item.findViewById(R.id.LblArtistaTop2);
				LblArtistaTop2.setText(lista_result_Fm.get(position).getArtista());;
				return (item);
				
			}
			
		}
/////////////////////
	
	public class LastAsync extends AsyncTask<Void, Void, Boolean> {
		
		

		public LastAsync(){
		}
		
		protected void onPreExecute() {
		}
		
		@Override
		protected Boolean doInBackground(Void... arg0) {
			LastFM=new LastfmAPI();

			lista_result_Fm=LastFM.transactionhandler("TopArtists", "");
		    return true;
		}
		protected void onProgressUpdate(Integer... values) {
		}
		
		
		protected void onPostExecute( Boolean  response) {
			// Una vez finalizado el proceso de obtener el top de tracks
			//Obtener ListView
			lstListado = (ListView)getView().findViewById(R.id.Lst_canciones_top);
			// Crear adapter para que este pase los datos al ListView
			lstListado.setAdapter(new AdaptadorCorreos(fragment));
		}	
	}	
	


}

