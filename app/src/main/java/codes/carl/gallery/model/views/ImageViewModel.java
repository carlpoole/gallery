package codes.carl.gallery.model.views;

import androidx.lifecycle.ViewModel;

import codes.carl.gallery.model.Picture;

public class ImageViewModel extends ViewModel {

    private Picture picture;

    public ImageViewModel() {
    }

    public Picture getPicture() {
        return picture;
    }

    public void setPicture(Picture picture) {
        this.picture = picture;
    }
}
