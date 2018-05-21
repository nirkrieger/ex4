package oop.ex4.data_structures;
import java.lang.reflect.Array;
import java.util.*;

public class TESTS {
	static boolean checkEq(AvlTree myAvl2, SortedSet checker){
		Iterator cItr=checker.iterator();
		String t="";
		for (Integer i : myAvl2) {
			while (cItr.hasNext()) {
				t = cItr.next().toString();
				if (t.equals(i.toString()))
					return true;
			}
			return false;
			}
	return false;
	}

	public static void main(String args[]){
		TESTS test= new TESTS();
		AvlTree myAvl=new AvlTree(new int[]{1,2,3,4,5,6,7,8,9,10});
		AvlTree myAvl2=new AvlTree(new int[]{16,8,30,6,9,20,40,7,18,32});
		SortedSet<Integer> checker = new TreeSet<Integer>();
		checker.addAll(Arrays.asList(new Integer[]{16, 8, 30, 6, 9, 20, 40, 7, 18, 32}));

		System.out.println("check1:"+checker.contains(30));
		System.out.println("check2:"+checker.contains(100));
		System.out.println("check3:"+checker.contains(16));
		checker.remove(16);
		System.out.println("check4:"+checker.contains(16));
		System.out.println("AVL Contains 6: " + myAvl2.contains(30));
		System.out.println("AVL Contains 10: " + myAvl2.contains(10));
		System.out.println("AVL Contains 16: " + myAvl2.contains(16));
//		myAvl2.delete(16);
		System.out.println("AVL Contains 16: " + myAvl2.contains(16));
		System.out.println("_______begin__test_#1_______");

//		Integer[] lst=new Integer[]{16, 8, 30, 6, 9, 20, 40, 7, 18, 32};
//		for(int q:lst) {
//			System.out.println("cur="+q);
//			myAvl2.delete(q);
//////			checker.remove(q);
//////			if (!checkEq(myAvl2, checker))
////			System.out.println("failed with" + q);
//////			checker.add(q);
//			myAvl2.add(q);
//		}

		myAvl2.delete(16);
		int h=myAvl2.contains(40);
		myAvl2.add(37);
		myAvl2.add(41);
		myAvl2.delete(18);

		myAvl2.add(28);
		myAvl2.add(29);

		myAvl2.delete(30);

		System.out.println("failed with" + 999);




		System.out.println("end");






		System.out.println("___________________________");

		myAvl.delete(4);
		myAvl.delete(10);
		myAvl.delete(9);

		System.out.println("___________________________");
		myAvl.add(4);
		myAvl.add(15);
		myAvl.add(700);
		System.out.println("___________________________");
		int size=30000;
		for (int i = 0; i <= size / 2; i++) {
			int x = (i * 1153) % size;
			if (x==16214){
				System.out.println("this is i:"+i);
				break;
			}}

		System.out.println("___________________________");
		System.out.println(myAvl.findMinNodes(3)==7);
		System.out.println(myAvl.findMinNodes(5)==20);
		System.out.println(myAvl.findMinNodes(8)==88);

	}
}


