package com.kingyee.vipclass.controller.jsoup;

import java.io.IOException;
import java.util.Optional;

public class test1 {
	public static void main(String[] args) throws IOException, InterruptedException {
		
		String s = "member.php?mod=logging&action=login&loginsubmit=yes&loginhash=LLy4w";
		String[] strs = s.split("&");
		System.out.println(strs);

//		boolean isAccess = Optional.ofNullable(wechatUser).map(WechatUser::getWuPradConsent).filter(value -> value.equals(1)).isPresent();
	}
}
