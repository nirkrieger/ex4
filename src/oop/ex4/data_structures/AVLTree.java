package oop.ex4.data_structures;

import java.util.Iterator;

/**
 * An implementation of the AVL tree data structure.
 */

public class AVLTree implements Tree {
	private static final int AVL_H0_MIN_NODES=1;
	private static final int AVL_H1_MIN_NODES=2;
	private static final int NOT_CONTAINED_INDICATOR =-1;
	private AvlNode lastNode;//search for contained last node;
	private boolean lastNodeRight;
	private AvlNode root;
	private int numOfNodes=0;

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
		 * @param parent parent of node.
		 * @param value value of node
		 */
		private AvlNode(AvlNode parent, int value){
			this.parent = parent;
			this.leftSon=null;
			this.rightSon=null;
			this.value=value;
			balanceFactor=0;
		}

		/**
		 * Updates node's balance factor.
		 */
		private void updateBalanceFactor() {
			return;
		}
	}



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
	 * Calculates the Inorder traversal of this tree.
	 * @return A Array-List of the tree in inorder traversal.

	 */
	protected final java.util.ArrayList<AvlNode> inorder(){
		return null;
	}

	/**
	 * Calculates the minimum number of nodes in an AVL tree of height h.
	 * @param h the height of the tree (a non-negative number) in question.
	 * @return the minimum number of nodes in an AVL tree of the given height.
	 */
	public static int findMinNodes(int h) {
		//max possible h input, for int output would be h=53
		if (h<0){
			throw new IllegalArgumentException();
		}
		if (h==0){
			return AVL_H0_MIN_NODES;
		}
		if (h==1){
			return AVL_H1_MIN_NODES;}
		int n0=AVL_H0_MIN_NODES;
		int n1=AVL_H1_MIN_NODES;
		int n2=0;
		for (int i=2;i<=h;i++){
			n2=n1+n0+1;
			n0=n1;
			n1=n2;
		}
		return n2;
	}

	/**
	 * Calculates the maximum number of nodes in an AVL tree of height h.
	 * @param h the height of the tree (a non-negative number) in question.
	 * @return the maximum number of nodes in an AVL tree of the given height.
	 */
	public static int findMaxNodes(int h) {

		return (int)Math.pow(2,h+1)+1;
	}


	private void checkDisorder(){

	}


	/**
	 *
	public boolean add(int newValue) {
		if (root==null){
			root= new AvlNode(null, newValue);
			numOfNodes++;
			return true;
		}
		if (contains(newValue)!=NOT_CONTAINED_INDICATOR)
			return false;
		if (lastNodeRight){lastNode.setRightSon(new AvlNode(lastNode,newValue));}
		if (!lastNodeRight){lastNode.setLeftSon(new AvlNode(lastNode,newValue));}
		numOfNodes++;
		checkDisorder();



		return true;
	}


	public int contains(int searchVal) {
		if (root==null)
			return NOT_CONTAINED_INDICATOR;
		int heightCounter=0;
		AvlNode currentNode=root;
		while (true){
			if (currentNode.getKey()==searchVal)
				return heightCounter;
			if (searchVal>currentNode.getKey()){
				if(currentNode.getRightSon()==null){
					lastNode = currentNode;
					lastNodeRight =true;
					return NOT_CONTAINED_INDICATOR;}
				heightCounter++;
				currentNode=currentNode.getRightSon();
			}
			if (currentNode.getLeftSon() == null) {
				lastNode=currentNode;
				lastNodeRight =false;
				return NOT_CONTAINED_INDICATOR;}
			heightCounter++;
			currentNode=currentNode.getLeftSon();
		}
	}
	 *
	 *
	 *
	 */
}