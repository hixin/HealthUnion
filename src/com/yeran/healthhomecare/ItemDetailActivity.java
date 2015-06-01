package com.yeran.healthhomecare;

import com.example.health.R;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;

/**
 * An activity representing a single Item detail screen. This activity is only
 * used on handset devices. On tablet-size devices, item details are presented
 * side-by-side with a list of items in a {@link ItemListActivity}.
 * <p>
 * This activity is mostly just a 'shell' activity containing nothing more than
 * a {@link ItemDetailFragment}.
 */
public class ItemDetailActivity extends FragmentActivity {
	private RegistFragment registfragment;
	private ItemDetailFragment fragment;
	private HistoryFragment historyfragment;
	private FragmentManager fragmentManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_activity_item_detail);
		System.out.println("Itemdetailactivity---oncreate");
		fragmentManager = this.getSupportFragmentManager();
		// Show the Up button in the action bar.
		getActionBar().setDisplayHomeAsUpEnabled(true);

		// savedInstanceState is non-null when there is fragment state
		// saved from previous configurations of this activity
		// (e.g. when rotating the screen from portrait to landscape).
		// In this case, the fragment will automatically be re-added
		// to its container so we don't need to manually add it.
		// For more information, see the Fragments API guide at:
		//
		// http://developer.android.com/guide/components/fragments.html
		//
		if (savedInstanceState == null) {
			// Create the detail fragment and add it to the activity
			// using a fragment transaction.
			FragmentTransaction transaction = fragmentManager
					.beginTransaction();

			String id = getIntent().getStringExtra(
					ItemDetailFragment.ARG_ITEM_ID);
			if (id.equals("3")) {
				hideFragments(transaction);
				if (registfragment == null) {
					registfragment = new RegistFragment();
					transaction.add(R.id.item_detail_container, registfragment);
				} else {
					transaction.show(registfragment);
				}

			} else if(id.equals("2")){
				hideFragments(transaction);
				if (historyfragment == null) {
					historyfragment = new HistoryFragment();
					transaction.add(R.id.item_detail_container, historyfragment);
				} else {
					transaction.show(historyfragment);
				}
			}else{
				hideFragments(transaction);
//				Bundle arguments = new Bundle();
//				arguments.putString(ItemDetailFragment.ARG_ITEM_ID, id);
				if (fragment == null) {
					fragment = new ItemDetailFragment();
					transaction.add(R.id.item_detail_container, fragment);					
				}else{
					transaction.show(fragment);
				}			
//				Bundle arguments = new Bundle();
//				arguments.putString(ItemDetailFragment.ARG_ITEM_ID, getIntent()
//						.getStringExtra(ItemDetailFragment.ARG_ITEM_ID));
//
//				if (fragment != null) {
//					transaction.remove(fragment);
//				}
//				fragment = new ItemDetailFragment();
//				fragment.setArguments(arguments);
//				transaction.add(R.id.item_detail_container, fragment);

				// getSupportFragmentManager().beginTransaction()
				// .replace(R.id.item_detail_container, fragment).commit();

			}
			transaction.commit();
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpTo(this,
					new Intent(this, ItemListActivity.class));
			return true;
		}
		System.out.println("Itemdetailactivity---onOptionsItemSelected");
		return super.onOptionsItemSelected(item);
	}

	private void hideFragments(FragmentTransaction transaction) {
		if (fragment != null) {
			transaction.hide(fragment);
		}

		if (registfragment != null) {
			transaction.hide(registfragment);
		}
		if (historyfragment != null) {
			transaction.hide(historyfragment);
		}
	}
}
