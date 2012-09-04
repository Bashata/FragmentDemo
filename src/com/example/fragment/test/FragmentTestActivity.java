package com.example.fragment.test;

import android.os.Bundle;
//important to pick android.support.v4.app classes rather than Android.app.Fragment so that we support 1.6+
import android.support.v4.app.FragmentActivity; 
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * This activity uses a layout that contains a TestFragment, a Button, and a TextView.  
 * The xml-specified Fragment cannot be removed because by Framework design, and we won't try to (see http://stackoverflow.com/questions/8903874/can-i-remove-a-fragment-defined-in-a-layout-xml-file)
 * The button (fragmentToggler) adds another TestFragment when tapped, and removes it when tapped again.
 * The TextView does nothing of consequence (and is not referred to within the FragmentTestActivity code), but illustrates that it looks just like the TextViews that are inside of Fragments.
 */
public class FragmentTestActivity extends FragmentActivity {

	/** We use this tag to refer to the fragment that we add and remove programmatically */
	private static final String TAG_TOGGLABLE_FRAG = "TOGGLABLE_FRAG";
	
	private FragmentManager fragMgr;
	
	private Button fragmentToggler;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("ActivityLifecycle", "onCreate, savedInstanceState is " + (savedInstanceState == null?"":"not ") + "null");
        setContentView(R.layout.activity_main);
        
        //getSupportFragmentManager instead of getFragmentManager - use the compatability library, to support down to verion 1.6/API 4 rather than 3.1+/API 12+ only
        fragMgr = getSupportFragmentManager();
        
        //fragmentToggler - the only button in the layout
        fragmentToggler = (Button)findViewById(R.id.fragment_toggle_button);
        fragmentToggler.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//Somewhat cleaner to look for the fragment directly rather than storing a separate boolean for "has fragment"
				//If we went with a boolean instead, we would have to be saved in onSaveInstanceState and restored in onRestoreInstanceState/onCreate,
				//otherwise we'd lose track of the fragment after an orientation change and could end up making duplicate fragments
				if (fragMgr.findFragmentByTag(TAG_TOGGLABLE_FRAG) != null) {
					removeTogglableFragment();
					fragmentToggler.setText(R.string.insert_fragment);
				} else {
					addTogglableFragment();
					fragmentToggler.setText(R.string.remove_fragment);
				}
			}
        	
        });
    }

	@Override
	public void onRestoreInstanceState(Bundle outState) {		
		super.onRestoreInstanceState(outState);
		Log.d("ActivityLifecycle", "onRestoreInstanceState");
	}
    
    //Lifecycle callbacks are overriden with simple logging so that it's easy to see which ones are called and in what order relative to the Fragment lifecycle callbacks
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	Log.d("onCreateOptionsMenu", "onCreateOptionsMenu, menu is " + (menu == null?"":"not ") + "null");
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    @Override
    public void onStart() {
    	super.onStart();
    	Log.d("ActivityLifecycle", "onStart");    	
    }
    
	@Override
	public void onSaveInstanceState(Bundle outState) {		
		//Note: there is no need to save whether or not the Fragment was showing, because it will automatically be retained
		//and restored for us across orientation changes.  This is the default behavior, there's no explicit setting in the code
		//that made it act that way.
		super.onSaveInstanceState(outState);
		Log.d("ActivityLifecycle", "onSaveInstanceState");
	}
    
    @Override
    public void onResume() {
    	super.onResume();
    	Log.d("ActivityLifecycle", "onResume");    	
    }
    
    @Override
    public void onPause() {
    	super.onPause();
    	Log.d("ActivityLifecycle", "onPause");    	
    }
    
    @Override
    public void onStop() {
    	super.onStop();
    	Log.d("ActivityLifecycle", "onStop");    	
    }
    
    @Override
    public void onDestroy() {
    	super.onDestroy();
    	Log.d("ActivityLifecycle", "onDestroy");    	
    }


	public void addTogglableFragment() {	
		FragmentTransaction transaction = fragMgr.beginTransaction();

		TestFragment fragment = new TestFragment(true); //true - Fragment's text should say that this is removable, rather than that it isn't.
		transaction.add(R.id.second_fragment_container, fragment, TAG_TOGGLABLE_FRAG); //note - this Fragment will remain in the UI after an orientation change
		
		transaction.commit();		
	}
    
	public void removeTogglableFragment() {
		TestFragment oldTestFragment = (TestFragment)fragMgr.findFragmentByTag(TAG_TOGGLABLE_FRAG);
		
		FragmentTransaction transaction = fragMgr.beginTransaction();
		
		transaction.remove(oldTestFragment);
		
		transaction.commit();		
	}
}
