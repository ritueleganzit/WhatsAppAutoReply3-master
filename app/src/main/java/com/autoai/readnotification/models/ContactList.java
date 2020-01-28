package com.autoai.readnotification.models;

public class ContactList {
    String name,number,msg;

    public ContactList(String name, String number, String msg) {
        this.name = name;
        this.number = number;
        this.msg = msg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
