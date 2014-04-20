package Fragments;

import com.example.kmusic.R;

import Fragments.FragmetTracks.SearchAsyncTracks;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentExtra extends Fragment {
	
	public View onCreateView(LayoutInflater inflater,ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.result_tab_extra, container, false);
         return v;
	}
	
	

}
