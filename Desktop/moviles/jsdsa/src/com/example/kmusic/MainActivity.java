package com.example.kmusic;


import Fragments.FragmentTopArtists;
import Fragments.FragmentTopTracks;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

//import com.facebook.UiLifecycleHelper;



public class MainActivity extends Activity  {
	
    protected void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);    
        FragmentTopTracks frgListado =(FragmentTopTracks)getFragmentManager().findFragmentById(R.id.FrgTopTracks);
      
        // llamada al fragment de la derecha        
        //FragmentTopArtists frgListado2 =(FragmentTopArtists)getFragmentManager().findFragmentById(R.id.FrgTopTracks2);        
        
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
    	this.getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
	        case R.id.menu_search:
	        	Intent i = new Intent(getApplicationContext(), Main.class);
                startActivity(i);
	            return true;
	        
	        default:
	            return super.onOptionsItemSelected(item);
		}
    
    
    }


}

