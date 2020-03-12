package com.example.dgstory;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.util.FPSLogger;
import org.andengine.extension.tmx.TMXLayer;
import org.andengine.extension.tmx.TMXLoader;
import org.andengine.extension.tmx.TMXLoader.ITMXTilePropertiesListener;
import org.andengine.extension.tmx.TMXProperties;
import org.andengine.extension.tmx.TMXTile;
import org.andengine.extension.tmx.TMXTileProperty;
import org.andengine.extension.tmx.TMXTiledMap;
import org.andengine.extension.tmx.util.exception.TMXLoadException;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.debug.Debug;

public class MainActivity extends SimpleBaseGameActivity {
    private Camera camera;
    private static final int CAMERA_WIDTH = 800;
    private static final int CAMERA_HEIGHT = 480;

    public Scene mScene;
    private TMXTiledMap mTMXTiledMap;

    @Override
    public EngineOptions onCreateEngineOptions() {
        camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
        EngineOptions engineOptions = new EngineOptions(true,
                ScreenOrientation.LANDSCAPE_FIXED, new FillResolutionPolicy(),
                camera);
        return engineOptions;
    }

    @Override
    protected void onCreateResources() {
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
    }

    @Override
    protected Scene onCreateScene() {
        this.mEngine.registerUpdateHandler(new FPSLogger());

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
                                final TMXProperties<TMXTileProperty> pTMXTileProperties){
                        }
                    });

            // 타일맵 로드
            this.mTMXTiledMap= tmxLoader.loadFromAsset("tmx/stage1.tmx");

        } catch (final TMXLoadException e) {
            Debug.e(e);
        }

        final TMXLayer tmxLayer = this.mTMXTiledMap.getTMXLayers().get(0);
        mScene.attachChild(tmxLayer);

        return mScene;
    }
}
