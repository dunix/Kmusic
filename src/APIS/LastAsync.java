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
	public static String key = "2e321ea1e5efd535b9a261c211cefe78";
	public static ArrayList<ObjectLastFM> lista = new ArrayList<ObjectLastFM>();
	public Context context;
	public ProgressDialog pDialog=new MainActivity().pDialog;
	public LastAsync(Context c){
		context = c;
		
	}
	public LastAsync(){
		
	}
	protected void onPreExecute() {
	
		
		pDialog.setProgress(0);
		pDialog.show();
	}
	@Override
	protected ArrayList<ObjectLastFM> doInBackground(Void... arg0) {
		//Caller.getInstance().setUserAgent("tst");
		Caller.getInstance().setCache(null);
		PaginatedResult<Track> chart = Chart.getTopTracks(1, key);
		Collection<Track> TopTracks = chart.getPageResults();
		 
	     for (Track track : TopTracks) {
	    	 //Log.w("MainActivity", track.getName()+"    "+track.getArtist());
	    	 ObjectLastFM a=new ObjectLastFM();
	    	 a.cancion=track.getName();
	    	 a.artista=track.getArtist();
	    	 lista.add(a);
	    	
	    	 
	       }
	
		//System.out.println(lista.get(0).artista);
	    return lista;
	}
	protected void onProgressUpdate(Integer... values) {
		int progreso = values[0].intValue();
		
		pDialog.setProgress(progreso);
	}
	protected void onPostExecute( ArrayList<ObjectLastFM>  response) {
			System.out.println("ya termino           "+lista.size());
			
			Intent i = new Intent();
			i.setClass(context, ActivityPrincipal.class);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); //required to launch from non-activity
			context.startActivity(i);
			pDialog.dismiss();
			
    	//TextView txtView = (TextView) findViewById(R.id.textView1);
    	//txtView.setText(answer);
	}
	
	
	     
	
	     
	     
	
	
	
	
	
	
}
