package com.zhangke.shizhong.util;

import java.text.DecimalFormat;

/**
 * 数字格式化
 * <p>
 * Created by ZhangKe on 2019/2/18.
 */
public class NumberFormatUtil {

    private static DecimalFormat oneDecimalFormat = new DecimalFormat("0.0");
    private static DecimalFormat twoDecimalFormat = new DecimalFormat("0.00");

    /**
     * 如果小数点后面有值则保留一位小数，否则不显示小数点及之后数字
     */
    public static String formatOnePoint(double src) {
        if (NumberUtil.equals(src, 0.0)) {
            return "0";
        }
        if (src % 1.0 >= 0.01) {
            //小数点后面有数据
            return oneDecimalFormat.format(src);
        } else {
            return String.valueOf(Double.valueOf(src).intValue());
        }
    }

    /**
     * 如果小数点后面有值则保留两位小数，否则不显示小数点及之后数字
     */
    public static String formatPoint(double src) {
        if (NumberUtil.equals(src, 0.0)) {
            return "0";
        }
        if (src % 1.0 >= 0.01) {
            //小数点后面有数据
            return twoDecimalFormat.format(src);
        } else {
            return String.valueOf(Double.valueOf(src).intValue());
        }
    }

    /**
     * 将输入的数字格式化为保留两位小数点
     */
    public static String formatTwoDecimal(double src) {
        return twoDecimalFormat.format(src);
    }
}
