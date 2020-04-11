package codes.carl.gallery.network;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Handles network communication for the Gallery app.
 */
public class Client {

    /**
     * The base URL of the Lorem Picsum API.
     */
    private static final String BASE_API_URL = "https://picsum.photos/v2/";

    /**
     * A built Retrofit HTTP client to access the Lorem Picsum API.
     */
    private API picsumAPI;

    /**
     * A singleton instance of the network client.
     */
    private static Client sInstance;

    /**
     * Gets a singleton instance of the network client.
     *
     * @return The network client
     */
    public static Client getInstance() {
        if (sInstance == null) {
            sInstance = new Client();
            sInstance.init();
        }

        return sInstance;
    }

    /**
     * Gets the built API object to access the Retrofit HTTP client.
     *
     * @return The Retrofit HTTP client
     */
    public API getPicsumAPI() {
        return picsumAPI;
    }

    /**
     * Creates an instance of the http client to use the Lorem Picsum API with JSON object
     *  serialization and configured to use RxJava directly.
     */
    private void init() {
        // Adding a logging interceptor to view network activity
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        final OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        clientBuilder.retryOnConnectionFailure(true);
        clientBuilder.addInterceptor(loggingInterceptor);

        OkHttpClient okHttpClient = clientBuilder.build();

        // Configure the network library with JSON object serializer and RxJava behavior
        Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient);

        retrofitBuilder.baseUrl(BASE_API_URL);
        picsumAPI = retrofitBuilder.build().create(API.class);
    }
}
