package com.example.myapplication.ui.grouplist;

import java.util.ArrayList;
import java.util.List;

public class GroupData {
    public List<GroupData> GroupDataList = new ArrayList<>(); //여러 사람의 정보를 담기위한 배열선언//
    String Group_id;
    int Group_goal;
    int Group_period_unit;
    int Group_members;
    String Group_template;
    String Group_unit;
    String Group_period_start;
    String Group_period_end;
    String Group_goal_unit;

    //getter/setter설정//
    public String getGroup_id() {
        return Group_id;
    }

    public void setGroup_id(String Group_id) {
        this.Group_id = Group_id;
    }

    public int getGroup_goal() {
        return Group_goal;
    }

    public void setGroup_goal(int Group_goal) {
        this.Group_goal = Group_goal;
    }

    public int getGroup_period_unit() {
        return Group_period_unit;
    }

    public void setGroup_period_unit(int Group_period_unit) {
        this.Group_period_unit = Group_period_unit;
    }

    public int getGroup_members() {
        return Group_members;
    }

    public void setGroup_members(int Group_members) {
        this.Group_members = Group_members;
    }



    public String getGroup_template() {
        return Group_template;
    }

    public void setGroup_template(String Group_template) {
        this.Group_template = Group_template;
    }

    public String getGroup_unit() {
        return Group_unit;
    }

    public void setGroup_unit(String Group_unit) {
        this.Group_unit = Group_unit;
    }



    public String getGroup_period_start() {
        return Group_period_start;
    }

    public void setGroup_period_start(String Group_period_start) {
        this.Group_period_start = Group_period_start;
    }

    public String getGroup_period_end() {
        return Group_period_end;
    }

    public void setGroup_period_end(String Group_period_end) {
        this.Group_period_end = Group_period_end;
    }

    public String getGroup_goal_unit() {
        return Group_goal_unit;
    }

    public void setGroup_goal_unit(String Group_goal_unit) {
        this.Group_goal_unit = Group_goal_unit;
    }

    public List<GroupData> getGroupDataList() {
        return GroupDataList;
    }

    public void setGroupDataList(List<GroupData> GroupDataList) {
        this.GroupDataList = GroupDataList;
    }
}
