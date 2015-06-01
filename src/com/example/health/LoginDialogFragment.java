/*package com.example.health;

import com.example.health.util.NetTool;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

public class LoginDialogFragment extends DialogFragment
{
	private EditText mUserID;
	private EditText mUsername;
	private EditText mUserpassward;
	public interface LoginInputListener
	{
		void onLoginInputComplete(String username, String userID, String passward);
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		// Get the layout inflater
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View view = inflater.inflate(R.layout.fragment_login_dialog, null);
		mUsername = (EditText) view.findViewById(R.id.id_txt_username);
		mUserID = (EditText) view.findViewById(R.id.id_txt_ID);
		mUserpassward = (EditText) view.findViewById(R.id.id_txt_pass);
		// Inflate and set the layout for the dialog
		// Pass null as the parent view because its going in the dialog layout
		builder.setView(view)
				// Add action buttons
				.setPositiveButton("确定",
						new DialogInterface.OnClickListener()
						{
							@Override
							public void onClick(DialogInterface dialog, int id)
							{
								if(NetTool.isInternetConnect()) {
									LoginInputListener listener = (LoginInputListener) getActivity();
									listener.onLoginInputComplete(mUsername.getText().toString().trim(), 
											mUserID.getText().toString().trim(),mUserpassward.getText().toString().trim());
								}else {
									 Toast.makeText(MyApplication.getContext(), "请保持网络连接", Toast.LENGTH_SHORT).show();
								}
								
							}
						});
		return builder.create();
	}
}
*/