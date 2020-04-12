package codes.carl.gallery.utils;

import java.util.List;

import codes.carl.gallery.model.Picture;
import codes.carl.gallery.utils.sort.Trie;

/**
 * A utilities class containing helper methods for sorting the pictures in the gallery.
 */
public class SortUtils {

    /**
     * Sorts a list of picture objects alphabetically based on the
     * English characters of the author's name.
     *
     * @param pictures A list of pictures to sort
     * @return A sorted list of pictures based on the author names
     */
    public static List<Picture> alphaSort(List<Picture> pictures) {
        Trie trieSort = new Trie();

        for (Picture pic : pictures) {
            trieSort.insert(pic);
        }

        return trieSort.flatten();
    }

    /**
     * Sorts a list of picture objects based on the size of the pictures dimensions.
     *
     * @param pictures A sorted list of pictures based on their dimensions
     */
    public static void sizeSort(List<Picture> pictures) {

    }
}
