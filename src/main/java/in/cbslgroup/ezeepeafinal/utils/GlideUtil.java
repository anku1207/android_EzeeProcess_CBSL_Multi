package in.cbslgroup.ezeepeafinal.utils;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.AppGlideModule;

import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

@GlideModule
public class GlideUtil extends AppGlideModule {

    public static final int TIMEOUT = 20*1000;

    @Override
    public void registerComponents(Context context, Glide glide, Registry registry) {
        final OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.readTimeout(TIMEOUT, TimeUnit.SECONDS);
        builder.writeTimeout(TIMEOUT, TimeUnit.SECONDS);
        builder.connectTimeout(TIMEOUT, TimeUnit.SECONDS);
        registry.append(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(builder.build()));
    }

}
// implements GlideModule
//    @Override
//    public void registerComponents(Context context, Glide glide, Registry registry) {
//
//        OkHttpClient client = new OkHttpClient.Builder()
//                .connectTimeout(20*1000, TimeUnit.SECONDS)
//                .readTimeout(20*1000, TimeUnit.SECONDS)
//                .build();
//
//        OkHttpUrlLoader.Factory factory = new OkHttpUrlLoader.Factory(client);
//        glide.getRegistry().replace(GlideUrl.class, InputStream.class, factory);
//
//
//    }
//
//
//    @Override
//    public void applyOptions(Context context, GlideBuilder builder) {
//
//    }

