package recursive;

import static org.junit.jupiter.api.Assertions.*;

import java.util.TreeSet;

import org.junit.jupiter.api.Test;

class TestAVL {

	@Test
	void test() {
		RecursiveAVL<Integer> avl = new RecursiveAVL<Integer>();
		assertSame(avl.contains(0),false);
		assertSame(avl.size(),0);
		avl.add(0);
		assertSame(avl.contains(0),true);
		assertSame(avl.size(),1);
		
		for(int i = 1;i < 6;i++) {
			avl.add(i);
		}
		assertSame(avl.size(), 6);
		avl.printTree();
		avl.remove(3);
		assertSame(avl.size(),5);
		avl.printTree();
		avl.remove(4);
		avl.printTree();
		assertSame(avl.size(),4);
	}
	
	@Test
	void competition() {
		TreeSet<Integer> treeSet = new TreeSet<Integer>();
		RecursiveAVL <Integer> avl = new RecursiveAVL<Integer>();
		
		int size = 1000000;
		long startTime = System.currentTimeMillis();
		while(avl.size() < size) {
			avl.add((int)(Math.random() * (size * 10)));
		}
		long endTime = System.currentTimeMillis();
		
		System.out.println(String.format("AVL %7d items %d ms",size, endTime - startTime));
		
		startTime = System.currentTimeMillis();
		while(treeSet.size() < size) {
			treeSet.add((int)(Math.random() * (size * 10)));
		}
		endTime = System.currentTimeMillis();
		
		System.out.println(String.format("TreeSet %7d items %d ms",size, endTime - startTime));
	}
}