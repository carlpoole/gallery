package codes.carl.gallery;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import codes.carl.gallery.model.Picture;
import codes.carl.gallery.utils.SortUtils;

import static org.junit.Assert.*;

/**
 * Tests for the custom Alphabetical sort method.
 *
 * @see codes.carl.gallery.utils.SortUtils
 */
public class AlphaSortTests {

    /**
     * Tests expected alphabetical sort behavior.
     */
    @Test
    public void typical() {
        // Initial unsorted test case
        List<Picture> pics = new ArrayList<>();
        pics.add(new Picture("Paul"));
        pics.add(new Picture("Carl"));
        pics.add(new Picture("Andrew"));
        pics.add(new Picture("Zoom"));
        pics.add(new Picture("Carl"));

        // Sorted test case
        List<Picture> sortedCheck = new ArrayList<>();
        sortedCheck.add(new Picture("Andrew"));
        sortedCheck.add(new Picture("Carl"));
        sortedCheck.add(new Picture("Carl"));
        sortedCheck.add(new Picture("Paul"));
        sortedCheck.add(new Picture("Zoom"));

        assertNotEquals(sortedCheck, pics);

        // Sort the unsorted test case
        List<Picture> sortedPics = SortUtils.alphaSort(pics);

        assertEquals(sortedCheck, sortedPics);
    }

    /**
     * Tests expected behavior if the list is empty.
     *
     * Should not break.
     */
    @Test
    public void emptyList() {
        List<Picture> pics = new ArrayList<>();
        pics = SortUtils.alphaSort(pics);

        assertTrue(pics.isEmpty());
    }

    /**
     * Tests expected behavior when the string contains some or all illegal characters.
     *
     * Should sort based on any available legal English characters.
     */
    @Test
    public void illegalCharacters() {
        // Initial unsorted test case
        List<Picture> pics = new ArrayList<>();
        pics.add(new Picture("д Paul дд"));
        pics.add(new Picture("      "));
        pics.add(new Picture("Zoom"));
        pics.add(new Picture("999_Carl!"));

        // Sorted test case
        List<Picture> sortedCheck = new ArrayList<>();
        sortedCheck.add(new Picture("      "));
        sortedCheck.add(new Picture("999_Carl!"));
        sortedCheck.add(new Picture("д Paul дд"));
        sortedCheck.add(new Picture("Zoom"));

        // Sort the unsorted test case
        List<Picture> sortedPics = SortUtils.alphaSort(pics);

        assertEquals(sortedCheck, sortedPics);
    }
}