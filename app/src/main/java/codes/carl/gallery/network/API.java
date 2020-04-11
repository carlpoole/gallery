package codes.carl.gallery.network;

import java.util.List;

import codes.carl.gallery.model.Picture;
import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.GET;

/**
 * Describes the Lorem Picsum API
 *
 * @see <a href="https://picsum.photos">Lorem Picsum</a>
 */
public interface API {

    /**
     * Gets a list of images from Lorem Picsum.
     *
     * @return A list of images
     */
    @GET("list")
    Observable<Response<List<Picture>>> getImageList();

}
