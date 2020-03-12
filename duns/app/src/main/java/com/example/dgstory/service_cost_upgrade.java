package com.example.dgstory;

/**
 * Created by user on 2017-10-10.
 */

public class service_cost_upgrade {

    public int setHpLevel(int myHpLevel){
        int cost = 0;
        int cost_level = myHpLevel / 10;

        switch (cost_level){
            case 0: cost= myHpLevel * 300; break;
            case 1: cost= myHpLevel * 500; break;
            case 2: cost= 10000 + ((myHpLevel-20) * 2000);break;
            case 3: cost= 30000 + ((myHpLevel-30) * 2000);break;
            case 4: cost= 50000 + ((myHpLevel-40) * 5000);break;
            case 5: cost= 100000 + ((myHpLevel-50) * 5000);break;
            case 6: cost= 150000 + ((myHpLevel-60) * 5000);break;
            case 7: cost= 200000 + ((myHpLevel-70) * 10000);break;
            case 8: cost= 300000 + ((myHpLevel-80) * 20000);break;
            case 9: cost= 500000 + ((myHpLevel-50) * 50000);break;
            default:cost=0; break;
        }

        return cost;
    }

    public int setMpLevel(int myMpLevel){
        int cost = 0;

        return cost;
    }

    public int setAtkLevel(int myAtkLevel){
        int cost = 0;

        return cost;
    }

    public int setDefLevel(int myDefLevel){
        int cost = 0;

        return cost;
    }

    public int setSpeedLevel(int mySpeedLevel){
        int cost = 0;

        return cost;
    }
}
