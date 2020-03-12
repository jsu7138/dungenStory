package com.example.dgstory;

import android.content.Intent;
import android.graphics.Color;
import android.hardware.SensorManager;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;

import org.andengine.audio.music.Music;
import org.andengine.audio.music.MusicFactory;
import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.engine.camera.SmoothCamera;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.camera.hud.controls.BaseOnScreenControl;
import org.andengine.engine.camera.hud.controls.BaseOnScreenControl.IOnScreenControlListener;
import org.andengine.engine.camera.hud.controls.DigitalOnScreenControl;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.handler.physics.PhysicsHandler;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.shape.IShape;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.util.FPSLogger;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.tmx.TMXLayer;
import org.andengine.extension.tmx.TMXLoader;
import org.andengine.extension.tmx.TMXLoader.ITMXTilePropertiesListener;
import org.andengine.extension.tmx.TMXObject;
import org.andengine.extension.tmx.TMXObjectGroup;
import org.andengine.extension.tmx.TMXProperties;
import org.andengine.extension.tmx.TMXTile;
import org.andengine.extension.tmx.TMXTileProperty;
import org.andengine.extension.tmx.TMXTiledMap;
import org.andengine.extension.tmx.util.exception.TMXLoadException;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.debug.Debug;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.microedition.khronos.opengles.GL10;

public class Stage_2_Activity extends SimpleBaseGameActivity {
    private SmoothCamera camera;

    private static final int CAMERA_WIDTH = 800;
    private static final int CAMERA_HEIGHT = 480;
    private Sound mSound_Clear,mSound_bim1,mSound_bim2,mSound_EnermyAttack1,
            mSound_sword1,mSound_sword2,mSound_arrow1,mSound_arrow2,mSound_heal;
    private Music mMusic;

    public Scene mScene;
    private TMXTiledMap mTMXTiledMap;
    public PhysicsWorld mPhysicsWorld;

    // --------------------------------
    // 이미지 스트라이프를 위한 처리 변수
    // --------------------------------
    private BitmapTextureAtlas mPlayerTextureAtlas;     //캐릭터 이미지 전용 판
    private BitmapTextureAtlas mBitmapTextureAtlas;     //전체 오브젝트 판
    private BitmapTextureAtlas mBitmap2TextureAtlas;     //전체 오브젝트 판
    private BitmapTextureAtlas mGameClearTextureAtlas;      //클리어판
    private BitmapTextureAtlas mPotionTextureAtlas;
    private BitmapTextureAtlas mEnermy1TextureAtlas,
            mEnermy2TextureAtlas,
            mEnermy3TextureAtlas,
            mEnermySkillAtlas;    //적 판 및 적 스킬판

    private TiledTextureRegion mPlayerTextureRegion;    //플레이어 캐릭터
    private TiledTextureRegion mGameClearTextureRegion;    //클리어 텍스쳐
    private TiledTextureRegion mMoneyTextureRegion;     //돈 텍스처
    private TiledTextureRegion mPotionTextureRegion;
    private TextureRegion mHPTextureRegion;             //HP바 텍스쳐
    private TextureRegion mMPTextureRegion;             //MP바 텍스쳐
    private TextureRegion mHudSkinTextureRegion;        //HP,MP HUD 틀 텍스쳐
    private TextureRegion mHudBackSkinTextureRegion;    //HP,MP HUD 밑 바탕 텍스처
    private TextureRegion mShootTextureRegion;          //공격 버튼
    private TextureRegion mJumpTextureRegion;           //점프 버튼
    private TextureRegion mSkill1TextureRegion;           //스킬 버튼1
    private TextureRegion mSkill2TextureRegion;           //스킬 버튼2
    private TiledTextureRegion mWallAir1TextureRegion;  //공중 벽

    private TiledTextureRegion mEnemyTextureRegion;     // 적 선언
    private TiledTextureRegion mEnemy2TextureRegion;     // 적2 선언
    private TiledTextureRegion mEnemy3TextureRegion;     // 적3 선언
    private TiledTextureRegion mEnemySkillTextureRegion;     // 적스킬 선언


    private TiledTextureRegion mDamageTextureRegion;    //데미지 이펙트 선언
    private TiledTextureRegion mSwordTextureRegion;     // 검기 선언
    private TiledTextureRegion mArrowTextureRegion;     // 화살 선언


    private TiledTextureRegion mChargeClashTextureRegion;   //챠지 슬래시
    private TiledTextureRegion mSwordThrowTextureRegion;   //스워드스로우
    // --------------------------------
    // 컨트롤러를 위한 텍스처 선언
    // --------------------------------
    public HUD mHUD;
    private DigitalOnScreenControl mDigitalOnScreenControl;
    private TextureRegion mOnScreenControlBaseTextureRegion;
    private TextureRegion mOnScreenControlKnobTextureRegion;

    private BitmapTextureAtlas mOnScreenControlTexture;
    private BitmapTextureAtlas mHUDTextureAtlas;


    // --------------------------------
    // FixtureDefs 생성을 위한 Category bits
    // --------------------------------
    private static final short CATEGORYBIT_PLAYER = 2;
    private static final short CATEGORYBIT_ENEMY = 4;
    private static final short CATEGORYBIT_SHURIKEN = 8;

    private static final short CATEGORYBIT_WALL_AIR = 1;
    private static final short CATEGORYBIT_WALL_AIR_WALL = 1;
    private static final short CATEGORYBIT_WALL = 1;
    private static final short CATEGORYBIT_MONEY = 1;
    private static final short CATEGORYBIT_TRAP = 1;
    private static final short CATEGORYBIT_ENERMY_WALL = 1;
    // --------------------------------
    // FixtureDefs 생성을 위한 Mask bits
    // --------------------------------
    private static final short MASKBITS_WALL = CATEGORYBIT_WALL
            + CATEGORYBIT_PLAYER + CATEGORYBIT_ENEMY + CATEGORYBIT_SHURIKEN;

    private static final short MASKBITS_PLAYER = CATEGORYBIT_WALL
            + CATEGORYBIT_PLAYER + CATEGORYBIT_ENEMY;

    private static final short MASKBITS_ENEMY = CATEGORYBIT_WALL
            + CATEGORYBIT_SHURIKEN + CATEGORYBIT_PLAYER;

    private static final short MASKBITS_SHURIKEN = CATEGORYBIT_WALL
            + CATEGORYBIT_ENEMY;


    // 오브 젝트의 처리의 필수 예시
    // MASKBITS_MONEY = CATEGORYBIT_MONEY + CATEGORYBIT_PLAYER

    private static final short MASKBITS_MONEY = CATEGORYBIT_MONEY
            + CATEGORYBIT_PLAYER;

    private static final short MASKBITS_TRAP = CATEGORYBIT_TRAP
            + CATEGORYBIT_PLAYER;

    private static final short MASKBITS_WALL_AIR = CATEGORYBIT_WALL + CATEGORYBIT_PLAYER;

    private static final short MASKBITS_WALL_AIR_WALL = CATEGORYBIT_WALL_AIR_WALL + CATEGORYBIT_WALL_AIR;

    private static final short MASKBITS_ENERMY_WALL = CATEGORYBIT_ENERMY_WALL
            + CATEGORYBIT_ENEMY;

    // --------------------------------
    // 기타 설정 변수
    // --------------------------------
    public ArrayList<IEntity> mEntityList;          //객체 관리용 리스트
    TimerHandler timerHandler = null;               //객체 타이머 핸들러
    TimerHandler damageHandler = null;              //데미지 타이머 핸들러
    int damageCount = 0;                            //데미지 텍스트 카운트
    int damageSkinCount = 0;                        //데미지 스킨 카운트
    int wallAir=0;
    float wall1[];
    private FixtureDef boxFixtureDef,air_FixtureDef,
            trapFixtureDef, enmey_rectFixtureDef;      //벽,트랩 관련
    private Rectangle rect,traps,enemy_rect,air_rect;            //벽,트랩 관련
    private Font mFont,dFont,pFont;                           // 폰트 설정
    private battle_damage damageCalculation;            // 데미지 계산기
    private int clearStageCount = 0;                    //클리어 최소 조건
    private int enermySkillCount = 0;                   //적의 공통 스킬 카운트
    private int potionCount = 0;

    //바디 충돌 처리 == fix1,fix2
    //스트라이프 충돌 처리 == fix3
    private String fix1_name = "", fix2_name = "",fix3_name = "";

    // --------------------------------------
    // 플레이어 설정 변수
    // --------------------------------------
    private static AnimatedSprite player_sprite;
    private static Body player_body;
    public static final short PLAYER_TO_RIGHT = 1;
    public static final short PLAYER_TO_LEFT = -1;
    public int playerDir = 1;                       //플레이어 방향 값
    private int skillCount = 0;                     //플레이어 스킬 카운트

    private static float mLinearVelocityX = 3.0f;   //플레이어 이동속도
    public static float mImpulseY = 5.5f;           //플레이어 점프력
    public boolean isPlayerMoving = false;          //플레이어 무빙모션 체크
    private boolean isLanded = false;               //플레이어 점프모션 체크
    private boolean isJumpTwo = false;              //플레이어 2단점프 모션 체크
    private boolean isPlayerStoping = true;         //플레이어 대기모션 체크
    private boolean isPlayerAttack = false;         //플레이어 공격중 체크
    private boolean desPlayer = false;              //플레이어의 죽음 체크
    private boolean clearPlayer = false;            //플레이어의 클리어 체크

    private service_CharacterInfo player_info;      //플레이어 정보 연동
    private int sword_speed = 100;                  //플레이어 기본 공격 스피드
    private float sword_delay = 0.8f;               //플레이어 기본 공격 딜레이
    private float sword_key_delay = 0f;             //플레이어 기본 공격 키 딜레이

    // --------------------------------------
    // 원거리 설정 변수
    // --------------------------------------
    private int arrowCount = 0;              // 원거리의 갯수를 체크하기 위해
    private boolean desArrow = false;        // 원거리의 메모리 해제를 위해

    // --------------------------------------
    // 근거리 스트라이프 설정 변수
    // --------------------------------------
    private int swordCount = 0;                  //기본 공격(검) 공격 카운트

    // --------------------------------------
    // 적 출현에 대한 선언
    // --------------------------------------
    public int enemyCount = 0;
    public int enemyCount2 = 0;
    public int enemyCount3 = 0;

    boolean[] enemyLandedArr,enemyLandedArr2,enemyLandedArr3;
    boolean[] enemyIsSkill;
    private ArrayList<battle_enermy> battleEnermy1;

    // --------------------------------------
    // 에너지바에 대한 설정
    // --------------------------------------

    private Sprite mHpSprite, mMpSprite;
    private boolean reduceHealth = false, reduceMp = false;       //HP,MP 차감 상태 체크

    // --------------------------------------
    // 오브젝트 처리
    // --------------------------------------
    Text leftText;              // 돈 표시뷰
    Integer dollarCount = 0;    //표시 되는 돈 금액
    int moneyCount = 0;  //돈 카운팅

    /*******************************************************************************************
     *
     * 메소드 부분 시작
     *
     ******************************************************************************************/

    @Override
    public EngineOptions onCreateEngineOptions() {
        // 5,6번째 인자로 카메라의 속도 조정
        this.camera = new SmoothCamera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT, 200,
                400, 1.0f);

        EngineOptions engineOptions = new EngineOptions(true,
                ScreenOrientation.LANDSCAPE_FIXED, new FillResolutionPolicy(),
                camera);

        engineOptions.getTouchOptions().setNeedsMultiTouch(true);
        engineOptions.getAudioOptions().setNeedsMusic(true);
        engineOptions.getAudioOptions().setNeedsSound(true);

        return engineOptions;
    }

    @Override
    protected void onCreateResources() {
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
        //음악
        loadSound();
        // --------------------------------------------------------------------
        // 글자의 Font 선언
        // --------------------------------------------------------------------

        FontFactory.setAssetBasePath("font/");

        this.mFont = FontFactory.createFromAsset(this.getFontManager(),
                this.getTextureManager(), 512, 512, TextureOptions.BILINEAR,
                this.getAssets(), "Plok.ttf", 28, true, Color.WHITE);

        this.dFont = FontFactory.createFromAsset(this.getFontManager(),
                this.getTextureManager(), 512, 512, TextureOptions.BILINEAR,
                this.getAssets(), "jetmix.ttf", 23, true, Color.BLUE);

        this.pFont =  FontFactory.createFromAsset(this.getFontManager(),
                this.getTextureManager(), 512, 512, TextureOptions.BILINEAR,
                this.getAssets(), "jetmix.ttf", 23, true, Color.RED);

        this.mFont.load();
        this.dFont.load();
        this.pFont.load();

        // 닌자, 적, 표창 이미지 호출
        loadImages();

        // 컨트롤러 이미지 호출
        loadControllers();
    }

    @Override
    protected Scene onCreateScene() {
        this.mEngine.registerUpdateHandler(new FPSLogger());
        mMusic.play();

        mScene = new Scene();

        try {
            final TMXLoader tmxLoader = new TMXLoader(this.getAssets(),
                    this.mEngine.getTextureManager(),
                    TextureOptions.BILINEAR_PREMULTIPLYALPHA,
                    this.getVertexBufferObjectManager(),
                    new ITMXTilePropertiesListener() {
                        @Override
                        public void onTMXTileWithPropertiesCreated(
                                final TMXTiledMap pTMXTiledMap,
                                final TMXLayer pTMXLayer,
                                final TMXTile pTMXTile,
                                final TMXProperties<TMXTileProperty> pTMXTileProperties) {
                        }
                    });

            // 타일맵 로드
            this.mTMXTiledMap = tmxLoader.loadFromAsset("tmx/stage2.tmx");

        } catch (final TMXLoadException e) {
            Debug.e(e);
        }

        final TMXLayer tmxLayer = this.mTMXTiledMap.getTMXLayers().get(0);
        mScene.attachChild(tmxLayer);

        //물리 엔진 설정
        this.mPhysicsWorld = new PhysicsWorld(new Vector2(0,
                SensorManager.GRAVITY_EARTH), false);
        mScene.registerUpdateHandler(this.mPhysicsWorld);

        //객체 관리 동적배열 설정
        mEntityList = new ArrayList<IEntity>(mScene.getChildCount());

        for (int i = 0; i < mScene.getChildCount(); i++)
            mEntityList.add(mScene.getChildByIndex(i));

        //적의 정보 설정
        battleEnermy1 = new ArrayList<>();

        loadObject(mTMXTiledMap);
        createCollision();
        createUpdateHandler();
        callController();
        mScene.setChildScene(this.mDigitalOnScreenControl);

        // 플레이어 캐릭터의 스프라이트와 바디 선언
        player_sprite = (AnimatedSprite) findShape("player");
        player_body = mPhysicsWorld.getPhysicsConnectorManager()
                .findBodyByShape(player_sprite);

        //데미지 계산기 선언
        damageCalculation = new battle_damage();

        // 적캐릭터를 로드하고, 배열에 할당합니다.
        // 적캐릭터에 인공지능을 부여
        enemyLandedArr = new boolean[enemyCount];
        for (int i = 0; i < enemyCount; i++) {
            enemyLandedArr[i] = true;
        }
        enemyLandedArr2 = new boolean[enemyCount2];
        for (int i = 0; i < enemyCount2; i++) {
            enemyLandedArr2[i] = true;
        }
        enemyLandedArr3 = new boolean[enemyCount3];
        enemyIsSkill = new boolean[enemyCount3];
        for (int i = 0; i < enemyCount3; i++) {
            enemyLandedArr3[i] = true;
            enemyIsSkill[i] = false;
        }

        //공중 타일 위치값 부여
        wall1 = new float[wallAir];
        for(int i = 0; i < wallAir; i ++){
            wall1[i] = 5f;
        }

        // 이 부분을 설정해야 타일맵에서 좌우 여백이 제대로 셋팅됨.
        this.camera.setBoundsEnabled(true);
        this.camera.setBounds(0, 0, tmxLayer.getWidth(), tmxLayer.getHeight());

        //맵에서 카메라 따라오기
        camera.setChaseEntity(player_sprite);

        return mScene;
    }

    /*********************************************************
     *
     * 컨트롤러 구성을 위한 이미지를 추가 및 음악
     *
     *********************************************************/

    private void loadControllers() {
        String temp_name;
        if(service_CharacterInfo.myCharacterNum == 1){
            temp_name = "hud_warrior.png";
        }else{
            temp_name = "hud_archer.png";
        }

        // create atlas of 512*1024
        this.mHUDTextureAtlas = new BitmapTextureAtlas(
                this.getTextureManager(), 512, 602,
                TextureOptions.BILINEAR_PREMULTIPLYALPHA);

        //전체 상태바 틀 이미지 로드
        this.mHudBackSkinTextureRegion = BitmapTextureAtlasTextureRegionFactory
                .createFromAsset(this.mHUDTextureAtlas, this, "hud_black.png", 0,0);

        this.mHudSkinTextureRegion = BitmapTextureAtlasTextureRegionFactory
                .createFromAsset(this.mHUDTextureAtlas, this, temp_name, 0,200);

        // 점프 버튼 이미지 로드
        this.mJumpTextureRegion = BitmapTextureAtlasTextureRegionFactory
                .createFromAsset(this.mHUDTextureAtlas, this, "hud_jump.png", 0,400);

        // 공격 버튼 이미지 로드
        this.mShootTextureRegion = BitmapTextureAtlasTextureRegionFactory
                .createFromAsset(this.mHUDTextureAtlas, this, "hud_attack.png", 128, 400);

        // 스킬1 버튼 이미지 로드
        this.mSkill1TextureRegion = BitmapTextureAtlasTextureRegionFactory
                .createFromAsset(this.mHUDTextureAtlas, this, "hud_skill1.png", 256, 400);

        // 스킬2 버튼 이미지 로드
        this.mSkill2TextureRegion = BitmapTextureAtlasTextureRegionFactory
                .createFromAsset(this.mHUDTextureAtlas, this, "hud_skill2.png", 384, 400);
        //상세 hp바
        this.mHPTextureRegion = BitmapTextureAtlasTextureRegionFactory
                .createFromAsset(this.mHUDTextureAtlas, this, "hud_hp.png", 0,528);
        //상세 mp바
        this.mMPTextureRegion = BitmapTextureAtlasTextureRegionFactory
                .createFromAsset(this.mHUDTextureAtlas, this, "hud_mp.png", 0,565);

        this.mEngine.getTextureManager().loadTexture(this.mHUDTextureAtlas);

        // -------------------------------------------
        // 컨트롤러 작업
        // -------------------------------------------

        // create atlas of 256*128
        this.mOnScreenControlTexture = new BitmapTextureAtlas(
                this.getTextureManager(), 256, 128,
                TextureOptions.BILINEAR_PREMULTIPLYALPHA);

        // 컨트롤러 이미지를 atlas에 추가
        this.mOnScreenControlBaseTextureRegion = BitmapTextureAtlasTextureRegionFactory
                .createFromAsset(this.mOnScreenControlTexture, this,
                        "onscreen_control_base.png", 0, 0);

        this.mOnScreenControlKnobTextureRegion = BitmapTextureAtlasTextureRegionFactory
                .createFromAsset(this.mOnScreenControlTexture, this,
                        "onscreen_control_knob.png", 128, 0);

        // 디지털 컨트롤의 이미지 출력
        this.mEngine.getTextureManager().loadTexture(
                this.mOnScreenControlTexture);
    }

    private void loadImages() {
        // 오브젝트 전용 판
        this.mBitmapTextureAtlas = new BitmapTextureAtlas(
                this.getTextureManager(), 1024, 1025, TextureOptions.BILINEAR);

        // 플레이어 전용 판
        this.mPlayerTextureAtlas = new BitmapTextureAtlas(
                this.getTextureManager(), 1024, 1025, TextureOptions.BILINEAR);

        // 클리어 판
        this.mGameClearTextureAtlas = new BitmapTextureAtlas(
                this.getTextureManager(), 500, 2000, TextureOptions.BILINEAR);
        // 클리어 판
        this.mPotionTextureAtlas = new BitmapTextureAtlas(
                this.getTextureManager(), 200, 200, TextureOptions.BILINEAR);

        //공중 벽 판
        this.mBitmap2TextureAtlas = new BitmapTextureAtlas(
                this.getTextureManager(), 500, 300, TextureOptions.BILINEAR);

        // 적1 전용 판
        this.mEnermy1TextureAtlas = new BitmapTextureAtlas(
                this.getTextureManager(), 1024, 1025, TextureOptions.BILINEAR);

        // 적2 전용 판
        this.mEnermy2TextureAtlas = new BitmapTextureAtlas(
                this.getTextureManager(), 1024, 1025, TextureOptions.BILINEAR);

        // 적3 전용 판
        this.mEnermy3TextureAtlas = new BitmapTextureAtlas(
                this.getTextureManager(), 1024, 1025, TextureOptions.BILINEAR);

        //적 공통 스킬 전용판
        this.mEnermySkillAtlas = new BitmapTextureAtlas(
                this.getTextureManager(), 1000, 2000, TextureOptions.BILINEAR);

        // 전사 전용
        if(player_info.myCharacterNum == 1) {
            this.mPlayerTextureRegion = BitmapTextureAtlasTextureRegionFactory
                    .createTiledFromAsset(this.mPlayerTextureAtlas, this,
                            "actor1.png", 0, 0, 11, 12);
            // 기본 검기 이미지
            this.mSwordTextureRegion = BitmapTextureAtlasTextureRegionFactory
                    .createTiledFromAsset(this.mBitmapTextureAtlas, this,
                            "skill_sword1.png", 500, 551, 4, 2);
            //스워드 스로우 이펙트 이미지
            this.mSwordThrowTextureRegion = BitmapTextureAtlasTextureRegionFactory
                    .createTiledFromAsset(this.mBitmapTextureAtlas,this,
                            "skill_sword_throw.png",500, 125,3,3);
            //챠지 슬래시 이펙트 이미지
            this.mChargeClashTextureRegion = BitmapTextureAtlasTextureRegionFactory
                    .createTiledFromAsset(this.mBitmapTextureAtlas,this,
                            "skill_charge_clash.png",0, 207,1,8);



            //궁수 전용
        }else if(player_info.myCharacterNum == 2){
            this.mPlayerTextureRegion = BitmapTextureAtlasTextureRegionFactory
                    .createTiledFromAsset(this.mPlayerTextureAtlas, this,
                            "actor2.png", 0, 0, 11, 8);
            // 화살 이미지
            this.mArrowTextureRegion = BitmapTextureAtlasTextureRegionFactory
                    .createTiledFromAsset(this.mBitmapTextureAtlas, this,
                            "arrow.png", 577, 0, 1, 8);

        }

        //기본 적 이미지
        this.mEnemyTextureRegion = BitmapTextureAtlasTextureRegionFactory
                .createTiledFromAsset(this.mEnermy1TextureAtlas, this,
                        "enermy1.png", 0, 0, 6, 3);

        this.mEnemy2TextureRegion = BitmapTextureAtlasTextureRegionFactory
                .createTiledFromAsset(this.mEnermy2TextureAtlas, this,
                        "enermy_evil1.png", 0, 0, 6, 3);

        this.mEnemy3TextureRegion = BitmapTextureAtlasTextureRegionFactory
                .createTiledFromAsset(this.mEnermy3TextureAtlas, this,
                        "enermy_evil2.png", 0, 0, 6, 3);

        //적 공통스킬
        this.mEnemySkillTextureRegion = BitmapTextureAtlasTextureRegionFactory
                .createTiledFromAsset(this.mEnermySkillAtlas, this,
                        "skill_enermy.png", 0, 0, 5, 10);

        //타격 이펙트 이미지
        this.mDamageTextureRegion = BitmapTextureAtlasTextureRegionFactory
                .createTiledFromAsset(this.mBitmapTextureAtlas,this,
                        "effect_damage.png",390,0,3,2);

        // 돈 오브젝트 이미지
        this.mMoneyTextureRegion = BitmapTextureAtlasTextureRegionFactory
                .createTiledFromAsset(this.mBitmapTextureAtlas, this,
                        "icon_mineral.png", 528, 1000, 1, 1);

        // 공중 벽 이미지
        this.mWallAir1TextureRegion =  BitmapTextureAtlasTextureRegionFactory
                .createTiledFromAsset(this.mBitmap2TextureAtlas, this,
                        "walls.png", 0, 0, 1, 1);

        this.mPotionTextureRegion =  BitmapTextureAtlasTextureRegionFactory
                .createTiledFromAsset(this.mPotionTextureAtlas, this,
                        "potion.png", 0, 0, 8, 1);

        // 클리어 이미지
        this.mGameClearTextureRegion = BitmapTextureAtlasTextureRegionFactory
                .createTiledFromAsset(this.mGameClearTextureAtlas, this,
                        "clear.png", 0, 0, 1, 8);



        this.mBitmapTextureAtlas.load();
        this.mBitmap2TextureAtlas.load();
        this.mPlayerTextureAtlas.load();
        this.mEnermy1TextureAtlas.load();
        this.mEnermy2TextureAtlas.load();
        this.mEnermy3TextureAtlas.load();
        this.mEnermySkillAtlas.load();
        this.mGameClearTextureAtlas.load();
        this.mPotionTextureAtlas.load();
    }

    private void loadSound(){
        SoundFactory.setAssetBasePath("mfx/");
        MusicFactory.setAssetBasePath("mfx/");

        try {
            //배경음
            this.mMusic = MusicFactory.createMusicFromAsset(this.mEngine.getMusicManager(), this, "stage1.ogg");
            this.mMusic.setLooping(true);
            //효과음
            this.mSound_Clear = SoundFactory.createSoundFromAsset(this.mEngine.getSoundManager(), this, "clear_sound.ogg");
            this.mSound_bim1 = SoundFactory.createSoundFromAsset(this.mEngine.getSoundManager(), this, "enermy_bim1.ogg");
            this.mSound_bim2 = SoundFactory.createSoundFromAsset(this.mEngine.getSoundManager(), this, "enermy_bim2.ogg");
            this.mSound_EnermyAttack1 = SoundFactory.createSoundFromAsset(this.mEngine.getSoundManager(), this, "enermy_attack1.ogg");
            this.mSound_sword1 = SoundFactory.createSoundFromAsset(this.mEngine.getSoundManager(), this, "sword1.ogg");
            this.mSound_sword2 = SoundFactory.createSoundFromAsset(this.mEngine.getSoundManager(), this, "sword2.ogg");
            this.mSound_arrow1 = SoundFactory.createSoundFromAsset(this.mEngine.getSoundManager(), this, "arrow1.ogg");
            this.mSound_arrow2 = SoundFactory.createSoundFromAsset(this.mEngine.getSoundManager(), this, "arrow2.ogg");
            this.mSound_heal  = SoundFactory.createSoundFromAsset(this.mEngine.getSoundManager(), this, "heal.ogg");
        } catch (final IOException e) {
            Debug.e(e);
        }

        if(!service_music.setting) {
            mMusic.setVolume(0f);
            mSound_Clear.setVolume(0f);
            mSound_bim1.setVolume(0f);
            mSound_bim2.setVolume(0f);
            mSound_EnermyAttack1.setVolume(0f);
            mSound_sword1.setVolume(0f);
            mSound_sword2.setVolume(0f);
            mSound_arrow1.setVolume(0f);
            mSound_arrow2.setVolume(0f);
            mSound_heal.setVolume(0f);
        }

    }

    /*********************************************************
     *
     * 버튼 컨트롤 작업 DigitalOnScreenControl
     *
     *********************************************************/

    public void callController() {
        mHUD = new HUD();
        callAttackController();
        // ------------------------------------------------------
        // 점프 버튼
        // ------------------------------------------------------
        Sprite jump = new Sprite(CAMERA_WIDTH - 120, CAMERA_HEIGHT - 175,
                mJumpTextureRegion, this.getVertexBufferObjectManager()) {

            @Override
            public boolean onAreaTouched(TouchEvent pEvent, float pX, float pY) {

                // 점프 버튼이 클릭되고, 플레이어 캐릭이 땅에 도착했을 때 점프한다.
                if (pEvent.isActionDown() && isLanded && !isPlayerAttack && !isJumpTwo) {
                    jumpPlayer(player_sprite,player_body);
                    isLanded = false;
                    this.setScale(0.55f);
                    this.setColor(0.5f,0.5f,0.5f);
                }else if(pEvent.isActionDown() && !isLanded && !isJumpTwo ){
                    jumpPlayer(player_sprite,player_body);
                    isJumpTwo = true;
                }
                if(pEvent.isActionMove() || pEvent.isActionUp()){
                    this.setScale(0.7f);
                    this.setColor(1f,1f,1f);
                }
                return false;
            }
        };
        jump.setScale(0.7f);
        mHUD.registerTouchArea(jump);
        mHUD.attachChild(jump);

        // -------------------------------------------------------------------------
        // 상태바 스프라이트를 생성 및 HUD 셋팅
        // -------------------------------------------------------------------------
        Sprite mHudBSprite = new Sprite(12, -5,
                mHudBackSkinTextureRegion, this.getVertexBufferObjectManager());
        Sprite mHudSSprite = new Sprite(-100, -40,
                mHudSkinTextureRegion, this.getVertexBufferObjectManager());
        mHpSprite = new Sprite(-92, 12,
                mHPTextureRegion, this.getVertexBufferObjectManager());
        mMpSprite = new Sprite(-92, 38,
                mMPTextureRegion, this.getVertexBufferObjectManager());

        mHudBSprite.setScale(0.6f);
        mHudSSprite.setScale(0.6f);
        mHpSprite.setScale(0.6f);
        mMpSprite.setScale(0.6f);
        mHUD.attachChild(mHudBSprite);
        mHUD.attachChild(mHpSprite);
        mHUD.attachChild(mMpSprite);
        mHUD.attachChild(mHudSSprite);

        //초기 HP,MP바 셋팅
        float hpX = (float) player_info.myCurrentHp/player_info.myMaxHp * 100;
        float HpBarX = hpX * 1.62f;
        mHpSprite.setX((-92) + HpBarX);
        hpX = (float) player_info.myCurrentMp/player_info.myMaxMp * 100;
        HpBarX = hpX * 1.62f;
        mMpSprite.setX((-92) + HpBarX);

        // ----------------------------------------------------------------------
        // text 돈 dollar 표기
        // ----------------------------------------------------------------------
        leftText = new Text(CAMERA_WIDTH-230, 25, this.mFont, "0 Dollar",
                "XXXX Dollar".length(), this.getVertexBufferObjectManager());
        leftText.registerEntityModifier(new ScaleModifier(2, 0.0f, 1.0f));
        mHUD.attachChild(leftText);


        // mCamera에 mHUD를 붙여야 캐릭터가 화면을 벗어나 이동을 해도 버튼이 그대로 머물게 됩니다.
        // 그리고 이 라인이 있어야 점프 버튼이 보입니다.
        camera.setHUD(mHUD);

        // ----------------------------------------------------------------------
        // 컨트롤러 셋팅
        // ----------------------------------------------------------------------
        this.mDigitalOnScreenControl = new DigitalOnScreenControl(10,
                CAMERA_HEIGHT- this.mOnScreenControlBaseTextureRegion.getHeight()- 5, this.camera,
                this.mOnScreenControlBaseTextureRegion,
                this.mOnScreenControlKnobTextureRegion, 0.1f,
                this.getVertexBufferObjectManager(),
                new IOnScreenControlListener() {

                    @Override
                    public void onControlChange(
                            final BaseOnScreenControl pBaseOnScreenControl,
                            final float pValueX, final float pValueY) {

                        //땅에서 멈추지 않고 바로 반대방향 이동을 했는가 또는 현재 대기상태인가
                        if (pValueX > 0  && !isPlayerAttack) {
                            if((playerDir == -1 && isLanded) || isPlayerStoping){
                                player_sprite.stopAnimation();
                            }
                            movePlayerRight(player_sprite, player_body);
                        }

                        else if (pValueX < 0  && !isPlayerAttack) {
                            if((playerDir == 1 && isLanded) || isPlayerStoping){
                                player_sprite.stopAnimation();
                            }
                            movePlayerLeft(player_sprite, player_body);
                        }

                        else if (pValueX == 0) {
                            //이동중에서 자연스럽게 멈추었는가?
                            if (isPlayerMoving && isLanded) {
                                stopPlayer(player_sprite, player_body);
                                isPlayerStoping = true;    //다시 이동시 대기모션을 자연스럽게 끊기 위해 stoping 을 활성화
                            }else if(!isPlayerMoving && isPlayerStoping && isLanded){
                                stopPlayer(player_sprite, player_body);
                            }
                        }
                    }
                });

        this.mDigitalOnScreenControl.getControlBase().setBlendFunction(
                GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
        this.mDigitalOnScreenControl.getControlBase().setAlpha(0.55f);
        this.mDigitalOnScreenControl.getControlBase().setScaleCenter(0, 128);
        this.mDigitalOnScreenControl.getControlBase().setScale(1.5f);
        this.mDigitalOnScreenControl.getControlKnob().setAlpha(0.40f);
        this.mDigitalOnScreenControl.getControlKnob().setScale(0.7f);
        this.mDigitalOnScreenControl.refreshControlKnobPosition();
        this.mDigitalOnScreenControl.setAllowDiagonal(false);

    }

    public void callAttackController(){
        // ------------------------------------------------------
        // 기본 공격 버튼
        // ------------------------------------------------------
        Sprite shoot = new Sprite(CAMERA_WIDTH - 200, CAMERA_HEIGHT - 110,
                mShootTextureRegion, this.getVertexBufferObjectManager()) {
            @Override
            public boolean onAreaTouched(TouchEvent pEvent, float pX, float pY) {
                if (pEvent.isActionDown() && !isPlayerAttack && !reduceHealth) {
                    if(player_info.myCharacterNum == 2){
                        arrowPlayer(player_sprite,playerDir);
                    }
                    else if(player_info.myCharacterNum == 1){
                        swordPlayer(player_sprite,playerDir);
                    }
                    isPlayerAttack = true;
                    this.setScale(0.55f);
                    this.setColor(0.5f,0.5f,0.5f);
                }
                if(pEvent.isActionUp()){
                    this.setScale(0.6f);
                    this.setColor(1f,1f,1f);
                }
                return false;
            }
        };
        shoot.setScale(0.60f);
        mHUD.registerTouchArea(shoot);
        mHUD.attachChild(shoot);

        // ------------------------------------------------------
        // 스킬 공격 버튼 1
        // ------------------------------------------------------
        Sprite skill1 = new Sprite(CAMERA_WIDTH - 300, CAMERA_HEIGHT - 110,
                mSkill1TextureRegion, this.getVertexBufferObjectManager()) {
            @Override
            public boolean onAreaTouched(TouchEvent pEvent, float pX, float pY) {
                if (pEvent.isActionDown() && !isPlayerAttack && !reduceHealth) {
                    isPlayerAttack = true;
                    skillListChecked(player_info.mySkill1,1);
                    this.setScale(0.45f);
                    this.setColor(0.5f,0.5f,0.5f);
                }
                if(pEvent.isActionMove() || pEvent.isActionUp()){
                    this.setScale(0.5f);
                    this.setColor(1f,1f,1f);
                }
                return false;
            }
        };
        skill1.setScale(0.5f);
        mHUD.registerTouchArea(skill1);
        mHUD.attachChild(skill1);

        // ------------------------------------------------------
        // 스킬 공격 버튼 2
        // ------------------------------------------------------
        Sprite skill2 = new Sprite(CAMERA_WIDTH - 400, CAMERA_HEIGHT - 110,
                mSkill2TextureRegion, this.getVertexBufferObjectManager()) {
            @Override
            public boolean onAreaTouched(TouchEvent pEvent, float pX, float pY) {
                if (pEvent.isActionDown() && !isPlayerAttack && !reduceHealth) {
                    isPlayerAttack = true;
                    skillListChecked(player_info.mySkill2,2);
                    this.setScale(0.45f);
                    this.setColor(0.5f,0.5f,0.5f);
                }
                if(pEvent.isActionMove() || pEvent.isActionUp()){
                    this.setScale(0.5f);
                    this.setColor(1f,1f,1f);
                }
                return false;
            }
        };
        skill2.setScale(0.5f);
        mHUD.registerTouchArea(skill2);
        mHUD.attachChild(skill2);
    }

    public void skillListChecked(int skillNumber, int skillkey){
        switch (skillNumber){
            case 1:showDoubleSword(player_sprite,playerDir,skillCount,skillkey); break;
            case 2:showChargePlayer(player_sprite,playerDir,skillkey); break;
            case 3:showThorwSword(player_sprite,playerDir,skillCount,skillkey); break;
            default: isPlayerAttack = false; break;
        }
    }

    /*********************************************************
     *
     * 컨트롤러 입력에 따른 플레이어 방향 지시 및 점프 처리
     *
     *********************************************************/

    public void movePlayerRight(AnimatedSprite _playerSprite, Body _playerBody) {
        //현재 점프 중이라면 좌표값의 변경만 취한다.
        if(!isLanded){
            _playerBody.setLinearVelocity(mLinearVelocityX, _playerBody.getLinearVelocity().y);
        }else if(!player_sprite.isAnimationRunning()) {
            //부드러운 움직임 처리를 위해 애니메이트가 완료 되었을때 다시 애니메이트한다.
            _playerBody.setLinearVelocity(mLinearVelocityX, _playerBody.getLinearVelocity().y);
            if(player_info.myCharacterNum == 2){
                _playerSprite.animate(new long[]{45, 45, 45, 45, 45, 45, 45, 45}, 0, 7, false);
            }else {
                _playerSprite.animate(new long[]{45, 45, 45, 45, 45, 45, 45, 45, 45, 45}, 0, 9, false);
            }
            isPlayerMoving = true;
            isPlayerStoping = false;
        }

        playerDir = PLAYER_TO_RIGHT;

    }

    public void movePlayerLeft(AnimatedSprite _playerSprite, Body _playerBody) {
        if(!isLanded){
            _playerBody.setLinearVelocity(-mLinearVelocityX, _playerBody.getLinearVelocity().y);
        }else if(!player_sprite.isAnimationRunning()) {
            _playerBody.setLinearVelocity(-mLinearVelocityX, _playerBody.getLinearVelocity().y);
            if(player_info.myCharacterNum == 2){
                _playerSprite.animate(new long[]{45, 45, 45, 45, 45, 45, 45, 45}, 8, 15, false);
            }else {
                _playerSprite.animate(new long[]{45, 45, 45, 45, 45, 45, 45, 45, 45, 45}, 11, 20, false);
            }
            isPlayerMoving = true;
            isPlayerStoping = false;
        }

        playerDir = PLAYER_TO_LEFT;
    }

    public void stopPlayer(AnimatedSprite _playerSprite, Body _playerBody) {
        int px=0,py=0;
        if(playerDir == 1){
            if(player_info.myCharacterNum == 2){
                px = 32;
                py = 41;
            }else {
                px = 44;
                py = 54;
            }
        }else if(playerDir == -1){
            if(player_info.myCharacterNum == 2){
                px = 42;
                py = 51;
            }else {
                px = 55;
                py = 65;
            }
        }
        _playerBody.setLinearVelocity(0f, _playerBody.getLinearVelocity().y);
        if(!player_sprite.isAnimationRunning() && isPlayerStoping)
            if(player_info.myCharacterNum == 2){
                _playerSprite.animate(new long[]{100, 100, 100, 100, 100, 100, 100, 100, 100, 100}, px, py, false);
            }else {
                _playerSprite.animate(new long[]{50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50}, px, py, false);
            }
        isPlayerMoving = false;
    }

    public void jumpPlayer(AnimatedSprite _playerSprite, Body _playerBody) {
        int px=0,py=0;
        float jump2;

        if(playerDir == 1){
            if(player_info.myCharacterNum == 2){
                px = 52; py =61;
            }else{
                px = 66; py = 76;
            }
        }else if(playerDir == -1){
            if(player_info.myCharacterNum == 2){
                px = 62; py = 71;
            }else {
                px = 77;
                py = 87;
            }
        }
        if(!isJumpTwo)
            jump2 = -mImpulseY;
        else
            jump2 = -2f;

        _playerBody.applyLinearImpulse(0, jump2, 0, _playerBody.getPosition().y);
        if(player_info.myCharacterNum == 2){
            _playerSprite.animate(new long[]{50, 50, 50, 355, 365, 300, 50, 50, 50, 50}, px, py, false);
        }else {
            _playerSprite.animate(new long[]{50, 50, 50, 55, 65, 900, 50, 50, 50, 50, 50}, px, py, false);
        }
        isPlayerStoping = false;
    }

    public void arrowPlayer(AnimatedSprite _playerSprite, int _playerDir){
        if(_playerDir == 1) {
            _playerSprite.animate(new long[]{sword_speed, sword_speed, sword_speed, sword_speed}, 16, 19, false);
        }else if(_playerDir == -1){
            _playerSprite.animate(new long[]{sword_speed, sword_speed, sword_speed, sword_speed}, 24, 27, false);
        }
        TimerHandler tempHandler = new TimerHandler( sword_delay/2f, new ITimerCallback() {
            @Override
            public void onTimePassed(final TimerHandler pTimerHandler) {
                showArrow(player_sprite, playerDir, "arrow");
            }
        });
        this.getEngine().registerUpdateHandler(tempHandler);
    }

    public void swordPlayer(AnimatedSprite _playerSprite, int _playerDir){
        player_body.setLinearVelocity(0f, player_body.getLinearVelocity().y);
        if(_playerDir == 1) {
            _playerSprite.animate(new long[] {sword_speed, sword_speed, sword_speed, sword_speed}, 22, 25, false);
        }else{
            _playerSprite.animate(new long[] {sword_speed, sword_speed, sword_speed, sword_speed}, 33, 36, false);
        }
        TimerHandler tempHandler = new TimerHandler( sword_delay/2f, new ITimerCallback() {
            @Override
            public void onTimePassed(final TimerHandler pTimerHandler) {
                showSword(player_sprite, playerDir,swordCount++);
            }
        });
        this.getEngine().registerUpdateHandler(tempHandler);
    }

    public void showChargePlayer(final AnimatedSprite _playerSprite, final int _playerDir, final int skillkey){
        player_body.setLinearVelocity(0f, player_body.getLinearVelocity().y);
        if(_playerDir == 1) {
            _playerSprite.animate(new long[] {sword_speed, sword_speed, sword_speed, sword_speed}, 88, 91, false);
        }else{
            _playerSprite.animate(new long[] {sword_speed, sword_speed, sword_speed, sword_speed}, 99, 102, false);
        }
        TimerHandler tempHandler = new TimerHandler( sword_delay/2f, new ITimerCallback() {
            @Override
            public void onTimePassed(final TimerHandler pTimerHandler) {
                showChargePlayer(player_sprite, playerDir, skillCount++, skillkey);
            }
        });
        this.getEngine().registerUpdateHandler(tempHandler);
    }

    /***********************************************************
     *
     * 스킬 스프라이트 및 처리
     *
     ***********************************************************/

    public void showDoubleSword(AnimatedSprite _playerSprite, int _playerDir, int number, int skillkey) {
        int sword_location = 30;
        int swordArray[];
        skillCount++;
        if(_playerDir == 1) {
            swordArray = new int[]{0,1,2,3,0,1,2,3};
            player_sprite.animate(new long[] {sword_speed, sword_speed, sword_speed, sword_speed,
                    sword_speed, sword_speed, sword_speed, sword_speed},new int[]{26,27,28,29,26,27,28,29}, false);
        }else{
            swordArray = new int[]{4,5,6,7,4,5,6,7};
            player_sprite.animate(new long[] {sword_speed, sword_speed, sword_speed, sword_speed,
                    sword_speed, sword_speed, sword_speed, sword_speed},new int[]{ 37, 38,39, 40, 37, 38, 39, 40}, false);
        }


        AnimatedSprite mSwordSprite = new AnimatedSprite(_playerSprite.getX() + (_playerDir * sword_location), _playerSprite.getY(),
                mSwordTextureRegion, this.getVertexBufferObjectManager());
        mSwordSprite.animate(new long[] {sword_speed, sword_speed, sword_speed, sword_speed,
                sword_speed, sword_speed, sword_speed, sword_speed},swordArray, false);
        mSwordSprite.setScale(1.5f);
        mSwordSprite.setColor(0.61774f,0.364f,0.42f);
        mSwordSprite.setUserData("sword"+number);
        mEntityList.add(mSwordSprite);
        mScene.attachChild(mSwordSprite);

        mSound_sword2.play();
        mSound_sword2.play();
        TimerHandler tmpHendle1 = new TimerHandler( sword_speed*3.99f*0.001f, new ITimerCallback() {
            @Override
            public void onTimePassed(final TimerHandler pTimerHandler) {
                mSound_sword2.play();
                mSound_sword2.play();
            }
        });
        this.getEngine().registerUpdateHandler(tmpHendle1);

        if(findShape("sword"+number) != null){
            int i = 0;  //엔티티의 번지수를 체크
            for (IEntity pEntity : mEntityList) {
                if (pEntity.getUserData() != null && (pEntity.getUserData().toString().contains("enemy") ||
                        pEntity.getUserData().toString().contains("middle") ||
                        pEntity.getUserData().toString().contains("high"))) {
                    AnimatedSprite bodySprite = (AnimatedSprite) mEntityList.get(i);
                    if (mSwordSprite.collidesWith(bodySprite)) {
                        fix3_name = pEntity.getUserData().toString();
                        int targetNumber = battleEnermy1.indexOf(new battle_enermy(fix3_name));
                        if (targetNumber >= 0) {
                            Debug.d(fix3_name + " Sword Hit!~~ ");
                            mSound_sword1.play();
                            TimerHandler tmpHendle2 = new TimerHandler( sword_speed*4f*0.001f, new ITimerCallback() {
                                @Override
                                public void onTimePassed(final TimerHandler pTimerHandler) {
                                    mSound_sword2.play();
                                    mSound_sword2.play();
                                }
                            });
                            this.getEngine().registerUpdateHandler(tmpHendle2);
                            int damage = damageCalculation.getDamageCalculation(false,
                                    player_info.myLevel, player_info.myATK,
                                    player_info.myF_ATK, player_info.myHit,
                                    battleEnermy1.get(targetNumber).lv,
                                    battleEnermy1.get(targetNumber).def,
                                    battleEnermy1.get(targetNumber).p_def);

                            float tmp_xDistance = 5f;
                            float levelbalance = battleEnermy1.get(targetNumber).lv - player_info.myLevel;
                            if(levelbalance >=5 && (pEntity.getUserData().toString().contains("enemy"))){
                                tmp_xDistance = 1f - ( (levelbalance-4) *  0.4f);
                            }
                            else if(levelbalance >=3 && pEntity.getUserData().toString().contains("middle")){
                                tmp_xDistance = 2f - ( (levelbalance-2) *  0.25f);
                            }
                            else if(levelbalance >=3 && pEntity.getUserData().toString().contains("high")){
                                tmp_xDistance = 3f - ( (levelbalance-2) *  0.1f);
                            }
                            //타겟에너미, 다단히트수, 공격횟수, 데미지, 타격 딜레이, 밀리는 거리 x축, 밀리는 거리 y축
                            if(skillkey == 1) {
                                float skillDamage = ((float) player_info.getSkill1_3() / 100f * 100f);
                                damage *= skillDamage;
                                damageCollision(fix3_name, 1, 2, damage , sword_speed*4f/1000f,
                                        tmp_xDistance + player_info.getSkill1_5(), -0.4f + player_info.getSkill1_6());
                            }else{
                                float skillDamage = ((float) player_info.getSkill2_3() / 100f * 100f);
                                damage *= skillDamage;
                                damageCollision(fix3_name, 1, 2, damage , sword_speed*4f/1000f,
                                        tmp_xDistance + player_info.getSkill2_5(), -0.4f + player_info.getSkill2_6());
                            }
                        }
                    }
                }
                i++;
            }
        }

        destroyDoubleSword("sword"+number);
    }
    public void destroyDoubleSword(final String swordName) {
        timerHandler = new TimerHandler( sword_delay, new ITimerCallback() {
            @Override
            public void onTimePassed(final TimerHandler pTimerHandler) {
                destroyGameObject(swordName);
                isPlayerAttack = false;

            }
        });
        this.getEngine().registerUpdateHandler(timerHandler);
    }

    public void showChargePlayer(AnimatedSprite _playerSprite, int _playerDir, int number,int skillkey){
        int sword_location = 20;
        int swordArray[];
        skillCount++;
        if(_playerDir == 1) {
            swordArray = new int[]{0,1,2,3};
            player_sprite.animate(new long[] {sword_speed, sword_speed, sword_speed, sword_speed},
                    new int[]{92,93,94,94}, false);
        }else{
            sword_location = 370;
            swordArray = new int[]{4,5,6,7};
            player_sprite.animate(new long[] {sword_speed, sword_speed, sword_speed, sword_speed},
                    new int[]{ 103,104,105,105}, false);
        }


        AnimatedSprite mSwordSprite = new AnimatedSprite(_playerSprite.getX() + (_playerDir * sword_location), _playerSprite.getY(),
                mChargeClashTextureRegion, this.getVertexBufferObjectManager());
        mSwordSprite.animate(new long[] {sword_speed, sword_speed, sword_speed, sword_speed},swordArray, false);
        mSwordSprite.setUserData("sword"+number);
        mEntityList.add(mSwordSprite);
        mScene.attachChild(mSwordSprite);

        mSound_sword2.play();
        mSound_sword2.play();

        //간격마다의 타격이 필요한 경우 아래 코드를 핸들러를 이용한다.
        if(findShape("sword"+number) != null){
            int i = 0;  //엔티티의 번지수를 체크
            for (IEntity pEntity : mEntityList) {
                if (pEntity.getUserData() != null && (pEntity.getUserData().toString().contains("enemy") ||
                        pEntity.getUserData().toString().contains("middle") ||
                        pEntity.getUserData().toString().contains("high"))) {
                    AnimatedSprite bodySprite = (AnimatedSprite) mEntityList.get(i);
                    if (mSwordSprite.collidesWith(bodySprite)) {
                        fix3_name = pEntity.getUserData().toString();
                        int targetNumber = battleEnermy1.indexOf(new battle_enermy(fix3_name));
                        if (targetNumber >= 0) {
                            Debug.d(fix3_name + " Sword Hit!~~ ");
                            mSound_sword1.play();

                            int damage = damageCalculation.getDamageCalculation(false,
                                    player_info.myLevel, player_info.myATK,
                                    player_info.myF_ATK, player_info.myHit,
                                    battleEnermy1.get(targetNumber).lv,
                                    battleEnermy1.get(targetNumber).def,
                                    battleEnermy1.get(targetNumber).p_def);

                            float tmp_xDistance = 5f;
                            float levelbalance = battleEnermy1.get(targetNumber).lv - player_info.myLevel;
                            if(levelbalance >=5 && (pEntity.getUserData().toString().contains("enemy"))){
                                tmp_xDistance = 1f - ( (levelbalance-4) *  0.4f);
                            }
                            else if(levelbalance >=3 && pEntity.getUserData().toString().contains("middle")){
                                tmp_xDistance = 2f - ( (levelbalance-2) *  0.25f);
                            }
                            else if(levelbalance >=3 && pEntity.getUserData().toString().contains("high")){
                                tmp_xDistance = 3f - ( (levelbalance-2) *  0.1f);
                            }
                            //타겟에너미, 다단히트수, 공격횟수, 데미지, 타격 딜레이, 밀리는 거리 x축, 밀리는 거리 y축
                            if(skillkey == 1) {
                                float skillDamage = ((float) player_info.getSkill1_3() / 100f * 100f);
                                damage *= skillDamage;
                                damageCollision(fix3_name, 1, 1, damage , 0,
                                        tmp_xDistance + player_info.getSkill1_5(), -1.5f + player_info.getSkill1_6());
                            }else{
                                float skillDamage = ((float) player_info.getSkill2_3() / 100f * 100f);
                                damage *= skillDamage;
                                damageCollision(fix3_name, 1, 1, damage , 0,
                                        tmp_xDistance + player_info.getSkill2_5(), -0.7f + player_info.getSkill2_6());
                            }
                        }
                    }else{

                    }
                }
                i++;
            }
        }
        destroyChargeSword("sword"+number);

    };
    public void destroyChargeSword(final String swordName){
        timerHandler = new TimerHandler( sword_delay/2f, new ITimerCallback() {
            @Override
            public void onTimePassed(final TimerHandler pTimerHandler) {
                destroyGameObject(swordName);
                isPlayerAttack = false;
            }
        });
        this.getEngine().registerUpdateHandler(timerHandler);
    }

    public void showThorwSword(final AnimatedSprite _playerSprite, final int _playerDir, final int number , final int skillkey){

        if(_playerDir == 1) {
            player_sprite.animate(new long[] {15, 15,15, 1555},
                    new int[]{92,93,94,94}, false);
        }else{
            player_sprite.animate(new long[] {15, 15,15, 1555},
                    new int[]{ 103,104,105,105}, false);
        }

        final AnimatedSprite mSwordSprite = new AnimatedSprite(_playerSprite.getX() + (_playerDir), _playerSprite.getY(),
                mSwordThrowTextureRegion, this.getVertexBufferObjectManager());
        mSwordSprite.animate(20);
        mSwordSprite.setUserData("sword"+number);
        mEntityList.add(mSwordSprite);
        mScene.attachChild(mSwordSprite);

        final PhysicsHandler physicsHandler = new PhysicsHandler(mSwordSprite);
        mSwordSprite.registerUpdateHandler(physicsHandler);
        physicsHandler.setVelocityX(_playerDir*500f);

        TimerHandler sowrdHand2 = new TimerHandler(0.8f, new ITimerCallback() {
            @Override
            public void onTimePassed(final TimerHandler pTimerHandler) {
                physicsHandler.setVelocityX(-500f * _playerDir);
            }
        });this.getEngine().registerUpdateHandler(sowrdHand2);

        for(int i = 1; i <= 15; i++) {
            final float finalI = i * 0.1075f;
            TimerHandler sowrdHand = new TimerHandler(finalI, new ITimerCallback() {
                @Override
                public void onTimePassed(final TimerHandler pTimerHandler) {
                    if(findShape("sword"+number) != null){
                        int i = 0;  //엔티티의 번지수를 체크
                        for (IEntity pEntity : mEntityList) {
                            if (pEntity.getUserData() != null && (pEntity.getUserData().toString().contains("enemy") ||
                                    pEntity.getUserData().toString().contains("middle") ||
                                    pEntity.getUserData().toString().contains("high"))) {
                                AnimatedSprite bodySprite = (AnimatedSprite) mEntityList.get(i);
                                if (mSwordSprite.collidesWith(bodySprite)) {
                                    fix3_name = pEntity.getUserData().toString();
                                    int targetNumber = battleEnermy1.indexOf(new battle_enermy(fix3_name));
                                    if (targetNumber >= 0) {
                                        Debug.d(fix3_name + " Sword Hit!~~ ");
                                        mSound_sword1.play();
                                        int damage = damageCalculation.getDamageCalculation(false,
                                                player_info.myLevel, player_info.myATK,
                                                player_info.myF_ATK, player_info.myHit,
                                                battleEnermy1.get(targetNumber).lv,
                                                battleEnermy1.get(targetNumber).def,
                                                battleEnermy1.get(targetNumber).p_def);

                                        if(skillkey == 1) {
                                            float skillDamage = ((float) player_info.getSkill1_3() / 100f * 100f);
                                            damage *= skillDamage;
                                        }else{
                                            float skillDamage = ((float) player_info.getSkill2_3() / 100f * 100f);
                                            damage *= skillDamage;
                                        }

                                        //타겟에너미, 다단히트수, 공격횟수, 데미지, 타격 딜레이, 밀리는 거리 x축, 밀리는 거리 y축
                                        damageCollision(fix3_name, 2, 1, damage , 0.2f,
                                                0.1f , -0.28f);
                                    }
                                }
                            }
                            i++;
                        }
                    }
                }
            });
            this.getEngine().registerUpdateHandler(sowrdHand);
        }

        destroyThorwSword("sword"+number);

    }
    public void destroyThorwSword(final String swordName) {
        timerHandler = new TimerHandler( 1.6f, new ITimerCallback() {
            @Override
            public void onTimePassed(final TimerHandler pTimerHandler) {
                destroyGameObject(swordName);
                isPlayerAttack = false;

            }
        });
        this.getEngine().registerUpdateHandler(timerHandler);
    }

    /***********************************************************
     *
     * 표창, 검기 등 스트라이프 처리
     *
     ***********************************************************/

    public void showDamage(final String fix_Damage, final int Damage, int damageTurn){

        if (mPhysicsWorld.getPhysicsConnectorManager().findBodyByShape(
                findShape(fix_Damage)) != null) {
            final IShape temp_enemy_shape = findShape(fix_Damage);
            showDamageSkin(fix_Damage,damageSkinCount++);
            for(int i = 0; i < damageTurn; i ++) {
                final int finalI = i;
                damageHandler = new TimerHandler(0.2f + (0.1f * i), new ITimerCallback() {
                    @Override
                    public void onTimePassed(final TimerHandler pTimerHandler) {
                        damageCount++;
                        Font A = dFont;
                        if(fix_Damage.equals(player_body.getUserData().toString())){
                            A = pFont;
                        }
                        Text damageText = new Text(temp_enemy_shape.getX(), temp_enemy_shape.getY() - 10 + (23* finalI),
                                A, String.valueOf(Damage), String.valueOf(Damage).length(),
                                getVertexBufferObjectManager());

                        damageText.setUserData("damage" + damageCount);
                        mEntityList.add(damageText);
                        mScene.attachChild(damageText);
                        destroyDamage("damage" + damageCount);
                    }
                });
                this.getEngine().registerUpdateHandler(damageHandler);
            }
        }


    }

    public void destroyDamage(final String desDamage){
        damageHandler = new TimerHandler((float) 1.0f, new ITimerCallback() {
            @Override
            public void onTimePassed(final TimerHandler pTimerHandler) {
                if(findShape(desDamage) != null) {
                    mScene.detachChild(findShape(desDamage));
                }
            }
        });
        this.getEngine().registerUpdateHandler(damageHandler);
    }

    public void showDamageSkin(String fix_Damage, final int number){
        if (mPhysicsWorld.getPhysicsConnectorManager().findBodyByShape(
                findShape(fix_Damage)) != null) {
            IShape temp_enemy_shape = findShape(fix_Damage);
            AnimatedSprite damageEffect = new AnimatedSprite(temp_enemy_shape.getX(), temp_enemy_shape.getY(),
                    mDamageTextureRegion, this.getVertexBufferObjectManager());
            damageEffect.setUserData("damageEffect"+number);
            damageEffect.animate(50,false);
            mScene.attachChild(damageEffect);
            mEntityList.add(damageEffect);

            TimerHandler tempTimer = new TimerHandler(0.3f, new ITimerCallback() {
                @Override
                public void onTimePassed(final TimerHandler pTimerHandler) {
                    destroyGameObject("damageEffect"+number);
                }
            });
            this.getEngine().registerUpdateHandler(tempTimer);
        }
    }

    public void showArrow(AnimatedSprite _playerSprite, int _playerDir, String ArrowName) {
        arrowCount++;
        mSound_sword2.play();
        if(playerDir == 1) {
            player_sprite.animate(new long[]{sword_speed, sword_speed, sword_speed, sword_speed}, 20, 23, false);
        }else if(playerDir == -1){
            player_sprite.animate(new long[]{sword_speed, sword_speed, sword_speed, sword_speed}, 28, 31, false);
        }

        AnimatedSprite mArrowSprite = new AnimatedSprite(
                _playerSprite.getX() + _playerDir, _playerSprite.getY()
                + (_playerSprite.getHeight() / 2) - 3,
                mArrowTextureRegion, this.getVertexBufferObjectManager());

        FixtureDef mShurikenFixtureDef = PhysicsFactory.createFixtureDef(0, 0f,
                0f, false, CATEGORYBIT_SHURIKEN, MASKBITS_SHURIKEN, (short) 0);

        Body mArrowBody = PhysicsFactory.createCircleBody(
                this.mPhysicsWorld, mArrowSprite, BodyType.DynamicBody,
                mShurikenFixtureDef);
        mArrowBody.setUserData(ArrowName + arrowCount);

        mArrowBody.setBullet(true);

        this.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(
                mArrowSprite, mArrowBody, true, true));
        mArrowSprite.setUserData(ArrowName + arrowCount);
        mArrowBody.setLinearVelocity(_playerDir * 20, 0);

        mScene.attachChild(mArrowSprite);
        if(playerDir == 1) {
            mArrowSprite.animate(new long[]{30,30,30,30},new int[]{0,1,2,3},true);
        }else if(playerDir == -1){
            mArrowSprite.animate(new long[]{30,30,30,30},new int[]{4,5,6,7},true);
        }
        mEntityList.add(mArrowSprite);
        destroyLuncher(ArrowName + String.valueOf(arrowCount));

    }

    //원거리 물리엔진 공격 삭제
    public void destroyLuncher(final String _deletObject) {

        timerHandler = new TimerHandler(0.5f, new ITimerCallback() {
            @Override
            public void onTimePassed(final TimerHandler pTimerHandler) {
                destroyGameObject(_deletObject);
            }
        });
        this.getEngine().registerUpdateHandler(timerHandler);

        timerHandler = new TimerHandler(sword_delay/2f + (0.8f - ((player_info.myA_Speed/sword_key_delay * 100f) * 0.008f)), new ITimerCallback() {
            @Override
            public void onTimePassed(final TimerHandler pTimerHandler) {
                isPlayerAttack = false;
            }
        });
        this.getEngine().registerUpdateHandler(timerHandler);
    }

    private void makeLuncherDefyGravity() {
        //화살의 무중력 조정
        for (IEntity pEntity : mEntityList) {
            if (pEntity.getUserData() != null) {
                if (pEntity.getUserData().toString().contains("arrow") ||
                        pEntity.getUserData().toString().contains("airwone")) {

                    final Body pShurikenBody = mPhysicsWorld
                            .getPhysicsConnectorManager().findBodyByShape(
                                    (IShape) pEntity);
                    if (pShurikenBody != null)
                        pShurikenBody.applyForce(new Vector2(0,
                                -SensorManager.GRAVITY_EARTH), pShurikenBody
                                .getWorldCenter());

                }
            }
        }
    }

    public void showSword(AnimatedSprite _playerSprite, int _playerDir, int number) {
        /*******************************************************************
         * 프레임 100 당 0.1초
         * 기본공속 100 기준
         * 기본공격 프레임 총 800 / 0.8초
         * 공속 2당 공격속도 1% , 프레임 8 차감(각 2씩 차감)
         *********************************************************************/
        int sword_location = 30, index_sword_x, index_sword_y;
        swordCount++;
        if(_playerDir == 1) {
            index_sword_x = 0; index_sword_y = 3;
            player_sprite.animate(new long[] {sword_speed, sword_speed, sword_speed, sword_speed}, 26, 29, false);
        }else{
            index_sword_x = 4; index_sword_y = 7;
            player_sprite.animate(new long[] {sword_speed, sword_speed, sword_speed, sword_speed}, 37, 40, false);
        }


        AnimatedSprite mSwordSprite = new AnimatedSprite(_playerSprite.getX() + (_playerDir * sword_location), _playerSprite.getY(),
                mSwordTextureRegion, this.getVertexBufferObjectManager());
        mSwordSprite.animate(new long[] {sword_speed, sword_speed, sword_speed, sword_speed}, index_sword_x, index_sword_y, false);
        mSwordSprite.setScale(1.5f);
        mSwordSprite.setUserData("sword"+number);
        mEntityList.add(mSwordSprite);
        mScene.attachChild(mSwordSprite);


        mSound_sword2.play();
        mSound_sword2.play();
        //간격마다의 타격이 필요한 경우 아래 코드를 핸들러를 이용한다.
        if(findShape("sword"+number) != null){
            int i = 0;  //엔티티의 번지수를 체크
            for (IEntity pEntity : mEntityList) {
                if (pEntity.getUserData() != null && (pEntity.getUserData().toString().contains("enemy") ||
                        pEntity.getUserData().toString().contains("middle") ||
                        pEntity.getUserData().toString().contains("high"))) {
                    AnimatedSprite bodySprite = (AnimatedSprite) mEntityList.get(i);
                    if (mSwordSprite.collidesWith(bodySprite)) {
                        fix3_name = pEntity.getUserData().toString();
                        int targetNumber = battleEnermy1.indexOf(new battle_enermy(fix3_name));
                        if (targetNumber >= 0) {
                            Debug.d(fix3_name + " Sword Hit!~~ ");
                            mSound_sword1.play();
                            int damage = damageCalculation.getDamageCalculation(false,
                                    player_info.myLevel, player_info.myATK,
                                    player_info.myF_ATK, player_info.myHit,
                                    battleEnermy1.get(targetNumber).lv,
                                    battleEnermy1.get(targetNumber).def,
                                    battleEnermy1.get(targetNumber).p_def);

                            float tmp_xDistance = 4f;
                            float levelbalance = battleEnermy1.get(targetNumber).lv - player_info.myLevel;
                            if(levelbalance >=5 && (pEntity.getUserData().toString().contains("enemy") ||
                                    pEntity.getUserData().toString().contains("middle"))){
                                tmp_xDistance = 3f - ( (levelbalance-4) *  0.3f);
                            }
                            else if(levelbalance >=3 && pEntity.getUserData().toString().contains("high")){
                                tmp_xDistance = 2f - ( (levelbalance-2) *  0.1f);
                            }
                            damageCollision(fix3_name, 1, 1, damage, 0f, tmp_xDistance, -0.4f);
                        }
                    }
                }
                i++;
            }
        }

        destroySword("sword"+number);
    }

    public void destroySword(final String swordName) {
        timerHandler = new TimerHandler( sword_delay/2f, new ITimerCallback() {
            @Override
            public void onTimePassed(final TimerHandler pTimerHandler) {
                destroyGameObject(swordName);

            }
        });
        this.getEngine().registerUpdateHandler(timerHandler);
        timerHandler = new TimerHandler( (sword_delay/2f) + (0.8f - (((100-player_info.myA_Speed)/sword_key_delay * 100f) * 0.008f)), new ITimerCallback() {
            @Override
            public void onTimePassed(final TimerHandler pTimerHandler) {
                isPlayerAttack = false;

            }
        });
        this.getEngine().registerUpdateHandler(timerHandler);
    }

    /*************************************************************
     *
     * 바디 충돌 처리 메소드 , 오브젝트 충돌 처리
     *
     *************************************************************/

    private void createCollision() {
        this.mPhysicsWorld.setContactListener(new ContactListener() {

            public void beginContact(Contact contact) {

                // 2개의 바디가 부딪혔을 때 userData 저장
                if (contact.getFixtureA().getBody().getUserData() != null
                        && contact.getFixtureA().getBody().getUserData() != null) {

                    fix1_name = contact.getFixtureA().getBody().getUserData()
                            .toString();
                    fix2_name = contact.getFixtureB().getBody().getUserData()
                            .toString();

                    //플레이어와 관련된 충돌이라면 fix1 에 player 이름을 고정 시킨다.
                    if(fix2_name.contains("player")){
                        String tmp_name = fix1_name;
                        fix1_name = fix2_name;
                        fix2_name = tmp_name;
                    }

                } else {
                    fix1_name = "";
                    fix2_name = "";
                }

                Debug.d("fix1_name : " + fix1_name);
                Debug.d("fix2_name : " + fix2_name);



                // 플레이어와 벽의 충돌이 생길 경우 점프를 허용
                if (fix1_name.contains("player") && fix2_name.contains("wall")) {
                    isLanded = true;
                    isPlayerStoping = true;
                    isJumpTwo = false;
                    Debug.d("player hits wall");
                }

                // 플레이어가 함정에 빠졌을 때에 대한 처리
                if (fix1_name.contains("player") && fix2_name.contains("trap")) {
                    Debug.d("player hits trap");
                    callTimerHandler(fix2_name,"desTrap");

                }

                // 플레이어가 보물에 빠졌을 때에 대한 처리
                if (fix1_name.contains("player") && fix2_name.contains("treasure")) {
                    Debug.d("player hits treasure");
                    callTimerHandler(fix2_name,"desTreasure");

                }

                // 플레이어와 돈의 충돌
                // 돈을 없애고, 돈 점수를 올립니다.
                if (fix1_name.contains("player") && fix2_name.contains("money")) {
                    Debug.d("player hits money");
                    dollarCount++;      // 획득 금액 상승
                    callTimerHandler(fix2_name,"desMoney");
                }

                if (fix1_name.contains("player") && fix2_name.contains("potion")) {
                    Debug.d("player hits potion");
                    dollarCount++;      // 획득 금액 상승
                    callTimerHandler(fix2_name,"desPotion");
                }

                // 플레이어와 적의 충돌시의 처리. 에너지바를 줄일 수 있도록 합니다.
                if (fix1_name.contains("player") && (fix2_name.contains("enemy")
                        || fix2_name.contains("middle")  || fix2_name.contains("high"))) {
                    Debug.d("player hits enemy");
                    if(reduceHealth == false) {
                        reduceHealth = true;
                        int targetNumber = battleEnermy1.indexOf(new battle_enermy(fix2_name));
                        if (targetNumber >= 0) {
                            updateHealthBar(
                                    damageCalculation.getDamageCalculation(true,
                                            battleEnermy1.get(targetNumber).lv,
                                            battleEnermy1.get(targetNumber).atk,
                                            battleEnermy1.get(targetNumber).f_atk,
                                            95,player_info.myLevel,player_info.myDEF,0)
                                    , 0);
                        }
                    }
                }

                // 화살과 벽의 충돌
                if ((fix1_name.contains("arrow") && fix2_name
                        .equalsIgnoreCase("wall"))
                        || (fix2_name.contains("arrow") && fix1_name
                        .equalsIgnoreCase("wall"))) {
                    desArrow = true;

                }

                //벽과 벽의 충돌
                if((fix1_name.contains("airwone") && fix2_name.contains("wall"))
                        || (fix2_name.contains("airwone") && fix1_name.contains("wall"))){

                    if(fix2_name.contains("airwone")){
                        String tmp_name = fix1_name;
                        fix1_name = fix2_name;
                        fix2_name = tmp_name;
                    }

                    for (int i = 0; i < wallAir; i++) {
                        if(fix1_name.equals("airwone"+i)) {
                            Debug.d("부딛힘");
                            wall1[i] = -wall1[i];
                            break;
                        }
                    }
                }

                // 화살과 적군의 충돌
                if ((fix1_name.contains("arrow") && (fix2_name
                        .contains("enemy") || fix2_name.contains("middle") || fix2_name.contains("high")))
                        || (fix2_name.contains("arrow") && (fix1_name
                        .contains("enemy") || fix1_name.contains("middle")) || fix1_name.contains("high"))) {
                    if (fix2_name.contains("arrow")){
                        String tmp_n = fix2_name;
                        fix2_name = fix1_name;
                        fix1_name = tmp_n;
                    }

                    if(fix1_name.contains("arrow")){
                        int targetNumber = battleEnermy1.indexOf(new battle_enermy(fix2_name));
                        if (targetNumber >= 0) {
                            int damage = damageCalculation.getDamageCalculation(false,
                                    player_info.myLevel, player_info.myATK,
                                    player_info.myF_ATK, player_info.myHit,
                                    battleEnermy1.get(targetNumber).lv,
                                    battleEnermy1.get(targetNumber).def,
                                    battleEnermy1.get(targetNumber).p_def);
                            float tmp_xDistance = 9f;
                            float levelbalance = battleEnermy1.get(targetNumber).lv - player_info.myLevel;
                            if(levelbalance <=5){
                                tmp_xDistance = 9f - ( (levelbalance-7) *  0.4f);
                            }
                            else if(levelbalance <=3 && fix2_name.contains("high")){
                                tmp_xDistance = 5f - ( (levelbalance-2) *  0.1f);
                            }
                            damageCollision(fix2_name, 1, 1, damage, 0f, tmp_xDistance, 2f);
                            mSound_arrow1.play();
                            mSound_arrow1.play();
                        }
                    }

                    desArrow = true;

                }


            }

            public void endContact(Contact contact) {
                //아무런 충돌이 일어나지 않을 때
            }

            public void preSolve(Contact contact, Manifold oldManifold) {

            }

            public void postSolve(Contact contact, ContactImpulse impulse) {

            }

        });

    }

    public void callTimerHandler(final String desName, final String desObject) {

        timerHandler = new TimerHandler((float) 0.01f, new ITimerCallback() {
            @Override
            public void onTimePassed(final TimerHandler pTimerHandler) {

                Debug.d("time handler is called" + desObject);
                // 돈의 충돌 및 메모리 해제
                if (desObject.equals("desMoney")) {
                    Debug.d("inside time handler is called");
                    destroyGameObject(desName);
                }

                // 함정의 충돌 및 메모리 해제
                if (desObject.equals("desTrap")) {
                    Debug.d("inside time handler is called about trap");
                    destroyGameObject(desName);
                    desPlayer = true;
                }

                // 보물의 충돌 및 메모리 해제
                if (desObject.equals("desTreasure")) {
                    Debug.d("inside time handler is called about treasure");
                    destroyGameObject(desName);
                }

                //포션의 충돌 및 메모리 해제
                if (desObject.equals("desPotion")) {
                    Random r = new Random();
                    int healDamge = r.nextInt(30);
                    float heal = player_info.myCurrentHp + (player_info.myMaxHp * ((float)healDamge * 0.01f));

                    updateHealAdd((int) heal);
                    Debug.d("Player do heal about Potion");
                    destroyGameObject(desName);
                }

            }
        });
        this.getEngine().registerUpdateHandler(timerHandler);
    }

    //타겟에너미, 다단히트수, 공격횟수, 데미지, 타격 딜레이, 밀리는 거리 x축, 밀리는 거리 y축
    private void damageCollision(final String fix_enermy, final int atk_counting, int atk_turn, final int hit_damage,
                                 float delay_target, final float distance_x, final float distance_y){
        final float Dirs = playerDir;
        for(int i = 0; i < atk_turn; i ++) {
            float delayi = delay_target*i;
            if(delayi <= 0f){
                delayi = 0.001f;
            }
            TimerHandler TdamageHandler = new TimerHandler(delayi, new ITimerCallback() {
                @Override
                public void onTimePassed(final TimerHandler pTimerHandler) {
                    if (mPhysicsWorld.getPhysicsConnectorManager().findBodyByShape(
                            findShape(fix_enermy)) != null) {
                        Body temp_enemy_body = mPhysicsWorld.getPhysicsConnectorManager()
                                .findBodyByShape(findShape(fix_enermy));
                        if(temp_enemy_body != null) {
                            temp_enemy_body.applyLinearImpulse(distance_x * Dirs, distance_y,
                                    temp_enemy_body.getPosition().x, temp_enemy_body.getPosition().y);

                            int targetNumber = battleEnermy1.indexOf(new battle_enermy(fix_enermy));
                            if (targetNumber >= 0) {
                                battleEnermy1.get(targetNumber).hp -= hit_damage * atk_counting;
                                showDamage(fix_enermy, hit_damage, atk_counting);
                                if (battleEnermy1.get(targetNumber).hp <= 0) {
                                    destroyGameObject(fix_enermy);
                                    clearStageCount++;
                                }
                            }
                        }
                    }
                }
            });
            this.getEngine().registerUpdateHandler(TdamageHandler);
        }
    }

    /**********************************************************************************
     *
     * 거리측정, 적 캐릭터 인공지능 설정 메소드
     *
     **********************************************************************************/

    public float getDistance(Body _player, Body _enemy) {
        //거리 측정 메소드
        float dist_x = (float) Math.pow(_player.getPosition().x - _enemy.getPosition().x, 2);
        float dist_y = (float) Math.pow(_player.getPosition().y - _enemy.getPosition().y, 2);

        return ((float) Math.sqrt(dist_x + dist_y));
    }

    public void doAICalculations(Body _playerBody) {
        //모든 에너미 객체를 체크하며 살아있는지 확인한다.
        for (int i = 0; i < enemyCount; i++) {
            IShape temp_enemy_shape = findShape("enemy" + i);
            Body temp_enemy_body = mPhysicsWorld.getPhysicsConnectorManager()
                    .findBodyByShape(temp_enemy_shape);

            //적이 이동모션중 갑자기 플레이어가 다가왔을때 잠깐 일시적으로 멈춰준다.
            if(temp_enemy_body != null && !enemyLandedArr[i] &&
                    (getDistance(_playerBody, temp_enemy_body) < 5.5f)){

                doAIActions(temp_enemy_body,-2f, false);
            }

            //플레이어가 일정거리 다가왔을 때
            if(temp_enemy_body != null && (getDistance(_playerBody, temp_enemy_body) < 5.5f)){
                //플레이어에게 공격을 받지 않았다면 계속 플레이어에게 이동
                if(temp_enemy_body.getLinearVelocity().y > -1) {
                    float enemyAction = _playerBody.getPosition().x - temp_enemy_body.getPosition().x;
                    if(enemyAction > 0){
                        doAIActions(temp_enemy_body, -1, true);
                    }
                    else{
                        doAIActions(temp_enemy_body, 1, true);
                    }
                }
                enemyLandedArr[i] = true;
            }


            //플레이어와 일정거리 떨어졌을 때
            else if(temp_enemy_body != null && enemyLandedArr[i] &&
                    (getDistance(_playerBody, temp_enemy_body) > 5.51f)){
                enemyLandedArr[i] = false;
                Random random = new Random();
                int ai_r = random.nextInt(100);
                if(ai_r >= 60){doAIActions(temp_enemy_body,1f, false);}
                else if(ai_r >=20){doAIActions(temp_enemy_body,-1f, false);}
                else{doAIActions(temp_enemy_body,0f, false);}
                ai_r = random.nextInt(6);
                final int finalI = i;
                TimerHandler aiBaseTimer = new TimerHandler((float) 2 + ai_r, new ITimerCallback() {
                    @Override
                    public void onTimePassed(final TimerHandler pTimerHandler) {
                        enemyLandedArr[finalI] = true;
                    }
                });
                this.getEngine().registerUpdateHandler(aiBaseTimer);
            }


        }

    }

    public void doAICalculations2(Body _playerBody) {
        //모든 에너미 객체를 체크하며 살아있는지 확인한다.
        for (int i = 0; i < enemyCount2; i++) {
            IShape temp_enemy_shape = findShape("middle" + i);
            Body temp_enemy_body = mPhysicsWorld.getPhysicsConnectorManager()
                    .findBodyByShape(temp_enemy_shape);

            //적이 이동모션중 갑자기 플레이어가 다가왔을때 잠깐 일시적으로 멈춰준다.
            if(temp_enemy_body != null && !enemyLandedArr2[i] &&
                    (getDistance(_playerBody, temp_enemy_body) < 8.5f)){

                doAIActions2(temp_enemy_body,-2f, false);
            }

            //플레이어가 일정거리 다가왔을 때
            if(temp_enemy_body != null && (getDistance(_playerBody, temp_enemy_body) < 8.5f)){
                //플레이어에게 공격을 받지 않았다면 계속 플레이어에게 이동
                if(temp_enemy_body.getLinearVelocity().y > -1) {
                    float enemyAction = _playerBody.getPosition().x - temp_enemy_body.getPosition().x;
                    if(enemyAction > 0){
                        doAIActions2(temp_enemy_body, -1, true);
                    }
                    else{
                        doAIActions2(temp_enemy_body, 1, true);
                    }
                }
                enemyLandedArr2[i] = true;
            }


            //플레이어와 일정거리 떨어졌을 때
            else if(temp_enemy_body != null && enemyLandedArr2[i] &&
                    (getDistance(_playerBody, temp_enemy_body) > 8.51f)){
                enemyLandedArr2[i] = false;
                Random random = new Random();
                int ai_r = random.nextInt(100);
                if(ai_r >= 60){doAIActions2(temp_enemy_body,1f, false);}
                else if(ai_r >=20){doAIActions2(temp_enemy_body,-1f, false);}
                else{doAIActions2(temp_enemy_body,0f, false);}
                ai_r = random.nextInt(6);
                final int finalI = i;
                TimerHandler aiBaseTimer = new TimerHandler((float) 2 + ai_r, new ITimerCallback() {
                    @Override
                    public void onTimePassed(final TimerHandler pTimerHandler) {
                        enemyLandedArr2[finalI] = true;
                    }
                });
                this.getEngine().registerUpdateHandler(aiBaseTimer);
            }


        }

    }

    public void doAICalculations3(Body _playerBody) {
        //모든 에너미 객체를 체크하며 살아있는지 확인한다.
        for (int i = 0; i < enemyCount3; i++) {
            IShape temp_enemy_shape = findShape("high" + i);
            Body temp_enemy_body = mPhysicsWorld.getPhysicsConnectorManager()
                    .findBodyByShape(temp_enemy_shape);

            if (temp_enemy_body != null && !enemyIsSkill[i]) {
                //적이 이동모션중 갑자기 플레이어가 다가왔을때 잠깐 일시적으로 멈춰준다.
                if (temp_enemy_body != null && !enemyLandedArr3[i] &&
                        (getDistance(_playerBody, temp_enemy_body) < 11.5f)) {

                    doAIActions3(temp_enemy_body, -2f, false,-1);
                }

                //플레이어가 일정거리 다가왔을 때
                if (temp_enemy_body != null && (getDistance(_playerBody, temp_enemy_body) < 11.5f)) {
                    //플레이어에게 공격을 받지 않았다면 계속 플레이어에게 이동
                    if (temp_enemy_body.getLinearVelocity().y > -1) {
                        float enemyAction = _playerBody.getPosition().x - temp_enemy_body.getPosition().x;
                        if (enemyAction > 0) {
                            doAIActions3(temp_enemy_body, -1, true,i);
                        } else {
                            doAIActions3(temp_enemy_body, 1, true,i);
                        }
                    }
                    enemyLandedArr3[i] = true;
                }


                //플레이어와 일정거리 떨어졌을 때
                else if (temp_enemy_body != null && enemyLandedArr3[i] &&
                        (getDistance(_playerBody, temp_enemy_body) > 11.51f)) {
                    enemyLandedArr3[i] = false;
                    Random random = new Random();
                    int ai_r = random.nextInt(100);
                    if (ai_r >= 60) {
                        doAIActions3(temp_enemy_body, 1f, false,-1);
                    } else if (ai_r >= 20) {
                        doAIActions3(temp_enemy_body, -1f, false,-1);
                    } else {
                        doAIActions3(temp_enemy_body, 0f, false,-1);
                    }
                    ai_r = random.nextInt(6);
                    final int finalI = i;
                    TimerHandler aiBaseTimer = new TimerHandler((float) 2 + ai_r, new ITimerCallback() {
                        @Override
                        public void onTimePassed(final TimerHandler pTimerHandler) {
                            enemyLandedArr3[finalI] = true;
                        }
                    });
                    this.getEngine().registerUpdateHandler(aiBaseTimer);
                }
            }else if(temp_enemy_body != null && enemyIsSkill[i]){
                temp_enemy_body.setLinearVelocity(0,0);
            }

        }

    }

    public void doAIActions(Body temp_enemy_body, float action, boolean danger_enermy) {
        if(temp_enemy_body.getLinearVelocity().x > 1.0f || temp_enemy_body.getLinearVelocity().y < -0.1f ){
            temp_enemy_body.setLinearVelocity(0f, 20f);
        }

        //초급 Ai
        AnimatedSprite temp_Sprite = (AnimatedSprite) findShape(temp_enemy_body.getUserData().toString());
        if(action == 1f){
            temp_enemy_body.setLinearVelocity(-0.5f, 0f);
            if(!temp_Sprite.isAnimationRunning())
                temp_Sprite.animate(new long[]{100,100,100,100,100,100},0,5,false);
            if(!danger_enermy)
                temp_Sprite.animate(new long[]{100,100,100,100,100,100},0,5,true);
        }else if(action == -1f){
            temp_enemy_body.setLinearVelocity(0.5f, 0f);
            if(!temp_Sprite.isAnimationRunning())
                temp_Sprite.animate(new long[]{100,100,100,100,100,100},6,11,false);
            if(!danger_enermy)
                temp_Sprite.animate(new long[]{100,100,100,100,100,100},6,11,true);
        }else if(action == -2f){
            temp_Sprite.stopAnimation();
            temp_enemy_body.setLinearVelocity(0,0);
        }else{
            temp_enemy_body.setLinearVelocity(0,0);
            temp_Sprite.animate(new long[]{100,100,100,100,100,100},12,17,true);
        }
    }

    public void doAIActions2(Body temp_enemy_body, float action, boolean danger_enermy) {
        if(temp_enemy_body.getLinearVelocity().x > 1.0f || temp_enemy_body.getLinearVelocity().y < -0.1f ){
            temp_enemy_body.setLinearVelocity(0f, 20f);
        }
        //중급 Ai
        AnimatedSprite temp_Sprite = (AnimatedSprite) findShape(temp_enemy_body.getUserData().toString());
        if(action == 1f){
            temp_enemy_body.setLinearVelocity(-1.0f, 0f);
            if(!temp_Sprite.isAnimationRunning())
                temp_Sprite.animate(new long[]{100,100,100,100,100,100},0,5,false);
            if(!danger_enermy)
                temp_Sprite.animate(new long[]{100,100,100,100,100,100},0,5,true);
        }else if(action == -1f){
            temp_enemy_body.setLinearVelocity(1.0f, 0f);
            if(!temp_Sprite.isAnimationRunning())
                temp_Sprite.animate(new long[]{100,100,100,100,100,100},6,11,false);
            if(!danger_enermy)
                temp_Sprite.animate(new long[]{100,100,100,100,100,100},6,11,true);
        }else if(action == -2f){
            temp_Sprite.stopAnimation();
            temp_enemy_body.setLinearVelocity(0,0);
        }else{
            temp_enemy_body.setLinearVelocity(0,0);
            temp_Sprite.animate(new long[]{100,100,100,100,100,100},12,17,true);
        }
    }

    public void doAIActions3(Body temp_enemy_body, float action, boolean danger_enermy, int enemy_number) {
        if(temp_enemy_body.getLinearVelocity().x > 1.0f || temp_enemy_body.getLinearVelocity().y < -0.1f ){
            temp_enemy_body.setLinearVelocity(0f, 20f);
        }
        //고급 원거리 Ai
        AnimatedSprite temp_Sprite = (AnimatedSprite) findShape(temp_enemy_body.getUserData().toString());
        Random i = new Random();
        int percen = i.nextInt(100);
        if(action == 1f){
            temp_enemy_body.setLinearVelocity(-1.5f, 0f);
            if(!temp_Sprite.isAnimationRunning() && danger_enermy) {
                temp_Sprite.animate(new long[]{100, 100, 100, 100, 100, 100}, 0, 5, false);
                if((percen < 2) && !enemyIsSkill[enemy_number]){
                    temp_Sprite.stopAnimation();
                    temp_enemy_body.setLinearVelocity(0,0);
                    enemyIsSkill[enemy_number] = true;
                    showBaseEnemySkill(enemy_number,temp_Sprite,-1,enermySkillCount++);
                }
            }
            if(!danger_enermy)
                temp_Sprite.animate(new long[]{100,100,100,100,100,100},0,5,true);
        }else if(action == -1f){
            temp_enemy_body.setLinearVelocity(1.5f, 0f);
            if(!temp_Sprite.isAnimationRunning() && danger_enermy) {
                temp_Sprite.animate(new long[]{100, 100, 100, 100, 100, 100}, 6, 11, false);
                if((percen < 2) && !enemyIsSkill[enemy_number]){
                    temp_Sprite.stopAnimation();
                    temp_enemy_body.setLinearVelocity(0,0);
                    enemyIsSkill[enemy_number] = true;
                    showBaseEnemySkill(enemy_number,temp_Sprite,1,enermySkillCount++);
                }
            }
            if(!danger_enermy)
                temp_Sprite.animate(new long[]{100,100,100,100,100,100},6,11,true);
        }else if(action == -2f){
            temp_Sprite.stopAnimation();
            temp_enemy_body.setLinearVelocity(0,0);
        }else{
            temp_enemy_body.setLinearVelocity(0,0);
            temp_Sprite.animate(new long[]{100,100,100,100,100,100},12,17,true);
        }
    }

    public void showBaseEnemySkill(final int _enermyNumber, Sprite _enermySprite, int _enermyDir, final int _number){

        int temp_x = 360;
        int temp_y = 55;
        if(_enermyDir == 1){
            temp_y += 20;
            temp_x -= 120;
        }

        AnimatedSprite mEnermySkill = new AnimatedSprite(_enermySprite.getX() + (_enermyDir * temp_x),
                _enermySprite.getY() - temp_y, mEnemySkillTextureRegion, this.getVertexBufferObjectManager());

        long[] _time = new long[50];
        for(int i= 0; i <_time.length ; i++){
            _time[i] = 80;
            if(i >= 25){
                _time[i] = 15;
            }
        }

        if(_enermyDir == 1)
            mEnermySkill.setRotation(180f);
        mSound_bim1.play();
        mSound_bim1.play();
        mEnermySkill.animate(_time,0,49,false);
        mEnermySkill.setScale(3.5f,.5f);
        mEnermySkill.setUserData("EnermyBaseSkill" + _number);
        mEntityList.add(mEnermySkill);
        mScene.attachChild(mEnermySkill);

        destroyBaseEnermySkill("EnermyBaseSkill" + _number, _enermyNumber);
    }

    public void destroyBaseEnermySkill(final String _skillName, final int _enermyNumber){

        timerHandler = new TimerHandler( 2f, new ITimerCallback() {
            @Override
            public void onTimePassed(final TimerHandler pTimerHandler) {
                mSound_bim2.play();
                mSound_bim2.play();

                AnimatedSprite _skillAni = (AnimatedSprite) findShape(_skillName);
                if(_skillAni.collidesWith(player_sprite)){

                    int targetNumber = battleEnermy1.indexOf(new battle_enermy("high"+_enermyNumber));
                    if (targetNumber >= 0) {
                        player_info.myCurrentHp -= damageCalculation.getDamageCalculation(true,
                                battleEnermy1.get(targetNumber).lv,
                                battleEnermy1.get(targetNumber).atk,
                                battleEnermy1.get(targetNumber).f_atk,
                                95,player_info.myLevel,player_info.myDEF,0);

                        player_body.applyLinearImpulse(-15f * playerDir,
                                -3f, player_body.getPosition().x,
                                player_body.getPosition().y);
                        player_sprite.setColor(0.8f,0f,0f);

                        float hpX = (float) player_info.myCurrentHp/player_info.myMaxHp * 100;
                        float HpBarX = hpX * 1.62f;
                        mHpSprite.setX((-92) + HpBarX);

                        showDamage(player_body.getUserData().toString(),damageCalculation.getDamageCalculation(true,
                                battleEnermy1.get(targetNumber).lv,
                                battleEnermy1.get(targetNumber).atk,
                                battleEnermy1.get(targetNumber).f_atk,
                                95,player_info.myLevel,player_info.myDEF,0),1);

                    }
                }
            }
        });
        this.getEngine().registerUpdateHandler(timerHandler);
        timerHandler = new TimerHandler( 2.375f, new ITimerCallback() {
            @Override
            public void onTimePassed(final TimerHandler pTimerHandler) {
                enemyIsSkill[_enermyNumber] = false;
                destroyGameObject(_skillName);
                player_sprite.setColor(1.0f,1.0f,1.0f);
            }
        });
        this.getEngine().registerUpdateHandler(timerHandler);
    }

    /**********************************************************************************
     * HP_Bar 컨트롤
     * 실시간 처리 시스템 (충돌이 일어난 메모리 해지 탐지, 메모리 삭제)
     * 스트라이프 충돌 처리
     **********************************************************************************/

    private void updateHealthBar(int downHp,int downMp) {
        //플레이어의 데미지 처리
        if (reduceHealth) {
            Body temp_enemy_body = mPhysicsWorld.getPhysicsConnectorManager()
                    .findBodyByShape(findShape(fix2_name));

            //초급 에너미일 경우
            if(fix2_name.contains("enemy") && temp_enemy_body != null){
                mSound_EnermyAttack1.play();
                temp_enemy_body.setLinearVelocity(3f * playerDir, -0.5f);
                player_body.setLinearVelocity(0f,0f);
                player_body.applyLinearImpulse(-8f * playerDir,
                        -3f, player_body.getPosition().x,
                        player_body.getPosition().y);
                player_info.myCurrentHp -=downHp;
            }
            //중급 에너미 일 경우
            else if(fix2_name.contains("middle") && temp_enemy_body != null){
                mSound_EnermyAttack1.play();
                temp_enemy_body.setLinearVelocity(5f * playerDir, -0.5f);
                player_body.setLinearVelocity(0f,0f);
                player_body.applyLinearImpulse(-8f * playerDir,
                        -3f, player_body.getPosition().x,
                        player_body.getPosition().y);

                player_info.myCurrentHp -=downHp;
            }
            //고급 에너미
            else if(fix2_name.contains("high") && temp_enemy_body != null){
                mSound_EnermyAttack1.play();
                temp_enemy_body.setLinearVelocity(12f * playerDir, -0.7f);
                player_body.setLinearVelocity(0f,0f);
                player_body.applyLinearImpulse(-8f * playerDir,
                        -3f, player_body.getPosition().x,
                        player_body.getPosition().y);

                player_info.myCurrentHp -=downHp;
            }

            player_sprite.setColor(0.8f,0f,0f);
            float hpX = (float) player_info.myCurrentHp/player_info.myMaxHp * 100;
            float HpBarX = hpX * 1.62f;
            mHpSprite.setX((-92) + HpBarX);

            showDamage(player_body.getUserData().toString(),downHp,1);

            TimerHandler hp_invincibility = new TimerHandler(0.2f, new ITimerCallback() {
                @Override
                public void onTimePassed(final TimerHandler pTimerHandler) {
                    reduceHealth = false;
                    player_sprite.setColor(1f,1f,1f);
                }
            });
            this.getEngine().registerUpdateHandler(hp_invincibility);
        }

        if(reduceMp){
            mSound_heal.play();
            mSound_heal.play();
            player_info.myCurrentMp -= downMp;
            float MpX = (float) player_info.myCurrentMp/player_info.myMaxMp * 100;
            float MpBarX = MpX * 1.62f;
            mMpSprite.setX((-92) + MpBarX);
        }
    }

    private void updateHealAdd(int upHp){
        mSound_heal.play();
        mSound_heal.play();
        player_info.myCurrentHp +=upHp;
        if(player_info.myCurrentHp > player_info.myMaxHp){
            player_info.myCurrentHp = player_info.myMaxHp;
        }

        player_sprite.setColor(0.0f,0.0f,0.8f);
        float hpX = (float) player_info.myCurrentHp/player_info.myMaxHp * 100;
        float HpBarX = hpX * 1.62f;
        mHpSprite.setX((-92) + HpBarX);

        TimerHandler hp_invincibility = new TimerHandler(0.2f, new ITimerCallback() {
            @Override
            public void onTimePassed(final TimerHandler pTimerHandler) {
                player_sprite.setColor(1f,1f,1f);
            }
        });
        this.getEngine().registerUpdateHandler(hp_invincibility);
    }

    public void createUpdateHandler() {
        //메모리의 해지 탐지
        this.mScene.registerUpdateHandler(new IUpdateHandler() {
            public void onUpdate(float pSecondsElapsed) {

                showMoney();                        // 화면의 돈을 실시간 표기
                makeLuncherDefyGravity();           // 원거리 무중력 작용 설정
                airWallGravity();                   // 공중 벽
                doAICalculations(player_body);      // 적 Ai 설정
                doAICalculations2(player_body);     // 적2 Ai 설정
                doAICalculations3(player_body);     // 적3 Ai 설정

                //스테이지 클리어조건
                if(clearStageCount > 5){
                    clearPlayerEvent();
                    clearPlayer = true;
                }

                //플레이어의 HP가 다하거나, 트랩에 걸렸는가
                if((desPlayer || (player_info.myCurrentHp < 1) && !clearPlayer)){
                    deadPlayerEvent();
                }
                //화살이 적, 오브젝트(벽,물건 등)에 적중했는가
                if(desArrow) {
                    if (fix1_name.contains("arrow")) {
                        destroyGameObject(fix1_name);
                    } else if(fix2_name.contains("arrow")) {
                        destroyGameObject(fix2_name);
                    }
                    desArrow = false;
                }



            }

            @Override
            public void reset() {}
        });
    }

    public void destroyGameObject(String name) {

        Debug.d(name + "메모리해제");
        if (mPhysicsWorld.getPhysicsConnectorManager().findBodyByShape(
                findShape(name)) != null) {
            Debug.d(name + "메모리해제 inside");
            // 스프라이트를 스크린에서 제거하고 스크린과 관련된 바디를 삭제합니다.
            IShape temp_enemy_shape = findShape(name);
            AnimatedSprite temp_Sprite = (AnimatedSprite) temp_enemy_shape;



            mPhysicsWorld.destroyBody(mPhysicsWorld
                    .getPhysicsConnectorManager().findBodyByShape(
                            findShape(name)));
            mPhysicsWorld.unregisterPhysicsConnector(mPhysicsWorld
                    .getPhysicsConnectorManager().findPhysicsConnectorByShape(
                            findShape(name)));
            mScene.detachChild(findShape(name));
            mEntityList.remove(name);
            temp_Sprite.setUserData("desObject");
        }
        if(findShape(name) != null){
            mScene.detachChild(findShape(name));
            mEntityList.remove(name);
        }

        System.gc();
        // if(name.contains("money")){
        // mScene.detachChild(findShape(name));
        // }

    }

    /**********************************************************************************
     *
     * 플레이어, 적, 오브젝트 생성
     *
     **********************************************************************************/

    public void showPlayer(String playerName, TiledTextureRegion playerTexture, int player_x, int player_y) {

        dataPlayer();
        AnimatedSprite mPlayerSprite = new AnimatedSprite(player_x, player_y,
                playerTexture, this.getVertexBufferObjectManager());
        mPlayerSprite.setScale(1.1f);

        final FixtureDef mPlayerFixtureDef = PhysicsFactory.createFixtureDef(0f,
                0f, 0f, false, CATEGORYBIT_PLAYER, MASKBITS_PLAYER, (short) 0);

        Body mPlayerBody = PhysicsFactory.createBoxBody(this.mPhysicsWorld,
                mPlayerSprite, BodyType.DynamicBody, mPlayerFixtureDef);
        mPlayerSprite.setUserData(playerName);
        mPlayerBody.setUserData(playerName);
        this.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(
                mPlayerSprite, mPlayerBody, true, false));

        mScene.attachChild(mPlayerSprite);
        mEntityList.add(mPlayerSprite);
    }

    public void dataPlayer(){
        player_info = new service_CharacterInfo();

        //공속 셋팅
        sword_speed = 101 - (int)((100-player_info.myA_Speed)/200f * 100f);
        sword_delay = 0.808f - (((100-player_info.myA_Speed)/200f * 100f) * 0.008f);
        if(player_info.myCharacterNum == 1){
            //전사 : 키딜레이 75% 기준 (공속 100%)
            sword_key_delay = 150;
        }else{
            //궁수 : 키딜레이 100% 기준 (공속 100%)
            sword_key_delay = 200;
        }
        if(player_info.myA_Speed >= sword_key_delay){
            //키딜레이 이상이 넘어가는 것을 방지
            sword_key_delay = player_info.myA_Speed;
        }

    }

    public void showMoney(int xLoc, int yLoc) {

        AnimatedSprite mMoneySprite = new AnimatedSprite(xLoc, yLoc,
                mMoneyTextureRegion, this.getVertexBufferObjectManager());
        mMoneySprite.setScale(1.2f);

        FixtureDef mMoneyFixtureDef = PhysicsFactory.createFixtureDef(0, 0f,
                0f, false, CATEGORYBIT_MONEY, MASKBITS_MONEY, (short) 0);

        Body mMoneyBody = PhysicsFactory.createCircleBody(this.mPhysicsWorld,
                mMoneySprite, BodyType.StaticBody, mMoneyFixtureDef);

        mMoneyBody.setUserData("money" + moneyCount);

        this.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(
                mMoneySprite, mMoneyBody, true, true));
        mMoneySprite.setUserData("money" + moneyCount);
        moneyCount++;

        mScene.attachChild(mMoneySprite);
        mEntityList.add(mMoneySprite);
    }

    public void showMoney() {
        leftText.setText(String.valueOf(dollarCount) + " Dollar");
    }

    public void showWallAir1(int xLoc, int yLoc) {

        AnimatedSprite mEnemySprite = new AnimatedSprite(xLoc, yLoc,
                mWallAir1TextureRegion, this.getVertexBufferObjectManager());

        //밀도,탄성,마찰력
        FixtureDef mEnemyFixtureDef = PhysicsFactory.createFixtureDef( 0.0f, 0f, 0f,
                false, CATEGORYBIT_ENEMY, MASKBITS_ENEMY, (short) 0);

        Body mEnemyBody = PhysicsFactory.createBoxBody(this.mPhysicsWorld,
                mEnemySprite, BodyType.DynamicBody, mEnemyFixtureDef);

        mEnemyBody.setUserData("airwone" + wallAir);

        this.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(
                mEnemySprite, mEnemyBody, true, false));
        mEnemySprite.setUserData("airwone" + wallAir);

        wallAir++;

        mScene.attachChild(mEnemySprite);
        mEnemySprite.setCullingEnabled(true);
        mEntityList.add(mEnemySprite);

    }

    public void airWallGravity(){
        for (int i = 0; i < wallAir; i++) {
            IShape temp_enemy_shape = findShape("AirWone" + i);
            Body temp_enemy_body = mPhysicsWorld.getPhysicsConnectorManager()
                    .findBodyByShape(temp_enemy_shape);
            if(temp_enemy_body != null) {
                temp_enemy_body.setLinearVelocity(-wall1[i],0f);
            }
        }

    }

    public void showPotion(int xLoc, int yLoc) {
        potionCount++;
        AnimatedSprite mMoneySprite = new AnimatedSprite(xLoc, yLoc,
                mPotionTextureRegion, this.getVertexBufferObjectManager());
        mMoneySprite.setScale(1.2f);

        FixtureDef mMoneyFixtureDef = PhysicsFactory.createFixtureDef(0, 0f,
                0f, false, CATEGORYBIT_MONEY, MASKBITS_MONEY, (short) 0);

        Body mMoneyBody = PhysicsFactory.createCircleBody(this.mPhysicsWorld,
                mMoneySprite, BodyType.StaticBody, mMoneyFixtureDef);

        mMoneyBody.setUserData("potion" + potionCount);

        this.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(
                mMoneySprite, mMoneyBody, true, true));
        mMoneySprite.setUserData("potion" + potionCount);

        mMoneySprite.animate(30);

        mScene.attachChild(mMoneySprite);
        mEntityList.add(mMoneySprite);
    }

    public void showEnemy(int xLoc, int yLoc) {

        AnimatedSprite mEnemySprite = new AnimatedSprite(xLoc, yLoc,
                mEnemyTextureRegion, this.getVertexBufferObjectManager());
        mEnemySprite.setScale(0.8f);
        //밀도,탄성,마찰력
        FixtureDef mEnemyFixtureDef = PhysicsFactory.createFixtureDef( 0.1f, 0f, 0f,
                false, CATEGORYBIT_ENEMY, MASKBITS_ENEMY, (short) 0);

        Body mEnemyBody = PhysicsFactory.createBoxBody(this.mPhysicsWorld,
                mEnemySprite, BodyType.DynamicBody, mEnemyFixtureDef);

        mEnemyBody.setUserData("enemy" + enemyCount);

        this.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(
                mEnemySprite, mEnemyBody, true, false));
        mEnemySprite.setUserData("enemy" + enemyCount);

        battle_enermy asd = new battle_enermy("enemy"+ String.valueOf(enemyCount),1,70,3,5,1,0);
        battleEnermy1.add(asd);

        enemyCount++;

        mScene.attachChild(mEnemySprite);

        mEnemySprite.setCullingEnabled(true);
        mEntityList.add(mEnemySprite);
    }

    public void showEnemy2(int xLoc, int yLoc) {

        AnimatedSprite mEnemySprite = new AnimatedSprite(xLoc, yLoc,
                mEnemy2TextureRegion, this.getVertexBufferObjectManager());
        mEnemySprite.setScale(0.9f);
        //밀도,탄성,마찰력
        FixtureDef mEnemyFixtureDef = PhysicsFactory.createFixtureDef( 0.2f, 0f, 0f,
                false, CATEGORYBIT_ENEMY, MASKBITS_ENEMY, (short) 0);

        Body mEnemyBody = PhysicsFactory.createBoxBody(this.mPhysicsWorld,
                mEnemySprite, BodyType.DynamicBody, mEnemyFixtureDef);

        mEnemyBody.setUserData("middle" + enemyCount2);

        this.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(
                mEnemySprite, mEnemyBody, true, false));
        mEnemySprite.setUserData("middle" + enemyCount2);

        battle_enermy asd = new battle_enermy("middle"+ String.valueOf(enemyCount2),1,270,2,5,1,0);
        battleEnermy1.add(asd);

        enemyCount2++;

        mScene.attachChild(mEnemySprite);

        mEnemySprite.setCullingEnabled(true);
        mEntityList.add(mEnemySprite);
    }

    public void showEnemy3(int xLoc, int yLoc) {

        AnimatedSprite mEnemySprite = new AnimatedSprite(xLoc, yLoc,
                mEnemy3TextureRegion, this.getVertexBufferObjectManager());
        mEnemySprite.setScale(0.9f);
        //밀도,탄성,마찰력
        FixtureDef mEnemyFixtureDef = PhysicsFactory.createFixtureDef( 0.3f, 0f, 0f,
                false, CATEGORYBIT_ENEMY, MASKBITS_ENEMY, (short) 0);

        Body mEnemyBody = PhysicsFactory.createBoxBody(this.mPhysicsWorld,
                mEnemySprite, BodyType.DynamicBody, mEnemyFixtureDef);

        mEnemyBody.setUserData("high" + enemyCount3);

        this.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(
                mEnemySprite, mEnemyBody, true, false));
        mEnemySprite.setUserData("high" + enemyCount3);

        battle_enermy asd = new battle_enermy("high"+ String.valueOf(enemyCount3),1,70,11,5,1,0);
        battleEnermy1.add(asd);

        enemyCount3++;

        mScene.attachChild(mEnemySprite);

        mEnemySprite.setCullingEnabled(true);
        mEntityList.add(mEnemySprite);
    }
    /***********************************************************
     *
     * UserData string (name)에 해당하는 모양을 찾는 메소드(스프라이트를 찾아주는 메소드)
     *
     ***********************************************************/

    public IShape findShape(String shapeName) {
        IShape pShape = null;

        // 존재하는 모든 entities들을 체크하면서
        // UserData 문자열에 해당하는 값이 있는 entity를 리턴합니다.
        for (IEntity pEntity : mEntityList) {
            if (pEntity.getUserData() != null) {
                if (pEntity.getUserData().toString()
                        .equalsIgnoreCase(shapeName)) {
                    pShape = (IShape) pEntity;
                }
            }
        }
        return pShape;
    }

    /************************************************************
     *
     * 맵 타일 오브젝트 출현 설정
     *
     ************************************************************/
    //총괄 설정
    private void loadObject(TMXTiledMap map) {

        // tmx map에서 모든 object groups을 탐색 및 가져와서 속성 이름을 체크
        for (final TMXObjectGroup group : map.getTMXObjectGroups()) {

            //벽 설정
            if (group.getName().equals("wall")) {
                loadWallFromObjects(group, "wall");
            }

            // 돈 로드
            if (group.getName().equals("money")) {
                loadMoneyFromObjects(group);
            }

            // 적 로드
            if (group.getName().equals("enemies")) {
                loadEnemiesFromObjects(group);
            }

            // 적2 로드
            if (group.getName().equals("middle")) {
                loadEnemies2FromObjects(group);
            }

            // 적3 로드
            if (group.getName().equals("high")) {
                loadEnemies3FromObjects(group);
            }

            //플레이어 로드
            if (group.getName().equals("player")) {
                loadPlayerFromObjects(group);
            }

            //함정 로드
            if (group.getName().equals("trap")) {
                loadTrapFromObjects(group, "trap");
            }

            //에너미 벽
            if (group.getName().equals("enemiwall")) {
                loadEnemiesWallFromObjects(group, "enemiwall");
            }

            if (group.getName().equals("wallair1")) {
                loadWallAir1FromObjects(group);
            }

            if (group.getName().equals("wallairwall")) {
                loadWallAirWallFromObjects(group, "enemiwall");
            }

            if (group.getName().equals("potion")) {
                loadPotionFromObjects(group);
            }
        }

    }
    //세부 설정 목록
    private void loadWallFromObjects(TMXObjectGroup _group, String _userData) {

        //벽 등장 처리
        for (final TMXObject object : _group.getTMXObjects()) {
            // tmx map에 사각형을 생성
            rect = new Rectangle(object.getX(), object.getY(),
                    object.getWidth(), object.getHeight(),
                    this.getVertexBufferObjectManager());

            // 벽에 해당하는 FixtureDef를 생성
            boxFixtureDef = PhysicsFactory.createFixtureDef(0, 0, 1f, false,
                    CATEGORYBIT_WALL, MASKBITS_WALL, (short) 0);

            // 벽을 StaticBody로 선언하고 userData에 셋팅합
            PhysicsFactory.createBoxBody(this.mPhysicsWorld, rect,
                    BodyType.StaticBody, boxFixtureDef).setUserData(_userData);

            // 사각형 영역을 보이지 않게 설정
            rect.setVisible(false);

            mScene.attachChild(rect);

            mEntityList.add(rect);
        }
    }

    private void loadWallAirWallFromObjects(TMXObjectGroup _group, String _userData) {
        //벽 등장 처리
        for (final TMXObject object : _group.getTMXObjects()) {
            // tmx map에 사각형을 생성
            air_rect = new Rectangle(object.getX(), object.getY(),
                    object.getWidth(), object.getHeight(),
                    this.getVertexBufferObjectManager());

            air_FixtureDef = PhysicsFactory.createFixtureDef(0, 0, 1f, false,
                    CATEGORYBIT_ENERMY_WALL, MASKBITS_ENERMY_WALL, (short) 0);

            PhysicsFactory.createBoxBody(this.mPhysicsWorld, air_rect,
                    BodyType.StaticBody, air_FixtureDef).setUserData(_userData);

            air_rect.setVisible(false);
            mScene.attachChild(air_rect);
            mEntityList.add(air_rect);
        }
    }

    private void loadTrapFromObjects(TMXObjectGroup _group, String _userData) {

        for (final TMXObject object : _group.getTMXObjects()) {

            traps = new Rectangle(object.getX(), object.getY(),
                    object.getWidth(), object.getHeight(),
                    this.getVertexBufferObjectManager());

            trapFixtureDef = PhysicsFactory.createFixtureDef(0, 0, 1f, false,
                    CATEGORYBIT_TRAP, MASKBITS_TRAP, (short) 0);

            PhysicsFactory.createBoxBody(this.mPhysicsWorld, traps,
                    BodyType.StaticBody, trapFixtureDef).setUserData(_userData);

            // 사각형 영역을 보이지 않게 설정
            traps.setVisible(false);

            mScene.attachChild(traps);

            mEntityList.add(traps);

        }
    }

    private void loadEnemiesWallFromObjects(TMXObjectGroup _group, String _userData) {

        for (final TMXObject object : _group.getTMXObjects()) {

            enemy_rect = new Rectangle(object.getX(), object.getY(),
                    object.getWidth(), object.getHeight(),
                    this.getVertexBufferObjectManager());

            enmey_rectFixtureDef = PhysicsFactory.createFixtureDef(0, 0, 1f, false,
                    CATEGORYBIT_ENERMY_WALL, MASKBITS_ENERMY_WALL, (short) 0);

            PhysicsFactory.createBoxBody(this.mPhysicsWorld, enemy_rect,
                    BodyType.StaticBody, enmey_rectFixtureDef).setUserData(_userData);

            // 사각형 영역을 보이지 않게 설정
            enemy_rect.setVisible(false);

            mScene.attachChild(enemy_rect);

            mEntityList.add(enemy_rect);

        }
    }

    private void loadEnemiesFromObjects(TMXObjectGroup _group) {

        //적 오브젝트 탐색
        for (final TMXObject object : _group.getTMXObjects()) {

            // 적 캐릭터를 등장시킴
            showEnemy(object.getX(), object.getY());
        }
    }

    private void loadEnemies2FromObjects(TMXObjectGroup _group) {

        //적 오브젝트 탐색
        for (final TMXObject object : _group.getTMXObjects()) {

            // 적 캐릭터를 등장시킴
            showEnemy2(object.getX(), object.getY());
        }
    }

    private void loadEnemies3FromObjects(TMXObjectGroup _group) {

        //적 오브젝트 탐색
        for (final TMXObject object : _group.getTMXObjects()) {

            // 적 캐릭터를 등장시킴
            showEnemy3(object.getX(), object.getY());
        }
    }

    private void loadMoneyFromObjects(TMXObjectGroup _group) {

        for (final TMXObject object : _group.getTMXObjects()) {

            showMoney(object.getX(), object.getY());
        }
    }

    private void loadPotionFromObjects(TMXObjectGroup _group) {

        for (final TMXObject object : _group.getTMXObjects()) {

            showPotion(object.getX(), object.getY());
        }
    }

    private void loadPlayerFromObjects(TMXObjectGroup _group) {

        // Player 위치 탐색 및 생성
        for (final TMXObject object : _group.getTMXObjects()) {

            showPlayer("player", mPlayerTextureRegion, object.getX(), object.getY());
        }
    }

    private void loadWallAir1FromObjects(TMXObjectGroup _group){

        for (final TMXObject object : _group.getTMXObjects()) {
            showWallAir1(object.getX(), object.getY());
        }
    }


    /************************************************************
     *
     * 이벤트 메소드
     *
     ************************************************************/
    private void deadPlayerEvent(){
        mMusic.stop();
        Intent i = new Intent(getApplicationContext(),gameOverActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
        finish();
    }

    private void clearPlayerEvent(){
        if(findShape("ClearStage") == null) {
            mMusic.stop();
            AnimatedSprite mClearSprite = new AnimatedSprite(camera.getCenterX(),camera.getCenterY(),
                    mGameClearTextureRegion, this.getVertexBufferObjectManager());
            mClearSprite.animate(new long[]{300,300,300,300,300,300,580,180},false);
            mClearSprite.setUserData("ClearStage");
            mEntityList.add(mClearSprite);
            mClearSprite.setCullingEnabled(true);
            mScene.attachChild(mClearSprite);

            this.mSound_Clear.play();

            TimerHandler intentHandler = new TimerHandler( 6f, new ITimerCallback() {
                @Override
                public void onTimePassed(final TimerHandler pTimerHandler) {
                    Intent i = getIntent();
                    int tempNum = i.getIntExtra("i",0);
                    i = new Intent(getApplicationContext(),clearActivity.class);
                    i.putExtra("i",tempNum);
                    startActivity(i);
                    overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                    finish();
                }
            });
            this.getEngine().registerUpdateHandler(intentHandler);

        }else{
            AnimatedSprite clAni = (AnimatedSprite) findShape("ClearStage");
            clAni.setPosition(camera.getCenterX() - clAni.getWidth()/2,camera.getCenterY() - clAni.getHeight()/2);
        }
    }

}
