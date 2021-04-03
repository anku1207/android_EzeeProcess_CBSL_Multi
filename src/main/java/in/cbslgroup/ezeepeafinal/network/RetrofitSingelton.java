package in.cbslgroup.ezeepeafinal.network;

import java.util.concurrent.TimeUnit;

import in.cbslgroup.ezeepeafinal.utils.ApiUrl;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitSingelton {

    private static Retrofit retrofit = null;

    public static ApiInterface getClient() {

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .connectTimeout(15, TimeUnit.MINUTES)
                 .readTimeout(15, TimeUnit.MINUTES)
                  .writeTimeout(15, TimeUnit.MINUTES)
                //.addInterceptor(new BasicAuthInterceptor(username, pwd))
               // .addInterceptor(new BasicAuthInterceptor("admin", "ad"))
                .build();

        // change your base URL
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                     .baseUrl(ApiUrl.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }
        //Creating object for our interface
        ApiInterface api = retrofit.create(ApiInterface.class);
        return api; // return the APIInterface object
    }

}
