package com.example.health.bp;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.util.LangUtils;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.health.MyApplication;
import com.example.health.R;
import com.example.health.bpdummy.DummyContent;
import com.example.health.util.LogUtil;
import com.viewpagerindicator.TabPageIndicator;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link ItemListActivity}
 * in two-pane mode (on tablets) or a {@link ItemDetailActivity}
 * on handsets.
 */
public class FragmentDetailOne extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";
    private static final String TAG = "FragmentDetailOneBO";
   
    /**
     * The dummy content this fragment is presenting.
     */
    private DummyContent.DummyItem mItem;
    private TabPageIndicator mIndicator ;  
    private ViewPager mViewPager ;  
    private TabAdapter mAdapter ; 


    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mItem = DummyContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.bp_fragment_item_detail1, container, false);
        LogUtil.i("position",getArguments().getString(ARG_ITEM_ID));
        // Show the dummy content as text in a TextView.
    /*    if (mItem != null) {
        	  mIndicator = (TabPageIndicator) rootView.findViewById(R.id.id_indicator);  
              mViewPager = (ViewPager) rootView.findViewById(R.id.id_pager);  
              mAdapter = new TabAdapter(getChildFragmentManager(),MyApplication.fragments);  
              mViewPager.setAdapter(mAdapter); 
              mIndicator.setViewPager(mViewPager, 0); 
              LogUtil.i("1",TAG);
        }*/
        return rootView;
    }
    
    @Override
	public void onDetach() {
		super.onDetach();
		super.onDetach();

	    try {
	        Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
	        childFragmentManager.setAccessible(true);
	        childFragmentManager.set(this, null);

	    } catch (NoSuchFieldException e) {
	        throw new RuntimeException(e);
	    } catch (IllegalAccessException e) {
	        throw new RuntimeException(e);
	    }
	}
}
