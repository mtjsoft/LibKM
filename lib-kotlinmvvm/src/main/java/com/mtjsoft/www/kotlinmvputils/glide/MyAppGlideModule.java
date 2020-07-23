package com.mtjsoft.www.kotlinmvputils.glide;

import android.content.Context;
import android.os.Environment;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.engine.cache.ExternalPreferredCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.AppGlideModule;

import java.io.InputStream;

import cc.shinichi.library.glide.progress.ProgressManager;


@GlideModule
public class MyAppGlideModule extends AppGlideModule {

    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide,
                                   @NonNull Registry registry) {
        super.registerComponents(context, glide, registry);

        // 替换底层网络框架为okhttp3，这步很重要！
        // 如果您的app中已经存在了自定义的GlideModule，您只需要把这一行代码，添加到对应的重载方法中即可。
        registry.replace(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(ProgressManager.getOkHttpClient()));
    }

    @Override
    public void applyOptions(@NonNull Context context, @NonNull GlideBuilder builder) {
        // 设置缓存大小为20mb
        int memoryCacheSizeBytes = 1024 * 1024 * 20;
        // 设置内存缓存大小
        builder.setMemoryCache(new LruResourceCache(memoryCacheSizeBytes));
        // 根据SD卡是否可用选择是在内部缓存还是SD卡缓存
        int diskCacheSizeBytes = 1024 * 1024 * 250;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            builder.setDiskCache(new ExternalPreferredCacheDiskCacheFactory(context, "MYManagerImagesPath", diskCacheSizeBytes));
        } else {
            builder.setDiskCache(new InternalCacheDiskCacheFactory(context, "MYManagerImagesPath", diskCacheSizeBytes));
        }
    }

    // 针对V4用户可以提升速度
    @Override
    public boolean isManifestParsingEnabled() {
        return false;
    }
}
