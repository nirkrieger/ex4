package oop.ex4.data_structures;

//import java.util.Collection;

import java.util.NoSuchElementException;
import java.util.Iterator;
//import java.util.function.Consumer;

/**
 * An implementation of the AVL tree data structure.
 */

public class AvlTree implements Tree, Iterable<Integer> {
	/**
	 * violation in the left subtree
	 */
	private static final int VIOLATION_LEFT = 2;
	/**
	 * violation in the right son-left subtree
	 */
	private static final int VIOLATION_RIGHT_LEFT = 1;
	/**
	 * violation in the right subtree
	 */
	private static final int VIOLATION_RIGHT = -2;
	/**
	 * violation in the left son-right subtree
	 */
	private static final int VIOLATION_LEFT_RIGHT = -1;
	/**
	 * number of minimum nodes in a tree root height 0
	 */
	private static final int AVL_H0_MIN_NODES = 1;
	/**
	 * number of minimum nodes in a tree root height 1
	 */
	private static final int AVL_H1_MIN_NODES = 2;
	/**
	 * indicate the operation was succeed
	 */
	private static final int SUCCESS_INDICATOR = 1;

	/**
	 * Tree root.
	 */
	private AvlNode root;
	/**
	 * Number of nodes counter.
	 */
	private int numOfNodes = 0;
	/**
	 * Represents a return value when value not found.
	 */
	private static final int NOT_CONTAINED_INDICATOR = -1;
	/**
	 * Represents a return value when value was found.
	 */
	private static final int CONTAINS_INDICATOR = -1;


	/**
	 * The default constructor.
	 */
	public AvlTree() {
		numOfNodes = 0;
		root = null;
	}

	/**
	 * A copy constructor that creates a deep copy of the given AvlTree. The new tree contains all the
	 * values of the given tree, but not necessarily in the same structure.
	 *
	 * @param tree The AVL tree to be copied.
	 */
	public AvlTree(AvlTree tree) {
		numOfNodes = 0;
		if (tree == null) {
			root = null;
		} else {
			Iterator nodesIterator = tree.iterator();
			while (nodesIterator.hasNext()) {
				add((int) nodesIterator.next());
			}
		}
	}

	/**
	 * A constructor that builds a new AVL tree containing all unique values in the input array.
	 *
	 * @param data the values to add to tree.
	 */
	public AvlTree(int[] data) {
		root = null;
		// iterate array and add to tree.
		if (data != null) {
			numOfNodes = 0;
			for (int value : data) {
				add(value);
			}
		}
	}

	/**
	 * Enumerates different binary search operations.
	 */
	private enum Operator {
		ADD,
		DELETE,
		CONTAINS
	}

	/**
	 *  prove for node with the given value and act accordingly to the given opertaion
	 * @param valToSearch the value to search
	 * @param operation the purpose for the probing - delete/add/contain
	 * @return indicator to the results
	 */
	private int probe(int valToSearch, Operator operation) {
		int depth = 0;
		AvlNode currentNode = root;
		if (root == null) {
			return NOT_CONTAINED_INDICATOR;
		}
		while (true) {
			if (valToSearch == currentNode.value) {
				if (operation == Operator.ADD) {
					// given value already exists, do not add and return contains constant.
					return CONTAINS_INDICATOR;
				} else if (operation == Operator.DELETE) {
					// delete the node in case of deleting.
					deleteNode(currentNode);
					return SUCCESS_INDICATOR;
				} else if (operation == Operator.CONTAINS) {
					// node was found, return height.
					return depth;
				}
			} else if (valToSearch > currentNode.value) {
				if (currentNode.hasRightSon()) {
					depth++;
					currentNode = currentNode.rightSon;
				} else {
					if (operation == Operator.ADD) {
						currentNode.rightSon = new AvlNode(currentNode, valToSearch);
						validateStructure(currentNode);
						return SUCCESS_INDICATOR;
					} else {
						return NOT_CONTAINED_INDICATOR;
					}
				}
			} else if (valToSearch < currentNode.value) {
				if (currentNode.hasLeftSon()) {
					depth++;
					currentNode = currentNode.leftSon;
				} else {
					if (operation == Operator.ADD) {
						currentNode.leftSon = new AvlNode(currentNode, valToSearch);
						validateStructure(currentNode);
						return SUCCESS_INDICATOR;
					} else {
						return NOT_CONTAINED_INDICATOR;
					}
				}
			}
		}
	}

	/**
	 * Returns the successor of the current node.
	 *
	 * @param current current node.
	 * @return AvlNode successor.
	 */
	private AvlNode successor(AvlNode current) {
		if (current == null) {
			return null;
		}
		// if current has a right son, the min node of the right sub tree is the successor.
		if (current.hasRightSon()) {
			current = current.rightSon;
			while (current.hasLeftSon()) {
				current = current.leftSon;
			}
			return current;
			// else - go up on parents until parent is null or iterNode is parent's leftSon
		} else if (current.parent == null) {
			return null;
		} else {
			while (current.isARightSon()) {
				// the successor is an ancestor, iterate ancestors.
				current = current.parent;
			}
			if (current.parent == null) {
				return null;
			}
			return current.parent;
		}
	}

	/**
	 * Swap between nodes in tree.
	 *
	 * @param current  current node to be removed.
	 * @param replacer Node to be replacing the current node.
	 */
	private void swap(AvlNode current, AvlNode replacer) {
		if (current == null) {
			return;
		}
		if (current.parent == null) {
			root = replacer;
		} else {
			if (current.isALeftSon()) {
				current.parent.leftSon = replacer;
			} else {
				current.parent.rightSon = replacer;
			}
		}
		if (replacer != null) {
			replacer.parent = current.parent;
		}
	}

	/**
	 * Delete a given node from tree and fix tree.
	 *
	 * @param toDelete
	 * @return The node that will takes the given node to delete, if there's such, otherwise return Null
	 */
	private void deleteNode(AvlNode toDelete) {
		AvlNode newCurrent = null;
		// if is a leaf.
		if (toDelete.isLeaf()) {
			// do leaf deletion.
			swap(toDelete, newCurrent);
			AvlNode OriginalParent = toDelete.parent;
			validateStructure(OriginalParent);
		}
		// else - has one or more children.
		else {
			// if has only a left son
			if (toDelete.hasLeftSon() && !toDelete.hasRightSon()) {
				AvlNode OriginalParent = toDelete.parent;
				newCurrent = toDelete.leftSon;
				swap(toDelete, newCurrent);
				validateStructure(OriginalParent);

				// else, if has only a right son
			} else if (!toDelete.hasLeftSon() && toDelete.hasRightSon()) {
				AvlNode OriginalParent = toDelete.parent;
				newCurrent = toDelete.rightSon;
				swap(toDelete, newCurrent);
				validateStructure(OriginalParent);

				// else, has both right and left. find successor and switch.
			} else {
				AvlNode successor = successor(toDelete);
				AvlNode OriginalParent = successor.parent;
				toDelete.value = successor.value;
				deleteNode(successor);
				validateStructure(OriginalParent);
			}
		}
	}

	/**
	 * given a node go up to tree head to the root, check on the way
	 * that the tree apply avl condition, if not repair on the go
	 *
	 * @param node - the node to begin the track up to the root
	 */
	private void validateStructure(AvlNode node) {
		while (node != null) {
			node.updateHeightAndBalance();
			int currentBalance = node.balanceFactor;
			if (currentBalance == VIOLATION_LEFT) {
				if (node.leftSon.balanceFactor == VIOLATION_LEFT_RIGHT) {
					rotateLeft(node.leftSon);
				}
				rotateRight(node);
			}
			if (currentBalance == VIOLATION_RIGHT) {
				if (node.rightSon.balanceFactor == VIOLATION_RIGHT_LEFT) {
					rotateRight(node.rightSon);
				}
				rotateLeft(node);
			}
//			node.updateHeightAndBalance();
			node = node.parent;
		}
	}

	/**
	 * Rotates the subtree rooted at the given node to the right.
	 *
	 * @param node node to rotate.
	 */
	private void rotateRight(AvlNode node) {
		AvlNode currentLeft = node.leftSon;
		if (currentLeft.hasRightSon()) {
			node.leftSon = currentLeft.rightSon;
			node.leftSon.parent = node;
		} else {
			node.leftSon = null;
		}
		currentLeft.parent = node.parent;
		if (node.parent == null) {
			root = currentLeft;
		} else {
			if (node.isARightSon()) {
				node.parent.rightSon = currentLeft;
			} else {
				node.parent.leftSon = currentLeft;
			}
		}
		currentLeft.rightSon = node;
		node.parent = currentLeft;
		node.updateHeightAndBalance();
		currentLeft.updateHeightAndBalance();
	}

	/**
	 * Rotates the subtree rooted at the given node to the left.
	 *
	 * @param node node to rotate.
	 */
	private void rotateLeft(AvlNode node) {
		AvlNode currentRight = node.rightSon;
		if (currentRight.hasLeftSon()) {
			node.rightSon = currentRight.leftSon;
			node.rightSon.parent = node;
		} else {
			node.rightSon = null;
		}
		currentRight.parent = node.parent;
		if (node.parent == null) {
			root = currentRight;
		} else {
			if (node.isARightSon()) {
				node.parent.rightSon = currentRight;
			} else {
				node.parent.leftSon = currentRight;
			}
		}
		currentRight.leftSon = node;
		node.parent = currentRight;
		node.updateHeightAndBalance();
		currentRight.updateHeightAndBalance();
	}

	/**
	 * Add a new node with the given key to the tree.
	 *
	 * @param newValue the value of the new node to add.
	 * @return true if the value to add is not already in the tree and it was successfully added, false
	 * otherwise.
	 */
	public boolean add(int newValue) {
		// in case tree is empty, add new node to be root and return.
		if (root == null) {
			root = new AvlNode(null, newValue);
			numOfNodes++;
			return true;
		}
		// binary search operation with Operator add.
		int probeIndicator = probe(newValue, Operator.ADD);
		// if the return value indicates the tree contains the value, return false.
		if (probeIndicator == CONTAINS_INDICATOR) {
			return false;
		}
		numOfNodes++;
		// adding was successful, return true.
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
		return probe(searchVal, Operator.CONTAINS);
	}

	/**
	 * Removes the node with the given value from the tree, if it exists.
	 *
	 * @param toDelete the value to remove from the tree.
	 * @return true if the given value was found and deleted, false otherwise.
	 */
	public boolean delete(int toDelete) {
		// binary search operation with Operator Delete.
		int probeIndicator = probe(toDelete, Operator.DELETE);
		// if not contains, return false.
		if (probeIndicator == NOT_CONTAINED_INDICATOR) {
			return false;
		}
		// deleting was successful, return true.
		numOfNodes--;
		return true;
	}

	/**
	 * @return the number of nodes in the tree.
	 */
	public int size() {
		return numOfNodes;
	}

	/**
	 * @return the minimal node in the tree.
	 */
	private AvlNode getMinNode() {
		AvlNode current = root;
		if (current == null) {
			return null;
		}
		// go left until the most left node is reached.
		while (current.hasLeftSon()) {
			current = current.leftSon;
		}
		return current;
	}

	/**
	 * @return an iterator for the Avl Tree. The returned iterator iterates over the tree nodes in an
	 * ascending order, and does NOT implement the remove() method.
	 */
	@Override
	public Iterator<Integer> iterator() {
		class AvlTreeIterator implements Iterator<Integer> {
			/** Holds the current node in the iteration.*/
			private AvlNode currentNode;
			/**Holds the next node.*/
			private AvlNode probedNode;
			/** Indicates whether the probed node was used. */
			private boolean wasLastNodeUsed;

			/**Default constructor. */
			private AvlTreeIterator() {
				currentNode = getMinNode();
				probedNode = currentNode;
				if (currentNode == null)
					wasLastNodeUsed = true;
				else wasLastNodeUsed = false;
			}

			/**
			 * @return Returns true if current node has a son.
			 */
			@Override
			public boolean hasNext() {
				// was last node used?
				if (!wasLastNodeUsed) {
					return true;
				}
				// get the node's successor.
				AvlNode nextNode = successor(currentNode);
				if (nextNode == null) {
					return false;
				}
				// next node is available.
				probedNode = nextNode;
				wasLastNodeUsed = false;
				return true;
			}

			/**
			 * @return the next integer in the tree.
			 */
			@Override
			public Integer next() {//throws NoSuchElementException{
				if (!hasNext())
					// no successor is present.
					// TODO: Check if correct exception.
					throw new NoSuchElementException();
				//
				wasLastNodeUsed = true;
				currentNode = probedNode;
				return currentNode.value;
			}
		}
		return new AvlTreeIterator();
	}

	/**
	 * Calculates the minimum number of nodes in an AVL tree of height h.
	 *
	 * @param h the height of the tree (a non-negative number) in question.
	 * @return the minimum number of nodes in an AVL tree of the given height.
	 */
	public static int findMinNodes(int h) {
		//max possible h input, for int output would be h=53
		if (h < 0) {
			throw new IllegalArgumentException();
		}
		if (h == 0) {
			return AVL_H0_MIN_NODES;
		}
		if (h == 1) {
			return AVL_H1_MIN_NODES;
		}
		int n0 = AVL_H0_MIN_NODES;
		int n1 = AVL_H1_MIN_NODES;
		int n2 = 0;
		for (int i = 2; i <= h; i++) {
			n2 = n1 + n0 + 1;
			n0 = n1;
			n1 = n2;
		}
		return n2;
	}

	/**
	 * Calculates the maximum number of nodes in an AVL tree of height h.
	 *
	 * @param h the height of the tree (a non-negative number) in question.
	 * @return the maximum number of nodes in an AVL tree of the given height.
	 */
	public static int findMaxNodes(int h) {
		if (h < 0) {
			throw new IllegalArgumentException();
		}
		return (int) Math.pow(2, h + 1) - 1;
	}

	/**
	 * Representing an AVL node class, as an inner class within AVL Tree.
	 */
	private class AvlNode {
		private static final int NO_SON_HEIGHT = -1;
		/**
		 * Parent of node.
		 */
		private AvlNode parent;
		/**
		 * Left son of node.
		 */
		private AvlNode leftSon;
		/**
		 * Right son of node.
		 */
		private AvlNode rightSon;
		/**
		 * Node's value.
		 */
		private int value;
		/**
		 * Node's balance factor.
		 */
		private int height;
		/**
		 * Node's balance factor.
		 */
		private int balanceFactor;

		/**
		 * Private constructor.
		 *
		 * @param parent parent of node.
		 * @param value  value of node
		 */
		private AvlNode(AvlNode parent, int value) {
			this.parent = parent;
			this.leftSon = null;
			this.rightSon = null;
			this.value = value;
			height = 0;
		}

		/**
		 * @return true if node is a leaft, false else.
		 */
		private boolean isLeaf() {
			return (leftSon == null && rightSon == null);
		}

		/**
		 * @return true if has a left son, false otherwise.
		 */
		private boolean hasLeftSon() {
			return leftSon != null;
		}

		/**
		 * @return true if has a right son, false otherwise.
		 */
		private boolean hasRightSon() {
			return rightSon != null;
		}

		/**
		 * @return true if this node is a left son to its parent.
		 */
		private boolean isALeftSon() {
			return (parent != null && parent.leftSon == this);
		}

		/**
		 * @return true if this node is a right son to its parent.
		 */
		private boolean isARightSon() {
			return (parent != null && parent.rightSon == this);
		}

		/**
		 * Updates node's balance factor.
		 */
		private void updateHeightAndBalance() {
			int heightLeft = NO_SON_HEIGHT, heightRight = NO_SON_HEIGHT;
			if (hasLeftSon())
				heightLeft = leftSon.height;
			if (hasRightSon())
				heightRight = rightSon.height;
			height = (Math.max(heightLeft, heightRight) + 1);
			balanceFactor = heightLeft - heightRight;
		}
	}

}