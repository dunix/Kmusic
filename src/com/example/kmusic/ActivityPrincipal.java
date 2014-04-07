package com.example.kmusic;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.ListView;


public class ActivityPrincipal extends Activity {
	private ListView lstOpciones;

	@Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_principal);
	        
	        adaptador Adaptador = new adaptador(this);
	            
	      
	        lstOpciones = (ListView)findViewById(R.id.LstOpciones);
	        registerForContextMenu(lstOpciones);
	        lstOpciones.setAdapter(Adaptador);
	 }
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo){
		super.onCreateContextMenu(menu, v, menuInfo);
		
		menu.setHeaderTitle("Acciones");
		menu.add(0, v.getId(), 0, "Ver video");
		
	}
	
	public boolean onContextItemSelected(MenuItem item)
	{
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
		
		String nombre;
		
		nombre=new adaptador(this.getParent()).lista2.get(info.position).cancion;
		try{
			
		if(item.getTitle() == "Ver video"){
			System.out.println("entro la concha de ");
			
			cambio(nombre);
			
		}
		else{
			return false;
			
		}
		
		}
		catch (Exception e){
			return true;
			
		}
		return true;
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.listing, menu);
		return true;
	}
	
	public void cambio(String nombre){
		Intent i = new Intent(ActivityPrincipal.this, YoutubeActivity.class);
		i.putExtra("cancion", nombre);
		//required to launch from non-activity
		startActivity(i);
		
	}

}



