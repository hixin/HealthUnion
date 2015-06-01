package com.yeran.healthhomecare.tools;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则匹配，判断是否是符合电话号码或者密码的输入
 * */
public class numorstring {
	
	public boolean isMobileNO(String mobiles) {
		Pattern p = Pattern
				.compile("^((13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$");
		Matcher m = p.matcher(mobiles);
		System.out.println("是否匹配成功" + m.matches());
		return m.matches();
	}

	public boolean isPwnum(String pwnum) {
		if (pwnum.length() == 6) {
			Pattern p = Pattern.compile("[0-9]*");
			Matcher m = p.matcher(pwnum);
			return m.matches();
		} else {
			return false;
		}
	}

}
