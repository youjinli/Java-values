package com.weshare.harbor.utils;

import com.weshare.varanus.service.impl.BankcardVarServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BlurMatchingNameUtil {


    private static final Logger logger = LoggerFactory.getLogger(BankcardVarServiceImpl.class);

    /**
     * 模糊匹配，比较姓名，返回匹配率
     *
     * @param inputName 用户录入姓名
     * @param returnName 银行返回姓名
     * @return 匹配率
     */
    public static float checkName(String inputName, String returnName) {
        logger.info("BlurMatchingNameUtil checkName inputName=[{}],returnName=[{}],",inputName,returnName);
        if (null == inputName || null == returnName) {
            return 0;
        }

        String filterInputName = inputName.toUpperCase();
        // 去掉除字母外的字符再比较，避免乱码或数字等非法字符影响比较结果
        String filteredReturnName = returnName.toUpperCase().replaceAll("[^A-Z ]", "");

        // 名字相等
        if (filterInputName != null && filterInputName.equalsIgnoreCase(filteredReturnName)) {
            return 1;
        }


        String usernameVerifySimilarRate = "0.8:0.5";
        if (StringUtils.isEmpty(usernameVerifySimilarRate)) {
            return 0;
        }
        float maxSimilarRate = Float.valueOf(usernameVerifySimilarRate.split(":")[0]);
        float minSimilarRate = Float.valueOf(usernameVerifySimilarRate.split(":")[1]);
        //检查名字配置度
        float similarRate = getSimilarRate(filterInputName, filteredReturnName);
        if (similarRate >= maxSimilarRate) {
            return similarRate;
        }

        if (similarRate > minSimilarRate && similarRate < maxSimilarRate) {
            similarRate = matchString(filterInputName, filteredReturnName);
            if (similarRate > maxSimilarRate) {
                return similarRate;
            }
        }
        logger.info("checkName similarRate,similarRate=[{}],",similarRate);
        return similarRate;

    }

    /**
     * 莱文斯坦距离
     *
     * @param s1 字符串一
     * @param s2 字符串二
     * @return 匹配率
     */
    public static float getSimilarRate(String s1, String s2) {

        char[] sa;
        int n;
        int p[]; // 'previous' cost array, horizontally
        int d[]; // cost array, horizontally
        int _d[]; // placeholder to assist in swapping p and d

        sa = s1.toCharArray();
        n = sa.length;
        p = new int[n + 1];
        d = new int[n + 1];

        final int m = s2.length();
        if (n == 0 || m == 0) {
            if (n == m) {
                return 1;
            } else {
                return 0;
            }
        }

        // indexes into strings s and t
        int i; // iterates through s
        int j; // iterates through t
        char t_j; // jth character of t
        int cost; // cost

        for (i = 0; i <= n; i++) {
            p[i] = i;
        }

        for (j = 1; j <= m; j++) {
            t_j = s2.charAt(j - 1);
            d[0] = j;
            for (i = 1; i <= n; i++) {
                cost = sa[i - 1] == t_j ? 0 : 1;
                // minimum of cell to the left+1, to the top+1, diagonally left
                // and up +cost
                d[i] = Math.min(Math.min(d[i - 1] + 1, p[i] + 1), p[i - 1] + cost);
            }

            // copy current distance counts to 'previous row' distance counts
            _d = p;
            p = d;
            d = _d;
        }


        // our last action in the above loop was to switch d and p, so p now
        // actually has the most recent cost counts
        return 1.0f - ((float) p[n] / Math.max(s2.length(), sa.length));

    }


    /**
     * 支持匹配缩写、截串（风控提供）
     *
     * @param var1
     * @param var2
     * @return
     */
    public static float matchString(String var1, String var2) {
        int pos1 = 1;
        int pos2 = 1;
        int match_char1 = 0;
        int match_char2 = 0;
        if (var1 != null && !var1.equals("")) {
            var1 = var1.replace(" ", "").toLowerCase();
        }
        if (var2 != null && !var2.equals("")) {
            var2 = var2.replace(" ", "").toLowerCase();
        }
        if (var1 == null || var1.length() == 1) {
            return 0;
        } else if (var2 == null || var2.length() == 1) {
            return 0;
        } else if (var1.contains(var2) || var2.contains(var1)) {
            return 1;
        } else {
            for (int i = 1; i <= var1.length(); i++) {
                for (int j = pos1; j <= var2.length(); j++) {
                    if (var1.substring(i - 1, i).equals(var2.substring(j - 1, j))) {
                        match_char1 = match_char1 + 1;
                        pos1 = j + 1;
                        break;
                    }
                }
            }
            float out1 = (float) match_char1 / (float) var1.length();

            for (int i = 1; i <= var2.length(); i++) {
                for (int j = pos2; j <= var1.length(); j++) {
                    if (var2.substring(i - 1, i).equals(var1.substring(j - 1, j))) {
                        match_char2 = match_char2 + 1;
                        pos2 = j + 1;
                        break;
                    }
                }
            }
            float out2 = (float) match_char2 / (float) var2.length();
            return Math.max(out1, out2);
        }
    }
}
