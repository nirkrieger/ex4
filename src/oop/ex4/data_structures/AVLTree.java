package oop.ex4.data_structures;

import java.util.NoSuchElementException;
import java.util.Iterator;
import java.util.function.Consumer;

/**
 * An implementation of the AVL tree data structure.
 */

public class AvlTree implements Tree {
	private static final int VIOLATION_LEFT = 2;
	private static final int VIOLATION_RIGHT_LEFT = 1;
	private static final int VIOLATION_RIGHT = -2;
	private static final int VIOLATION_LEFT_RIGHT = -1;
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
		private static final int NO_SON_BALANCE_FACTOR = -1;
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
			int checkLeft = NO_SON_BALANCE_FACTOR, checkRight = NO_SON_BALANCE_FACTOR;
			if (hasLeftSon())
				checkLeft = leftSon.height;
			if (hasRightSon())
				checkRight = rightSon.height;
			height = (Math.max(checkLeft, checkRight) + 1);
			balanceFactor = checkLeft - checkRight;
		}
		@Override
		public String toString() {
			return "AvlNode{" +
					"value=" + value +
					", balanceFactor=" + balanceFactor +
					'}';
		}
	}

	/**
	 * The default constructor.
	 */
	public AvlTree() {
		root = null;
	}

	/**
	 * A copy constructor that creates a deep copy of the given AvlTree. The new tree contains all the
	 * values of the given tree, but not necessarily in the same structure.
	 *
	 * @param tree The AVL tree to be copied.
	 */
	public AvlTree(AvlTree tree) {
		AvlTree copyTree = new AvlTree();
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
	public AvlTree(int[] data) {
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
				currentNode.updateHeightAndBalance();
			} else {
				// go recursively to the left son.
				retValue = binarySearchOperation(value, height + 1, currentNode.leftSon, operation);
			}
			// go right.
		} else if (value > currentNode.value) {
			if (operation == Operator.ADD && !currentNode.hasRightSon()) {
				// right son is null, add the new value in its place.
				currentNode.rightSon = new AvlNode(currentNode, value);
				currentNode.updateHeightAndBalance();
			} else {
				// go recursively to the right son.
				retValue = binarySearchOperation(value, height + 1, currentNode.rightSon, operation);
			}
		}
		if (operation != Operator.CONTAINS && retValue == SUCCESS_FLAG) {
			if (currentNode != null) {
				currentNode.updateHeightAndBalance();
			}
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
			if (current.isALeftSon()) {
				return current.parent;
			} else if (current.isARightSon()) {
				AvlNode iterNode = current.parent;
				while (iterNode.isARightSon()) {
					// the successor is an ancestor, iterate ancestors.
					iterNode = iterNode.parent;
				}
				if (iterNode.parent == null) {
					return null;
				}
				return iterNode.parent;
			} else {
				return null;
			}
		}
	}

	/**
	 * Returns the predecessor of the current node.
	 *
	 * @param current current node.
	 * @return AvlNode predecessor.
	 */
	private AvlNode predecessor(AvlNode current) {
		if (current == null) {
			return null;
		}
		// if current has a right son, the min node of the right sub tree is the successor.
		if (current.hasLeftSon()) {
			current = current.leftSon;
			while (current.hasRightSon()) {
				current = current.rightSon;
			}
			return current;
			// else - go up on parents until parent is null or iterNode is parent's leftSon
		} else if (current.parent == null) {
			return null;
		} else {
			AvlNode iterNode = current.parent;
			while (iterNode.isALeftSon()) {
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
		/**
		 * 1. if current parent is null, it means current parent is the root
		 * 2. else -
		 * 		a. if a current is a left son
		 */
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
		// if is a leaf:
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
				// get predecessor, which is ultimately
//				AvlNode successor = successor(toDelete);
				//TODO: change this code to successor if needed.(!!)
				AvlNode predecessor = predecessor(toDelete);
				if (predecessor != null) {
					if (predecessor != toDelete.leftSon) {
						predecessor.parent.rightSon = predecessor.leftSon;
						predecessor.leftSon = toDelete.leftSon;
						predecessor.leftSon.parent = predecessor;
					}
					predecessor.rightSon = toDelete.rightSon;
					predecessor.rightSon.parent = predecessor;
				}
				swap(toDelete, predecessor);
				newCurrent = null;
			}
		}
		return newCurrent;
	}

	private void checkDisorder(AvlNode node) {
		if (node == null) {
			return;
		}
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
	}

	/**
	 * Rotates the subtree rooted at the given node to the right.
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
			}
			else {
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
		// in case tree is empty, add new node to root and return.
		if (root == null) {
			root = new AvlNode(null, newValue);
			numOfNodes++;
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
		return (Iterator<Integer>) new AvlTreeIterator();
	}

	private class AvlTreeIterator implements Iterator<Integer> {
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

		private boolean firstRun = true;

		/**
		 * Default constructor.
		 */
		private AvlTreeIterator() {
			currentNode = null;
			probedNode = null;
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
			AvlNode nextNode;
			if (firstRun) {
				nextNode = getMinNode();
				firstRun = false;
			} else {
				nextNode = successor(currentNode);
			}
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

		@Override
		public void forEachRemaining(Consumer<? super Integer> action) {
			while (hasNext()) {
				action.accept(next());
			}
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