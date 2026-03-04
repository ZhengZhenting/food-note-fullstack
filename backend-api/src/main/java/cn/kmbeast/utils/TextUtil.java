package cn.kmbeast.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * text utils
 */

public class TextUtil {
    /**
     * get sub-text
     * @param targetStr target text
     * @param fontNumber number of texts
     * @return
     */
    public static String parseText(String targetStr, Integer fontNumber) {
        if(targetStr == null || targetStr.equals("")) {
            return "";
        }

        Document document = Jsoup.parse(targetStr);
        String text = document.text();
        if(text.length() < fontNumber) {
            return text;
        }
        return text.substring(0,fontNumber) + "...";
    }
}
