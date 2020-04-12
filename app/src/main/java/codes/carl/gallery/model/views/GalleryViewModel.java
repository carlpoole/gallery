package codes.carl.gallery.model.views;

import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import codes.carl.gallery.model.Picture;
import codes.carl.gallery.utils.sort.SortType;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Handles the view model data for the Gallery Activity.
 *
 * @see codes.carl.gallery.GalleryActivity
 */
public class GalleryViewModel extends ViewModel {

    /**
     * The pictures displayed in the gallery view.
     */
    private List<Picture> pictures = new ArrayList<>();

    /**
     * A picture selected to be viewed in a web info modal.
     */
    private Picture infoPicture = null;

    /**
     * Whether the user is currently viewing the web info modal.
     */
    private boolean viewingInfo = false;

    /**
     * The sort mode for the gallery.
     */
    private SortType sortType = SortType.NORMAL;

    /**
     * Tracks whether the gallery is currently being refreshed from Lorem Picsum.
     */
    private boolean isRefreshing = false;

    /**
     * Used for cleanup for RxAndroid after the app is closed.
     */
    private CompositeDisposable rxDisposables = new CompositeDisposable();

    /**
     * Constructs a new empty Gallery View Model.
     */
    public GalleryViewModel() {
    }

    /**
     * Gets the gallery pictures.
     *
     * @return The gallery pictures.
     */
    public List<Picture> getPictures() {
        return pictures;
    }

    /**
     * Sets the gallery pictures.
     *
     * @param pictures The gallery pictures.
     */
    public void setPictures(List<Picture> pictures) {
        this.pictures = pictures;
    }

    /**
     * Gets the current sort type for the pictures in the gallery.
     *
     * @return The gallery sort type
     */
    public SortType getSortType() {
        return sortType;
    }

    /**
     * Sets the current sort type for the pictures in the gallery.
     *
     * @param sortType The gallery sort type
     */
    public void setSortType(SortType sortType) {
        this.sortType = sortType;
    }

    /**
     * Check if the gallery view is refreshing from the network.
     *
     * @return Is the gallery being refreshed
     */
    public boolean isRefreshing() {
        return isRefreshing;
    }

    /**
     * Sets the refreshing state of the gallery from the network.
     *
     * @param refreshing The refreshing state
     */
    public void setRefreshing(boolean refreshing) {
        isRefreshing = refreshing;
    }

    /**
     * Gets the RxAndroid observers.
     *
     * @return The RxAndroid observers.
     */
    public CompositeDisposable getRxDisposables() {
        return rxDisposables;
    }

    /**
     * Gets the currently selected picture for the web info modal.
     *
     * @return The picture to be viewed in the web info modal.
     */
    public Picture getInfoPicture() {
        return infoPicture;
    }

    /**
     * Sets the selected picture for the web info modal.
     *
     * @param infoPicture The picture to be viewed in the web info modal.
     */
    public void setInfoPicture(Picture infoPicture) {
        this.infoPicture = infoPicture;
    }

    /**
     * Is the user currently viewing a picture in the web info modal.
     *
     * @return whether the web info modal should be displayed
     */
    public boolean isViewingInfo() {
        return viewingInfo;
    }

    /**
     * Sets whether the user is currently viewing a picture in the web info modal.
     *
     * @param viewingInfo The state of the web info modal
     */
    public void setViewingInfo(boolean viewingInfo) {
        this.viewingInfo = viewingInfo;
    }
}
