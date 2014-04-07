package com.example.kmusic;


import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import APIS.*;





public class MainActivity extends Activity  {
	
	public  static ProgressDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pDialog = new ProgressDialog(MainActivity.this);
    	pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
    	pDialog.setMessage("Procesando...");
    	pDialog.setCancelable(true);
    	pDialog.setMax(100);
    	
    	
    	LastAsync a= new LastAsync(this.getApplicationContext());
    	a.execute();
		

		 
       // Log.w("MainActivity", "This is a warning");
        
        
    }

 
 
public void devolverPrimerActivity (View activity_main) {
	setContentView(R.layout.activity_main);
	
	//new LastAsync().execute();
	 
	 
 }
public ProgressDialog getprogress(){
	return pDialog;
	
}



public void devolverSegundoActivity (View activity_main) {
	
	
	//while(a.lista.size()==0){
	//System.out.println("nulaaaaaaaa");
	//}
	//Intent act = new Intent(this, Listing.class);
	//startActivity(act);
	//setContentView(R.layout.elementos);

	//new CallMashapeAsync().execute();
	 
	 
 }
 
}

