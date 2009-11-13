package org.jfree.chart.util;

/**
 * Created by IntelliJ IDEA.
 * User: Dan
 * Date: 13-Nov-2009
 * Time: 11:31:20
 * To change this template use File | Settings | File Templates.
 */
public class StringUtil {
    private StringUtil() {}

    public static boolean isEmpty(String s) {
        return s == null || "".equals(s.trim());
    }
}
