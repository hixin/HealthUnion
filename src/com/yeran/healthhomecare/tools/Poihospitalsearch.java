package com.yeran.healthhomecare.tools;

import java.util.ArrayList;
import java.util.List;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.yeran.healthhomecare.data.hospitalinfors;

public class Poihospitalsearch implements OnGetPoiSearchResultListener {
	public PoiSearch mPoiSearch = null;
	@SuppressWarnings("unused")
	private ArrayList<hospitalinfors> hosinfor;
	private List<PoiInfo> poiinfor;
	private PoiResult poiresult;
	private PoiDetailResult poidetailresult;

	private Poihospitalsearch() {
	}

	private static Poihospitalsearch poihospital = null;

	public static Poihospitalsearch getInstance() {
		if (poihospital == null) {
			poihospital = new Poihospitalsearch();
		}
		return poihospital;
	}

	public void Init() {
		mPoiSearch = PoiSearch.newInstance();
		mPoiSearch.setOnGetPoiSearchResultListener(Poihospitalsearch.this);
	}

	private void Poihospital(String Keyword, int load_Index, int numb, LatLng ll) {
		Init();

		// mPoiSearch
		// .searchInCity((new PoiCitySearchOption())
		// .city(city)
		// .keyword(Keyword)
		// .pageNum(load_Index)
		// .pageCapacity(numb));

		mPoiSearch.searchNearby(new PoiNearbySearchOption().keyword(Keyword)
				.location(ll).radius(5000).pageNum(load_Index)
				.pageCapacity(numb));
	}

	@Override
	public void onGetPoiDetailResult(PoiDetailResult detailresult) {
		// TODO Auto-generated method stub
		if (detailresult.error != SearchResult.ERRORNO.NO_ERROR) {

		} else {
			poidetailresult = detailresult;
		}
	}

	@Override
	public void onGetPoiResult(PoiResult result) {
		// TODO Auto-generated method stub
		if (result == null
				|| result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
			System.out.println("没有找到 结果");
			return;
		}
		if (result.error == SearchResult.ERRORNO.NO_ERROR) {

			poiinfor = result.getAllPoi();
		}
		poiresult = result;
	}

	public List<PoiInfo> getpoiinfors(String Keyword, int load_Index, int numb,
			LatLng ll) {
		Poihospital(Keyword, load_Index, numb, ll);

		if (poiinfor != null) {
			return poiinfor;
		}
		return null;
	}

	public PoiResult getpoiresult(String Keyword, int load_Index, int numb,
			LatLng ll) {
		Poihospital(Keyword, load_Index, numb, ll);

		if (poiresult != null) {
			return poiresult;
		}
		return null;
	}

	public PoiDetailResult getpoidetail(String poiuid) {
		mPoiSearch
				.searchPoiDetail((new PoiDetailSearchOption()).poiUid(poiuid));
		return poidetailresult;
	}

}
