/*
 * Nebula2D is a cross-platform, 2D game engine for PC, Mac, & Linux
 * Copyright (c) 2014 Jon Bonazza
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.suiton2d.assets;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;
import com.suiton2d.assets.loaders.*;
import com.suiton2d.scene.Scene;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ScriptableObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * AssetManager is a singleton class used to manage a game's assets
 *
 * @author Jon Bonazza <jonbonazza@gmail.com>
 */
public class AssetManager {

    private static Map<String, List<AssetDescriptor>> assetMap = new HashMap<String, List<AssetDescriptor>>();
    private static ScriptableObject globalScriptScope;
    private static com.badlogic.gdx.assets.AssetManager manager;

    public static void init(FileHandleResolver fileHandleResolver) {
        manager = new com.badlogic.gdx.assets.AssetManager(fileHandleResolver);
        initLoaders(fileHandleResolver);
        Context context = Context.enter();
        context.setOptimizationLevel(-1);
        try {
            globalScriptScope = context.initStandardObjects();
        } finally {
            Context.exit();
        }
    }

    private static void initLoaders(FileHandleResolver resolver) {
        manager.setLoader(MusicTrack.class, new MusicTrackLoader(resolver));
        manager.setLoader(Script.class, new ScriptLoader(resolver));
        manager.setLoader(SoundEffect.class, new SoundEffectLoader(resolver));
        manager.setLoader(Sprite.class, new SpriteLoader(resolver));
        manager.setLoader(TiledTileSheet.class, new TiledTileSheetLoader(resolver));
    }

    public static ScriptableObject getGlobalScriptScope() {
        return globalScriptScope;
    }

    public static <T> T getAsset(String filename, Class<T> type) {
        return manager.get(filename, type);
    }

    /**
     * Loads the assets for the {@link Scene} with the given name into memory.
     * @param sceneName the name of the Scene whose assets should be loaded.
     */
    public static void loadAssets(String sceneName) {
        List<AssetDescriptor> assets = assetMap.get(sceneName);
        if (assets != null) {
            for (AssetDescriptor asset : assets)
                manager.load(asset);
        }

        manager.finishLoading();
    }

    /**
     * Unloads the assets for the Scene with the given name from memory.
     * @param sceneName the name of the Scene whose assets should be unloaded.
     */
    public static void unloadAssets(String sceneName) {
        List<AssetDescriptor> assets = assetMap.get(sceneName);
        for (AssetDescriptor asset : assets)
            manager.unload(asset.fileName);
    }

    public static void installAssets(FileHandle assetsFile) throws IOException {
        XmlReader reader = new XmlReader();
        XmlReader.Element root = reader.parse(assetsFile);

        Array<XmlReader.Element> assets = root.getChildrenByName("asset");
        for (XmlReader.Element assetElement : assets) {
            String path = assetElement.getAttribute("path");
            String type = assetElement.getAttribute("assetType");
            String sceneName = assetElement.getAttribute("sceneName");

            List<AssetDescriptor> assetList = assetMap.getOrDefault(sceneName, new ArrayList<AssetDescriptor>());
            if (type.equalsIgnoreCase("SPRITE")) {
                assetList.add(new AssetDescriptor<Sprite>(path, Sprite.class));
            } else if (type.equalsIgnoreCase("MUSIC")) {
                assetList.add(new AssetDescriptor<MusicTrack>(path, MusicTrack.class));
            } else if (type.equalsIgnoreCase("SFX")) {
                assetList.add(new AssetDescriptor<SoundEffect>(path, SoundEffect.class));
            } else if (type.equalsIgnoreCase("SCRIPT")) {
                assetList.add(new AssetDescriptor<Script>(path, Script.class));
            } else if (type.equalsIgnoreCase("TILED_TILE_SHEET")) {
                assetList.add(new AssetDescriptor<TiledTileSheet>(path, TiledTileSheet.class));
            }

            assetMap.put(sceneName, assetList);
        }
    }

    public static void cleanup() {
        for (List<AssetDescriptor> assetList : assetMap.values()) {
            for (AssetDescriptor asset : assetList) {
                manager.unload(asset.fileName);
            }

            assetList.clear();
        }

        assetMap.clear();
    }
}
