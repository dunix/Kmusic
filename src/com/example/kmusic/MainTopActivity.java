package com.example.kmusic;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;





// Activity asociado a la pagina en donde se muestran los tops de canciones según LastFM
public class MainTopActivity extends Activity  {
	
    protected void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_top);    
    }
    
    // Método para inicializar el menu en el action bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
    	this.getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
    
    // Método para definir las acciones de los items agregados al action bar
    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
	        case R.id.menu_search:
	        	Intent i = new Intent(getApplicationContext(), SearchActivity.class);
                startActivity(i);
	            return true;
	        
	        default:
	            return super.onOptionsItemSelected(item);
		}
    
    
    }


}

