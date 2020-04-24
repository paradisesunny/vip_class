package com.kingyee.common.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 生成随机的字符串
 * @author peihong
 *
 */
public class RandomUtil {

	public static final String allChar = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
	public static final String letterChar = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
	public static final String numberChar = "0123456789";

	
	/**
	 * 取得指定范围的随机数
	 * @param min 最小值
	 * @param max 最大值
	 * @return
	 * @throws Exception 
	 */
	public static int generateIntRange(int min, int max){
		if(min >= max){
			throw new RuntimeException("参数中，最小指" + min + "不应该大于最大值" + max);
		}
		return (int) ((max - min + 1) * Math.random() + min);
	}
	
	/**
	 * 返回一个定长的随机字符串(只包含大小数字)
	 * 
	 * @param length
	 *            随机字符串长度
	 * @return 随机字符串
	 */
	public static String generateIntString(int length) {
		StringBuffer sb = new StringBuffer();
		Random random = new Random();
		for (int i = 0; i < length; i++) {
			sb.append(numberChar.charAt(random.nextInt(numberChar.length())));
		}
		return sb.toString();
	}

	
	/**
	 * 返回一个定长的随机字符串(只包含大小写字母、数字)
	 * 
	 * @param length
	 *            随机字符串长度
	 * @return 随机字符串
	 */
	public static String generateString(int length) {
		StringBuffer sb = new StringBuffer();
		Random random = new Random();
		for (int i = 0; i < length; i++) {
			sb.append(allChar.charAt(random.nextInt(allChar.length())));
		}
		return sb.toString();
	}

	/**
	 * 返回一个定长的随机纯字母字符串(只包含大小写字母)
	 * 
	 * @param length
	 *            随机字符串长度
	 * @return 随机字符串
	 */
	public static String generateMixString(int length) {
		StringBuffer sb = new StringBuffer();
		Random random = new Random();
		for (int i = 0; i < length; i++) {
			sb.append(allChar.charAt(random.nextInt(letterChar.length())));
		}
		return sb.toString();
	}

	/**
	 * 返回一个定长的随机纯大写字母字符串(只包含大小写字母)
	 * 
	 * @param length
	 *            随机字符串长度
	 * @return 随机字符串
	 */
	public static String generateLowerString(int length) {
		return generateMixString(length).toLowerCase();
	}

	/**
	 * 返回一个定长的随机纯小写字母字符串(只包含大小写字母)
	 * 
	 * @param length
	 *            随机字符串长度
	 * @return 随机字符串
	 */
	public static String generateUpperString(int length) {
		return generateMixString(length).toUpperCase();
	}

	/**
	 * 生成一个定长的纯0字符串
	 * 
	 * @param length
	 *            字符串长度
	 * @return 纯0字符串
	 */
	public static String generateZeroString(int length) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			sb.append('0');
		}
		return sb.toString();
	}

	/**
	 * 根据数字生成一个定长的字符串，长度不够前面补0
	 * 
	 * @param num
	 *            数字
	 * @param fixdlenth
	 *            字符串长度
	 * @return 定长的字符串
	 */
	public static String toFixdLengthString(long num, int fixdlenth) {
		StringBuffer sb = new StringBuffer();
		String strNum = String.valueOf(num);
		if (fixdlenth - strNum.length() >= 0) {
			sb.append(generateZeroString(fixdlenth - strNum.length()));
		} else {
			throw new RuntimeException("将数字" + num + "转化为长度为" + fixdlenth
					+ "的字符串发生异常！");
		}
		sb.append(strNum);
		return sb.toString();
	}

	/**
	 * 根据数字生成一个定长的字符串，长度不够前面补0
	 * 
	 * @param num
	 *            数字
	 * @param fixdlenth
	 *            字符串长度
	 * @return 定长的字符串
	 */
	public static String toFixdLengthString(int num, int fixdlenth) {
		StringBuffer sb = new StringBuffer();
		String strNum = String.valueOf(num);
		if (fixdlenth - strNum.length() >= 0) {
			sb.append(generateZeroString(fixdlenth - strNum.length()));
		} else {
			throw new RuntimeException("将数字" + num + "转化为长度为" + fixdlenth
					+ "的字符串发生异常！");
		}
		sb.append(strNum);
		return sb.toString();
	}


    /**
     * 随机从list中抽取出num个，组成新的list返回
     * @param list
     * @param num
     * @param <E>
     * @return
     */
	public static <E> List<E> getNumOfList(List<E> list, int num){
	    List<E> nList = new ArrayList<>();
	    for(int i = 0; i < num; i++){
            int length = list.size();
            int index = generateIntRange(0, length - 1);
            nList.add(list.get(index));
            list.remove(index);
        }
        return nList;
    }


	public static void main(String[] args) {
//		System.out.println(generateIntRange(10, 20));
//		System.out.println(generateIntString(6));
//		System.out.println(generateString(15));
//		System.out.println(generateMixString(15));
//		System.out.println(generateLowerString(15));
//		System.out.println(generateUpperString(15));
//		System.out.println(generateZeroString(15));
//		System.out.println(toFixdLengthString(123, 15));
//		System.out.println(toFixdLengthString(123L, 15));

		List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);
        list.add(6);
        list.add(7);
        list.add(8);
        list.add(9);

        List<Integer> nList = getNumOfList(list, 6);
        for (Integer i : nList){
            System.out.println(i);
        }

        System.out.println("=============");

        list.removeAll(nList);

        for (Integer i : list){
            System.out.println(i);
        }
	}
}
