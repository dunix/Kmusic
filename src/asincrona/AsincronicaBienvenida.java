package asincrona;


import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import com.example.kmusic.MainTopActivity;
import de.umass.lastfm.Caller;
//Clase asincrona con mensaje de bienvenida
public class AsincronicaBienvenida extends AsyncTask<Void, Void, Boolean> {

		private Context context;
		
		public AsincronicaBienvenida(Context context_Activity){
			context = context_Activity;
			
		}
		
		public AsincronicaBienvenida(){
			
		}
		
		protected void onPreExecute() {

		}
		
		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO Auto-generated method stub
			Caller.getInstance().setCache(null);
			//Instanciar
		
				try {
					get(4000,TimeUnit.MILLISECONDS);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (TimeoutException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		
				
			return true;
		}
		
		
		protected void onPostExecute(Boolean response) {
			
				
				Intent i = new Intent();
				i.setClass(context, MainTopActivity.class);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); //required to launch from non-activity
				context.startActivity(i);
		}
}

