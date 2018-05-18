package oop.ex4.data_structures;
///TODO: consider make as nested class in AvlTree
public class AvlNode {
	private AvlNode father;
	private AvlNode leftSon;
	private AvlNode rightSon;
	private int key;
	private int height;
	AvlNode(AvlNode father,int key){
		this.father=father;
		this.leftSon=null;
		this.rightSon=null;
		this.key=key;
		height=0;
	}

	public AvlNode getFather() {
		return father;
	}

	public AvlNode getLeftSon() {
		return leftSon;
	}

	public AvlNode getRightSon() {
		return rightSon;
	}

	public void setFather(AvlNode father) {
		this.father = father;
	}

	public void setLeftSon(AvlNode leftSon) {
		this.leftSon = leftSon;
	}

	public void setRightSon(AvlNode rightSon) {
		this.rightSon = rightSon;
	}


	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getKey() {
		return key;
	}

	public void setKey(int key) {
		this.key = key;
	}

	@Override
	public String toString() {
		return String.valueOf(key);
	}
}
