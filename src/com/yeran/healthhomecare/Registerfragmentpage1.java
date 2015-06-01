package com.yeran.healthhomecare;

import java.text.SimpleDateFormat;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.health.MyApplication;
import com.example.health.R;
import com.yeran.healthhomecare.connectinternet.Deviceaddorchange;
import com.yeran.healthhomecare.connectinternet.ThreadFactory;
import com.yeran.healthhomecare.data.ShuaidaoDeviceinfor;
import com.yeran.healthhomecare.data.peoplelist;
import com.yeran.healthhomecare.tools.numorstring;

@SuppressLint("SimpleDateFormat")
public class Registerfragmentpage1 extends Fragment implements OnClickListener {
	private View contactsLayout;
	private EditText name, deviceid, oldphone, shuaidaophone, secret;
	private TextView save;
//	private InputFilter[] inputFilters;

	// private static LocalDB dbService;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// dbService = new LocalDB(getActivity());
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		contactsLayout = inflater.inflate(R.layout.map_fragment_register, null);

		Initview();

//		inputFilters = new InputFilter[] { new InputFilter() {
//			@Override
//			public CharSequence filter(CharSequence source, int start, int end,
//					Spanned dest, int dstart, int dend) {
//				// TODO Auto-generated method stub
//				return source.length() < 1 ? dest.subSequence(dstart, dend)
//						: "";
//			}
//		} };

		return contactsLayout;
	}

	private void Initview() {
		save = (TextView) contactsLayout.findViewById(R.id.btn_save);
		name = (EditText) contactsLayout.findViewById(R.id.et_peopname);
		deviceid = (EditText) contactsLayout.findViewById(R.id.et_deviceNo);
		oldphone = (EditText) contactsLayout.findViewById(R.id.old_mobileNo);
		shuaidaophone = (EditText) contactsLayout
				.findViewById(R.id.et_mobileNo);
		secret = (EditText) contactsLayout.findViewById(R.id.oldpassword);

		save.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String createtime = sDateFormat.format(new java.util.Date());

		if (view.equals(save)) {
			numorstring numors = new numorstring();
			if ((numors.isMobileNO((oldphone.getText().toString())))
					&& (name.getText().toString() != "")
					&& ((name.getText().toString().length()) < 4)
					&& (numors.isPwnum(deviceid.getText().toString()))
					&& (numors.isMobileNO(shuaidaophone.getText().toString()))) {

				// 服务器插入设备信息
				ThreadFactory threadfactory = new ThreadFactory();
				threadfactory.setContext(getActivity());
				Deviceaddorchange deviceclass = (Deviceaddorchange) threadfactory
						.getThread(2);
				deviceclass
						.setparams(deviceid.getText().toString(),
								MyApplication.homeid,
								name.getText().toString(), oldphone.getText()
										.toString(), shuaidaophone.getText()
										.toString(), secret.getText()
										.toString(),secret.getText()
										.toString(),createtime, "Add");
				if (deviceclass.startthread()) {
					// 本地数据库插入
					peoplelist sdlist = peoplelist.getInstance();
					ShuaidaoDeviceinfor newsddinfor = new ShuaidaoDeviceinfor(
							deviceid.getText().toString(), name.getText()
									.toString(), oldphone.getText().toString(),
							shuaidaophone.getText().toString(), secret
									.getText().toString(), createtime);
					sdlist.addDevice(newsddinfor, getActivity());

					Devicelistfragmentpage.devicelist.add(newsddinfor);
					Log.v(getTag(), newsddinfor.deviceid);
					Devicelistfragmentpage.refresh(getActivity());
				}
			} else {
				Toast.makeText(getActivity(), "请填写正确的格式！", Toast.LENGTH_SHORT)
						.show();
			}

		}
	}

}
