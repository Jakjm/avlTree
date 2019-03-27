package recursive;
/**
 * Testing here for the AVL's capacity to add random numbers without becoming imbalanced.
 * @author jakjm
 */
public class AVLRandomsTest {

	public static void main(String[] args) {
		AVL<Integer> tree = new AVL<Integer>();
		int size = 1000000;
		int maxItem = size * 2;
        for(int i = 0;i < size;i++){tree.add((int)(Math.random()*maxItem));}
        for(int i = 0;i < size;i++){tree.remove((int)(Math.random()*maxItem));}
        tree.printTree();
        /*
         * If you're using this check method, add an if statement to the print tree method that checks if the node's balance factor 
         * is greater than one or less than -1, and throw an exception. The tree has preformed properly if none of the nodes are imbalanced.
         * Printing takes awhile so be sparing with the size. The bigger it is, the longer you have to wait before the check is done.
         */
	}

}
