package com.example.health.bp;

import com.example.health.R;
import com.example.health.bo.FragmentDetailOneBO;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;

/**
 * An activity representing a single Item detail screen. This
 * activity is only used on handset devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link ItemListActivity}.
 * <p>
 * This activity is mostly just a 'shell' activity containing nothing
 * more than a {@link FragmentDetailOneBO}.
 */
public class ItemDetailActivity extends FragmentActivity {
	 private FragmentDetailOne fragment1;
	 private FragmentDetailTwo fragment2;
	 private FragmentDetailThree fragment3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bp_activity_item_detail);

        // Show the Up button in the action bar.
//        getActionBar().setDisplayHomeAsUpEnabled(true);

       
        if (savedInstanceState == null) {
        	
            Bundle arguments = new Bundle();
//            arguments.putString(FragmentDetailOne.ARG_ITEM_ID,
//                    getIntent().getStringExtra(FragmentDetailOne.ARG_ITEM_ID));
            String id = getIntent().getStringExtra(FragmentDetailOne.ARG_ITEM_ID);
            
            switch(Integer.parseInt(id)) {
            case 1: 
            	  arguments.putString(FragmentDetailOne.ARG_ITEM_ID, id);
            		if(fragment1 == null) {
            			fragment1 = new FragmentDetailOne();
            			fragment1.setArguments(arguments);
            		}
                 
                   if(!fragment1.isAdded()) {
	                   getSupportFragmentManager().beginTransaction()
	                           .replace(R.id.item_detail_container, fragment1,"Detail")
	                           .commit();
                   }
            	break;
            case 2:
            	  arguments.putString(FragmentDetailOne.ARG_ITEM_ID, id);
            	if(fragment2 == null) {
            		fragment2 = new FragmentDetailTwo();
            		fragment2.setArguments(arguments);
        		}
               
                
                if(!fragment2.isAdded()) {
	                getSupportFragmentManager().beginTransaction()
	                        .replace(R.id.item_detail_container, fragment2,"Detai2")
	                        .commit();
                }
            	
            	break;
            case 3:
            	arguments.putString(FragmentDetailOne.ARG_ITEM_ID, id);
            	if(fragment3 == null) {
            		fragment3 = new FragmentDetailThree();
            		fragment3.setArguments(arguments);
        		}
                if(!fragment3.isAdded()){
                	 getSupportFragmentManager().beginTransaction()
                     .replace(R.id.item_detail_container, fragment3,"Detai3")
                     .commit();
                }
               
            	break;
            }
        
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. 
            NavUtils.navigateUpTo(this, new Intent(this, ItemListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
