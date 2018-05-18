package oop.ex4.data_structures;

public class helper {
	private static final int VIOLATION_1_LEFT = 2;
	private static final int VIOLATION_1_RIGHT = -2;
	private static final int VIOLATION_2_DIFFERENT_SIDE = -1;
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
