package oop.ex4.data_structures;
import org.junit.Test;
import sun.util.locale.provider.AvailableLanguageTags;

import java.util.Iterator;

import static org.junit.Assert.*;

public class TreeTester {
    public static void main(String[] args) {
        AvlTree tree = new AvlTree();
        int treeSize = 20;
        for (int i = 0; i < treeSize; i++) {
            assertTrue("Problem while adding number " + i, tree.add(i));
        }
        for (int i = 0; i < treeSize; i++) {
            assertTrue("Problem in index " + i, tree.contains(i) != -1);
        }

        for (int i = 0; i < treeSize; i++) {
            assertTrue("Problem while adding already existent number " + i, !tree.add(i));
        }

        System.out.println("Tree built successfully.");

        /* Checks the Iterator */
        int index = 0;
        for (int value: tree) {
            assertTrue("Iterator should've returned " + index + ", got " + value, value == index);
            index++;
        }
        assertTrue("Iterator should've stopped at " + treeSize + ", instead it got to " + index, index == treeSize);
        System.out.println("Iterator finished successfully.");

        /* Checks the copy constructor */
        AvlTree tree2 = new AvlTree(tree);
        assertTrue("Copy failed - Bad size", tree.size() == tree2.size());
        for (int i = 0; i < treeSize; i++) {
            assertTrue("Problem in copy, at index " + i, tree2.contains(i) != -1);
        }
        for (int i = 0; i < treeSize; i++) {
            assertTrue("Problem while deleting existent number " + i, tree2.delete(i));
        }

        for (int i = 0; i < treeSize; i++) {
            assertTrue("Problem while deleting already deleted number " + i, !tree2.delete(i));
            assertTrue("Original tree should still have value, but did not: " + i, tree.contains(i) != -1);
        }
        assertTrue("Bad sizes after deletion", tree.size() == treeSize && tree2.size() == 0);
        System.out.println("Elements deleted successfully");
        System.out.println("Tree copied successfully");
    }
}
