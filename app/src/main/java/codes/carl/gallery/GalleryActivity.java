package codes.carl.gallery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

import codes.carl.gallery.model.Picture;
import codes.carl.gallery.network.Client;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

/**
 * Activity that displays the image gallery
 */
public class GalleryActivity extends AppCompatActivity {

    /**
     * Constant TAG string for logs.
     */
    private static final String TAG = "Gallery";

    /**
     * Used to clean up RxAndroid after.
     */
    @NonNull
    private CompositeDisposable rxDisposables = new CompositeDisposable();

    RecyclerView gallery;
    GalleryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Todo: handle rotation configuration change

        gallery = findViewById(R.id.galleryView);

        int columns;
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            columns = 3;
        } else {
            columns = 2;
        }

        gallery.setLayoutManager(new GridLayoutManager(this, columns));

        loadImages();

    }

    @Override
    protected void onDestroy() {
        // Clean up after RxAndroid
        rxDisposables.clear();

        super.onDestroy();
    }

    private void loadImages() {
        // Todo: show a spinner or loading view

        rxDisposables.add(Client.getInstance().getPicsumAPI().getImageList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Response<List<Picture>>>() {

                    @Override
                    public void onNext(Response<List<Picture>> response) {
                        if(response.code() == 200) {
                            if(response.body() != null && !response.body().isEmpty()) {
                                setupGallery(response.body());
                            } else {
                                // Todo: do something else if empty response is possible
                            }
                        } else {
                            Log.e(TAG, "Response code: " + response.code());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "Network Error: " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        // Empty
                    }
                }));
    }

    private void setupGallery(List<Picture> pictures) {
        adapter = new GalleryAdapter(this, pictures);
        gallery.setAdapter(adapter);
    }
}
