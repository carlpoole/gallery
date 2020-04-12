package codes.carl.gallery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import org.parceler.Parcels;

import codes.carl.gallery.model.Picture;
import codes.carl.gallery.model.views.ImageViewModel;
import codes.carl.gallery.utils.GlideApp;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

/**
 * A full-screen Activity to display a picture. Allows pinch to zoom and panning.
 */
public class ImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_image);

        ImageViewModel viewModel = new ViewModelProvider(this).get(ImageViewModel.class);

        if (getIntent().getExtras() != null) {
            Picture picture = Parcels.unwrap(getIntent().getParcelableExtra("picture"));
            viewModel.setPicture(picture);
        }

        ImageView pictureView = findViewById(R.id.fullscreenImage);
        pictureView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        GlideApp.with(this)
                .load(viewModel.getPicture().getDownload_url())
                .fitCenter()
                .placeholder(R.drawable.gallery_item_placeholder)
                .error(android.R.drawable.stat_notify_error)
                .transition(withCrossFade())
                .into(pictureView);

        // Go back to gallery
        findViewById(R.id.backButton).setOnClickListener(v -> finish());
    }
}
