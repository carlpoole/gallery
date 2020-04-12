package codes.carl.gallery.model;

import java.math.BigInteger;
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

    /**
     * Creates a new empty picture with just an author name.
     *
     * @param author The author of the blank picture
     */
    public Picture(String author) {
        this.author = author;
    }

    /**
     * Creates a new empty picture with just a "canvas" size.
     *
     * @param width The width of the blank picture
     * @param height The height of the blank picture
     */
    public Picture(long width, long height) {
        this.width = width;
        this.height = height;
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

    public long totalPixelsSize() {
        BigInteger area = BigInteger.valueOf(height).multiply(BigInteger.valueOf(width));

        if(area.compareTo(BigInteger.valueOf(Long.MAX_VALUE)) > 0) {
            return Long.MAX_VALUE;
        } else if (area.compareTo(BigInteger.ZERO) < 0) {
            return 0;
        }

        return area.longValue();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Picture)) return false;
        Picture picture = (Picture) o;
        return width == picture.width &&
                height == picture.height &&
                Objects.equals(id, picture.id) &&
                Objects.equals(author, picture.author) &&
                Objects.equals(url, picture.url) &&
                Objects.equals(download_url, picture.download_url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, author, width, height, url, download_url);
    }
}
