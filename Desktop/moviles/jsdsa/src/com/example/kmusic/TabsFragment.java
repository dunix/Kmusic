package com.example.kmusic;

import Fragments.FragmentArtist;
import Fragments.FragmentNewArtists;
import Fragments.FragmentResultSearch;
import android.app.Fragment;
import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

public class TabsFragment extends Fragment  implements OnTabChangeListener{
	private static final String TAG = "FragmentTabs";
	public static final String TAB_New_Artist = "1";
	public static final String TAB_Personalized_Search = "2";

	private View mRoot;
	private TabHost mTabHost;
	private int mCurrentTab;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mRoot = inflater.inflate(R.layout.tabs_fragment, null);
		
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
		updateTab(TAB_New_Artist, R.id.tab_1);
	}

	private void setupTabs() {
		mTabHost.setup(); // important!
		mTabHost.addTab(newTab(TAB_New_Artist, R.string.tab_New_Artist, R.id.tab_1));
		mTabHost.addTab(newTab(TAB_Personalized_Search, R.string.tab_Personalized_Search, R.id.tab_2));
	}

	private TabSpec newTab(String tag, int labelId, int tabContentId) {
		Log.d(TAG, "buildTab(): tag=" + tag);

		View indicator = LayoutInflater.from(getActivity()).inflate(
				R.layout.tab,
				(ViewGroup) mRoot.findViewById(android.R.id.tabs), false);
		((TextView) indicator.findViewById(R.id.text)).setText(labelId);

		TabSpec tabSpec = mTabHost.newTabSpec(tag);
		tabSpec.setIndicator(indicator);
		tabSpec.setContent(tabContentId);
		return tabSpec;
	}

	@Override
	public void onTabChanged(String tabId) {
		Log.d(TAG, "onTabChanged(): tabId=" + tabId);
		if (TAB_New_Artist.equals(tabId)) {
			ListView lstListado = (ListView)this.getActivity().findViewById(R.id.busqueda);
			lstListado.setAdapter(null);
			EditText artista= (EditText)this.getActivity().findViewById(R.id.getArtista);
			EditText album= (EditText)this.getActivity().findViewById(R.id.getAlbum);
			EditText cancion= (EditText)this.getActivity().findViewById(R.id.getSong);
			artista.setText(""); album.setText("");cancion.setText(""); 
			
			updateTab(tabId, R.id.tab_1);
			mCurrentTab = 0;
			return;
		}
		if (TAB_Personalized_Search.equals(tabId)) {
			ListView lstListado = (ListView)this.getActivity().findViewById(R.id.busqueda);
	
			EditText buscar= (EditText) this.getActivity().findViewById(R.id.obtenerartista);
			buscar.setText("");
			//buscar.setText("");
			lstListado.setAdapter(null);
			updateTab(tabId, R.id.tab_2);
			mCurrentTab = 1;
			return;
		}
	}

	private void updateTab(String tabId, int placeholder) {
		FragmentManager fm = getFragmentManager();

		if (fm.findFragmentByTag(tabId) == null) {
			if (tabId.equals("1")){
			fm.beginTransaction()
					.replace(placeholder, new FragmentNewArtists(), tabId)
					.commit();
			}
			if (tabId.equals("2")){
				fm.beginTransaction()
						.replace(placeholder, new FragmentResultSearch(), tabId)
						.commit();
				}
		}
	}

}
