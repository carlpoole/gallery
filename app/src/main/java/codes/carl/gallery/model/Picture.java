package codes.carl.gallery.model;

import org.parceler.Parcel;

import java.math.BigInteger;
import java.util.Objects;

/**
 * Models a picture retrieved from Lorem Picsum
 */
@Parcel
public class Picture {

    /**
     * The picture id.
     */
    String id;

    /**
     * The picture author.
     */
    String author;

    /**
     * The width of the picture in pixels.
     */
    long width;

    /**
     * The height of the picture in pixels.
     */
    long height;

    /**
     * The web page url with more information about the picture.
     */
    String url;

    /**
     * The direct download url of the picture.
     */
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
     * @param width  The width of the blank picture
     * @param height The height of the blank picture
     */
    public Picture(long width, long height) {
        this.width = width;
        this.height = height;
    }

    /**
     * Gets the picture id.
     *
     * @return The picture id
     */
    public String getId() {
        return id;
    }

    /**
     * Gets the author of the picture.
     *
     * @return The picture's author
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Gets the width of the picture in pixels.
     *
     * @return The picture's width
     */
    public long getWidth() {
        return width;
    }

    /**
     * Gets the height of the picture in pixels.
     *
     * @return The picture's height
     */
    public long getHeight() {
        return height;
    }

    /**
     * Gets the web info url for the picture.
     *
     * @return The web info url for the picture
     */
    public String getUrl() {
        return url;
    }

    /**
     * Gets the direct download url for the picture.
     *
     * @return The download url for the picture
     */
    public String getDownload_url() {
        return download_url;
    }

    /**
     * Gets the total pixel area of the picture: height * width
     *
     * @return The area of the picture in pixels
     */
    public long totalPixelsSize() {
        BigInteger area = BigInteger.valueOf(getHeight()).multiply(BigInteger.valueOf(getWidth()));

        if (area.compareTo(BigInteger.valueOf(Long.MAX_VALUE)) > 0) {
            return Long.MAX_VALUE;
        } else if (area.compareTo(BigInteger.ZERO) < 0) {
            return 0;
        }

        return area.longValue();
    }

    /**
     * Determines the equivalence of two different Picture objects.
     *
     * @param o An object to compare this picture to
     * @return whether the picture is equal to the provided object
     */
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

    /**
     * Provides a hash code for the Picture.
     *
     * @return A hash code for the picture
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, author, width, height, url, download_url);
    }
}
