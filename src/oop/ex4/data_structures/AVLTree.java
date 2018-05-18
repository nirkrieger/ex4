package oop.ex4.data_structures;

import java.util.NoSuchElementException;
import java.util.Iterator;

/**
 * An implementation of the AVL tree data structure.
 */

public class AVLTree implements Tree {
	private static final int VIOLATION_1_LEFT = 2;
	private static final int VIOLATION_1_RIGHT = -2;
	private static final int VIOLATION_2_DIFFERENT_SIDE = -1;
	private static final int AVL_H0_MIN_NODES = 1;
	private static final int AVL_H1_MIN_NODES = 2;
	/**
	 * Represents a return value when value not found.
	 */
	private static final int NOT_CONTAINED_INDICATOR = -1;
	/**
	 * Represents a return value when value was found.
	 */
	private static final int CONTAINS_INDICATOR = -1;
	/**
	 * Default root height.
	 */
	private static final int ROOT_HEIGHT = 0;
	/**
	 * Indicates success during adding or deleting.
	 */
	private static final int SUCCESS_FLAG = 0;
	//private AvlNode lastNode;//search for contained last node;
	//private boolean lastNodeRight;
	/**
	 * Tree root.
	 */
	private AvlNode root;
	/**
	 * Number of nodes counter.
	 */
	private int numOfNodes = 0;

	/**
	 * Enumerates different binary search operations.
	 */
	private enum Operator {
		ADD,
		DELETE,
		CONTAINS
	}

	/**
	 * Representing an AVL node class, as an inner class within AVL Tree.
	 */
	private class AvlNode {
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
			balanceFactor = 0;
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
		private void updateBalanceFactor() {
			int checkLeft = -1;
			int checkeRight = -1;
			if (leftSon != null)
				checkLeft = leftSon.balanceFactor;
			if (rightSon != null)
				checkeRight = rightSon.balanceFactor;
			balanceFactor = checkLeft + checkeRight;
		}
	}

	/**
	 * The default constructor.
	 */
	public AVLTree() {
		root = null;
	}

	/**
	 * A copy constructor that creates a deep copy of the given AvlTree. The new tree contains all the
	 * values of the given tree, but not necessarily in the same structure.
	 *
	 * @param tree The AVL tree to be copied.
	 */
	public AVLTree(AVLTree tree) {
		AVLTree copyTree = new AVLTree();
		Iterator myIter = tree.iterator();
		while (myIter.hasNext()) {
			copyTree.add((int) myIter.next());
		}
		this.root = copyTree.root;
	}

	/**
	 * A constructor that builds a new AVL tree containing all unique values in the input array.
	 *
	 * @param data the values to add to tree.
	 */
	public AVLTree(int[] data) {
		root = null;
		// iterate array and add to tree.
		for (int value : data) {
			add(value);
		}
	}

	/**
	 * Performs a generic binary search operation.
	 *
	 * @param value       value to be used.
	 * @param height      currentNode tree height.
	 * @param currentNode current node.
	 * @param operation   operation to be performed.
	 * @return return value according to requested operation.
	 */
	private int binarySearchOperation(int value, int height, AvlNode currentNode, Operator operation) {
		// initialize the return value.
		int retValue = SUCCESS_FLAG;
		if (currentNode == null) {
			// if node is null, the given value was not found. return not found.
			return NOT_CONTAINED_INDICATOR;
		}
		if (value == currentNode.value) {
			// if value equals node's value
			//TODO: switch to switchCase
			if (operation == Operator.ADD) {
				// given value already exists, do not add and return contains constant.
				return CONTAINS_INDICATOR;
			} else if (operation == Operator.DELETE) {
				// delete the node in case of deleting.
				currentNode = deleteNode(currentNode);
			} else if (operation == Operator.CONTAINS) {
				// node was found, return height.
				return height;
			}
			// go left.
		} else if (value < currentNode.value) {
			if (operation == Operator.ADD && !currentNode.hasLeftSon()) {
				// left son is null, add the new value in its place.
				currentNode.leftSon = new AvlNode(currentNode, value);
				currentNode.updateBalanceFactor();
			} else {
				// go recursively to the left son.
				retValue = binarySearchOperation(value, height + 1, currentNode.leftSon, operation);
			}
			// go right.
		} else if (value > currentNode.value) {
			if (operation == Operator.ADD && !currentNode.hasRightSon()) {
				// right son is null, add the new value in its place.
				currentNode.rightSon = new AvlNode(currentNode, value);
				currentNode.updateBalanceFactor();
			} else {
				// go recursively to the right son.
				retValue = binarySearchOperation(value, height + 1, currentNode.rightSon, operation);
			}
		}
		if (operation != Operator.CONTAINS && retValue == SUCCESS_FLAG) {
			checkDisorder(currentNode);
		}
		return retValue;
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
		if (current.rightSon != null) {
			current = current.rightSon;
			while (current.leftSon != null) {
				current = current.leftSon;
			}
			return current;
			// else - go up on parents until parent is null or iterNode is parent's leftSon
		} else if (current.parent == null) {
			return null;
		} else {
			AvlNode iterNode = current.parent;
			while (iterNode.parent != null && iterNode != iterNode.parent.rightSon) {
				// the successor is an ancestor, iterate ancestors.
				iterNode = iterNode.parent;
			}
			if (iterNode.parent == null) {
				return null;
			}
			return iterNode.parent;
		}
	}

	/**
	 * Swap between nodes in tree.
	 *
	 * @param current  current node to be removed.
	 * @param replacer Node to be replacing the current node.
	 */
	private void swap(AvlNode current, AvlNode replacer) {
		// TODO: maybe exception here?
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
	 */
	private AvlNode deleteNode(AvlNode toDelete) {
		AvlNode newCurrent = null;
		// if is a leaf.
		if (toDelete.isLeaf()) {
			// do leaf deletion.
			swap(toDelete, null);
			// else - has one or more children.
		} else {
			// if has only a left son
			if (toDelete.hasLeftSon() && !toDelete.hasRightSon()) {
				newCurrent = toDelete.leftSon;
				swap(toDelete, toDelete.leftSon);

				// else, if has only a right son
			} else if (toDelete.hasLeftSon() && !toDelete.hasRightSon()) {
				newCurrent = toDelete.rightSon;
				swap(toDelete, toDelete.rightSon);
				// else, has both right and left. find successor and switch.
			} else {
				// get successor.
				AvlNode successor = successor(toDelete);
				swap(toDelete, successor);
				successor.leftSon = toDelete.leftSon;
				successor.leftSon.parent = successor;
				newCurrent = successor;
			}
		}
		newCurrent.updateBalanceFactor();
		return newCurrent;
	}

	private void checkDisorder(AvlNode node) {
		int currentBalance = node.balanceFactor;
		if (currentBalance == VIOLATION_1_LEFT) {
			if (node.leftSon.balanceFactor == VIOLATION_2_DIFFERENT_SIDE) {
				rotateLeft(node.leftSon);
			}
			rotateRight(node);
		}
		if (currentBalance == VIOLATION_1_RIGHT) {
			if (node.rightSon.balanceFactor == VIOLATION_2_DIFFERENT_SIDE) {
				rotateRight(node.rightSon);
			}
			rotateLeft(node);
		}
	}

	private void rotateRight(AvlNode node) {
		AvlNode currentLeft = node.leftSon;
		node.leftSon = currentLeft.rightSon;
		currentLeft.parent = node.parent;
		if (node.parent == null)
			root = currentLeft;
		else {
			if (node == node.parent.rightSon)
				node.parent.rightSon = currentLeft;
			else
				node.parent.leftSon = currentLeft;
		}
		currentLeft.rightSon = node;
		node.parent = currentLeft;
		node.updateBalanceFactor();
		currentLeft.updateBalanceFactor();

	}

	private void rotateLeft(AvlNode node) {
		AvlNode currentRight = node.rightSon;
		node.leftSon = currentRight.leftSon;
		currentRight.parent = node.parent;
		if (node.parent == null) {
			root = currentRight;
		} else {
			if (node == node.parent.rightSon)
				node.parent.rightSon = currentRight;
			else
				node.parent.leftSon = currentRight;
		}
		currentRight.leftSon = node;
		node.parent = currentRight;
		currentRight.updateBalanceFactor();
		node.updateBalanceFactor();
	}

	/**
	 * Add a new node with the given key to the tree.
	 *
	 * @param newValue the value of the new node to add.
	 * @return true if the value to add is not already in the tree and it was successfully added, false
	 * otherwise.
	 */
	public boolean add(int newValue) {
		// in case tree is empty, add new node to root and return.
		if (root == null) {
			root = new AvlNode(null, newValue);
			return true;
		}
		// binary search operation with Operator add.
		int retValue = binarySearchOperation(newValue, ROOT_HEIGHT, root, Operator.ADD);
		// if the return value indicates the tree contains the value, return false.
		if (retValue == CONTAINS_INDICATOR) {
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
		return binarySearchOperation(searchVal, ROOT_HEIGHT, root, Operator.CONTAINS);
	}

	/**
	 * Removes the node with the given value from the tree, if it exists.
	 *
	 * @param toDelete the value to remove from the tree.
	 * @return true if the given value was found and deleted, false otherwise.
	 */
	public boolean delete(int toDelete) {
		// binary search operation with Operator add.
		int retValue = binarySearchOperation(toDelete, ROOT_HEIGHT, root, Operator.DELETE);
		// if not contains, return false.
		if (retValue == NOT_CONTAINED_INDICATOR) {
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
		return new AVLTreeIterator();
	}

	private class AVLTreeIterator implements Iterator<Integer> {
		/**
		 * Holds the current node in the iteration.
		 */
		private AvlNode currentNode;
		/**
		 * Holds the next node.
		 */
		private AvlNode probedNode;
		/**
		 * Indicates whether the probed node was used.
		 */
		private boolean wasLastNodeUsed = true;

		/**
		 * Default constructor.
		 */
		private AVLTreeIterator() {
			currentNode = getMinNode();
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
		public Integer next() throws NoSuchElementException{
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

		return (int) Math.pow(2, h + 1) + 1;
	}

}