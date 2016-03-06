package com.moinut.littleaccount;

/**
 * Created by c on 2016/3/4.
 */
public class Card {
    private String id;
    private String name;
    private double money;
    private String time;

    public String getId() { return id; }
    public String getName() {return name;}
    public double getMoney() {return money;}
    public String getTime() {return time;}

    public Card(String id, String name,double money,String time){
        this.id = id;
        this.name = name;
        this.money = money;
        this.time = time;
    }

    public Card(String name,double money,String time) {
        this(null, name, money, time);
    }

}

