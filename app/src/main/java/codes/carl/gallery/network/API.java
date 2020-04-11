package codes.carl.gallery.network;

import codes.carl.gallery.model.network.PicturesResponse;
import io.reactivex.rxjava3.core.Observable;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;

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
    Observable<Response<PicturesResponse>> getImageList();

}
