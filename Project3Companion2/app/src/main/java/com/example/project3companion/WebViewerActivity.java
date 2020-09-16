package com.example.project3companion;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


public class WebViewerActivity
        extends AppCompatActivity
        implements TitleFragment.ListSelectionListener {

    public static String[] mTitleArray;
    public static String[] mWebArray;

    private WebFragment mWebFragment = new WebFragment();
    private TitleFragment mTitleFragment = null ;

    private FragmentManager mFragmentManager;

    private FrameLayout mTitleFrameLayout, mWebFrameLayout;
    private static final int MATCH_PARENT = LinearLayout.LayoutParams.MATCH_PARENT;
    private static final String TAG = "WebViewerActivity";

    int mShownIndex = -1 ;
    static String OLD_ITEM = "old_item" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.i(TAG, getClass().getSimpleName() + ": entered onCreate()");
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        String array_name = extras.getString("Name");

        if (array_name.equalsIgnoreCase("attraction")){

            Log.i("WebViewerActivity", "In attraction condition");
            mTitleArray = getResources().getStringArray(R.array.attraction_names);
            mWebArray = getResources().getStringArray(R.array.attraction_url);

        }
        else{
            Log.i("WebViewerActivity", "In restaurant condition");
            mTitleArray = getResources().getStringArray(R.array.restaurant_names);
            mWebArray = getResources().getStringArray(R.array.restaurant_url);
        }

        setContentView(R.layout.content_main_fragment);

        // Get references to the TitleFragment and to the WebFragment
        mTitleFrameLayout = (FrameLayout) findViewById(R.id.title_fragment_container);
        mWebFrameLayout = (FrameLayout) findViewById(R.id.web_fragment_container);

        // Get reference to the SupportFragmentManager instead of original FragmentManager
        mFragmentManager = getSupportFragmentManager();

        // Start a new FragmentTransaction with backward compatibility
        FragmentTransaction fragmentTransaction = mFragmentManager
                .beginTransaction();

        mTitleFragment = new TitleFragment() ;
        mShownIndex = -1 ;

        fragmentTransaction.replace(R.id.title_fragment_container, mTitleFragment) ;

        // Commit the FragmentTransaction
        fragmentTransaction.commit();

        // Add a OnBackStackChangedListener to reset the layout when the back stack changes
        mFragmentManager.addOnBackStackChangedListener(
                // UB 2/24/2019 -- Use support version of Listener
                new FragmentManager.OnBackStackChangedListener() {
                    public void onBackStackChanged() {
                        setLayout();
                    }
                });

        // Update the current index id
        if (savedInstanceState != null) {
            mShownIndex = savedInstanceState.getInt(OLD_ITEM) ;
        }

    }

    private void setLayout() {

        // Determine whether the WebFragment has been added
        // Or if the added WebFragment is empty
        if (!mWebFragment.isAdded() || mShownIndex == -1) {

            // Make the TitleFragment occupy the entire layout
            mTitleFrameLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    MATCH_PARENT, MATCH_PARENT));
            mWebFrameLayout.setLayoutParams(new LinearLayout.LayoutParams(0,
                    MATCH_PARENT));
        } else {

            // Split the activity into two parts based on orientation
            int orientation = this.getResources().getConfiguration().orientation;

            if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                Log.i("WebViewerActivity","Orientation is portrait");
                mTitleFrameLayout.setLayoutParams(new LinearLayout.LayoutParams(0,
                        MATCH_PARENT, 0f));

                mWebFrameLayout.setLayoutParams(new LinearLayout.LayoutParams(0,
                        MATCH_PARENT, 1f));
            } else {
                Log.i("WebViewerActivity","Orientation is landscape");
                mTitleFrameLayout.setLayoutParams(new LinearLayout.LayoutParams(0,
                        MATCH_PARENT, 1f));

                mWebFrameLayout.setLayoutParams(new LinearLayout.LayoutParams(0,
                        MATCH_PARENT, 2f));
            }

        }
    }

    // Implement Java interface ListSelectionListener defined in TitlesFragment
    // Called by TitlesFragment when the user selects an item in the TitlesFragment
    @Override
    public void onListSelection(int index) {

        // If the WebFragment has not been added, add it now

        if (!mWebFragment.isAdded()) {

            Log.i("WebViewerActivity", "Adding web fragment to stack");
            // Start a new FragmentTransaction
            FragmentTransaction fragmentTransaction = mFragmentManager
                    .beginTransaction();

            // Add the WebFragment to the layout
            fragmentTransaction.add(R.id.web_fragment_container, mWebFragment);

            // Add this FragmentTransaction to the backstack
            fragmentTransaction.addToBackStack(null);

            // Commit the FragmentTransaction
            fragmentTransaction.commit();

            // Force Android to execute the committed FragmentTransaction
            mFragmentManager.executePendingTransactions();
        }

        if (mWebFragment.getShownIndex() != index) {

            // Tell the WebFragment to show the quote string at position index
            mWebFragment.showQuoteAtIndex(index);
            mShownIndex = index ;

            // Redo layout on selection of an item
            setLayout();
        }
    }

    //Methods to save and recover index and layout of web view selected
    // in the fragments before changing orientation.
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState) ;
        if (mShownIndex >= 0) {
            outState.putInt(OLD_ITEM, mShownIndex) ;
        }
        else {
            outState.putInt(OLD_ITEM, -1 ) ;
        }
    }

    // Restore the layout content from before orientation change
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.i("WebViewerActivity", "in onRestoreInstanceState");
        mShownIndex = savedInstanceState.getInt(OLD_ITEM);
        super.onRestoreInstanceState(savedInstanceState);
        setLayout();
        onListSelection(mShownIndex);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        Intent intent = new Intent();
        intent.setClass(this, WebViewerActivity.class);

        if (id == R.id.attraction) {

            intent.putExtra("Name", "attraction");
            // Prevent unnecessary addition of activities
            // Goes back to the main activity on clicking the back button
            intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        else {

            intent.putExtra("Name", "restaurant");
            // Prevent unnecessary addition of activities
            // Goes back to the main activity on clicking the back button
            intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    // UB: Activity about to be visible: Retrieve previous quote, if saved
    //     reset state of titles fragment
    public void onStart() {
        super.onStart() ;
        if (mShownIndex >= 0) {
            mWebFragment.showQuoteAtIndex(mShownIndex);
            mTitleFragment.setSelection(mShownIndex);
            mTitleFragment.getListView().setItemChecked(mShownIndex,true);
        }
    }

}