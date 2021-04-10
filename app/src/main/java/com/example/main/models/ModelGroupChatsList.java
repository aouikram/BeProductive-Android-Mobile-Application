package com.example.main.models;

import android.util.Log;

public class ModelGroupChatsList {
    String groupId , groupTitle ;

    public ModelGroupChatsList(){}

    public ModelGroupChatsList (String groupId , String groupTitle) {

        this.groupId=groupId;
        this.groupTitle=groupTitle;
        Log.v("TAG" , "Model created");
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupTitle() {
        return groupTitle;
    }

    public void setGroupTitle(String groupTitle) {
        this.groupTitle = groupTitle;
    }
}
