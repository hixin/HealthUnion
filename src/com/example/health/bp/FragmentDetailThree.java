package com.example.health.bp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.health.R;
import com.example.health.bpdummy.DummyContent;
import com.example.health.util.LogUtil;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link ItemListActivity}
 * in two-pane mode (on tablets) or a {@link ItemDetailActivity}
 * on handsets.
 */
public class FragmentDetailThree extends Fragment{
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";
    private static final String TAG = "FragmentDetailThree";
    
    /**
     * The dummy content this fragment is presenting.
     */
    private DummyContent.DummyItem mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FragmentDetailThree() {
    }

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
        View rootView = inflater.inflate(R.layout.bp_fragment_item_detail3, container, false);
        LogUtil.i("position",getArguments().getString(ARG_ITEM_ID));
        // Show the dummy content as text in a TextView.
        if (mItem != null) {
            
            /* three.setOnClickListener(new OnClickListener() {				
				@Override
				public void onClick(View v) {
					((ItemListActivityBO) getActivity()).onItemSelected("2");
					((ItemListFragmentBO) ((ItemListActivityBO) getActivity()).getSupportFragmentManager()
		                    .findFragmentById(R.id.item_list)).setActivatedPosition(1);
				}
			});*/
        }
        LogUtil.i("3",TAG);
 
        return rootView;
    }

	
}
