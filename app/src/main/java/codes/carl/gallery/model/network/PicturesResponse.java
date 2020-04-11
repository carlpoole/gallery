package codes.carl.gallery.model.network;

import java.util.List;

import codes.carl.gallery.model.Picture;

/**
 * Models the response payload for a list of pictures from Lorem Picsum
 */
public class PicturesResponse {

    List<Picture> pictures;

    /**
     * Empty constructor for Gson
     */
    public PicturesResponse() {
    }

    public List<Picture> getPictures() {
        return pictures;
    }
}
