package com.zhangke.shizhong.util;

import java.text.DecimalFormat;

/**
 * Created by ZhangKe on 2019/3/15.
 */
public class TaxHelper {

    /**
     * 获取扣税总和
     *
     * @param salaryArray 每月工资
     * @param insurance   五险一金
     * @param special     专项附加扣除
     */
    public static String getTaxDescription(double[] salaryArray, double insurance, double[] special) {
        if (salaryArray == null || salaryArray.length == 0) return "";
        StringBuilder taxBuilder = new StringBuilder();
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        double taxTotal = 0;//累计扣税
        double salaryTotal = 0;//工资累计金额
        for (int i = 0; i < salaryArray.length; i++) {
            salaryTotal += salaryArray[i];
            double specialTotal = 0;
            if (special != null && special.length > 0) {
                for (double item : special) {
                    specialTotal += item * (i + 1);
                }
            }
            //应纳税所得额度
            double needTax = salaryTotal - 5000 * (i + 1) - specialTotal - insurance * (i + 1);
            double thisMonthTax;
            if (needTax >= 960000) {
                thisMonthTax = needTax * 0.45 - 181920 - taxTotal;
            } else if (needTax >= 660000) {
                thisMonthTax = needTax * 0.35 - 85920 - taxTotal;
            } else if (needTax >= 420000) {
                thisMonthTax = needTax * 0.30 - 52920 - taxTotal;
            } else if (needTax >= 300000) {
                thisMonthTax = needTax * 0.25 - 31920 - taxTotal;
            } else if (needTax >= 144000) {
                thisMonthTax = needTax * 0.20 - 16920 - taxTotal;
            } else if (needTax >= 36000) {
                thisMonthTax = needTax * 0.1 - 2520 - taxTotal;
            } else if (needTax > 0) {
                thisMonthTax = needTax * 0.03 - taxTotal;
            } else {
                thisMonthTax = 0;
            }
            taxTotal += thisMonthTax;
            taxBuilder.append(String.format(
                    "第%s月份工资：%s，应纳税额：%s元，到手工资：%s，当前纳税总额：%s元\n\n",
                    i + 1,
                    salaryArray[i],
                    decimalFormat.format(thisMonthTax),
                    salaryArray[i] - thisMonthTax - insurance,
                    decimalFormat.format(taxTotal)));
        }
        taxBuilder.append("累计扣税：");
        taxBuilder.append(taxTotal);
        return taxBuilder.toString();
    }

}
