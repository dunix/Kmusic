package com.example.kmusic;

import Fragments.FragmentAlbum;
import Fragments.FragmentArtist;

import Fragments.YoutubeFragment;
import android.app.Fragment;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;


// Fragment encargado de manejar los tabs de los resultados de las busquedas realizadas
public class TabsFragmentResult extends Fragment  implements OnTabChangeListener{
	private static final String TAG = "FragmentTabsResult";
	public static final String TAB_Info_Artist = "1";
	public static final String TAB_Albums = "2";
	public static final String TAB_Extras = "3";


	private View mRoot;
	private TabHost mTabHost;
	private int mCurrentTab;
	Intent intent;


	

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		mRoot = inflater.inflate(R.layout.result_tabs, null);
		
		mTabHost = (TabHost) mRoot.findViewById(android.R.id.tabhost);
		setupTabs();
		
		return mRoot;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		
		
		setRetainInstance(true);

		mTabHost.setOnTabChangedListener(this);
		mTabHost.setCurrentTab(mCurrentTab);
		// manually start loading stuff in the first tab
		updateTab(TAB_Info_Artist, R.id.tab_info_artist);
	}

	private void setupTabs() {
		mTabHost.setup(); // important!
		mTabHost.addTab(newTab(TAB_Info_Artist, R.string.tab_Info_Artist, R.id.tab_info_artist));
		mTabHost.addTab(newTab(TAB_Albums, R.string.tab_Albums, R.id.tab_albums));
		mTabHost.addTab(newTab(TAB_Extras, R.string.tab_Extras, R.id.tab_extra));
		
	}

	private TabSpec newTab(String tag, int labelId, int tabContentId) {
		Log.d(TAG, "buildTab(): tag=" + tag);

		View indicator = LayoutInflater.from(getActivity()).inflate(
				R.layout.result_tab,
				(ViewGroup) mRoot.findViewById(android.R.id.tabs), false);
		((TextView) indicator.findViewById(R.id.text)).setText(labelId);

		TabSpec tabSpec = mTabHost.newTabSpec(tag);
		tabSpec.setIndicator(indicator);
		tabSpec.setContent(tabContentId);
		return tabSpec;
	}

	// Método encargado de capturar el cambio en los tabs
	@Override
	public void onTabChanged(String tabId) {
		Log.d(TAG, "onTabChanged(): tabId=" + tabId);
		if (TAB_Info_Artist.equals(tabId)) {
		
			
			updateTab(tabId, R.id.tab_info_artist);
			mCurrentTab = 0;
			return;
		}
		if (TAB_Albums.equals(tabId)) {
			
			updateTab(tabId, R.id.tab_albums);
			mCurrentTab = 1;
			return;
		}
		if (TAB_Extras.equals(tabId)) {
			
			updateTab(tabId, R.id.tab_extra);
			mCurrentTab = 2;
	
			
			return;
		}
	}

	// Método encargado de realizar el cambio de tabs con el fragment asociado	
	private void updateTab(String tabId, int placeholder) {
		FragmentManager fm = getFragmentManager();

		if (fm.findFragmentByTag(tabId) == null) {
			if (tabId.equals("1")){
			fm.beginTransaction()
					.replace(placeholder, new FragmentArtist(), tabId)
					.commit();
			}
			if (tabId.equals("2")){
				fm.beginTransaction()
						.replace(placeholder, new FragmentAlbum(), tabId)
						.commit();
				}
			if (tabId.equals("3")){
				
				 Intent intent= getActivity().getIntent();
				YoutubeFragment f = YoutubeFragment.newInstance(intent.getStringExtra("artist")); 
				fm.beginTransaction().replace(placeholder, f,tabId).commit();
				}
		}
	}

}

