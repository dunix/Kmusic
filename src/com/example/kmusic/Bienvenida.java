package com.example.kmusic;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import asincrona.AsincronicaBienvenida;

public class Bienvenida extends Activity {
	AsincronicaBienvenida a = new AsincronicaBienvenida();
	
	public  static ProgressDialog pDialog;
	public boolean isActivityisRestaring=false;
	
	public Bienvenida() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bienvenida);
        
        a = new AsincronicaBienvenida(getApplicationContext());
    	a.execute();       
        
    }
	
	// Metodo para poder determinar si se reinicio el activity
	@Override
	protected void onRestart(){
      super.onRestart();
      isActivityisRestaring=true;
     
	}
	
	
	// Metodo para poder hacer resume del activity pausado
	@Override
	protected void onResume(){
      super.onResume();
      if(isActivityisRestaring){
    	a = new AsincronicaBienvenida(getApplicationContext());
      	a.execute();         
      }
      
	}

}
