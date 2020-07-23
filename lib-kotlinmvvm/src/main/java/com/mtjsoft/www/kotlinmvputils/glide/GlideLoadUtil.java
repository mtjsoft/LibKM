package com.mtjsoft.www.kotlinmvputils.glide;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.mtjsoft.www.kotlinmvputils.utils.AndAppUtils;

import java.lang.ref.SoftReference;


/**
 * @author mtj
 */
public class GlideLoadUtil {

    // 是否跳过内存缓存
    private static boolean memoryCache = false;
    private static float thumbnail = 0.5f;

    /**
     * Glide加载矩形图片
     *
     * @param path      图片地址
     * @param def       默认图片
     * @param imageView
     */
    public static void setImage2Glide(String path, int def, ImageView imageView) {
        if (!TextUtils.isEmpty(path) && (path.endsWith(".gif") || path.endsWith(".GIF"))) {
            setImageGif2Glide(path, def, imageView);
        } else {
            SoftReference<ImageView> softReference = new SoftReference<>(imageView);
            RequestOptions options = new RequestOptions()
                    .dontAnimate()
                    //加载成功之前占位图
                    .placeholder(def)
                    //加载错误之后的错误图
                    .error(def)
                    .override(softReference.get().getWidth(), softReference.get().getHeight())
                    .skipMemoryCache(memoryCache)
                    .diskCacheStrategy(DiskCacheStrategy.ALL);
            try {
                Glide.with(softReference.get().getContext())
                        .load(path)
                        .thumbnail(thumbnail)
                        .apply(options)
                        .into(softReference.get());
            } catch (Exception e) {
                Log.e("mtj", "glide加载失败");
            }
        }
    }

    /**
     * Glide加载Gif图片
     *
     * @param path      图片地址
     * @param def       默认图片
     * @param imageView
     */
    public static void setImageGif2Glide(Object path, int def, ImageView imageView) {
        SoftReference<ImageView> softReference = new SoftReference<>(imageView);
        RequestOptions options = new RequestOptions()
                //加载成功之前占位图
                .placeholder(def)
                //加载错误之后的错误图
                .error(def)
                .skipMemoryCache(false)
                .override(softReference.get().getWidth(), softReference.get().getHeight())
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        try {
            Glide.with(softReference.get().getContext())
                    .asGif()
                    .load(path)
                    .apply(options)
                    .into(softReference.get());
        } catch (Exception e) {
            Log.e("mtj", "glide加载失败");
        }
    }

    /**
     * 指定宽高大小
     *
     * @param path
     * @param w
     * @param h
     * @param def
     * @param imageView
     */
    public static void setImage2Glide(String path, int w, int h, int def, ImageView imageView) {
        SoftReference<ImageView> softReference = new SoftReference<>(imageView);
        RequestOptions options = new RequestOptions()
                .dontAnimate()
                //加载成功之前占位图
                .placeholder(def)
                //加载错误之后的错误图
                .error(def)
                .override(w, h)
                .skipMemoryCache(memoryCache)
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        try {
            Glide.with(softReference.get().getContext())
                    .load(path)
                    .thumbnail(thumbnail)
                    .apply(options)
                    .into(softReference.get());
        } catch (Exception e) {
            Log.e("mtj", "glide加载失败");
        }
    }

    /**
     * Glide加载圆形图片
     *
     * @param path      图片地址
     * @param def       默认图片
     * @param imageView
     */
    public static void setCircleImage2Glide(String path, int def, ImageView imageView) {
        SoftReference<ImageView> softReference = new SoftReference<>(imageView);
        RequestOptions options = new RequestOptions()
                .dontAnimate()
                .transforms(new CenterCrop(), new CircleCrop())
                //加载成功之前占位图
                .placeholder(def)
                //加载错误之后的错误图
                .error(def)
                .override(softReference.get().getWidth(), softReference.get().getHeight())
                .skipMemoryCache(memoryCache)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop();
        try {
            Glide.with(softReference.get().getContext())
                    .load(path)
                    .thumbnail(thumbnail)
                    .apply(options)
                    .into(softReference.get());
        } catch (Exception e) {
            Log.e("mtj", "glide加载失败");
        }
    }

    public static void setCircleImage2Glide(String path, int def, ImageView imageView, boolean memoryCache) {
        SoftReference<ImageView> softReference = new SoftReference<>(imageView);
        RequestOptions options = new RequestOptions()
                .dontAnimate()
                .transforms(new CenterCrop(), new CircleCrop())
                //加载成功之前占位图
                .placeholder(def)
                //加载错误之后的错误图
                .error(def)
                .override(softReference.get().getWidth(), softReference.get().getHeight())
                .skipMemoryCache(memoryCache)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop();
        try {
            Glide.with(softReference.get().getContext())
                    .load(path)
                    .thumbnail(thumbnail)
                    .apply(options)
                    .into(softReference.get());
        } catch (Exception e) {
            Log.e("mtj", "glide加载失败");
        }
    }


    /**
     * Glide加载圆角图片
     *
     * @param path      图片地址
     * @param def       默认图片
     * @param imageView
     */
    public static void setRoundImage2Glide(String path, int def, int round, ImageView imageView) {
        SoftReference<ImageView> softReference = new SoftReference<>(imageView);
        RequestOptions options = new RequestOptions()
                .dontAnimate()
                //加载成功之前占位图
                .placeholder(def)
                //加载错误之后的错误图
                .error(def)
                .override(softReference.get().getWidth(), softReference.get().getHeight())
                .skipMemoryCache(memoryCache)
                .transforms(new CenterCrop(), new RoundedCorners(round))
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        try {
            Glide.with(softReference.get().getContext())
                    .load(path)
                    .thumbnail(thumbnail)
                    .apply(options)
                    .into(softReference.get());
        } catch (Exception e) {
            Log.e("mtj", "glide加载失败");
        }
    }

    /**
     * 内存缓存清理（主线程）
     *
     * @param context
     */
    public static void clearMemoryCache(Context context) {
        try {
            Glide.get(context).clearMemory();
        } catch (Exception e) {
            Log.e("mtj", "glide内存缓存清除失败");
        }
    }

    /**
     * 磁盘缓存清理（子线程）
     *
     * @param context
     */
    public static void clearFileCache(final Context context) {
        AndAppUtils.INSTANCE.getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Glide.get(context).clearDiskCache();
                } catch (Exception e) {
                    Log.e("mtj", "glide磁盘缓存清除失败");
                }
            }
        });
    }
}
