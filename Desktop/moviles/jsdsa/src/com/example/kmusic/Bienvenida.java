package com.example.kmusic;

import Fragments.FragmentTopArtists;
import Fragments.FragmentTopTracks;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import asincrona.AsincronicaBienvenida;

public class Bienvenida extends Activity {

	
	public  static ProgressDialog pDialog;
	
	public Bienvenida() {
		// TODO Auto-generated constructor stub
	}
	
	
	protected void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bienvenida);
        
    	AsincronicaBienvenida a = new AsincronicaBienvenida(getApplicationContext());
    	a.execute();       
        
    }


}
