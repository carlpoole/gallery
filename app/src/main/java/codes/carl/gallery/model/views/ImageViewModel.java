package codes.carl.gallery.model.views;

import androidx.lifecycle.ViewModel;

import codes.carl.gallery.model.Picture;

/**
 * Handles the view model data for the Image Viewe Activity.
 *
 * @see codes.carl.gallery.ImageActivity
 */
public class ImageViewModel extends ViewModel {

    /**
     * The picture viewed in the image viewer.
     */
    private Picture picture;

    /**
     * Constructs a new empty Image View Model.
     */
    public ImageViewModel() {
    }

    /**
     * Gets the picture viewed in the image viewer.
     *
     * @return The picture viewed in the image viewer
     */
    public Picture getPicture() {
        return picture;
    }

    /**
     * Sets the picture viewed in the image viewer.
     *
     * @param picture The picture viewed in the image viewer
     */
    public void setPicture(Picture picture) {
        this.picture = picture;
    }
}
