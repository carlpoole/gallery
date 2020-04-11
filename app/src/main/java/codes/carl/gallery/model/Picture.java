package codes.carl.gallery.model;

/**
 * Models a picture retrieved from Lorem Picsum
 */
public class Picture {

    String id;
    String author;
    long width;
    long height;
    String url;
    String download_url;

    /**
     * Empty constructor for Gson
     */
    public Picture() {
    }

    public String getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public long getWidth() {
        return width;
    }

    public long getHeight() {
        return height;
    }

    public String getUrl() {
        return url;
    }

    public String getDownload_url() {
        return download_url;
    }
}
