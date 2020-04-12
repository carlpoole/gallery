package codes.carl.gallery;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import codes.carl.gallery.model.Picture;
import codes.carl.gallery.utils.SortUtils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Tests for the custom quicksort method.
 *
 * @see codes.carl.gallery.utils.sort.QuickSort
 */
public class SizeSortTests {

    /**
     * Tests the quicksort on a range of typical values
     */
    @Test
    public void typical() {
        // Initial unsorted test case
        List<Picture> pics = new ArrayList<>();
        pics.add(new Picture(1,1));
        pics.add(new Picture(100,100));
        pics.add(new Picture(10000, 10000));
        pics.add(new Picture(500, 500));
        pics.add(new Picture(500, 500));
        pics.add(new Picture(0,0));
        pics.add(new Picture(300, 300));

        // Sorted test case
        List<Picture> sortedCheck = new ArrayList<>();
        sortedCheck.add(new Picture(0,0));
        sortedCheck.add(new Picture(1,1));
        sortedCheck.add(new Picture(100, 100));
        sortedCheck.add(new Picture(300, 300));
        sortedCheck.add(new Picture(500, 500));
        sortedCheck.add(new Picture(500, 500));
        sortedCheck.add(new Picture(10000, 10000));

        assertNotEquals(sortedCheck, pics);

        // Sort the unsorted test case
        SortUtils.sizeSort(pics);

        assertEquals(sortedCheck, pics);
    }

    /**
     * Tests that image sizes below zero are observed as 0
     */
    @Test
    public void negativeSizes() {
        Picture negSize = new Picture(-1, 1000);

        assertEquals(0, negSize.totalPixelsSize());
    }

    /**
     * Tests that images sizes too large don't exceed the max value of long
     */
    @Test
    public void jumboSizes() {
        Picture bigSize = new Picture(92233720900L, 99999999000000L);

        assertEquals(Long.MAX_VALUE, bigSize.totalPixelsSize());
    }
}
