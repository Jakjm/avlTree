package avl;
/**
 * 
 * Tests that the AVL adds properly and deletes properly
 * @author jakjm
 */
public class AVLAccuracyTest 
{
	public static void main(String [] args) {
		AVL<Integer> tree = new AVL<Integer>();
		int removalNumber = 16;
		
		//Play around with removal number.
		//If the avl works, the output should be: the normal tree, false, and the tree without removal number.
		for(int i = 0; i < 32;i++){tree.add(i);}
		tree.printTree();
		tree.remove(removalNumber);
		System.out.println(tree.contains(removalNumber));
		tree.printTree();
	}

}
