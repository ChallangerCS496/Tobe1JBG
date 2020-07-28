package com.example.myapplication.ui.grouplist;

import java.util.ArrayList;
import java.util.List;

public class PersonData {
    public List<PersonData> PersonDataList = new ArrayList<>(); //여러 사람의 정보를 담기위한 배열선언//
    String Person_id;

    int Person_goal;
    int Person_period_unit;
    int Person_index;
    int Person_history;
    String Person_goal_unit;
    String Person_unit;

    //getter/setter설정//
    public String getPerson_id() {
        return Person_id;
    }

    public void setPerson_id(String Person_id) {
        this.Person_id = Person_id;
    }

    public int getPerson_goal() {
        return Person_goal;
    }

    public void setPerson_history(int Person_history) {
        this.Person_history = Person_history;
    }

    public int getPerson_history() {
        return Person_history;
    }

    public void setPerson_goal(int Person_goal) {
        this.Person_goal = Person_goal;
    }

    public int getPerson_period_unit() {
        return Person_period_unit;
    }

    public void setPerson_period_unit(int Person_period_unit) {
        this.Person_period_unit = Person_period_unit;
    }

    public int getPerson_index() {
        return Person_index;
    }

    public void setPerson_index(int Person_index) {
        this.Person_index = Person_index;
    }


    public String getPerson_goal_unit() {
        return Person_goal_unit;
    }

    public void setPerson_goal_unit(String Person_goal_unit) {
        this.Person_goal_unit = Person_goal_unit;
    }

    public String getPerson_unit() {
        return Person_unit;
    }

    public void setPerson_unit(String Person_unit) {
        this.Person_unit = Person_unit;
    }

    public List<PersonData> getPersonDataList() {
        return PersonDataList;
    }

    public void setPersonDataList(List<PersonData> PersonDataList) {
        this.PersonDataList = PersonDataList;
    }
}
