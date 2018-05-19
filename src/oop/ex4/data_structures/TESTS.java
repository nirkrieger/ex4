package oop.ex4.data_structures;

public class TESTS {

	public static void main(String args[]){
		TESTS test= new TESTS();
		AvlTree myAvl=new AvlTree(new int[]{1,2,3,4,5,6,7,8,9,10});
		System.out.println("Contains 6: " + myAvl.contains(6));
		System.out.println("Contains 10: " + myAvl.contains(10));
		System.out.println("Contains 16: " + myAvl.contains(16));

		myAvl.delete(1);
		myAvl.delete(6);
		myAvl.delete(4);
		(new AvlTree()).delete(10);


		System.out.println(myAvl.findMinNodes(3)==7);
		System.out.println(myAvl.findMinNodes(5)==20);
		System.out.println(myAvl.findMinNodes(8)==88);





	}
}


