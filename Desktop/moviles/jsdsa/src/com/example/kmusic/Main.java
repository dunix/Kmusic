package com.example.kmusic;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;


public class Main extends Activity {
	

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        TabsFragment frgListado=(TabsFragment) getFragmentManager().findFragmentById(R.id.tabs_fragment);
        
    }
	
	
}