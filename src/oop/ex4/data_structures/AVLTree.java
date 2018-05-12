package oop.ex4.data_structures;

import java.util.Iterator;

/**
 * An implementation of the AVL tree data structure.
 */

public class AVLTree implements Iterable<Integer> {

	private static final int AVL_H0_MIN_NODES=1;
	private static final int AVL_H1_MIN_NODES=2;


	/**
	 * The default constructor.
	 */
	public AVLTree() {

	}

	/**
	 * A copy constructor that creates a deep copy of the given AvlTree. The new tree contains all the
	 * values of the given tree, but not necessarily in the same structure.
	 *
	 * @param tree The AVL tree to be copied.
	 */
	public AVLTree(AVLTree tree) {

	}

	/**
	 * A constructor that builds a new AVL tree containing all unique values in the input array.
	 *
	 * @param data the values to add to tree.
	 */
	public AVLTree(int[] data) {

	}

	/**
	 * Add a new node with the given key to the tree.
	 *
	 * @param newValue the value of the new node to add.
	 * @return true if the value to add is not already in the tree and it was successfully added, false
	 * otherwise.
	 */
	public boolean add(int newValue) {
		return true;
	}

	/**
	 * Check whether the tree contains the given input value.
	 *
	 * @param searchVal value to search for
	 * @return if val is found in the tree, return the depth of the node (0 for the root) with the given
	 * value if it was found in the tree, -1 otherwise.
	 */
	public int contains(int searchVal) {
		return 0;
	}

	/**
	 * Removes the node with the given value from the tree, if it exists.
	 *
	 * @param toDelete the value to remove from the tree.
	 * @return true if the given value was found and deleted, false otherwise.
	 */
	public boolean delete(int toDelete) {
		return true;
	}

	/**
	 * @return the number of nodes in the tree.
	 */
	public int size() {
		return 0;
	}

	/**
	 * @return an iterator for the Avl Tree. The returned iterator iterates over the tree nodes in an
	 * ascending order, and does NOT implement the remove() method.
	 */
	@Override
	public Iterator<Integer> iterator() {
		return null;
	}

	/**
	 * Calculates the minimum number of nodes in an AVL tree of height h.
	 * @param h the height of the tree (a non-negative number) in question.
	 * @return the minimum number of nodes in an AVL tree of the given height.
	 */
	public static int findMinNodes(int h) {
		if (h==0){
			return AVL_H0_MIN_NODES;
		}
		if(h==1){
			return AVL_H1_MIN_NODES;
		}
		return 1+findMaxNodes(h-1)+findMaxNodes(h-2);
	}

	/**
	 * Calculates the maximum number of nodes in an AVL tree of height h.
	 * @param h the height of the tree (a non-negative number) in question.
	 * @return the maximum number of nodes in an AVL tree of the given height.
	 */
	public static int findMaxNodes(int h) {

		return (int)Math.pow(2,h+1)+1;
	}
}