package com.example.fragment.test;

import android.app.Activity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * A Fragment that contains a single TextView, which can either show a "this Fragment can't be replaced" or "this Fragment can be replaced" message
 * It rememebers which of those messages it was showing in onSaveInstanceState and restores that memory in onCreateView
 * This Fragment also overloads its lifecycle callback methods so that logcat will show the order in which callbacks happen relative to each other and
 * to the parent Activity's lifecycle callbacks
 */
public class TestFragment extends android.support.v4.app.Fragment {

	//How we track whether to show "this Fragment can't be replaced" or "this Fragment can be replaced"
	private boolean useTogglableLanguage = false;
	
	private static final String KEY_USE_TOGGLABLE_LANGUAGE = "usetog";
	
	//Declaring this fragment within XML means we have to have a zero-arg constructor. 
	public TestFragment() {
		//By default, show the "this Fragment can't be replaced", by passing false to the one-arg constructor
		this(false);
	}
	
	public TestFragment(boolean useTogglableLanguage) {
		this.useTogglableLanguage = useTogglableLanguage;
	}
	
	//Lifecycle callbacks are overriden with simple logging so that it's easy to see which ones are called and in what order relative to the Activity lifecycle callbacks
	
	@Override
	public void onInflate(Activity activity, AttributeSet attrs, Bundle savedInstanceState) {
		super.onInflate(activity, attrs, savedInstanceState);
		Log.d("FragmentLifecycle", "onInflate");
	}
	
    @Override
    public void onAttach(Activity activity) {
    	super.onAttach(activity);
    	if (activity != null) {
    		Log.d("FragmentLifecycle", "onAttach attach to " + activity.getClass().getName());    		
    	} else {
    		//I've never seen this come up null, and it shouldn't, but someone on the interwebs seems to have seen just that:
    		//http://stackoverflow.com/questions/8729290/fragment-activity-lifecycles-and-orientation-change
    		Log.d("FragmentLifecycle", "onAttach attach to null");
    	}
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {    	
    	super.onCreate(savedInstanceState);
    	Log.d("FragmentLifecycle", "onCreate savedInstanceState is " + (savedInstanceState == null?"":"not ") + "null");    	
    }
    
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {		
		Log.d("FragmentLifecycle", "onCreateView savedInstanceState is " + (savedInstanceState == null?"":"not ") + "null");
		//It is quite important to use the three-arg version of inflate and pass in false for attachToRoot.
		//Otherwise, we'll crash when trying to add this programmatically - "The specified child already has a parent"
		//see http://stackoverflow.com/questions/6035711/android-fragment-with-compatibility-package-on-2-3-3-creates-specified-child
		View layout = inflater.inflate(R.layout.test_fragment, container, false);
		
		if (savedInstanceState != null) {
			useTogglableLanguage = savedInstanceState.getBoolean(KEY_USE_TOGGLABLE_LANGUAGE);
		}
		
		if (useTogglableLanguage) {
			TextView fragmentText = (TextView)layout.findViewById(R.id.test_fragment_text);
			fragmentText.setText(R.string.removable_fragment_info);
		} //else the TextView already uses R.string.unremovable_fragment_info because of the definition in test_fragment.xml, no need to change anything
		
		return layout;		
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Log.d("FragmentLifecycle", "onActivityCreated savedInstanceState is " + (savedInstanceState == null?"":"not ") + "null");
	}
	
	@Override
	public void onStart() {
		super.onStart();
		Log.d("FragmentLifecycle", "onStart");		
	}

	@Override
	public void onResume() {
		super.onResume();
		Log.d("FragmentLifecycle", "onResume");		
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		//Save a useTogglableLanguage to remind us of whether or not to show the "can be removed" or "cannot be removed" message if this Fragment has to be destroyed/recreated,
		//for example because of an orientation change
		outState.putBoolean(KEY_USE_TOGGLABLE_LANGUAGE, useTogglableLanguage);
		Log.d("FragmentLifecycle", "onSaveInstanceState");		
	}
	
	@Override
	public void onPause() {
		super.onPause();
		Log.d("FragmentLifecycle", "onPause");		
	}
	
	@Override
	public void onStop() {
		super.onStart();
		Log.d("FragmentLifecycle", "onStop");		
	}

	@Override
	public void onDestroyView () {
		super.onDestroyView();
		Log.d("FragmentLifecycle", "onDestroyView");
	}
	
	@Override
	public void onDestroy () {
		super.onDestroy();
		Log.d("FragmentLifecycle", "onDestroy");
	}
	
	@Override
	public void onDetach() {
		super.onDetach();
		Log.d("FragmentLifecycle", "onDetach");
	}
}
