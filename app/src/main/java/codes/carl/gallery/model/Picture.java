package codes.carl.gallery.model;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Picture)) return false;
        Picture picture = (Picture) o;
        return width == picture.width &&
                height == picture.height &&
                id.equals(picture.id) &&
                author.equals(picture.author) &&
                url.equals(picture.url) &&
                download_url.equals(picture.download_url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, author, width, height, url, download_url);
    }
}
