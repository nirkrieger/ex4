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
//		TESTS test= new TESTS();
		AvlTree oldAvl=new AvlTree(new int[]{1,2,3,4,5,6,7,8,9,10});
		AvlTree myAvl2=new AvlTree(new int[]{16,8,30,6,9,20,40,7,18,32});
		AvlTree testT= new AvlTree();
		SortedSet<Integer> checker = new TreeSet<Integer>();
		checker.addAll(Arrays.asList(new Integer[]{16, 8, 30, 6, 9, 20, 40, 7, 18, 32}));

		for(int i=0;i<9;i++){
			testT.add(i);
		}
		testT.delete(2);
		testT.delete(0);
		System.out.println(oldAvl.findMinNodes(3)==7);
		System.out.println(oldAvl.findMinNodes(5)==20);
		System.out.println(oldAvl.findMinNodes(8)==88);

	}
}


