package com.kingyee.prad.common;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.CRC32;

public class EmtUtil {

	public static void main(String[] args) {
		String[] hashs = hashUserInfo(1914214);
		System.out.println("Hashid:" + hashs[0] + ",checkid:" + hashs[1]);
	}
	
	public static String[] hashUserInfo(long userid) {
		String[] infos = new String[2];
		infos[0] = hashUser(userid, "dasfgfsdbz");	//hashId固定参数
		infos[1] = hashUser(userid, "hiewrsbzxc");	//checkId固定参数
		return infos;
	}
	
	private static String hashUser(long userid, String encodeStr) {
		// $crc = intval(sprintf('%u', crc32($downloadKey .
		// "asdfwrew.USER_SEED")));
		// $hash = $crc - $user;
		// $hash2 = sprintf('%u', crc32($hash . 'werhhs.USER_SEED2'));
		// $k1 = substr($hash2, 0, 3);
		// $k2 = substr($hash2, -2);
		// return $k1 . $hash . $k2;
		
		final String key = "asdfwrew.USER_SEED";
		final String key2 = "werhhs.USER_SEED2";
		
		CRC32 crc32 = new CRC32();
		crc32.update((encodeStr + key).getBytes());
		long hash = crc32.getValue() - userid;
		
		crc32 = new CRC32();
		crc32.update((hash + key2).getBytes());
		long hash2 = crc32.getValue();
		
		String temp = String.valueOf(hash2);
		String k1 = temp.substring(0, 3);
		String k2 = temp.substring(temp.length() - 2);
		return k1 + hash + k2;
	}
	public static String unicodeToString(String str) {

		Pattern pattern = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");
		Matcher matcher = pattern.matcher(str);
		char ch;
		while (matcher.find()) {
			//group 6728
			String group = matcher.group(2);
			//ch:'木' 26408
			ch = (char) Integer.parseInt(group, 16);
			//group1 \u6728
			String group1 = matcher.group(1);
			str = str.replace(group1, ch + "");
		}
		return str;
	}

}
