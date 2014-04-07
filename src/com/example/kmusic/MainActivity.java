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
    	pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    	pDialog.setMessage("Procesando...");
    	pDialog.setCancelable(true);
    	pDialog.setMax(100);
    	LastAsync a= new LastAsync(this.getApplicationContext(),pDialog);
    	a.execute();
    }
}

