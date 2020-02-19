package com.anroidbrowser.browser.myTypes;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 13.09.17.
 */
public class Person {
    private String name;
    private String family;
    private String age;
    private String sex;

    private String pass;
    private String mail;
    private String country;
    private String city;
    private long id;
    private List<HistoryElement> historyElementList = new ArrayList<HistoryElement>();
    private List<HistoryElement> bestElementList = new ArrayList<HistoryElement>();
    private List<HistoryElement> downloadsList = new ArrayList<HistoryElement>();
    private String avatar;

    public Person(String[] datas, long id, List<HistoryElement> historyElementList, List<HistoryElement> bestElementList, List<HistoryElement> downloadsList) {

        this.name = datas[0];
        this.family = datas[1];
        this.age = datas[2];
        this.sex = datas[3];
        this.mail = datas[4];
        this.pass = datas[5];
        this.country = datas[6];
        this.city = datas[7];
        this.avatar = datas[8];
        if(historyElementList != null)
            this.historyElementList = historyElementList;
        if(bestElementList != null)
            this.bestElementList = bestElementList;
        if(downloadsList != null)
            this.downloadsList = downloadsList;



        this.id = id;
    }

    public String getMail() {
        return mail;
    }

    public String getName() {
        return name;
    }

    public String getPass() {
        return pass;
    }

    public long getId() {
        return id;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public String getAge() {
        return age;
    }

    public String getSex() {
        return sex;
    }

    public String getFamily() {
        return family;
    }

    public List<HistoryElement> getHistoryElementList() {
        return historyElementList;
    }

    public void addHistory(HistoryElement e){

        historyElementList.add(e);
    }
    public List<HistoryElement> getBestElementList() {
        return bestElementList;
    }

    public void addBest(HistoryElement e){

        bestElementList.add(e);
    }

    public List<HistoryElement> getDownloadsList() {
        return downloadsList;
    }

    public void addDownload(HistoryElement e){

        downloadsList.add(e);
    }

    public String getAvatar() {
        return avatar;
    }
}
