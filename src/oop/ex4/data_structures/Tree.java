package oop.ex4.data_structures;

import java.util.Iterator;

/**
 * This interface represent a Tree data structure
 */
public interface Tree{


	/**
	 * given a value try to add a node with a key of that value
	 * @param newValue - the value to add
	 * @return true if the node was added otherwise false
	 */
	boolean add(int newValue);

	/**
	 * try to delete the node with the given value
	 * @param toDelete the value to delete
	 * @return true if a node with the given value was deleted, otherwise false
	 */
	boolean delete(int toDelete);

	/**
	 * return the number of nodes in the tree
	 * @return number of nodes in the tree
	 */
	int size();

	/**
	 * given a value check if there's a node with a key equal to the value
	 * @param searchVal the value to search
	 * @return true if there's a node with a key equal to the value, otherwise false
	 */
	int contains(int searchVal);

	/**
	 * return an iterator that iterate over the tree nodes
	 * @return iterator that iterate over the tree nodes
	 */
	Iterator<Integer> iterator();







}
