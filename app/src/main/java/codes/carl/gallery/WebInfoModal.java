package codes.carl.gallery;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

/**
 * Controller class for the web info modal, used to display the web url for the image page.
 */
class WebInfoModal {

    /**
     * The application context of the web info modal.
     */
    private Context context;

    /**
     * The container layout for the entire web info modal.
     */
    private FrameLayout webModal;

    /**
     * The layout location where the web view is placed in the info modal.
     */
    private FrameLayout webFrame;

    /**
     * The web view used to display picture info content.
     */
    private WebView webView;

    /**
     * Emits the hide event when the modal is hidden.
     */
    private PublishSubject<Boolean> modalHidden = PublishSubject.create();

    /**
     * Constructs a web info modal.
     *
     * @param context  The activity context where the view is located
     * @param webModal The view used for the info modal
     */
    WebInfoModal(Context context, FrameLayout webModal) {
        this.context = context;
        this.webModal = webModal;

        webFrame = webModal.findViewById(R.id.info_web_view);

        // dismiss modal view if tapped outside
        webModal.setOnClickListener(v -> hideForm());

        // ignore taps on the frame of the modal to prevent accidental dismissal
        ConstraintLayout modalFrame = webModal.findViewById(R.id.modal_window);
        modalFrame.setOnClickListener(null);

        AppCompatButton browserBack = webModal.findViewById(R.id.browser_back);
        browserBack.setOnClickListener(v -> goBack());

        AppCompatButton browserForward = webModal.findViewById(R.id.browser_forward);
        browserForward.setOnClickListener(v -> webView.goForward());

        AppCompatButton browserRefresh = webModal.findViewById(R.id.browser_refresh);
        browserRefresh.setOnClickListener(v -> webView.reload());
    }

    /**
     * Access observable to subscribe to modal hidden event.
     *
     * @return Observer for modal hidden event
     */
    Observable<Boolean> modalHiddenEvent() {
        return modalHidden;
    }

    /**
     * Reveals the modal popup showing the web view.
     */
    void showForm(boolean animated) {
        if (animated) {
            Animation bottomUp = AnimationUtils.loadAnimation(context, R.anim.slide_up);
            webModal.startAnimation(bottomUp);
        }

        setupWebview();

        webModal.setVisibility(View.VISIBLE);
    }

    /**
     * Hides the modal popup showing the web view.
     */
    void hideForm() {
        destroyWebView();

        Animation slideOut = AnimationUtils.loadAnimation(context, R.anim.slide_down);
        webModal.startAnimation(slideOut);
        webModal.setVisibility(View.GONE);

        modalHidden.onNext(true);
    }

    /**
     * Sets up a new webview to ensure there is no back history between selected pictures.
     */
    private void setupWebview() {
        webView = new WebView(context);
        webFrame.addView(webView);

        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setAppCacheEnabled(false);
    }

    /**
     * Cleans up a web view that is no longer needed.
     */
    private void destroyWebView() {
        webView.destroy();
        webView = null;
    }

    /**
     * Loads a url in the web view.
     *
     * @param url The url to load
     */
    void loadURL(String url) {
        webView.loadUrl(url);
    }

    /**
     * Directs web view to go back if it can, otherwise hide the modal info window.
     */
    void goBack() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            hideForm();
        }
    }
}
