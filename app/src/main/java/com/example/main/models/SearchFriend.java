package com.example.main.models;

public class SearchFriend {
    public String profileimage;
    public String fullname;
    public String age;

    public SearchFriend()
    {

    }

    public SearchFriend(String profileimage, String fullname, String age)
    {
        this.profileimage = profileimage;
        this.fullname = fullname;
        this.age = age;
    }

    public String getProfileimage() {
        return profileimage;
    }

    public String getFullname() {
        return fullname;
    }

    public void setProfileimage(String profileimage) {
        this.profileimage = profileimage;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public void setAge(String age) { this.age = age;}

    public String getAge() {
        return age;
    }
}

