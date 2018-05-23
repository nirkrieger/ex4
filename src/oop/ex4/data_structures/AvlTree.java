package oop.ex4.data_structures;

import java.util.NoSuchElementException;
import java.util.Iterator;


/**
 * This class is the complete and tested implementation of an AVL-tree.
 *
 * @author nirkr
 * @author arnon.turetzky
 */

public class AvlTree implements Tree, Iterable<Integer> {

	// static constants:
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
	 * indicates the operation succeeded
	 */
	private static final int SUCCESS_INDICATOR = 0;
	/**
	 * indicates the operation failed
	 */
	private static final int FAIL_INDICATOR = -1;

	/**
	 * Represents a return value when value not found.
	 */
	private static final int NOT_CONTAINED_INDICATOR = -1;
	/**
	 * Represents a return value when value was found.
	 */
	private static final int CONTAINS_INDICATOR = -1;

	// Data members:

	/**
	 * Tree root.
	 */
	private AvlNode root;
	/**
	 * Number of nodes counter.
	 */
	private int numOfNodes = 0;

	/**
	 * Representing an AVL node class, as an inner class within AVL Tree.
	 */
	private class AvlNode {
		/**
		 * Represents the height of a non-existing son.
		 */
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
			this.height = 0;
		}

		/**
		 * @return true if node is a leaf, false else.
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
			if (hasLeftSon()) {
				heightLeft = leftSon.height;
			}
			if (hasRightSon()) {
				heightRight = rightSon.height;
			}
			// set the new height and balance factors.
			height = (Math.max(heightLeft, heightRight) + 1);
			balanceFactor = heightLeft - heightRight;
		}
	}

	/**
	 * A default constructor.
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
			// if a null tree is given.
			root = null;
		} else {
			// Iterate over given tree nodes.
			for(int value : tree) {
				add(value);
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
	 * prove for node with the given value and act accordingly to the given opertaion
	 *
	 * @param valToSearch the value to search
	 * @param operation   the purpose for the probing - delete/add/contain
	 * @return indicator to the results
	 */
	private int binaryOperation(int valToSearch, Operator operation) {
		int depth = 0;
		AvlNode currentNode = root;
		while (currentNode != null) {
			if (valToSearch == currentNode.value) {
				switch (operation) {
					case ADD:
						// given value already exists, do not add and return contains constant.
						return CONTAINS_INDICATOR;
					case DELETE:
						// delete the node in case of deleting.
						deleteNode(currentNode);
						return SUCCESS_INDICATOR;
					case CONTAINS:
						// node was found, return height.
						return depth;
				}
			} else if (valToSearch > currentNode.value) {
				if (currentNode.hasRightSon()) {
					depth++;
					currentNode = currentNode.rightSon;
				} else if (operation == Operator.ADD) {
					// add new node to tree.
					currentNode.rightSon = new AvlNode(currentNode, valToSearch);
					validateStructure(currentNode);
					return SUCCESS_INDICATOR;
				} else {
					// node was not found in tree
					return NOT_CONTAINED_INDICATOR;
				}
			} else if (valToSearch < currentNode.value) {
				if (currentNode.hasLeftSon()) {
					depth++;
					currentNode = currentNode.leftSon;
				} else if (operation == Operator.ADD) {
					// add new node to tree.
					currentNode.leftSon = new AvlNode(currentNode, valToSearch);
					validateStructure(currentNode);
					return SUCCESS_INDICATOR;
				} else {
					// node was not found in tree
					return NOT_CONTAINED_INDICATOR;
				}
			}
		}
		// given tree was empty, return -1 which indicates false for all using methods.
		return FAIL_INDICATOR;
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
		if (current.hasRightSon()) {
			// if current has a right son, the min node of the right sub tree is the successor.
			current = current.rightSon;
			while (current.hasLeftSon()) {
				current = current.leftSon;
			}
			return current;
		} else if (current.parent == null) {
			return null;
		} else {
			// else - go up on parents while current is a right son.
			while (current.isARightSon()) {
				// the successor is an ancestor, iterate ancestors.
				current = current.parent;
			}
			// if the root was reached while looking for successor, a successor doesn't exist.
			if (current.parent == null) {
				return null;
			}
			return current.parent;
		}
	}

	/**
	 * Swaps between nodes in tree.
	 *
	 * @param current  current node to be removed.
	 * @param replacer Node to be replacing the current node.
	 */
	private void swap(AvlNode current, AvlNode replacer) {
		if (current == null) {
			return;
		}
		if (root.equals(current)) {
			// if current is the root, set the replacer to be the root.
			root = replacer;
		} else {
			// set replacer to be left or right son of current's parent.
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
	 * @param toDelete a node to delete.
	 */
	private void deleteNode(AvlNode toDelete) {
		AvlNode OriginalParent = toDelete.parent;
		// if is a leaf.
		if (toDelete.isLeaf()) {
			// do leaf deletion.
			swap(toDelete, null);
		}
		// else - has one or more children.
		else {
			if (toDelete.hasLeftSon() && !toDelete.hasRightSon()) {
				swap(toDelete, toDelete.leftSon);
			} else if (!toDelete.hasLeftSon() && toDelete.hasRightSon()) {
				swap(toDelete, toDelete.rightSon);
			} else {
				// else, has both right and left. find successor and switch.
				AvlNode successor = successor(toDelete);
				OriginalParent = successor.parent;
				// set the current node with successor's value
				toDelete.value = successor.value;
				// delete the successor node.
				deleteNode(successor);
			}
		}
		// fix tree.
		validateStructure(OriginalParent);
	}

	/**
	 * given a node go up to tree head to the root, check on the way
	 * that the tree apply avl condition, if not repair on the go
	 *
	 * @param node - the node to begin the track up to the root
	 */
	private void validateStructure(AvlNode node) {
		// iterate over nodes from current to root.
		while (node != null) {
			node.updateHeightAndBalance();
			int currentBalance = node.balanceFactor;
			if (currentBalance == VIOLATION_LEFT) {
				if (node.leftSon.balanceFactor == VIOLATION_LEFT_RIGHT) {
					rotateLeft(node.leftSon);
				}
				rotateRight(node);
			} else if (currentBalance == VIOLATION_RIGHT) {
				if (node.rightSon.balanceFactor == VIOLATION_RIGHT_LEFT) {
					rotateRight(node.rightSon);
				}
				rotateLeft(node);
			}
			// advance iteration.
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
			// if the current left has a right son, make it the node's right son.
			node.leftSon = currentLeft.rightSon;
			node.leftSon.parent = node;
		} else {
			// else - set the node's left son to be null.
			node.leftSon = null;
		}
		// swap between node and current left.
		swap(node, currentLeft);
		// set node to be current left's right son.
		currentLeft.rightSon = node;
		node.parent = currentLeft;
		// update height and balance factors of both nodes.
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
			// if the current right has a left son, make it the node's right son.
			node.rightSon = currentRight.leftSon;
			node.rightSon.parent = node;
		} else {
			// else - set the node's right son to be null.
			node.rightSon = null;
		}
		// swap between node and current left.
		swap(node, currentRight);
		// set node to be current right's left son.
		currentRight.leftSon = node;
		node.parent = currentRight;
		// update height and balance factors of both nodes.
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
		int probeIndicator = binaryOperation(newValue, Operator.ADD);
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
		return binaryOperation(searchVal, Operator.CONTAINS);
	}

	/**
	 * Removes the node with the given value from the tree, if it exists.
	 *
	 * @param toDelete the value to remove from the tree.
	 * @return true if the given value was found and deleted, false otherwise.
	 */
	public boolean delete(int toDelete) {
		// binary search operation with Operator Delete.
		int probeIndicator = binaryOperation(toDelete, Operator.DELETE);
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
			/** Holds the next node in iteration*/
			private AvlNode lastNode;
			/** Indicates whether the last node was used. */
			private boolean wasLastNodeUsed;

			/**Default constructor. */
			private AvlTreeIterator() {
				lastNode = getMinNode();
				wasLastNodeUsed = false;
				if (lastNode == null) {
					wasLastNodeUsed = true;
				}
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
				AvlNode nextNode = successor(lastNode);
				if (nextNode == null) {
					return false;
				}
				// next node is available.
				lastNode = nextNode;
				wasLastNodeUsed = false;
				return true;
			}

			/**
			 * @return the next integer in the tree.
			 * @throws NoSuchElementException stop iteration exception.
			 */
			@Override
			public Integer next() throws NoSuchElementException {
				if (!hasNext())
					// no successor is present.
					throw new NoSuchElementException();
				//
				wasLastNodeUsed = true;
				return lastNode.value;
			}
		}
		return new AvlTreeIterator();
	}

	/**
	 * Calculates the minimum number of nodes in an AVL tree of height h.
	 *
	 * @param h the height of the tree (a non-negative number) in question.
	 * @return the minimum number of nodes in an AVL tree of the given height.
	 * @throws IllegalArgumentException when height lower than 0 is given.
	 */
	public static int findMinNodes(int h) throws IllegalArgumentException {
		//max possible h input, for int output would be h=53
		if (h < 0) {
			throw new IllegalArgumentException();
		}
		if (h == 0) {
			// a tree with one node.
			return AVL_H0_MIN_NODES;
		}
		if (h == 1) {
			// a tree with a root and only one son.
			return AVL_H1_MIN_NODES;
		}
		// assign minimal num in height 0 and height 1 to n0 and n1 respectively.
		int n0 = AVL_H0_MIN_NODES;
		int n1 = AVL_H1_MIN_NODES;
		int minNodesNum = 0;
		for (int i = 2; i <= h; i++) {
			minNodesNum = n1 + n0 + 1;
			n0 = n1;
			n1 = minNodesNum;
		}
		return minNodesNum;
	}

	/**
	 * Calculates the maximum number of nodes in an AVL tree of height h.
	 *
	 * @param h the height of the tree (a non-negative number) in question.
	 * @return the maximum number of nodes in an AVL tree of the given height.
	 * @throws IllegalArgumentException when height lower than 0 is given.
	 */
	public static int findMaxNodes(int h) throws IllegalArgumentException {
		if (h < 0) {
			throw new IllegalArgumentException();
		}
		return (int) Math.pow(2, h + 1) - 1;
	}


}