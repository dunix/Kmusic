package APIS;

import java.util.ArrayList;
import java.util.Collection;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;

import com.example.kmusic.ActivityPrincipal;
import com.example.kmusic.MainActivity;
import com.example.kmusic.ObjectLastFM;

import de.umass.lastfm.Caller;
import de.umass.lastfm.Chart;
import de.umass.lastfm.PaginatedResult;
import de.umass.lastfm.Track;

public class LastAsync extends AsyncTask<Void, Void, ArrayList<ObjectLastFM>> {
	private static String key = "2e321ea1e5efd535b9a261c211cefe78";
	private static ArrayList<ObjectLastFM> lista_result_Fm = new ArrayList<ObjectLastFM>();
	private Context context;
	private ProgressDialog pDialog;
	
	public LastAsync(Context context_Activity){
		context = context_Activity;
		
	}
	
	public LastAsync(){
		
	}
	
	public LastAsync(Context context_Activity,ProgressDialog new_pDialog){
		pDialog=new_pDialog;
		context=context_Activity;
	}
	protected void onPreExecute() {
	
		
		pDialog.setProgress(0);
		pDialog.show();
	}
	
	@Override
	protected ArrayList<ObjectLastFM> doInBackground(Void... arg0) {
		Caller.getInstance().setCache(null);
		PaginatedResult<Track> response = Chart.getTopTracks(1, key);
		getTop(response);
	    return lista_result_Fm;
	}
	protected void onProgressUpdate(Integer... values) {
		int progreso = values[0].intValue();
		
		pDialog.setProgress(progreso);
	}
	
	private void getTop(PaginatedResult<Track> response){
		Collection<Track> TopTracks = response.getPageResults();
		 
	     for (Track track : TopTracks) {
	    	 ObjectLastFM objectfm=new ObjectLastFM();
	    	 objectfm.setCancion(track.getName());
	    	 objectfm.setArtista(track.getArtist());
	    	 lista_result_Fm.add(objectfm);
	       }
		
		
	}
	public ArrayList<ObjectLastFM> getLista_result_FM(){
		
		return lista_result_Fm;
		
	}
	
	protected void onPostExecute( ArrayList<ObjectLastFM>  response) {
			Intent i = new Intent();
			i.setClass(context, ActivityPrincipal.class);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); //required to launch from non-activity
			context.startActivity(i);
			pDialog.dismiss();
	}
	
	
	     
	
	     
	     
	
	
	
	
	
	
}
