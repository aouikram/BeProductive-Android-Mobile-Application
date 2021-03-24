package com.example.main.users;

public class User {
    private int id;
    private String name;
    private String lastname;
    private int birthday;
    private String mail;
    private String password;
    
    public int getId(){
        return id;
    }
    public void setId(int id){
        this.id=id;
    }
    public String getName(){
        return  name;
    }
    public void setName(String name){
        this.name=name;
    }
    public String getLastname(){
        return lastname;
    }
    public void setLastname(String lastname){
        this.lastname=lastname;
    }
    public int getBirthday(){
        return birthday;
    }
    public void setBirthday(int birthday){
        this.birthday=birthday;
    }
    public String getMail(){
        return  mail;
    }
    public void setMail(String mail){
        this.mail=mail;
    }
    public String getPassword(){
        return password;
    }
    public void setPassword(String password){
        this.password=password;
    }
}
