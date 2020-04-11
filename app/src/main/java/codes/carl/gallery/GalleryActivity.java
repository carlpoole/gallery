package codes.carl.gallery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.bumptech.glide.Glide;
import com.bumptech.glide.integration.recyclerview.RecyclerViewPreloader;
import com.bumptech.glide.util.ViewPreloadSizeProvider;

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
     * Tracks if the gallery view is currently reloading data from Lorem Picsum.
     */
    private boolean isRefreshing = false;

    /**
     * Used to clean up RxAndroid after.
     */
    @NonNull
    private CompositeDisposable rxDisposables = new CompositeDisposable();

    /**
     * Used for pre-loading images in the gallery view with Glide.
     */
    ViewPreloadSizeProvider<Picture> sizeProvider = new ViewPreloadSizeProvider<>();

    /**
     * The layout supporting pull-down-to-refresh behavior.
     */
    SwipeRefreshLayout swipeReload;

    /**
     * The gallery view;
     */
    RecyclerView gallery;

    /**
     * The adapter for the images in the gallery view.
     */
    GalleryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Todo: handle rotation configuration change

        gallery = findViewById(R.id.galleryView);
        swipeReload = findViewById(R.id.swipeReload);

        swipeReload.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(!isRefreshing)
                    loadImages();
            }
        });

        // Set the columns in the gallery grid depending on the orientation of the device
        int orientation = getResources().getConfiguration().orientation;
        int columns = orientation == Configuration.ORIENTATION_LANDSCAPE ? 3 : 2;

        gallery.setLayoutManager(new GridLayoutManager(this, columns));

        loadImages();
    }

    @Override
    protected void onDestroy() {
        // Clean up after RxAndroid
        rxDisposables.clear();

        super.onDestroy();
    }

    /**
     * Create the sort menu in the top right of the Activity.
     *
     * @param menu The menu placeholder used for the sort menu
     * @return true since we inflated a menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sort_menu, menu);
        return true;
    }

    /**
     * Handles the sort menu item selection.
     *
     * @param item The menu item that was selected
     * @return true if we handled the menu item selection
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.authorSort:
                // Todo: sort by author name
                return true;
            case R.id.imageSizeSort:
                // Todo: sort by image size
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Downloads image details from Lorem Picsum
     */
    private void loadImages() {
        isRefreshing = true;
        // Todo: show a spinner or loading view

        rxDisposables.add(Client.getInstance().getPicsumAPI().getImageList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Response<List<Picture>>>() {

                    @Override
                    public void onNext(Response<List<Picture>> response) {
                        if (response.code() == 200) {
                            if (response.body() != null && !response.body().isEmpty()) {
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
                        isRefreshing = false;
                        swipeReload.setRefreshing(false);
                    }
                }));
    }

    /**
     * Loads downloaded image data into the gallery adapter.
     *
     * @param newPictures The pictures to load into the adapter
     */
    private void setupGallery(List<Picture> newPictures) {
        if(adapter == null || didImagesChange(adapter.getPictures(), newPictures)) {
            adapter = new GalleryAdapter(this, newPictures, sizeProvider);
            gallery.setAdapter(adapter);

            RecyclerViewPreloader<Picture> preloader = new RecyclerViewPreloader<>(
                    Glide.with(this), adapter, sizeProvider, 10);

            gallery.addOnScrollListener(preloader);
        }
    }

    private boolean didImagesChange(List<Picture> pictures, List<Picture> newPictures) {
        return !pictures.equals(newPictures);
    }
}
