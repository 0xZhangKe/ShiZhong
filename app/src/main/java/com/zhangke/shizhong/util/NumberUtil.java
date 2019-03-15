package com.zhangke.shizhong.util;

/**
 * Created by ZhangKe on 2018/12/29.
 */
public class NumberUtil {

    private static double epsilon = 0.00000001;

    public static boolean equals(float a, float b){
        return Math.abs(a - b) < epsilon;
    }

    public static boolean equals(double a, double b){
        return Math.abs(a - b) < epsilon;
    }
}
