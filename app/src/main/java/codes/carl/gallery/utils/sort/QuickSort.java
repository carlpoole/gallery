package codes.carl.gallery.utils.sort;

import java.util.Collections;
import java.util.List;
import java.util.Stack;

import codes.carl.gallery.model.Picture;

/**
 * A utilities class to perform quicksort on a list of pictures based on its area in pixels.
 */
public class QuickSort {

    /**
     * An object to store the start and end partition pointers in while quicksort operates.
     */
    static class Partition {
        int start;
        int end;

        Partition(int start, int end) {
            this.start = start;
            this.end = end;
        }
    }

    /**
     * The list to be sorted.
     */
    private static List<Picture> sortData;

    /**
     * Runs quicksort on the provided list of pictures.
     *
     * @param sortData The unsorted list.
     */
    public static void run(List<Picture> sortData) {
        QuickSort.sortData = sortData;

        // Uses a stack to avoid a stack-overflow exception with big lists
        Stack<Partition> sortStack = new Stack<>();
        sortStack.push(new Partition(0, sortData.size() - 1));

        while(!sortStack.isEmpty()) {
            Partition partition = sortStack.pop();

            if (partition.start < partition.end) {
                int partitionVal = partition(partition.start, partition.end);
                sortStack.push(new Partition(partition.start, partitionVal));
                sortStack.push(new Partition(partitionVal + 1, partition.end));
            }
        }
    }

    /**
     * Operates over the provided partition points and swaps pictures in the list based
     *  on their total area in pixels.
     *
     * @param start The beginning of the partition
     * @param end The end of the partition
     *
     * @return The new partition point
     */
    private static int partition(int start, int end) {
        long x = sortData.get(start).totalPixelsSize();
        int i = start;
        int j = end;

        while (true) {
            while (i < end && sortData.get(i).totalPixelsSize() < x)
                i++;

            while (j > start && sortData.get(j).totalPixelsSize() > x)
                j--;

            if (i < j) {
                Collections.swap(sortData, i, j);

                i++;
                j--;
            } else {
                return j;
            }
        }
    }


}
