package com.phone.apple.getphone_contacts.a_z;

/**
 * 创 建 人 PeaceJay
 * 创建时间 2019/8/26
 * 类 描 述：
 */
public class BeanPhoneDto {
    private String name;        //联系人姓名
    private String telPhone;    //电话号码
    private String sortLetters;//显示数据拼音的首字母

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTelPhone() {
        return telPhone;
    }

    public void setTelPhone(String telPhone) {
        this.telPhone = telPhone;
    }

    public BeanPhoneDto() {
    }
    public String getSortLetters() {
        return sortLetters;
    }

    public void setSortLetters(String sortLetters) {
        this.sortLetters = sortLetters;
    }

    public BeanPhoneDto(String name, String telPhone) {
        this.name = name;
        this.telPhone = telPhone;
    }
}