package recursive;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
/**
 * A recursive implementation of the AVL tree
 * @author jordan
 * @param <Item>
 * @version Tuesday April 16th 2019
 */
public class RecursiveAVL <Item extends Comparable<Item>> implements Set<Item>{
	/**The root node of the tree*/
	private AVLNode<Item> root;
	
	/**The size of the tree**/
	private int size;
	
	public RecursiveAVL() {
		root = null;
		size = 0;
	}
	
	
	/**
	 *Adds an item to the recursive AVL Tree. 
	 *
	 *@param item - the item to be added to the tree. 
	 *@return whether the item was sucessfully added to the tree.
	 *If the item is already contained within the tree, it will <b>not</b> be added.
	 */
	public boolean add(Item item) {
		if(contains(item)) {
			return false;
		}
		else {
			root = add(item, root);
			++size;
			return true;
		}
	}
	
	/**
     * This method recursively adds objects to the tree.
     * If the subRoot is empty, we create the node here.
     * Else, we add the object to the side it's closer to.
     * Then we perform AVL rebalances on the way back up the tree.
     * 
     * @param object - the object we're adding to the subTree.
     * @param node - the node we're currently at.
     * @return the new head of the subTree.
     * @throws ItemInTreeException if the node's object is the same as the object we're trying to add.
     */
    private AVLNode<Item> add(Item object,AVLNode<Item> node){
    	
    	//If we've reached a mystical space of nothingness, we create a binary node and return it.
    	if(node == null){
    		node = new AVLNode<Item>(object);
    		return node;
    	}
    	int compareValue = object.compareTo(node.item); //Comparsion value between the objects.
    	
    	//If the object is greater than the node we're at, we add the object to the right of the node.
    	if(compareValue > 0){ 
    		node.right = add(object,node.right);
    		node.updateHeight();
    		
    		//Since we're adding to the right, the only imbalance possible is from the right.
    		if(node.balanceFactor() > 1) {
    			node = rebalanceRight(node); 
    		}
    	}  
    	
    	//Else if the object is less than the node we're at, we add the object to the left of the node.
    	else {
    		node.left = add(object,node.left);
    		node.updateHeight();
    		
    		// Since we're adding to the left, the only imbalance possible is from the left.
    		if(node.balanceFactor() < -1) {
    			node = rebalanceLeft(node); 
    		}
    	}
    	return node;
    }
    /**
     * Removes the given object from the AVL tree if it is contained. 
     */
    @Override
	public boolean remove(Object obj) {
    	Item item;
		try {
			item = (Item)obj;
		}
		catch(ClassCastException e) {
			return false;
		}
		if(!contains(item)) {
			return false;
		}
		else {
			root = remove(item,root);
			--size;
			return true;
		}
	}
    /**
	 * Returns the new head of the subTree, post deletion.
	 * This method is called from the other method.
	 * 
	 * @param object - the object we're removing from the tree.
	 * @param node - the node we're currently at.
	 * @return the new head of the node.
	 * @throws ItemNotInTreeException - if the object you would like removed is not part of the tree in the first place.
	 */
	private AVLNode<Item> remove(Item object,AVLNode<Item> node){
		int compareValue = object.compareTo(node.item);
		
		//If we've found the item we're looking for, we delete the node.
		//Then, we return the node that has taken it's place.
		if(compareValue == 0){
	    	node = delete(node);
	    	if(node != null)node.updateHeight();
	    }
		/*
		 * If not we remove the object from the side the object is in.
		 * Because we're removing from that side, we have to use a rebalance as if an object was added to the other side.
		 */
	    else if(compareValue > 0){
	    	node.right = remove(object, node.right);
	    	node.updateHeight();
	    	if(node.balanceFactor() < -1)node = rebalanceLeft(node);
	    }
	    else{
	    	node.left = remove(object, node.left);
	    	node.updateHeight();
            if(node.balanceFactor() > 1)node = rebalanceRight(node);
	    }
		return node;
	}
	/**
	 * Deletes a node from the tree.
	 * @param node - the node that is to be removed.
	 * @return the node, now that it has been replaced or deleted.
	 * @throws ItemNotInTreeException - please ignore this. It's impossible for the exception to be thrown here.
	 */
	private AVLNode<Item> delete(AVLNode<Item> node){
		//If the node is a leaf, we can just delete since there are no subTrees.
		if(node.left == null && node.right == null){
    		node = null;
    	}
		//Else, if there's only one child, subRoot now points to that child.
    	else if(node.right == null){
    		return node.left;
    	}
    	else if(node.left == null){
    		return node.right;
    	}
		/*
		 * Otherwise, the node has two children. 
		 * We take the object of the right's farthest left, and remove it from the right - this ensures we stay balanced!
		 * Then set that object as the object of the node.
		 * Once all that's done, since we've removed from the right, we rebalance.
		 */
    	else{
    		Item object = node.right.farthestLeft().item;
    	    node.right = remove(object,node.right);
    	    node.item = object;
    	    if(node.balanceFactor() < -1)node = rebalanceLeft(node);
    	}
		return node;
	}
    /**
     * Rebalances a node that has a heavier left side. 
     * @param node - the node that is off-balanced.
     * @return - the node that should take the place of the node now that the node has been balanced. 
     */
    public AVLNode<Item> rebalanceLeft(AVLNode<Item> node){
    	/*
    	 * If the node's left child is left heavy, we do a left-left rebalance. 
    	 *      20
         *     /  \
         *    15   D                  15
         *   /  \       becomes     /    \
         *  10   C                 10     20
         * /  \                   / \     / \
         *A    B                 A   B   C   D
         */
    	if(node.left.balanceFactor() <= 0) {
    		AVLNode<Item> nodeC = node.left.right;
    		node.left.right = node;
    		node = node.left;
    		node.right.left = nodeC; 
    	}
    	/*
    	 *If the node's left child is right heavy, we do a left-right rebalance. 
    	 *     20
         *    /  \
         *  10    D                    15
         * /  \        becomes       /    \
         *A    15                  10      20
         *    /  \                /  \    /  \
         *   B    C              A    B  C    D
    	 */
    	else {
    		AVLNode<Item> newRoot = node.left.right;
			AVLNode<Item> nodeB = newRoot.left;
			AVLNode<Item> nodeC = newRoot.right;
			
			//Setting up the right child.
			newRoot.left = node.left;
			newRoot.left.right = nodeB;
			
			newRoot.right = node;
			newRoot.right.left = nodeC;
			node = newRoot;
    	}
    	node.left.updateHeight();
    	node.right.updateHeight();
		node.updateHeight();
    	return node;
    }
    /**
     * Rebalances a node that has a heavier right side.
     * @param node - the node that is unbalanced. 
     * @return - the node that should take the place of the node. 
     */
    public AVLNode<Item> rebalanceRight(AVLNode<Item> node){
    	/*If the node's right child is right heavy, we do a right-right rebalance.
    	 *  12                     
         *  /  \ 
         * A   20      becomes     20 
         *     / \                /  \ 
         *    B   24            12    24
         *       /  \          /  \   / \          
         *      C    D        A    B C   D
    	 */
		if(node.right.balanceFactor() >= 0) {
			AVLNode<Item> nodeB = node.right.left;
			node.right.left = node;
			node = node.right;
			node.left.right = nodeB; 
		}
	    /* If the node's right child is left heavy, we do a right-left rebalance. 
	     * 	 20
		 *   /  \  
		 *  A    30   becomes      25
		 *      /  \            /     \ 
		 *    25    D          20      30
	     *   /  \             /  \    /  \
		 *  B    C           A    B  C    D
		 */
		else {
			AVLNode<Item> newRoot = node.right.left;
			AVLNode<Item> nodeB = newRoot.left;
			AVLNode<Item> nodeC = newRoot.right;
			
			//Setting up the right child.
			newRoot.right = node.right;
			newRoot.right.left = nodeC;
			
			newRoot.left = node;
			newRoot.left.right = nodeB;
			node = newRoot;
			
		}
		node.right.updateHeight();
		node.left.updateHeight();
		node.updateHeight();
		return node;
	}
    
    
    /**
     * This method prints every object inside the AVL.
     * Efficiency of this operation is O(n), though it's usually much worse because it has to print.
     */
   public void printTree(){
    	if(root == null){
    		return;
    	}
    	else{
         printTree(root);
         System.out.println("");
    	}
    }
    /**
     * This method is mostly used for testing the AVL tree.
     * @param subRoot, the node we want to print, along with it's subTrees.
     */
	private void printTree(AVLNode<Item> node){
		//You can use this commented line to check that the balancing works correctly.
       if(Math.abs(node.balanceFactor()) > 1) {
       	throw new RuntimeException("Tree not balanced");
       }
       String leftString="";
       String rightString ="";
       if(node.left != null)leftString = node.left.item.toString();
       if(node.right != null)rightString = node.right.item.toString();
       System.out.println("Object: " + node.item + " Node height: " + node.height + " Balance factor: " + node.balanceFactor() + " left: " + leftString + " right : " + rightString);
       if(node.left != null)printTree(node.left);
       if(node.right != null)printTree(node.right);
    }
    
	@Override
	public boolean addAll(Collection<? extends Item> col) {
		return false;
	}

	@Override
	public void clear() {
		this.root = null;
		this.size = 0;
	}
	/**
	 * Returns whether the Recursive AVL Tree contains the item. 
	 * @param obj - the item that could be contained within the tree. 
	 * @return - whether the given object is contained within the tree or not. 
	 */
	@Override
	public boolean contains(Object obj) {
		//Casting the item. 
		Item item;
		try {
			item = (Item)obj;
		}
		catch(ClassCastException e) {
			return false;
		}
		
		//The node we're currently at in the tree.
		AVLNode<Item> currentNode = root;
		
		//Go down until we either find the item or hit null. 
		while(currentNode != null) {
			int compareValue = item.compareTo(currentNode.item);
			
			//If the compare value indicates the item should be to the right
			if(compareValue > 0) {
				currentNode = currentNode.right;
			}
			//Else if it should be to the left...
			else if(compareValue < 0) {
				currentNode = currentNode.left;
			}
			//Else, if we have found the item, return true. 
			else {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean containsAll(Collection<?> col) {
		
		return false;
	}

	@Override
	public boolean isEmpty() {
		return size == 0;
	}

	@Override
	public Iterator<Item> iterator() {
		
		return null;
	}

	

	@Override
	public boolean removeAll(Collection<?> col) {
		
		return false;
	}

	@Override
	public boolean retainAll(Collection<?> col) {
		
		return false;
	}

	@Override
	public int size() {
		return this.size;
	}

	@Override
	public Object[] toArray() {
		
		return null;
	}

	@Override
	public <T> T[] toArray(T[] array) {
		
		return null;
	}
	
	/**
	 * Inner class for the nodes of the AVL BST.
	 * @author jakjm
	 * @param <Item> - the type of object contained within the node.
	 * @version April 16th 2019 2019
	 */
	private static class AVLNode <Item>{
		 /**The right child of this node.**/
	     private AVLNode <Item> right;
	     /**The left child of this node.**/
	     private AVLNode <Item> left;
	     
	     /**The item contained within this node. **/
	     private Item item;
	     
	     /**The height of this node**/
	     private int height;
	     
	     /**
	      * The object that will be contained inside of the binary node.
	      * @param object - the object the tree is placing inside of a binary node.
	      */
	     public AVLNode(Item object){
	    	 item = object;
	    	 height = 0;
	     }
	     /**
	      * Returns the balance factor of this node.
	      * A positive value indicates <b>right heaviness</b>.
	      * A negative value indicates <b>left heaviness</b>
	      * @return the balance factor of this node.
	      */
	     public int balanceFactor() {
	    	 int rightHeight = -1;
	    	 int leftHeight = -1;
	    	 if(this.right != null)rightHeight = right.height;
	    	 if(this.left != null)leftHeight = left.height;
	    	 return rightHeight - leftHeight;
	     }
	     
	     /**
	  	 * @return the farthest node all the way down the left path
	  	 */
	  	private AVLNode<Item>farthestLeft(){
	  		AVLNode<Item> leftMost = this;
	  		while(leftMost.left != null){
	  			leftMost = leftMost.left;
	  		}
	  		return leftMost;
	  	}
	  	/**
	  	 * Updates the height of the current AVL node.
	  	 */
	  	public void updateHeight() {
	  		int right = -1, left = -1;
	  		if(this.right != null)right = this.right.height;
	  		if(this.left != null)left = this.left.height;
	  		this.height =  1 + Math.max(right,left);
	  	}
	}
}
