package com.example.dgstory;

/**
 * Created by user on 2017-10-07.
 */

public class battle_enermy {
    public String name;
    public int hp;
    public int atk;
    public int def;
    public int lv;
    public int f_atk;   //고정 공격력
    public int p_def;   //방어율

    public battle_enermy(String name){
        //몹 찾기용 생성자
        this.name = name;
    }

    public battle_enermy(String name, int lv, int hp, int atk, int def, int f_atk, int p_def) {
        this.hp = hp;
        this.name = name;
        this.atk = atk;
        this.def = def;
        this.lv = lv;
        this.f_atk = f_atk;
        this.p_def = p_def;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        battle_enermy temp = (battle_enermy) obj;
        if((temp.name).equals(name)){
            return true;
        }
        else
            return false;
    }
}
