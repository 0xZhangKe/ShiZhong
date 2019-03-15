package com.zhangke.shizhong.model.other;

/**
 * Created by ZhangKe on 2019/3/15.
 */
public class MonthSalaryEntity {

    private int month;
    private double salary;

    public MonthSalaryEntity() {
    }

    public MonthSalaryEntity(int month) {
        this(month, 0.0);
    }

    public MonthSalaryEntity(int month, double salary) {
        this.month = month;
        this.salary = salary;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }
}
