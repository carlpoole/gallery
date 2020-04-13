package codes.carl.gallery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.integration.recyclerview.RecyclerViewPreloader;
import com.bumptech.glide.util.ViewPreloadSizeProvider;
import com.github.ybq.android.spinkit.SpinKitView;

import org.parceler.Parcels;

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
     * Used when the app is fresh and there are no views to show while the
     * pictures are being downloaded from Lorem Picsum.
     */
    SpinKitView loadingProgress;

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

    /**
     * The modal web view for viewing picture info.
     */
    private ConstraintLayout webModal;

    /**
     * The web info modal
     */
    private WebInfoModal webInfoModal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewModel = new ViewModelProvider(this).get(GalleryViewModel.class);

        gallery = findViewById(R.id.galleryView);

        loadingProgress = findViewById(R.id.loading_spinner);
        swipeReload = findViewById(R.id.swipeReload);

        webModal = findViewById(R.id.web_modal);
        webInfoModal = new WebInfoModal(this, webModal);

        // Add observer for when info modal is hidden
        viewModel.getRxDisposables().add(webInfoModal.modalHiddenEvent().observeOn(AndroidSchedulers.mainThread()).subscribe(t -> viewModel.setViewingInfo(false)));

        // Reloads the gallery images when the user pulls down at the top of the gallery grid
        swipeReload.setOnRefreshListener(() -> {
            if (!viewModel.isRefreshing())
                loadImages();
        });

        // Set the columns in the gallery grid depending on the orientation of the device
        int orientation = getResources().getConfiguration().orientation;
        int columns = orientation == Configuration.ORIENTATION_LANDSCAPE ? 3 : 2;

        gallery.setLayoutManager(new GridLayoutManager(this, columns));

        // Trigger load images if there is no image data
        if (viewModel.getPictures().isEmpty()) {
            loadingProgress.setVisibility(View.VISIBLE);
            loadImages();
        } else {
            setupGallery(viewModel.getPictures());
        }

        // Reveal the info modal if it was previously open before config change
        if (viewModel.isViewingInfo()) {
            webInfoModal.showForm(false);
            webInfoModal.loadURL(viewModel.getInfoPicture().getUrl());
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
     * Detect back press to handle back action in case web view modal is displayed.
     *
     * @param keyCode The key pressed
     * @param event   Key event detected
     * @return true when event is handled
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            if (viewModel.isViewingInfo()) {
                webInfoModal.goBack();
                return true;
            }
        }

        return super.onKeyDown(keyCode, event);
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
                                showEmptyMessage();
                            }
                        } else {
                            Log.e(TAG, "Response code: " + response.code());
                            showDownloadError("Unable to download images from server - Code " + response.code());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "Network Error: " + e.getMessage());
                        showDownloadError(e.getMessage());
                        hideProgress();
                    }

                    @Override
                    public void onComplete() {
                        hideProgress();
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

        // Only setup adapter and view logic if necessary
        if (adapter == null) {
            adapter = new GalleryAdapter(this, viewModel, sizeProvider);
            gallery.setAdapter(adapter);

            RecyclerViewPreloader<Picture> preloader = new RecyclerViewPreloader<>(
                    Glide.with(this), adapter, sizeProvider, 10);

            gallery.addOnScrollListener(preloader);

            // Add observer for the full-screen image viewer
            viewModel.getRxDisposables().add(adapter.clickedPictureEvent().observeOn(AndroidSchedulers.mainThread()).subscribe(this::viewFullScreenImage));

            // Add observer for the info viewer
            viewModel.getRxDisposables().add(adapter.clickedInfoEvent().observeOn(AndroidSchedulers.mainThread()).subscribe(this::viewWebModal));
        }

        // Only update the list if the downloaded image data changed
        if (!adapter.getPictures().equals(newPictures)) {
            viewModel.setPictures(newPictures);
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * Hide progress views
     */
    private void hideProgress() {
        loadingProgress.setVisibility(View.GONE);
        viewModel.setRefreshing(false);
        swipeReload.setRefreshing(false);
    }

    /**
     * Loads an image into a full screen view.
     *
     * @param picture The image to display full screen
     */
    private void viewFullScreenImage(Picture picture) {
        Intent intent = new Intent(this, ImageActivity.class);
        Bundle bundle = new Bundle();

        bundle.putParcelable("picture", Parcels.wrap(picture));
        intent.putExtras(bundle);

        startActivity(intent);
    }

    /**
     * Loads a modal webview with more details about a picture.
     *
     * @param picture The image to load in the webview
     */
    private void viewWebModal(Picture picture) {
        viewModel.setInfoPicture(picture);
        viewModel.setViewingInfo(true);
        webInfoModal.showForm(true);
        webInfoModal.loadURL(viewModel.getInfoPicture().getUrl());
    }

    /**
     * Display an alert in case of network trouble to allow user to try again.
     */
    private void showDownloadError(String message) {

        if (message.contains("Unable to resolve host")) {
            message = "Please check internet and try again";
        }

        showAlertDialog("Problem", message);
    }

    /**
     * Display an alert in case the API gives us an empty result set.
     */
    private void showEmptyMessage() {
        showAlertDialog("Whoops", "There are no images to view at this time!");
    }

    private void showAlertDialog(String title, String message) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setTitle(title);
        alertBuilder.setMessage(message)
                .setPositiveButton("Retry", (dialog, id) -> loadImages())
                .setNegativeButton("Cancel", (dialog, id) -> {

                    /*
                        If an error occurred but there are images downloaded to view, allow
                        the user to continue viewing downloaded images, otherwise quit.
                     */
                    if (viewModel.getPictures().isEmpty()) {
                        finish();
                    }
                });

        alertBuilder.create().show();
    }

}
