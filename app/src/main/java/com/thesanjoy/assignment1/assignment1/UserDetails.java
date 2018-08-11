package com.thesanjoy.assignment1.assignment1;

public class UserDetails {
    public String name;
    public String email;

    public UserDetails(){};
    public UserDetails(String name, String email, String mNo, String hNo) {
        this.name = name;
        this.email = email;
        this.mNo = mNo;
        this.hNo = hNo;
    }

    public String mNo;
    public String hNo;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getmNo() {
        return mNo;
    }

    public void setmNo(String mNo) {
        this.mNo = mNo;
    }

    public String gethNo() {
        return hNo;
    }

    public void sethNo(String hNo) {
        this.hNo = hNo;
    }
}
