package com.kzh.sys.enums;

/**
 * Created by IntelliJ IDEA.
 */
public enum Months {
    JAN("01", "一月", "jan", "January"),
    FEB("02", "二月", "feb", "February"),
    MAR("03", "三月", "mar", "March"),
    APR("04", "四月", "apr", "April"),
    MAY("05", "五月", "may", "May"),
    JUN("06", "六月", "jun", "June"),
    JUL("07", "七月", "jul", "July"),
    AUG("08", "八月", "aug", "August"),
    SEP("09", "九月", "sep", "September"),
    OCT("10", "十月", "oct", "September"),
    NOV("11", "十一月", "nov", "September"),
    DEC("12", "十二月", "dec", "September"),
    ;

    private Months(String no, String name, String forShort, String fullName) {
        this.no = no;
        this.name = name;
        this.forShort = forShort;
        this.fullName = fullName;
    }

    private String no;
    private String name;
    private String forShort;
    private String fullName;

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getForShort() {
        return forShort;
    }

    public void setForShort(String forShort) {
        this.forShort = forShort;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
