package recursive;

import static org.junit.jupiter.api.Assertions.*;
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
		avl.printTree();
	}

}
