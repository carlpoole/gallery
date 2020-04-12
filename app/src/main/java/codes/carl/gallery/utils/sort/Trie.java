package codes.carl.gallery.utils.sort;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import codes.carl.gallery.model.Picture;

/**
 * A trie structure to sort the String names of picture authors alphabetically.
 */
public class Trie {

    /**
     * A node in the trie.
     */
    static class TrieNode {

        /**
         * A list to contain pictures at this node's position in the trie.
         */
        List<Picture> pictures = new ArrayList<>();

        /**
         *
         */
        TrieNode[] children = new TrieNode[26];

        /**
         * Constructs a new trie node.
         */
        TrieNode() {
        }
    }

    /**
     * The root node of the trie.
     */
    private TrieNode rootNode;

    /**
     * Constructs a new trie.
     */
    public Trie() {
        rootNode = new TrieNode();
    }

    /**
     * Inserts the picture in the trie based on its alphabetic position.
     * Strings containing all characters that are outside the range a-z will appear at the front.
     *
     * @param picture The picture being sorted into the trie
     */
    public void insert(Picture picture) {
        TrieNode cursorNode = rootNode;

        // Get author name, make lowercase
        String authorName = picture.getAuthor().toLowerCase();

        for (int i = 0; i < authorName.length(); i++) {
            char c = authorName.charAt(i);

            // Skip whitespace
            if (Character.isWhitespace(c))
                continue;

            // Skip if the character is not English alphabet
            if (c < 'a' || c > 'z') {
                continue;
            }

            int index = c - 'a';
            if (cursorNode.children[index] == null) {
                TrieNode newChild = new TrieNode();
                cursorNode.children[index] = newChild;
                cursorNode = newChild;
            } else {
                cursorNode = cursorNode.children[index];
            }
        }

        cursorNode.pictures.add(picture);
    }

    /**
     * Flattens the trie structure into a list of pictures in alphabetical order.
     *
     * @return The sorted list of pictures
     */
    public List<Picture> flatten() {
        List<Picture> pictures = new ArrayList<>();
        Stack<TrieNode> nodeStack = new Stack<>();

        // performs a DFS using a stack instead of recursive calls that risk a stack overflow
        nodeStack.push(rootNode);

        while (!nodeStack.isEmpty()) {
            TrieNode node = nodeStack.pop();

            if (!node.pictures.isEmpty())
                pictures.addAll(node.pictures);

            // iterate backwards over the children to preserve alphabetical order when popping
            for (int i = node.children.length - 1; i >= 0; i--) {
                if (node.children[i] != null) {
                    nodeStack.push(node.children[i]);
                }
            }
        }

        return pictures;
    }
}
