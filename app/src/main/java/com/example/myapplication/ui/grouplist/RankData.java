package com.example.myapplication.ui.grouplist;

import java.util.ArrayList;
import java.util.List;

public class RankData {
    public List<RankData> RankDataList = new ArrayList<>(); //여러 사람의 정보를 담기위한 배열선언//
    String Rank_id;
    int Rank_index;
    int Rank_total;
    String Rank_name;
    String Rank_nickname;
    String Rank_unit;

    //getter/setter설정//
    public String getRank_id() {
        return Rank_id;
    }

    public void setRank_id(String Rank_id) {
        this.Rank_id = Rank_id;
    }


    public void setRank_total(int Rank_total) {
        this.Rank_total = Rank_total;
    }

    public int getRank_total() {
        return Rank_total;
    }



    public int getRank_index() {
        return Rank_index;
    }

    public void setRank_index(int Rank_index) {
        this.Rank_index = Rank_index;
    }



    public String getRank_name() {
        return Rank_name;
    }

    public void setRank_name(String Rank_name) {
        this.Rank_name = Rank_name;
    }

    public String getRank_nickname() {
        return Rank_nickname;
    }

    public void setRank_nickname(String Rank_nickname) {
        this.Rank_nickname = Rank_nickname;
    }



    public String getRank_unit() {
        return Rank_unit;
    }

    public void setRank_unit(String Rank_unit) {
        this.Rank_unit = Rank_unit;
    }



    public List<RankData> getRankDataList() {
        return RankDataList;
    }

    public void setRankDataList(List<RankData> RankDataList) {
        this.RankDataList = RankDataList;
    }
}
