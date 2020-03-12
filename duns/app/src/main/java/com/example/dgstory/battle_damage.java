package com.example.dgstory;

import java.util.Random;

/**
 * Created by user on 2017-10-22.
 */

public class battle_damage {
                                //적의 공격인가 ,공격자 레벨, 공격자의 공격력, 공격자의 고정데미지,  공격자 명중률
                                //피격자의 레벨, 피격자의 방어력, 피격자의 방어율,
    public int getDamageCalculation(boolean enermy,int Attacker_lv, float Attacker_atk,float Attacker_fixAtk, int Attacker_hit
                                    ,int Deffender_lv, float Deffender_def, float Deffender_p_def){
        Random random = new Random();
        float damgeSum,viranc;
        viranc = random.nextInt(31);
        if(viranc < 15){
            viranc = -viranc;
        }else{
            viranc -= 15;
        }
        if(enermy){
            //피격자가 플레이어라면 방어율 공식을 적용하지 않는다
            Deffender_p_def = 1;
        }

        /**------------------------------------------------------------------------------------------------
         * 데미지 공식
         --------------------------------------------------------------------------------------------------*/
        damgeSum = Attacker_atk * 2.5f - Deffender_def * 1.3f;
        damgeSum -= damgeSum / 100f * Deffender_p_def;
        damgeSum += damgeSum / 100f * viranc;
        damgeSum += Attacker_fixAtk;


        if(!enermy) {
            //크리티컬 5%
            viranc = random.nextInt(101);
            if (viranc >= 95) {
                viranc -= 95;
                viranc *= 0.1;
                damgeSum += damgeSum * viranc;
            }
        }
        /**------------------------------------------------------------------------------------------------
         *  명중률 공식
         --------------------------------------------------------------------------------------------------*/
        if((Deffender_lv - Attacker_lv) > 4 ){
            //레벨 5이상이 차이난다면 그 이후 레벨당 4% 만큼 명중률이 차감 된다.
            Attacker_hit -= ((Deffender_lv - Attacker_lv - 4) * 4);
        }
        Attacker_hit *= 10;
        /**------------------------------------------------------------------------------------------------
         *  데미지 처리 반환
         --------------------------------------------------------------------------------------------------*/
        int fixHit = random.nextInt(1001);
        Attacker_hit -= fixHit;
        if((Attacker_hit < 1) || (damgeSum < 1)){
            //Miss 가 판단되거나 데미지가 0보다 작다면 데미지를 0으로 반환한다.
            damgeSum = 0;
        }
        return (int)damgeSum;
    }
}
