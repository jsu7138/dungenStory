package com.example.dgstory;

import android.content.Context;
import android.content.Intent;

import java.util.Random;

import static com.badlogic.gdx.math.MathUtils.random;

/**
 * Created by user on 2017-10-02.
 */

public class service_CharacterInfo {
    //*기본 세이브 및 로드 변수
    public static String myName;            //이름
    public static int myCharacterNum;       //캐릭터도트
    public static int myLevel;              //레벨

    public static int myMaxHp;              //순수 HP
    public static int myMaxMp;              //순수 MP
    public static int myMaxSp;              //순수 SP
    public static int myExp;                //EXP
    public static int myATK;                //순수 공격력
    public static int myF_ATK;              //고정 데미지
    public static int myDEF;                //순수 방어력
    public static int mySpeed;              //이동 스피드
    public static int myA_Speed;            //공격 스피드(2당 1퍼)
    public static int myHit;                //명중률

    public static int myWeaponNum;          //무기 번호
    public static int myArmorNum;           //아머 번호
    public static int myArtefactNum;        //장신구 번호
    public static int myStage;              //최대 클리어 단계
    public static int myMoney;              //돈
    public static int myDays;               //휴식 횟수

    public static int myHpLevel;            //업글 단계
    public static int myMpLevel;
    public static int myAtkLevel;
    public static int myDefLevel;
    public static int mySpeedLevel;

    public static int mySkill1;             //액티브 스킬 번호
    public static int mySkill2;             //액티브 스킬 번호
    public static int mySkill3;             //패시브 스킬 번호
    //다단히트수, 공격횟수, 데미지, 타격 딜레이, 밀리는 거리 x축, 밀리는 거리 y축
    public static int skill1_1,skill1_2,skill1_3,skill1_4,skill1_5,skill1_6;
    public static int skill2_1,skill2_2,skill2_3,skill2_4,skill2_5,skill2_6;
    //상승되는 공격력,방어력,공격속도
    public static int skill3_1,skill3_2,skill3_3;

    //*세이브 및 로드 아이템변수

    //*전투에 사용 될 임시 변수
    public static int myCurrentHp;
    public static int myCurrentMp;
    public static int myCurrentSp;

    //*셋터 겟터----------------------------------------------------------------------------------
    public static String getMyName() {
        return myName;
    }

    public static void setMyName(String myName) {
        service_CharacterInfo.myName = myName;
    }

    public static int getMyCharacterNum() {
        return myCharacterNum;
    }

    public static void setMyCharacterNum(int myCharacterNum) {
        service_CharacterInfo.myCharacterNum = myCharacterNum;
    }

    public static int getMyLevel() {
        return myLevel;
    }

    public static void setMyLevel(int myLevel) {
        service_CharacterInfo.myLevel = myLevel;
    }

    public static int getMyMaxHp() {
        return myMaxHp;
    }

    public static void setMyMaxHp(int myMaxHp) {
        service_CharacterInfo.myMaxHp = myMaxHp;
    }

    public static int getMyMaxMp() {
        return myMaxMp;
    }

    public static void setMyMaxMp(int myMaxMp) {
        service_CharacterInfo.myMaxMp = myMaxMp;
    }

    public static int getMyMaxSp() {
        return myMaxSp;
    }

    public static void setMyMaxSp(int myMaxSp) {
        service_CharacterInfo.myMaxSp = myMaxSp;
    }

    public static int getMyExp() {
        return myExp;
    }

    public static void setMyExp(int myExp) {
        service_CharacterInfo.myExp = myExp;
    }

    public static int getMyATK() {
        return myATK;
    }

    public static void setMyATK(int myATK) {
        service_CharacterInfo.myATK = myATK;
    }

    public static int getMyDEF() {
        return myDEF;
    }

    public static void setMyDEF(int myDEF) {
        service_CharacterInfo.myDEF = myDEF;
    }

    public static int getMySpeed() {
        return mySpeed;
    }

    public static void setMySpeed(int mySpeed) {
        service_CharacterInfo.mySpeed = mySpeed;
    }

    public static int getMyWeaponNum() {
        return myWeaponNum;
    }

    public static void setMyWeaponNum(int myWeaponNum) {
        service_CharacterInfo.myWeaponNum = myWeaponNum;
    }

    public static int getMyArmorNum() {
        return myArmorNum;
    }

    public static void setMyArmorNum(int myArmorNum) {
        service_CharacterInfo.myArmorNum = myArmorNum;
    }

    public static int getMyArtefactNum() {
        return myArtefactNum;
    }

    public static void setMyArtefactNum(int myArtefactNum) {
        service_CharacterInfo.myArtefactNum = myArtefactNum;
    }

    public static int getMyStage() {
        return myStage;
    }

    public static void setMyStage(int myStage) {
        service_CharacterInfo.myStage = myStage;
    }

    public static int getMyMoney() {
        return myMoney;
    }

    public static void setMyMoney(int myMoney) {
        service_CharacterInfo.myMoney = myMoney;
    }

    public static int getMyDays() {
        return myDays;
    }

    public static void setMyDays(int myDays) {
        service_CharacterInfo.myDays = myDays;
    }

    public static int getMyHpLevel() {
        return myHpLevel;
    }

    public static void setMyHpLevel(int myHpLevel) {
        service_CharacterInfo.myHpLevel = myHpLevel;
    }

    public static int getMyMpLevel() {
        return myMpLevel;
    }

    public static void setMyMpLevel(int myMpLevel) {
        service_CharacterInfo.myMpLevel = myMpLevel;
    }

    public static int getMyAtkLevel() {
        return myAtkLevel;
    }

    public static void setMyAtkLevel(int myAtkLevel) {
        service_CharacterInfo.myAtkLevel = myAtkLevel;
    }

    public static int getMyDefLevel() {
        return myDefLevel;
    }

    public static void setMyDefLevel(int myDefLevel) {
        service_CharacterInfo.myDefLevel = myDefLevel;
    }

    public static int getMySpeedLevel() {
        return mySpeedLevel;
    }

    public static void setMySpeedLevel(int mySpeedLevel) {
        service_CharacterInfo.mySpeedLevel = mySpeedLevel;
    }

    public static int getMyCurrentHp() {
        return myCurrentHp;
    }

    public static void setMyCurrentHp(int myCurrentHp) {
        service_CharacterInfo.myCurrentHp = myCurrentHp;
    }

    public static int getMyCurrentMp() {
        return myCurrentMp;
    }

    public static void setMyCurrentMp(int myCurrentMp) {
        service_CharacterInfo.myCurrentMp = myCurrentMp;
    }

    public static int getMyCurrentSp() {
        return myCurrentSp;
    }

    public static void setMyCurrentSp(int myCurrentSp) {
        service_CharacterInfo.myCurrentSp = myCurrentSp;
    }

    public static int getMyF_ATK() {
        return myF_ATK;
    }

    public static void setMyF_ATK(int myF_ATK) {
        service_CharacterInfo.myF_ATK = myF_ATK;
    }

    public static int getMyA_Speed() {
        return myA_Speed;
    }

    public static void setMyA_Speed(int mtA_Speed) {
        service_CharacterInfo.myA_Speed = mtA_Speed;
    }

    public static int getMyHit() {
        return myHit;
    }

    public static void setMyHit(int myHit) {
        service_CharacterInfo.myHit = myHit;
    }

    public static int getMySkill1() {
        return mySkill1;
    }

    public static void setMySkill1(int mySkill1) {
        service_CharacterInfo.mySkill1 = mySkill1;
    }

    public static int getMySkill2() {
        return mySkill2;
    }

    public static void setMySkill2(int mySkill2) {
        service_CharacterInfo.mySkill2 = mySkill2;
    }

    public static int getMySkill3() {
        return mySkill3;
    }

    public static void setMySkill3(int mySkill3) {
        service_CharacterInfo.mySkill3 = mySkill3;
    }

    public static int getSkill1_1() {
        return skill1_1;
    }

    public static void setSkill1_1(int skill1_1) {
        service_CharacterInfo.skill1_1 = skill1_1;
    }

    public static int getSkill1_2() {
        return skill1_2;
    }

    public static void setSkill1_2(int skill1_2) {
        service_CharacterInfo.skill1_2 = skill1_2;
    }

    public static int getSkill1_3() {
        return skill1_3;
    }

    public static void setSkill1_3(int skill1_3) {
        service_CharacterInfo.skill1_3 = skill1_3;
    }

    public static int getSkill1_4() {
        return skill1_4;
    }

    public static void setSkill1_4(int skill1_4) {
        service_CharacterInfo.skill1_4 = skill1_4;
    }

    public static int getSkill1_5() {
        return skill1_5;
    }

    public static void setSkill1_5(int skill1_5) {
        service_CharacterInfo.skill1_5 = skill1_5;
    }

    public static int getSkill1_6() {
        return skill1_6;
    }

    public static void setSkill1_6(int skill1_6) {
        service_CharacterInfo.skill1_6 = skill1_6;
    }

    public static int getSkill2_1() {
        return skill2_1;
    }

    public static void setSkill2_1(int skill2_1) {
        service_CharacterInfo.skill2_1 = skill2_1;
    }

    public static int getSkill2_2() {
        return skill2_2;
    }

    public static void setSkill2_2(int skill2_2) {
        service_CharacterInfo.skill2_2 = skill2_2;
    }

    public static int getSkill2_3() {
        return skill2_3;
    }

    public static void setSkill2_3(int skill2_3) {
        service_CharacterInfo.skill2_3 = skill2_3;
    }

    public static int getSkill2_4() {
        return skill2_4;
    }

    public static void setSkill2_4(int skill2_4) {
        service_CharacterInfo.skill2_4 = skill2_4;
    }

    public static int getSkill2_5() {
        return skill2_5;
    }

    public static void setSkill2_5(int skill2_5) {
        service_CharacterInfo.skill2_5 = skill2_5;
    }

    public static int getSkill2_6() {
        return skill2_6;
    }

    public static void setSkill2_6(int skill2_6) {
        service_CharacterInfo.skill2_6 = skill2_6;
    }

    public static int getSkill3_1() {
        return skill3_1;
    }

    public static void setSkill3_1(int skill3_1) {
        service_CharacterInfo.skill3_1 = skill3_1;
    }

    public static int getSkill3_2() {
        return skill3_2;
    }

    public static void setSkill3_2(int skill3_2) {
        service_CharacterInfo.skill3_2 = skill3_2;
    }

    public static int getSkill3_3() {
        return skill3_3;
    }

    public static void setSkill3_3(int skill3_3) {
        service_CharacterInfo.skill3_3 = skill3_3;
    }


    //이벤트 발생시 + hp mp sp  - hp mp sp uexp 확인  service_CharacterInfo
    public void eventcheck(int uhp, int ump, int usp, int uexp, int dhp, int dmp, int dsp, int dexp, Context context) {
        int iring = 1;
        int ahp=uhp, amp=ump,asp=usp,aexp=uexp;
        int bhp=dhp, bmp=dmp,bsp=dsp,bexp=dexp;

        while (iring >= 1) {
            if (ahp != 0) {
                if (service_CharacterInfo .getMyMaxHp() < service_CharacterInfo.getMyCurrentHp() + uhp) {
                    service_CharacterInfo.setMyCurrentHp(service_CharacterInfo.getMyMaxHp());
                    iring++;
                    ahp=0;
                } else {
                    service_CharacterInfo.setMyCurrentHp(service_CharacterInfo.getMyCurrentHp() + uhp);
                    iring++;
                    ahp=0;
                }
            } else if (amp != 0) {
                if (service_CharacterInfo.getMyMaxMp() < service_CharacterInfo.getMyCurrentMp() + ump) {
                    service_CharacterInfo.setMyCurrentMp(service_CharacterInfo.getMyMaxMp());
                    iring++;
                    amp=0;
                } else {
                    service_CharacterInfo.setMyCurrentMp(service_CharacterInfo.getMyCurrentMp() + ump);
                    iring++;
                    amp=0;
                }
            } else if (asp != 0) {
                if (service_CharacterInfo.getMyMaxSp() < service_CharacterInfo.getMyCurrentSp() + usp) {
                    service_CharacterInfo.setMyCurrentSp(service_CharacterInfo.getMyMaxSp());
                    iring++;
                    asp=0;
                } else {
                    service_CharacterInfo.setMyCurrentSp(service_CharacterInfo.getMyCurrentSp() + usp);
                    iring++;
                    asp=0;
                }
            } else if (aexp != 0) {
                service_CharacterInfo.setMyExp(service_CharacterInfo.getMyExp() + uexp);
                iring++;
                aexp=0;
            }
            else if (bhp != 0) {
                if (service_CharacterInfo.getMyCurrentHp() - dhp <= 0) {
                    //게임오버
                    Intent i = new Intent(context,gameOverActivity.class);
                    context.startActivity(i);

                    bhp=0;

                } else {
                    service_CharacterInfo.setMyCurrentHp(service_CharacterInfo.getMyCurrentHp() - dhp);
                    iring++;
                    bhp=0;
                }
            } else if (bmp != 0) {
                if (service_CharacterInfo.getMyCurrentMp() - dmp <= 0) {
                    service_CharacterInfo.setMyCurrentMp(0);
                    iring++;
                    bmp=0;
                } else {
                    service_CharacterInfo.setMyCurrentMp(service_CharacterInfo.getMyCurrentMp() - dmp);
                    iring++;
                    bmp=0;
                }
            } else if (bsp !=0) {
                if (service_CharacterInfo.getMyCurrentSp() - dsp <= 0) {
                    //게임오버
                    bsp=0;
                } else {
                    service_CharacterInfo.setMyCurrentSp(service_CharacterInfo.getMyCurrentSp() - dsp);
                    iring++;
                    bsp=0;
                }
            } else if (bexp !=0) {
                service_CharacterInfo.setMyExp(service_CharacterInfo.getMyExp() + uexp);
                iring++;
                bexp=0;
            }
            iring--;
        }

    }


    //레벨체크
    public boolean levelcheck(Context context){
        int levelupexp[] = {10,10,10,10,10,15,15,15,15,15,40,40,40,40,40,50,50,50,50,50,90,90,90,90,90,120,120,120,120,120,150,150,150,150,150,240,240,240,240,240,280,280,280,280,280,320,320,320,320,320,500,500,500,500,500,600,600,600,600,600,
                900,900,900,900,900,1200,1200,1200,1200,1200,1500,1500,1500,1500,1500,2000,2000,2000,2000,2000,2500,2500,2500,2500,2500,3000,3000,3000,3000,3000,4000,4000,4000,4000,4000,5000,5000,5000,5000,5000,999999999};
        //레벨업시
        if(levelupexp[service_CharacterInfo.getMyLevel()-1] <= service_CharacterInfo.getMyExp()) {
            service_CharacterInfo.setMyExp(service_CharacterInfo.getMyExp() - (levelupexp[service_CharacterInfo.getMyLevel()-1])); //경험치 깍
            service_CharacterInfo.setMyLevel(service_CharacterInfo.getMyLevel() + 1);

            int point = random.nextInt(5) + 5;

            for (int i = 0; i < point; i++) {
                int stat = random.nextInt(4) + 1;
                //체력
                if (stat == 1) {
                    if (service_CharacterInfo.myCharacterNum == 1) {
                        int x = random.nextInt(8) + 7;
                        service_CharacterInfo.setMyMaxHp(service_CharacterInfo.getMyMaxHp() + x);
                    } else {
                        int x = random.nextInt(3) + 7;
                        service_CharacterInfo.setMyMaxHp(service_CharacterInfo.getMyMaxHp() + x);
                    }
                }
                //마력
                else if (stat == 2) {
                    if (service_CharacterInfo.myCharacterNum == 1) {
                        int x = random.nextInt(2) + 1;
                        service_CharacterInfo.setMyMaxMp(service_CharacterInfo.getMyMaxMp() + x);
                    } else {
                        int x = random.nextInt(3) + 1;
                        service_CharacterInfo.setMyMaxMp(service_CharacterInfo.getMyMaxMp() + x);
                    }
                }
                //공격력
                else if (stat == 3) {
                    if (service_CharacterInfo.myCharacterNum == 1) {
                        int x = random.nextInt(8) + 3;
                        service_CharacterInfo.setMyATK(service_CharacterInfo.getMyATK() + x);
                    } else {
                        int x = random.nextInt(6) + 5;
                        service_CharacterInfo.setMyATK(service_CharacterInfo.getMyATK() + x);
                    }
                }
                //방어력
                else if (stat == 4) {
                    if (service_CharacterInfo.myCharacterNum == 1) {
                        int x = random.nextInt(4) + 4;
                        service_CharacterInfo.setMyDEF(service_CharacterInfo.getMyDEF() + x);
                    } else {
                        int x = random.nextInt(7) + 1;
                        service_CharacterInfo.setMyDEF(service_CharacterInfo.getMyDEF() + x);
                    }
                }

            }
            double dice1 = rand();
            if (dice1 <= 10) {
                if (dice1 <= 5) {
                    service_CharacterInfo.setMyA_Speed(service_CharacterInfo.getMyA_Speed() + 1);
                } else {
                    service_CharacterInfo.setMySpeed(service_CharacterInfo.getMySpeed() + 1);
                }
            }
            //고정뎀지
            int level = service_CharacterInfo.getMyLevel();
            if(level<=10){

            }
            else if(level>10 && level <=20 ){
                int x = random.nextInt(2)+2;
                service_CharacterInfo.setMyF_ATK(service_CharacterInfo.getMyF_ATK()+x);
            }
            else if(level>20 && level <=30 ){
                int x = random.nextInt(3)+2;
                service_CharacterInfo.setMyF_ATK(service_CharacterInfo.getMyF_ATK()+x);
            }
            else if(level>30 && level <=40 ){
                int x = random.nextInt(4)+2;
                service_CharacterInfo.setMyF_ATK(service_CharacterInfo.getMyF_ATK()+x);
            }
            else if(level>40 && level <=50 ){
                int x = random.nextInt(5)+2;
                service_CharacterInfo.setMyF_ATK(service_CharacterInfo.getMyF_ATK()+x);
            }
            else if(level>50 && level <=60 ){
                int x = random.nextInt(6)+2;
                service_CharacterInfo.setMyF_ATK(service_CharacterInfo.getMyF_ATK()+x);
            }
            else if(level>60 && level <=70 ){
                int x = random.nextInt(1)+7;
                service_CharacterInfo.setMyF_ATK(service_CharacterInfo.getMyF_ATK()+x);
            }
            else if(level>70 && level <=80 ){
                int x = random.nextInt(1)+7;
                service_CharacterInfo.setMyF_ATK(service_CharacterInfo.getMyF_ATK()+x);
            }
            else if(level>80 && level <=90 ){
                int x = random.nextInt(1)+9;
                service_CharacterInfo.setMyF_ATK(service_CharacterInfo.getMyF_ATK()+x);
            }
            else if(level>90 && level <=100){
                int x = random.nextInt(1)+10;
                service_CharacterInfo.setMyF_ATK(service_CharacterInfo.getMyF_ATK()+x);
            }

            service_CharacterInfo.setMyMaxSp(service_CharacterInfo.getMyMaxSp()+1);
            service_CharacterInfo.setMyCurrentHp(service_CharacterInfo.myMaxHp);
            service_CharacterInfo.setMyCurrentMp(service_CharacterInfo.myMaxMp);
            service_CharacterInfo.setMyCurrentSp(service_CharacterInfo.myMaxSp);
            return true;
        }
        //경험치가 모자르면
        return false;
    }

    private double rand(){
        Random random = new Random();
        int r = random.nextInt(1000);
        r = r / 10;
        double percentage = (double) r / 100.0 * 100.0;
        return percentage;
    }


}
