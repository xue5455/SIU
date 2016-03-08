package com.xue.siu.common.util.media;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import com.android.internal.util.Predicate;
import com.facebook.binaryresource.BinaryResource;
import com.facebook.cache.common.CacheKey;
import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.common.internal.Supplier;
import com.facebook.common.references.CloseableReference;
import com.facebook.common.util.ByteConstants;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.cache.CountingMemoryCache;
import com.facebook.imagepipeline.cache.DefaultCacheKeyFactory;
import com.facebook.imagepipeline.cache.MemoryCacheParams;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.core.ImagePipelineFactory;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.image.CloseableStaticBitmap;
import com.facebook.imagepipeline.memory.PooledByteBuffer;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.xue.siu.R;
import com.xue.siu.application.AppProfile;
import com.xue.siu.common.util.LogUtil;

import java.io.IOException;

/**
 * Created by zyl06 on 9/18/15.
 */
public class FrescoUtil {

    public static final int MAX_SMALL_DISK_VERYLOW_CACHE_SIZE = 5 * ByteConstants.MB;//小图极低磁盘空间缓存的最大值（特性：可将大量的小图放到额外放在另一个磁盘空间防止大图占用磁盘空间而删除了大量的小图）
    public static final int MAX_SMALL_DISK_LOW_CACHE_SIZE = 10 * ByteConstants.MB;//小图低磁盘空间缓存的最大值（特性：可将大量的小图放到额外放在另一个磁盘空间防止大图占用磁盘空间而删除了大量的小图）
    public static final int MAX_SMALL_DISK_CACHE_SIZE = 20 * ByteConstants.MB;//小图磁盘缓存的最大值（特性：可将大量的小图放到额外放在另一个磁盘空间防止大图占用磁盘空间而删除了大量的小图）

    public static final int MAX_DISK_CACHE_VERYLOW_SIZE = 10 * ByteConstants.MB;//默认图极低磁盘空间缓存的最大值
    public static final int MAX_DISK_CACHE_LOW_SIZE = 30 * ByteConstants.MB;//默认图低磁盘空间缓存的最大值
    public static final int MAX_DISK_CACHE_SIZE = 50 * ByteConstants.MB;//默认图磁盘缓存的最大值

    private static final String IMAGE_PIPELINE_SMALL_CACHE_DIR = "imagepipeline_cache";//小图所放路径的文件夹名
    private static final String IMAGE_PIPELINE_CACHE_DIR = "imagepipeline_cache";//默认图所放路径的文件夹名

    private static final int MAX_HEAP_SIZE = (int) Runtime.getRuntime().maxMemory();//分配的可用内存
    public static final int MAX_MEMORY_CACHE_SIZE =
//            MAX_HEAP_SIZE / 4 > 15 * ByteConstants.MB ?
//                    15 * ByteConstants.MB :
            MAX_HEAP_SIZE / 4;//使用的缓存数量

    private static ImagePipelineConfig sImagePipelineConfig;

    private static final String TAG = "FrescoUtil";


    public static void initialize(Context context) {
        ImagePipelineConfig config = configureCaches(context);
        Fresco.initialize(context, config);
    }

    private static ImagePipelineConfig configureCaches(Context context) {
        //内存配置
        final MemoryCacheParams bitmapCacheParams = new MemoryCacheParams(
                FrescoUtil.MAX_MEMORY_CACHE_SIZE, // 内存缓存中总图片的最大大小,以字节为单位。
                Integer.MAX_VALUE,                     // 内存缓存中图片的最大数量。
                FrescoUtil.MAX_MEMORY_CACHE_SIZE, // 内存缓存中准备清除但尚未被删除的总图片的最大大小,以字节为单位。
                Integer.MAX_VALUE,                     // 内存缓存中准备清除的总图片的最大数量。
                Integer.MAX_VALUE);                    // 内存缓存中单个图片的最大大小。

        //修改内存图片缓存数量，空间策略（这个方式有点恶心）
        Supplier<MemoryCacheParams> mSupplierMemoryCacheParams = new Supplier<MemoryCacheParams>() {
            @Override
            public MemoryCacheParams get() {
                return bitmapCacheParams;
            }
        };

        //小图片的磁盘配置
        DiskCacheConfig diskSmallCacheConfig = DiskCacheConfig.newBuilder()
                .setBaseDirectoryPath(context.getApplicationContext().getCacheDir())//缓存图片基路径
                .setBaseDirectoryName(IMAGE_PIPELINE_SMALL_CACHE_DIR)//文件夹名
//            .setCacheErrorLogger(cacheErrorLogger)//日志记录器用于日志错误的缓存。
//            .setCacheEventListener(cacheEventListener)//缓存事件侦听器。
//            .setDiskTrimmableRegistry(diskTrimmableRegistry)//类将包含一个注册表的缓存减少磁盘空间的环境。
                .setMaxCacheSize(FrescoUtil.MAX_DISK_CACHE_SIZE)//默认缓存的最大大小。
                .setMaxCacheSizeOnLowDiskSpace(MAX_SMALL_DISK_LOW_CACHE_SIZE)//缓存的最大大小,使用设备时低磁盘空间。
                .setMaxCacheSizeOnVeryLowDiskSpace(MAX_SMALL_DISK_VERYLOW_CACHE_SIZE)//缓存的最大大小,当设备极低磁盘空间
//            .setVersion(version)
                .build();

        //默认图片的磁盘配置
        DiskCacheConfig diskCacheConfig = DiskCacheConfig.newBuilder()
                .setBaseDirectoryPath(Environment.getExternalStorageDirectory().getAbsoluteFile())//缓存图片基路径
                .setBaseDirectoryName(IMAGE_PIPELINE_CACHE_DIR)//文件夹名
//            .setCacheErrorLogger(cacheErrorLogger)//日志记录器用于日志错误的缓存。
//            .setCacheEventListener(cacheEventListener)//缓存事件侦听器。
//            .setDiskTrimmableRegistry(diskTrimmableRegistry)//类将包含一个注册表的缓存减少磁盘空间的环境。
                .setMaxCacheSize(FrescoUtil.MAX_DISK_CACHE_SIZE)//默认缓存的最大大小。
                .setMaxCacheSizeOnLowDiskSpace(MAX_DISK_CACHE_LOW_SIZE)//缓存的最大大小,使用设备时低磁盘空间。
                .setMaxCacheSizeOnVeryLowDiskSpace(MAX_DISK_CACHE_VERYLOW_SIZE)//缓存的最大大小,当设备极低磁盘空间
//            .setVersion(version)
                .build();

        //缓存图片配置
        ImagePipelineConfig.Builder configBuilder = ImagePipelineConfig.newBuilder(context)
//            .setAnimatedImageFactory(AnimatedImageFactory animatedImageFactory)//图片加载动画
                .setBitmapMemoryCacheParamsSupplier(mSupplierMemoryCacheParams)//内存缓存配置（一级缓存，已解码的图片）
//            .setCacheKeyFactory(cacheKeyFactory)//缓存Key工厂
//            .setEncodedMemoryCacheParamsSupplier(encodedCacheParamsSupplier)//内存缓存和未解码的内存缓存的配置（二级缓存）
//            .setExecutorSupplier(executorSupplier)//线程池配置
//            .setImageCacheStatsTracker(imageCacheStatsTracker)//统计缓存的命中率
//            .setImageDecoder(ImageDecoder imageDecoder) //图片解码器配置
//            .setIsPrefetchEnabledSupplier(Supplier<Boolean> isPrefetchEnabledSupplier)//图片预览（缩略图，预加载图等）预加载到文件缓存
                .setMainDiskCacheConfig(diskCacheConfig)//磁盘缓存配置（总，三级缓存）
//            .setMemoryTrimmableRegistry(memoryTrimmableRegistry) //内存用量的缩减,有时我们可能会想缩小内存用量。比如应用中有其他数据需要占用内存，不得不把图片缓存清除或者减小 或者我们想检查看看手机是否已经内存不够了。
//            .setNetworkFetchProducer(networkFetchProducer)//自定的网络层配置：如OkHttp，Volley
//            .setPoolFactory(poolFactory)//线程池工厂配置
//            .setProgressiveJpegConfig(progressiveJpegConfig)//渐进式JPEG图
//            .setRequestListeners(requestListeners)//图片请求监听
//            .setResizeAndRotateEnabledForNetwork(boolean resizeAndRotateEnabledForNetwork)//调整和旋转是否支持网络图片
                .setSmallImageDiskCacheConfig(diskSmallCacheConfig)//磁盘缓存配置（小图片，可选～三级缓存的小图优化缓存）
                ;
        return configBuilder.build();
    }

    // 单位是byte
    public static long getCacheSize(boolean hasMemoryCache, boolean hasDiskCache) {
        long byteSize = 0;
        ImagePipelineFactory factory = Fresco.getImagePipelineFactory();
        if (factory != null) {
            if (hasMemoryCache) {
                //CountingMemoryCache<CacheKey, CloseableImage> bitmapMemoryCache = factory.getBitmapCountingMemoryCache();
                CountingMemoryCache<CacheKey, PooledByteBuffer> encodedMemoryCache = factory.getEncodedCountingMemoryCache();
                //byteSize += bitmapMemoryCache.getSizeInBytes();
                byteSize += encodedMemoryCache.getSizeInBytes();
            }

            if (hasDiskCache) {
                byteSize += factory.getSmallImageDiskStorageCache().getSize();
                byteSize += factory.getMainDiskStorageCache().getSize();
            }
        }

        return byteSize;
    }

//    public static String getFormatedCacheSize(boolean hasMemoryCache, boolean hasDiskCache, @Nullable DecimalFormat decimalFormat) {
//        long byteSize = getCacheSize(hasMemoryCache, hasDiskCache);
//        if (decimalFormat == null) decimalFormat = new DecimalFormat("0.00");
//
//        if (byteSize < C.BYTES_IN_M) {
//            float k = 1.0f * byteSize / C.BYTES_IN_K;
//            return decimalFormat.format(k) + "K";
//        } else {
//            float m = 1.0f * byteSize / C.BYTES_IN_M;
//            return decimalFormat.format(m) + "M";
//        }
//    }

    public static void clearCache(boolean clearMemoryCache, boolean clearDiskCache) {
        ImagePipelineFactory factory = Fresco.getImagePipelineFactory();
        if (clearMemoryCache) {
            Predicate<CacheKey> predicateAll = new Predicate<CacheKey>() {
                @Override
                public boolean apply(CacheKey cacheKey) {
                    return true;
                }
            };
            factory.getBitmapMemoryCache().removeAll(predicateAll);
            factory.getEncodedMemoryCache().removeAll(predicateAll);
        }
        if (clearDiskCache) {
            factory.getMainDiskStorageCache().clearAll();
            factory.getSmallImageDiskStorageCache().clearAll();
        }
    }



    public static String getUrl(int resId) {
        return "res" + "://"
                + AppProfile.getContext().getResources().getResourceTypeName(resId) + "/"
                + resId;
    }

    public static Uri getUri(int resId) {
//        Uri uri = new Uri.Builder()
//                .scheme(UriUtil.LOCAL_RESOURCE_SCHEME) // "res"
//                .path(String.valueOf(resId))
//                .build();
//
//        return uri;
        return Uri.parse(getUrl(resId));
    }

    public static boolean hasCache(final String url) {
        if (TextUtils.isEmpty(url)) return false;

        return hasCache(Uri.parse(url));
    }

    public static boolean hasCache(final Uri uri) {
        return hasCacheFrom(uri, true, true, true, true);
    }

    public static boolean hasCacheFrom(final String url,
                                       boolean fromBitmapCache,
                                       boolean fromEncodeMemoryCache,
                                       boolean fromMainDisk,
                                       boolean fromSmallImageDisk) {
        if (TextUtils.isEmpty(url)) return false;

        return hasCacheFrom(Uri.parse(url), fromBitmapCache, fromEncodeMemoryCache, fromMainDisk, fromSmallImageDisk);
    }

    public static boolean hasCacheFrom(final Uri uri,
                                       boolean fromBitmapCache,
                                       boolean fromEncodeMemoryCache,
                                       boolean fromMainDisk,
                                       boolean fromSmallImageDisk) {
        ImagePipelineFactory factory = Fresco.getImagePipelineFactory();

        ImageRequest imageRequest = ImageRequest.fromUri(uri);
        CacheKey key = null;

        if (fromBitmapCache) {
            key = DefaultCacheKeyFactory.getInstance().getBitmapCacheKey(imageRequest);
            CloseableReference<CloseableImage> bitmap = factory.getBitmapMemoryCache().get(key);
            if (bitmap != null) return true;
        }
        if (fromEncodeMemoryCache) {
            key = DefaultCacheKeyFactory.getInstance().getEncodedCacheKey(imageRequest);
            CloseableReference<PooledByteBuffer> pooledByteBuffer = factory.getEncodedMemoryCache().get(key);
            if (pooledByteBuffer != null) return true;
        }
        if (fromMainDisk) {
            if (factory.getMainDiskStorageCache().hasKey(key)) return true;
        }
        if (fromSmallImageDisk) {
            if (factory.getSmallImageDiskStorageCache().hasKey(key)) return true;
        }

        return false;
    }




    public static void setImageUri(final SimpleDraweeView imageView, String uri) {
        setImageUri(imageView, uri, 0, 0);
    }

    public static void setImageUri(final SimpleDraweeView imageView,
                                   String uri,
                                   int width,
                                   int height) {
        setImageUri(imageView, uri, width, height, null);
    }

    public static void setImageUri(final SimpleDraweeView imageView,
                                   String uri,
                                   int width,
                                   int height,
                                   Float radius) {
        setImageUri(imageView, uri, width, height, radius, radius, radius, radius);
    }

    public static void setImageUri(final SimpleDraweeView imageView,
                                   String uri,
                                   Float radius) {
        setImageUri(imageView, uri, 0, 0, radius, radius, radius, radius);
    }

    public static void setImageUri(final SimpleDraweeView imageView,
                                   String uri,
                                   Float topLeftRadius,
                                   Float topRightRadius,
                                   Float bottomRightRadius,
                                   Float bottomLeftRadius) {
        setImageUri(imageView, uri, 0, 0, topLeftRadius, topRightRadius, bottomRightRadius, bottomLeftRadius);
    }

    public static void setImageUri(final SimpleDraweeView imageView,
                                   String url,
                                   int width,
                                   int height,
                                   Float topLeftRadius,
                                   Float topRightRadius,
                                   Float bottomRightRadius,
                                   Float bottomLeftRadius) {
        setImageUri(imageView,url,width,height,topLeftRadius,topRightRadius,bottomRightRadius,bottomLeftRadius,null);
    }

    public static void setImageUri(final SimpleDraweeView imageView,
                                   String url,
                                   int width,
                                   int height,
                                   Float topLeftRadius,
                                   Float topRightRadius,
                                   Float bottomRightRadius,
                                   Float bottomLeftRadius,
                                   final Drawable background
                                   ){
        if (width >= 500 && height >= 500) {
            imageView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

        Resources resources = AppProfile.getContext().getResources();

        GenericDraweeHierarchyBuilder hierarchyBuilder = new GenericDraweeHierarchyBuilder(resources);
        hierarchyBuilder
                .setFadeDuration(300);
        if (background!=null) {
                hierarchyBuilder.setBackground(background);
        }

        if (bottomLeftRadius != null && topLeftRadius != null && bottomRightRadius != null && topRightRadius != null) {
            hierarchyBuilder.setRoundingParams(RoundingParams.fromCornersRadii(topLeftRadius, topRightRadius, bottomRightRadius, bottomLeftRadius));
        }
        imageView.setHierarchy(hierarchyBuilder.build());

        ImageRequest imageRequest = ImageRequestBuilder.newBuilderWithSource(Uri.parse(url))
                .setAutoRotateEnabled(true)
                .setResizeOptions((width > 0 && height > 0) ? new ResizeOptions(width, height) : null)
                .build();
//        ImagePipeline imagePipeline = Fresco.getImagePipeline();
//        final DataSource<CloseableReference<CloseableImage>> dataSource = imagePipeline.fetchDecodedImage(imageRequest, imageView);
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setOldController(imageView.getController())
                .setImageRequest(imageRequest)
//                .setControllerListener(new BaseControllerListener<ImageInfo>() {
//                    @Override
//                    public void onFinalImageSet(String s, @Nullable ImageInfo imageInfo, @Nullable Animatable animatable) {
//                        CloseableReference<CloseableImage> imageReference = null;
//                        try {
//                            imageReference = dataSource.getResult();
//                            if (imageReference != null) {
//                                CloseableImage image = imageReference.get();
//                                if (image != null && image instanceof CloseableStaticBitmap) {
//                                    CloseableStaticBitmap closeableStaticBitmap = (CloseableStaticBitmap) image;
//                                    Bitmap bitmap = closeableStaticBitmap.getUnderlyingBitmap();
//                                    if (bitmap != null) {
//                                        imageView.setImageBitmap(bitmap);
//                                    }
//                                }
//                            }
//                        } finally {
//                            dataSource.close();
//                            CloseableReference.closeSafely(imageReference);
//                        }
//                    }
//                })
                .setTapToRetryEnabled(true)
                .build();
        imageView.setController(controller);
    }


    public static Bitmap getBitmapFromCache(Uri uri) {
//        Postprocessor blurPostprocesor = new Postprocessor() {
//            @Override
//            public CloseableReference<Bitmap> process(Bitmap bitmap, PlatformBitmapFactory platformBitmapFactory) {
//                return null;
//            }
//
//            @Override
//            public String getName() {
//                return null;
//            }
//        };
        ImageRequest imageRequest = ImageRequest.fromUri(uri);
        ImagePipeline imagePipeline = Fresco.getImagePipeline();
//        DataSource<CloseableReference<CloseableImage>> dataSource = imagePipeline.fetchDecodedImage(imageRequest, null);
        DataSource<CloseableReference<CloseableImage>> dataSource = imagePipeline.fetchImageFromBitmapCache(imageRequest, null);
        Bitmap result = getBitmapFromDataSource(dataSource);
        if (result != null) return result;


        CacheKey key = DefaultCacheKeyFactory.getInstance().getEncodedCacheKey(imageRequest);
        BinaryResource binaryResource = Fresco.getImagePipelineFactory().getMainDiskStorageCache().getResource(key);
        result = readFromBinaryResource(binaryResource);
        if (result != null) return result;

        binaryResource = Fresco.getImagePipelineFactory().getMainDiskStorageCache().getResource(key);
        result = readFromBinaryResource(binaryResource);
        if (result != null) return result;

        binaryResource = Fresco.getImagePipelineFactory().getSmallImageDiskStorageCache().getResource(key);
        result = readFromBinaryResource(binaryResource);
        if (result != null) return result;

        return null;
    }

    private static Bitmap readFromBinaryResource(BinaryResource binaryResource) {
        try {
            if (binaryResource != null) {
                return BitmapFactory.decodeStream(binaryResource.openStream());
            }
        } catch (IOException e) {
            LogUtil.e(TAG, e.toString());
        }

        return null;
    }

    public static Bitmap getBitmapFromNetwork(Uri uri) {
        ImageRequest imageRequest = ImageRequest.fromUri(uri);
        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        DataSource<CloseableReference<CloseableImage>> dataSource = imagePipeline.fetchDecodedImage(imageRequest, null);
        return getBitmapFromDataSource(dataSource);
    }

//    public static File getBitmapFromCache(String url) throws NullPointerException {
//        ImageRequest imageRequest = ImageRequest.fromUri(Uri.parse(url));
//        CacheKey cacheKey = DefaultCacheKeyFactory.getInstance().getEncodedCacheKey(imageRequest);
//        BinaryResource resource = ImagePipelineFactory.getInstance().getMainDiskStorageCache().getResource(cacheKey);
//        if (resource == null) {
//            return null;
//        }
//
//        File file = ((FileBinaryResource) resource).getFile();
//        return file;
//    }

    public static DataSource<Void> prefetchBitmapToDisk(String url) {
        if (!TextUtils.isEmpty(url)) {
            ImagePipeline imagePipeline = Fresco.getImagePipeline();
            ImageRequest imageRequest = ImageRequest.fromUri(Uri.parse(url));
            return imagePipeline.prefetchToDiskCache(imageRequest, null);
        }
        return null;
    }

    /**
     * 触发请求图片，返回DataSource变量，根据dataSource对象isFinished接口可以知道是否已经下载完成
     *
     * @param url 图片url
     * @return 请求对象，当url为空或者内存中已经有这张图片的缓存了，则不请求这张图片，统一返回null
     */
    public static DataSource<Void> prefetchBitmapToBitmapCache(String url) {
        if (!TextUtils.isEmpty(url)) {
            if (!FrescoUtil.hasCacheFrom(url, true, false, false, false)) {
                ImagePipeline imagePipeline = Fresco.getImagePipeline();
                ImageRequest imageRequest = ImageRequest.fromUri(Uri.parse(url));
                return imagePipeline.prefetchToBitmapCache(imageRequest, null);
            }
        }
        return null;
    }

    public static Bitmap getBitmap(String url) throws NullPointerException {
        if (TextUtils.isEmpty(url)) return null;
        return hasCache(url) ? getBitmapFromCache(Uri.parse(url)) : getBitmapFromNetwork(Uri.parse(url));
    }

    private static Bitmap getBitmapFromDataSource(DataSource<CloseableReference<CloseableImage>> dataSource) {
        if (dataSource == null) {
            LogUtil.e(TAG, "XXX is dataSource");
            return null;
        }

        try {
            int i = 0;
            while (!dataSource.isFinished() && (i++ < 5)) {
                Thread.sleep(100);
                LogUtil.d(TAG, "wait to fetch image");
            }
        } catch (InterruptedException e) {
            LogUtil.d(TAG, e.toString());
        }

        CloseableReference<CloseableImage> imageReference = null;
        Bitmap bitmap = null;
        try {
            imageReference = dataSource.getResult();
            if (imageReference != null) {
                CloseableStaticBitmap closableStaticBitmap = (CloseableStaticBitmap) imageReference.get();
                bitmap = closableStaticBitmap.getUnderlyingBitmap();
            }
        } finally {
            dataSource.close();
            CloseableReference.closeSafely(imageReference);
        }

        return bitmap;
    }
}
