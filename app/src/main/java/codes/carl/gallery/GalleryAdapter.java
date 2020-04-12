package codes.carl.gallery;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.ListPreloader;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.util.ViewPreloadSizeProvider;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

import java.util.List;

import codes.carl.gallery.model.Picture;
import codes.carl.gallery.model.views.GalleryViewModel;
import codes.carl.gallery.utils.GlideApp;
import codes.carl.gallery.utils.GlideRequest;
import codes.carl.gallery.utils.SortUtils;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

/**
 * Handles the display and replacement of picture views in the gallery view.
 */
public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.PictureViewHolder> implements
        ListPreloader.PreloadModelProvider<Picture> {

    /**
     * ViewModel containing picture list data.
     */
    private GalleryViewModel viewModel;

    /**
     * Inflates the picture views into the adapter.
     */
    private LayoutInflater layoutInflater;

    /**
     * Used by the pre-loader to determine the view size.
     */
    private ViewPreloadSizeProvider<Picture> preloadSizeProvider;

    /**
     * Used by Glide to load images into the picture image view.
     */
    private GlideRequest<Drawable> glideRequest;

    /**
     * Emits the clicked picture to subscribers.
     */
    private PublishSubject<Picture> clickedPicture = PublishSubject.create();

    /**
     * Constructs a gallery adapter.
     *
     * @param context             Reference to the Activity context.
     * @param viewModel           The ViewModel containing the list of pictures to add to the adapter.
     * @param preloadSizeProvider Used by the pre-loader to track the view size for preloading images.
     */
    GalleryAdapter(Context context, GalleryViewModel viewModel, ViewPreloadSizeProvider<Picture> preloadSizeProvider) {
        this.viewModel = viewModel;
        this.preloadSizeProvider = preloadSizeProvider;

        layoutInflater = LayoutInflater.from(context);

        glideRequest = GlideApp.with(context)
                .asDrawable()
                .fitCenter()
                .centerCrop()
                .placeholder(R.drawable.gallery_item_placeholder)
                .error(android.R.drawable.stat_notify_error)
                .thumbnail(0.5f)
                .transition(withCrossFade());
    }

    /**
     * Remove all images from the gallery grid.
     */
    public void clear() {
        viewModel.getPictures().clear();
        notifyDataSetChanged();
    }

    /**
     * Adds the provided list of pictures to the current gallery.
     *
     * @param pictures A list of pictures to add to the gallery grid
     */
    public void addAll(List<Picture> pictures) {
        viewModel.getPictures().addAll(pictures);
        notifyDataSetChanged();
    }

    /**
     * Sorts the pictures in the gallery by the author's name.
     */
    public void alphaSort() {
        viewModel.setPictures(SortUtils.alphaSort(viewModel.getPictures()));
        notifyDataSetChanged();
    }

    /**
     * Sorts the pictures in the gallery by the image size.
     */
    public void sizeSort() {
        SortUtils.sizeSort(viewModel.getPictures());
        notifyDataSetChanged();
    }

    /**
     * Gets the list of pictures in the current gallery.
     *
     * @return The pictures in the gallery
     */
    public List<Picture> getPictures() {
        return viewModel.getPictures();
    }

    @NonNull
    @Override
    public PictureViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.gallery_item, parent, false);
        return new PictureViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PictureViewHolder holder, int position) {
        Picture picture = viewModel.getPictures().get(position);

        holder.picture = picture;
        holder.authorName.setText(picture.getAuthor());
        holder.loadImage(picture.getDownload_url());

        preloadSizeProvider.setView(holder.galleryImage);

    }

    /**
     * Gets the overall picture count in the adapter.
     *
     * @return The overall picture count.
     */
    @Override
    public int getItemCount() {
        return viewModel.getPictures().size();
    }

    /**
     * Gets a picture to pre-load ahead in the gallery view.
     *
     * @param position The position of a picture in the list to pre-load.
     * @return A list containing an image to pre-load.
     */
    @NonNull
    @Override
    public List<Picture> getPreloadItems(int position) {
        return viewModel.getPictures().subList(position, position + 1);
    }

    /**
     * Gets the Glide request logic to pre-load a picture in the gallery view.
     *
     * @param item The picture to pre-load.
     * @return A constructed request to call to pre-load the image.
     */
    @Nullable
    @Override
    public RequestBuilder<?> getPreloadRequestBuilder(@NonNull Picture item) {
        return glideRequest.clone().load(item.getDownload_url());
    }

    public Observable<Picture> clickedPictureEvent() {
        return clickedPicture;
    }

    /**
     * A ViewHolder representing an individual picture view in the gallery grid.
     */
    class PictureViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        /**
         * The picture to display.
         */
        ImageView galleryImage;

        /**
         * The info button leading to the webview.
         */
        ImageView infoButton;

        /**
         * The name of the picture's author.
         */
        TextView authorName;

        /**
         * The picture subject of the gallery tile.
         */
        Picture picture;

        /**
         * Constructs the picture ViewHolder.
         *
         * @param itemView The reference to the Android view.
         */
        PictureViewHolder(@NonNull View itemView) {
            super(itemView);

            galleryImage = itemView.findViewById(R.id.galleryImage);
            authorName = itemView.findViewById(R.id.authorName);
            infoButton = itemView.findViewById(R.id.infoButton);

            galleryImage.setOnClickListener(this);
        }

        /**
         * Loads an image into the picture ImageView provided an initial location URL.
         *
         * @param url The url of the image to load in the view.
         */
        void loadImage(String url) {
            glideRequest.load(url).into(galleryImage);
        }

        /**
         * Handles the behavior when the user clicks a picture in the gallery.
         *
         * @param view The clicked view.
         */
        @Override
        public void onClick(View view) {
            clickedPicture.onNext(picture);
        }
    }
}
