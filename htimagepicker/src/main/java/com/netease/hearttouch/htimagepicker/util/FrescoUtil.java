package com.netease.hearttouch.htimagepicker.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.View;

import com.facebook.binaryresource.BinaryResource;
import com.facebook.cache.common.CacheKey;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.cache.DefaultCacheKeyFactory;
import com.facebook.imagepipeline.common.ImageDecodeOptions;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.image.CloseableStaticBitmap;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.io.IOException;

/**
 * Created by zyl06 on 2/29/16.
 */
public class FrescoUtil {
    private static String TAG = "HTImagePicker_FrescoUtil";

    public static void setImageUrl(final SimpleDraweeView imageView, String url, int width, int height) {
        setImageUri(imageView, Uri.parse(url), width, height);
    }

    public static void setImageUri(final SimpleDraweeView imageView, Uri uri, int width, int height) {
        if (width >= 500 && height >= 500) {
            imageView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

        Bitmap bitmap = getBitmapFromCache(uri);
        if (bitmap != null && !bitmap.isRecycled()) {
            imageView.setImageBitmap(bitmap);
            return;
        }

        ImageDecodeOptions decodeOptions = ImageDecodeOptions.newBuilder().
                setDecodeAllFrames(false).
                build();

        ImageRequest imageRequest = ImageRequestBuilder.newBuilderWithSource(uri)
                .setAutoRotateEnabled(true)
                .setResizeOptions(new ResizeOptions(width, height))
                .setImageDecodeOptions(decodeOptions)
                .build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setOldController(imageView.getController())
                .setImageRequest(imageRequest)
                .setTapToRetryEnabled(true)
                .setAutoPlayAnimations(false)
                .build();
        imageView.setController(controller);
    }

    public static Bitmap getBitmapFromCache(Uri uri) {
        ImageRequest imageRequest = ImageRequest.fromUri(uri);
        ImagePipeline imagePipeline = Fresco.getImagePipeline();
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
            Log.e(TAG, e.toString());
        }

        return null;
    }

    private static Bitmap getBitmapFromDataSource(DataSource<CloseableReference<CloseableImage>> dataSource) {

        if (dataSource == null) {
            Log.e(TAG, "XXX is dataSource");
            return null;
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
