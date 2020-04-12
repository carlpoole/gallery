package codes.carl.gallery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.bumptech.glide.Glide;
import com.bumptech.glide.integration.recyclerview.RecyclerViewPreloader;
import com.bumptech.glide.util.ViewPreloadSizeProvider;

import org.parceler.Parcels;
import org.reactivestreams.Subscriber;

import java.util.List;

import codes.carl.gallery.model.Picture;
import codes.carl.gallery.model.views.GalleryViewModel;
import codes.carl.gallery.network.Client;
import codes.carl.gallery.utils.SortUtils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

import static codes.carl.gallery.utils.sort.SortType.ALPHA;
import static codes.carl.gallery.utils.sort.SortType.SIZE;

/**
 * Activity that displays the image gallery
 */
public class GalleryActivity extends AppCompatActivity {

    /**
     * Constant TAG string for logs.
     */
    private static final String TAG = "Gallery";

    /**
     * The activity view model to persist data across config changes.
     */
    private GalleryViewModel viewModel;

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

        viewModel = new ViewModelProvider(this).get(GalleryViewModel.class);

        gallery = findViewById(R.id.galleryView);
        swipeReload = findViewById(R.id.swipeReload);

        swipeReload.setOnRefreshListener(() -> {
            if (!viewModel.isRefreshing())
                loadImages();
        });

        // Set the columns in the gallery grid depending on the orientation of the device
        int orientation = getResources().getConfiguration().orientation;
        int columns = orientation == Configuration.ORIENTATION_LANDSCAPE ? 3 : 2;

        gallery.setLayoutManager(new GridLayoutManager(this, columns));

        // Trigger load images if there is no image data
        if(viewModel.getPictures().isEmpty()) {
            loadImages();
        } else {
            setupGallery(viewModel.getPictures());
        }
    }

    @Override
    protected void onDestroy() {
        // Clean up after RxAndroid
        viewModel.getRxDisposables().clear();

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
                if (adapter != null) {
                    viewModel.setSortType(ALPHA);
                    adapter.alphaSort();
                }
                return true;
            case R.id.imageSizeSort:
                if (adapter != null) {
                    viewModel.setSortType(SIZE);
                    adapter.sizeSort();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Downloads image details from Lorem Picsum
     */
    private void loadImages() {
        viewModel.setRefreshing(true);

        viewModel.getRxDisposables().add(Client.getInstance().getPicsumAPI().getImageList()
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
                        viewModel.setRefreshing(false);
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

        // Pre-sort new image results based on the current sort option
        switch (viewModel.getSortType()) {
            case ALPHA:
                newPictures = SortUtils.alphaSort(newPictures);
                break;
            case SIZE:
                SortUtils.sizeSort(newPictures);
                break;
            default:
                break;
        }

        // Only update the list if the downloaded image data changed
        if (adapter == null || !adapter.getPictures().equals(newPictures)) {
            viewModel.setPictures(newPictures);
            adapter = new GalleryAdapter(this, viewModel, sizeProvider);

            viewModel.getRxDisposables().add(adapter.clickedPictureEvent().observeOn(AndroidSchedulers.mainThread()).subscribeWith(new DisposableObserver<Picture>() {
                @Override
                public void onNext(Picture picture) {
                    viewFullScreenImage(picture);
                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onComplete() {

                }
            }));

            gallery.setAdapter(adapter);

            RecyclerViewPreloader<Picture> preloader = new RecyclerViewPreloader<>(
                    Glide.with(this), adapter, sizeProvider, 10);

            gallery.addOnScrollListener(preloader);
        }
    }

    /**
     * Loads an image into a full screen view.
     *
     * @param picture The image to display full screen
     */
    public void viewFullScreenImage(Picture picture) {
        Intent intent = new Intent(this, ImageActivity.class);
        Bundle bundle = new Bundle();

        bundle.putParcelable("picture", Parcels.wrap(picture));
        intent.putExtras(bundle);

        startActivity(intent);
    }
}
