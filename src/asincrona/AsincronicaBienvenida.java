package asincrona;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Timer;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;
import android.app.Activity;

import com.example.kmusic.MainActivity;
import com.example.kmusic.R;
import com.example.kmusic.YoutubeActivity;

import Fragments.FragmentTopTracks;
import ObjectsAPIS.ObjectLastFM;
import android.app.Fragment;

import de.umass.lastfm.Caller;
import de.umass.lastfm.Chart;
import de.umass.lastfm.PaginatedResult;
import de.umass.lastfm.Track;

public class AsincronicaBienvenida extends AsyncTask<Void, Void, Boolean> {

		private Context context;
		
		public AsincronicaBienvenida(Context context_Activity){
			context = context_Activity;
			
		}
		
		public AsincronicaBienvenida(){
			
		}
		
		protected void onPreExecute() {

			//pDialog.setProgress(0);
			//pDialog.show();
		}
		
		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO Auto-generated method stub
			Caller.getInstance().setCache(null);
			//Instanciarsh
			System.out.println("Está entrando al background ANTES DEL FRAGMENT");
			
			
			try {
				Thread.sleep(4000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("Está entrando al background");
			
			return true;
		}
		
		
		protected void onPostExecute(Boolean response) {
				System.out.println("En el post execute");
				
				Intent i = new Intent();
				i.setClass(context, MainActivity.class);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); //required to launch from non-activity
				context.startActivity(i);
		}
}

