package codes.carl.gallery;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import codes.carl.gallery.model.Picture;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.PictureViewHolder> {

    private List<Picture> galleryImages;
    private LayoutInflater layoutInflater;

    public GalleryAdapter(Context context, List<Picture> galleryImages) {
        this.galleryImages = galleryImages;
        layoutInflater = LayoutInflater.from(context);

    }

    @NonNull
    @Override
    public PictureViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.gallery_item, parent, false);
        return new PictureViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PictureViewHolder holder, int position) {
        Picture picture = galleryImages.get(position);

        holder.authorName.setText(picture.getAuthor());

        // Todo: set image into image view

        // Todo: link button to new view

    }

    @Override
    public int getItemCount() {
        return galleryImages.size();
    }

    static class PictureViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView galleryImage;
        ImageView infoButton;
        TextView authorName;

        PictureViewHolder(@NonNull View itemView) {
            super(itemView);

            galleryImage = itemView.findViewById(R.id.galleryImage);
            authorName = itemView.findViewById(R.id.authorName);
            infoButton = itemView.findViewById(R.id.infoButton);
        }

        @Override
        public void onClick(View v) {
            // Todo: hook up to load full screen view
        }
    }
}
