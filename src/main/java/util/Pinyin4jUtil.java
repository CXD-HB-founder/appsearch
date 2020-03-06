/**
 * Copyright (C), 2015-2020, XXX有限公司
 * <p>
 * FileName: Pinyin4jUtil
 * <p>
 * Author:   HASEE
 * <p>
 * Date:     2020/1/12 11:25
 * <p>
 * Description:
 * <p>
 * History:
 *
 * <XD>          <time>          <1.1>          <javaDamo>
 */
package util;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author HASEE

 * @create 2020/1/12

 * @since 1.0.0

 */

public class Pinyin4jUtil {

    /*** 中文字符格式 */
    private static final String CHINESE_PATTERN = "[\\u4E00-\\u9FA5]";

    private static final HanyuPinyinOutputFormat FORMAT = new HanyuPinyinOutputFormat();

    static {
        //设置拼音小写
        FORMAT.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        //设置不带音调
        FORMAT.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        //设置带v字符
        FORMAT.setVCharType(HanyuPinyinVCharType.WITH_V);
    }

    /**
     * 判断是否为中文字符
     * @param str
     * @return
     */
    public static boolean containsChinese(String str){
        return str.matches(".*"+CHINESE_PATTERN+".*");
    }

    public static String[] get(String hanyu) {
        String[] array = new String[2];
        //全拼
        StringBuilder pinyin = new StringBuilder();
        //首字母
        StringBuilder pinyin_first = new StringBuilder();
        for (int i = 0; i < hanyu.length(); i++) {
            try {
                String[] pinyins = PinyinHelper.
                        toHanyuPinyinStringArray(hanyu.charAt(i), FORMAT);
                //中文字符返回的字符串数组，可能为null或者长度为0
                //返回原始数组
                if(pinyins == null || pinyins.length == 0){
                    pinyin.append(hanyu.charAt(i));
                    pinyin_first.append(hanyu.charAt(i));
                }else {// 可以转换为拼音
                    pinyin.append(pinyins[0]);
                    pinyin_first.append(pinyins[0].charAt(0));
                }
            } catch (Exception e) {
                pinyin.append(hanyu.charAt(i));
                pinyin_first.append(hanyu.charAt(i));
            }
        }
        array[0] = pinyin.toString();
        array[1] = pinyin_first.toString();
        return array;
    }

    /**
     *
     * @param hanyu
     * @param fullSpell
     * @return
     */
    public static String[][] get(String hanyu, boolean fullSpell){
        String[][] array = new String[hanyu.length()][];
        //全拼
        StringBuilder pinyin = new StringBuilder();
        //首字母
        StringBuilder pinyin_first = new StringBuilder();
        for (int i = 0; i < hanyu.length(); i++) {
            try {
                String[] pinyins = PinyinHelper.
                        toHanyuPinyinStringArray(hanyu.charAt(i), FORMAT);
                //中文字符返回的字符串数组，可能为null或者长度为0
                //返回原始数组
                if(pinyins == null || pinyins.length == 0){
                    array[i] = new String[]{String.valueOf(hanyu.charAt(i))};
                }else {// 可以转换为拼音
                    array[i] = unique(pinyins, fullSpell);
                }
            } catch (Exception e) {
                array[i] = new String[]{String.valueOf(hanyu.charAt(i))};
            }
        }
        return array;
    }
    //拼音去重
    private static String[] unique(String[] pinyins, boolean fullSpell) {
        Set<String> set = new HashSet<>();
        for (int i = 0; i < pinyins.length; i++) {
            if(fullSpell){
                set.add(pinyins[i]);
            }else{
                set.add(String.valueOf(pinyins[i].charAt(0)));
            }
        }
        set.addAll(Arrays.asList(pinyins));
        return set.toArray(new String[set.size()]);

    }

    public static void main(String[] args) throws BadHanyuPinyinOutputFormatCombination {
        String[] pinyins = get("中华1人民A共和国");
        System.out.println(Arrays.toString(pinyins));
//        for(String[] array : get("中华1人民A共和国", true)){//true为全拼
//            System.out.println(Arrays.toString(array));
//        }
    }
}