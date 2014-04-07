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
	        
	        adaptador_listviewCanciones_Top adapter_listview_canciones = new adaptador_listviewCanciones_Top(this);
	            
	      
	        lstOpciones = (ListView)findViewById(R.id.Lst_canciones_top);
	        registerForContextMenu(lstOpciones);
	        lstOpciones.setAdapter(adapter_listview_canciones);
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
		
		nombre=new adaptador_listviewCanciones_Top(this.getParent()).list_resultados_Fmtop.get(info.position).getCancion();
		try{
			
		if(item.getTitle() == "Ver video"){
						
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



