package oop.ex4.data_structures;

public class helper {
	public AVLTree(AVLTree tree) {
		AVLTree copyTree = new AVLTree();
		Iterator myIter=tree.iterator();
		while (myIter.hasNext()){
			copyTree.add((int)myIter.next());
		}
		this.root=copyTree.root;
	}
	private class AVLTreeIterator implements Iterator<Integer> {

		private AvlNode currentNode;
		private AvlNode lastNode;

		private AVLTreeIterator() {
			currentNode = root;
		}
		/**
		 * @return Returns true if current node has a son.
		 */
		@Override
		public boolean hasNext() {
			AvlNode nextNode=successor(currentNode);
			if(nextNode!=null){
				lastNode=nextNode;
				return true;}
			return false;
		}
		@Override
		public Integer next() {
			if (successor(currentNode)==null)
				throw new NoSuchElementException();
			currentNode=lastNode;
			return lastNode.value;
		}
	}
}
