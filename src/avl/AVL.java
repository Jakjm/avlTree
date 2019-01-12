package avl;


/**
 * AVL self-balancing binary tree class.
 * @author jakjm
 * @version 1.00 - November 21st 2016
 * @param <Contents> - the type of the objects you will add to this tree.
 */
public class AVL <Contents extends Comparable<Contents>>{
	/**The top of the tree*/
	private BinaryNode <Contents> head; 
	public AVL(){
	    this.head = null;
	}
	/**
	 * This method uses the recursive size method to calculate the size of the tree.
	 * The efficiency of this method is about O(n), so use sparingly.
	 * @return the size of the tree.
	 */
	public int size(){
	return size(head);
	}
	/**
	 * This method recursively determines the size of the tree.
	 * @param subRoot
	 * @return the size of the tree.
	 */
    private int size(BinaryNode<Contents> subRoot){
     if(subRoot == null)return 0;
     else return 1 + size(subRoot.getRight()) + size(subRoot.getLeft());
    }
	/**
	 * Returns the height of the root node.
	 * You might want this to know how well the tree has been balanced, in comparison to its size.
	 * Efficiency of this operation is O(1).
	 * @return the height of the top-most node.
	 */
	public int height(){
		return head.height;
	}
    /**
     * Updates the stats of a BinaryNode that we need, the height and the balance factor.
     * @param subRoot
     */
    private void updateStats(BinaryNode<Contents> subRoot){
    	if(subRoot == null)return;
        short left = -1;
        short right = -1;
        if(subRoot.getRight() != null)right = subRoot.getRight().height;
        if(subRoot.getLeft() != null)left = subRoot.getLeft().height;
        subRoot.height = (byte) (1 + Math.max(right,left));
        subRoot.balanceFactor = (byte)(right - left);
    }
    /**
     * Performs a left-left rebalance.
     *      20
     *     /  \
     *    15   D                  15
     *   /  \       becomes     /    \
     *  10   C                 10     20
     * /  \                   / \     / \
     *A    B                 A   B   C   D
     * @param subRoot, the node we're rebalancing.
     * @return the newly balanced subRoot.
     */
    private BinaryNode<Contents> rebalanceLeftLeft(BinaryNode<Contents> subRoot){
    	BinaryNode<Contents> TLR = subRoot.getLeft().getRight(); 
    	subRoot.getLeft().setRight(subRoot);
    	subRoot = subRoot.getLeft();
    	subRoot.getRight().setLeft(TLR);
    	return subRoot;
    }
    /**
     * Performs a left-right rebalance.
     *     20
     *    /  \
     *  10    D                    15
     * /  \        becomes       /    \
     *A    15                  10      20
     *    /  \                /  \    /  \
     *   B    C              A    B  C    D
     * @param subRoot, the node that is to be rebalanced.
     * @return the now rebalanced subRoot.
     */
    private BinaryNode<Contents> rebalanceLeftRight(BinaryNode<Contents> subRoot){
    	//Storing the tails of the new head, TLR, that we will need later.
    	BinaryNode <Contents> leftRightRight = subRoot.getLeft().getRight().getRight();
    	BinaryNode <Contents> leftRightLeft = subRoot.getLeft().getRight().getLeft();
    	
    	//TLR becomes new head, it's subtrees are now the head and the head's left.
    	subRoot.getLeft().getRight().setRight(subRoot);
    	subRoot.getLeft().getRight().setLeft(subRoot.getLeft());
    	subRoot = subRoot.getLeft().getRight();
    	
    	//Now all we have to do is reassign the former subTrees of the new head.
    	subRoot.getLeft().setRight(leftRightLeft);
    	subRoot.getRight().setLeft(leftRightRight);
    	return subRoot;
    }
    /**
     * Rebalances the root, given it is left heavy.
     * This method is called after adding to the left or removing from the right.
     * Either a left-left or left-right rebalance may be necessary.
     * @param subRoot, the root we are rebalancing if necessary.
     * @return the possibly rebalanced root.
     */
    private BinaryNode<Contents> rebalanceLeft(BinaryNode<Contents> subRoot){
    	//Updates the height stats of the node, before we check if it needs rebalancing.
    	updateStats(subRoot); 
    	
    	    //If the node is left heavy to the point of non-AVL, we rebalance.
    		if(subRoot.balanceFactor < -1){
    			
    			//If the node's left is left heavy or balanced, do a left-left rebalance.
    			if(subRoot.getLeft().balanceFactor <= 0){
    				subRoot = rebalanceLeftLeft(subRoot);
    			}
    			
    			//Else, if the node's left is right heavy, do a left-right rebalance.
    			else{
    				subRoot = rebalanceLeftRight(subRoot);
    			}
    			
    			//Updates stats of the node and its direct subRoots, since we've just rebalanced.
				updateStats(subRoot.getLeft()); 
		   	    updateStats(subRoot.getRight());
		   	    updateStats(subRoot);
    		}
    	return subRoot;
    }
    /**
     * Performs a right-right rebalance to the node.
     *   12                     
     *  /  \ 
     * A   20      becomes     20 
     *     / \                /  \ 
     *    B   24            12    24
     *       /  \          /  \   / \          
     *      C    D        A    B C   D
     * @param node, the node that is to be rebalanced.
     * @return the now rebalanced node.
     */
    private BinaryNode<Contents> rebalanceRightRight(BinaryNode<Contents> node){
    	//Need to reassign tail of the new head, TR.
    	BinaryNode<Contents> TRL = node.getRight().getLeft();
    	node.getRight().setLeft(node);
        node = node.getRight();
        node.getLeft().setRight(TRL);
        return node;
    }
    /**
     * This method reforms a right-left rebalance to the node.
     *    20
     *   /  \  
     *  A    30   becomes      25
     *      /  \            /     \ 
     *    25    D          20      30
     *   /  \             /  \    /  \
     *  B    C           A    B  C    D
     * @param node, the node that is to be rebalanced.
     * @return the now rebalanced node.
     */
    private BinaryNode<Contents> rebalanceRightLeft(BinaryNode<Contents> node){
    	//Storing the tails of the newHead, TRL, that we will need later.
    	BinaryNode<Contents> rightLeftRight = node.getRight().getLeft().getRight();
    	BinaryNode<Contents> rightLeftLeft = node.getRight().getLeft().getLeft();
    	
    	//TRL becomes new head, it's subtrees are now the head and head's right
    	node.getRight().getLeft().setLeft(node); //Head goes to the left of the new head.
    	node.getRight().getLeft().setRight(node.getRight()); //Head's right goes to the right of the new head.
    	node = node.getRight().getLeft(); //Now that the reassignment is complete, the old tops can now bow to their new master.
    	
    	//Now all that is left is to reassign the former subTrees of the new head.
    	node.getLeft().setRight(rightLeftLeft);
    	node.getRight().setLeft(rightLeftRight);
    	
    	return node;
    }
    /**
     * Rebalances the root, which can only be balanced or right heavy.
     * This is used when we've added to the right or removed from the left.
     * @param subRoot - the node we're currently working on.
     * @return the newly rebalanced node.
     */
    private BinaryNode<Contents> rebalanceRight(BinaryNode<Contents> subRoot)
    {
    	 updateStats(subRoot);
    	 //If the node is right heavy to the point of not being avl, we rebalance.
         if(subRoot.balanceFactor > 1){
        	     //If the node's right is right heavy or balanced, we do a right-right rebalance.
        		 if(subRoot.getRight().balanceFactor >= 0){
        			 subRoot = rebalanceRightRight(subRoot); 
        		 }
        		 //Else, if the node's right is left heavy, we perform a right-left rebalance.
        		 else{
        		     subRoot = rebalanceRightLeft(subRoot); 
        		 }
        		//Updates stats of the node and its direct subRoots, since we've just rebalanced.
        		updateStats(subRoot.getLeft()); 
  		   	    updateStats(subRoot.getRight());
  		   	    updateStats(subRoot);
        	 }
    	 return subRoot;
    }
     /**
     * Given the object is not already part of the tree, this method adds an object to the AVL tree.
     * Efficiency of this operation is about O(log(n))
     * @param object - the object you would like to add to the tree.
     * @return whether the object was already part of the tree or not.
     */
    public boolean add(Contents object){
    	try{
    	head = add(object,head);
    	}
    	//If the item failed to be added to the tree since it was already in the tree, we return false.
    	catch(ItemInTreeException i){
    		return false;
    	}
    	//Otherwise, we return true, since the item has been added.
    	return true;
    }
    /**
     * This method recursively adds objects to the tree.
     * If the subRoot is empty, we create the node here.
     * Else, we add the object to the side it's closer to.
     * Then we perform AVL rebalances on the way back up the tree.
     * @param object - the object we're adding to the subTree.
     * @param node - the node we're currently at.
     * @return the new head of the subTree.
     * @throws ItemInTreeException if the node's object is the same as the object we're trying to add.
     */
    private BinaryNode<Contents> add(Contents object,BinaryNode<Contents> node) throws ItemInTreeException{
    	
    	//If we've reached a mystical space of nothingness, we create a binary node and return it.
    	if(node == null){
    		node = new BinaryNode<Contents>(object);
    		return node;
    	}
    	int compareValue = object.compareTo(node.getObject()); //Comparsion value between the objects.
    	
    	//If the object is greater than the node we're at, we add the object to the right of the node.
    	if(compareValue > 0){ 
    		node.setRight(add(object,node.getRight()));
    		node = rebalanceRight(node); //Since we're adding to the right, the only imbalance possible is from the right.
    		return node;
    	}  
    	
    	//Else if the object is less than the node we're at, we add the object to the left of the node.
    	else if(compareValue < 0){ 
    		node.setLeft(add(object,node.getLeft()));
    		node = rebalanceLeft(node); // Since we're adding to the left, the only imbalance possible is from the left.
    		return node;
    	}
    	//Otherwise, the node's object is the same as the object we're trying to add.
    	//And so the object is already part of the tree - we have no business trying to add.
    	//The other add method will return false since the object was already part of the tree.
    	else{
    		throw new ItemInTreeException();
    	}
    }  
    /**
     * This method determines if an object is part of the AVL tree.
     * The efficiency of this operation is about O(Log(n)).
     * @param object - the object to look for within the tree.
     * @return whether the object is part of the tree.
     */
    public boolean contains(Contents object){
    	return contains(object,head);
    }
    /**
     * This checks:
     * - to see if we've over extended our bounds,  returns false if so(because we've gone off the tree, it won't contain the item further down)
     * - if not, see if the object contained by the top is equal to the object we're checking for - returns true if so.
     * - else, checks if it's further down the tree recursively with a binary search. Much more efficient than checking every single value.
     * @param object - the object we're attempting to find in the tree.
     * @param node - the top node of the subTree we're currently searching in.
     * @return whether the node or it's subTrees contain the object.
     */
    private boolean contains(Contents object,BinaryNode <Contents> node){
         if(node == null)return false;
         else{
        	 int compareValue = object.compareTo(node.getObject()); //Difference between objects.
        	 if(compareValue == 0)return true; //If objects are equal, then we return true.
        	 else if(compareValue > 0)return contains(object,node.getRight()); //If the object is greater than the current object we check right.
        	 else return contains(object,node.getLeft()); //Else, object is less than the current object, so we check left.
         }
    }
	/**
	 * This method removes an object from the tree.
	 * The efficiency of this operation is O(Log(n))
	 * However, it's possible that this method could cause some java garbage collection lag.
	 * @param object - the object you would like removed from the tree.
	 * @return false if the object was not a part of the tree, or returns true if it was, after removing the object.
	 */
	public boolean remove(Contents object){
		try{
			head = remove(object,head);
		}
		catch(ItemNotInTreeException i){
			return false;
		}
	    return true;
	}
	/**
	 * Returns the new head of the subTree, post deletion.
	 * This method is called from the other method.
	 * @param object - the object we're removing from the tree.
	 * @param node - the node we're currently at.
	 * @return the new head of the node.
	 * @throws ItemNotInTreeException - if the object you would like removed is not part of the tree in the first place.
	 */
	private BinaryNode<Contents> remove(Contents object,BinaryNode<Contents> node) throws ItemNotInTreeException{
        if(node == null)
        {
        	throw new ItemNotInTreeException();
        }
		int compareValue = object.compareTo(node.getObject());
		//If we've found the item we're looking for, we delete the node.
		//Then, we return the node that has taken it's place.
		if(compareValue == 0){
	    	node = delete(node);
	    	return node;
	    }
		/*
		 * If not we remove the object from the side the object is in.
		 * Because we're removing from that side, we have to use a rebalance as if an object was added to the other side.
		 */
		
	    else if(compareValue > 0){
	    	node.setRight(remove(object,node.getRight()));
	    	node = rebalanceLeft(node);
	    	return node;
	    }
	    else{
	    	node.setLeft(remove(object,node.getLeft()));
            node = rebalanceRight(node);
	    	return node;
	    }
	}
	/**
	 * Deletes a node from the tree.
	 * @param node - the node that is to be removed.
	 * @return the node, now that it has been replaced or deleted.
	 * @throws ItemNotInTreeException - please ignore this. It's impossible for the exception to be thrown here.
	 */
	private BinaryNode<Contents> delete(BinaryNode<Contents> node) throws ItemNotInTreeException{
		//If the node is a leaf, we can just delete since there are no subTrees.
		if(node.getLeft() == null && node.getRight() == null){
    		node = null;
    		return node;
    	}
		//Else, if there's only one child, subRoot now points to that child.
    	else if(node.getRight() == null){
    		node = node.getLeft();
    		return node;
    	}
    	else if(node.getLeft() == null){
    		node = node.getRight();
    		return node;
    	}
		/*
		 * Otherwise, the node has two children. 
		 * We take the object of the right's farthest left, and remove it from the right - this ensures we stay balanced!
		 * Then set that object as the object of the node.
		 * Once all that's done, since we've removed from the right, we rebalance.
		 */
    	else{
    		Contents object = node.getRight().farthestLeft().getObject();
    	    node.setRight(remove(object,node.getRight()));
    	    node.setObject(object);
    	    node = rebalanceLeft(node);
    	}
		return node;
	}
	/**
	 * This method clears the binary tree of all objects. 
	 * Warning: Because of Java's garbage collection, this could potentially cause some noticeable lag.
	 */
     public void clear(){
		head = null;
	}
     /**
      * This method prints every object inside the AVL.
      * Efficiency of this operation is O(n), though it's usually much worse because it has to print.
      */
    public void printTree(){
     	if(head == null){
     		return;
     	}
     	else{
          printTree(head);
     	}
     }
     /**
      * This method is mostly used for testing the AVL tree.
      * @param subRoot, the node we want to print, along with it's subTrees.
      */
 	private void printTree(BinaryNode<Contents> node){
 		//You can use this commented line to check that the balancing works correctly.
        if(node.balanceFactor > 1 || node.balanceFactor < -1)throw new NullPointerException();
 		
 		String leftString="";
        String rightString ="";
     	if(node.getLeft() != null)leftString = node.getLeft().getObject().toString();
     	if(node.getRight() != null)rightString = node.getRight().getObject().toString();
     	System.out.println("Object: " + node.getObject() + " Node height: " + node.height + "Balance factor: " + node.balanceFactor + " left: " + leftString + " right : " + rightString);
     	if(node.getLeft() != null)printTree(node.getLeft());
     	if(node.getRight() != null)printTree(node.getRight());
     }
	/**
	 * Inner class for the nodes of the AVL BST.
	 * @author jakjm
	 * @param <Item> - the type of object contained within the node.
	 * @version November 21st 2016
	 */
	private class BinaryNode <Item extends Comparable<Item>>{
	     private BinaryNode <Item> right;
	     private BinaryNode <Item> left;
	     private Item containedObject;
	     
	     //Keeping these variables nice and small because this is a data structure - lower data use is desirable.
	     private byte height; //Height of this node. If the node has no subTrees, height is 0. Otherwise, it's one more than its subTree heights.
	     private byte balanceFactor; //Difference in height between this node's subTrees.
	     
	     /**
	      * The object that will be contained inside of the binary node.
	      * @param object - the object the tree is placing inside of a binary node.
	      */
	     public BinaryNode(Item object){
	    	 containedObject = object;
	    	 height = 0;
	    	 balanceFactor = 0;
	     }
	     /**
	      * Use this method when assigning the reference to the left node to a new object.
	      * Now the reference will point to whatever you're telling it to point to.
	      * @param newLeft, the new node we're setting the left to.
	      */
	     private void setLeft(BinaryNode <Item> newLeft){
	    	 this.left = newLeft;
	     }
	     /**
	      * Use this method when assigning the reference to the right node to a new object.
	      * Now the reference will point to whatever you're telling it to point to.
	      * @param newRight, the new node we're setting the right to.
	      */
	     private void setRight(BinaryNode <Item> newRight){
	    	 this.right = newRight;
	     }
	     
	     /**
	      * Use this to traverse lower to the right.
	      * @return the node to the lower right.
	      */
	     public BinaryNode<Item> getRight(){
	    	 return this.right;
	     }
	     /**
	      * Use this to traverse lower to the left.
	      * @return the node to the lower left.
	      */
	     public BinaryNode<Item> getLeft(){
	    	 return this.left;
	     }
	     /**
	      * Returns the object that this node contains.
	      * @return the object within the node.
	      */
	     public Item getObject(){
	    	 return containedObject;
	     }
	     /**
	      * Sets the contained object to a new one.
	      * @param object, the object that will become the node's object.
	      */
	     public void setObject(Item object)
	     {
	    	 this.containedObject = object;
	     }
	     /**
	  	 * JAVA is pass-by-reference-by-value so therefore setting a parameter equal to something won't change its real value.
	  	 * @return the farthest node all the way down the left path
	  	 */
	  	private BinaryNode<Item>farthestLeft(){
	  		BinaryNode<Item> leftMost = this;
	  		while(leftMost.getLeft() != null){
	  			leftMost = leftMost.getLeft();
	  		}
	  		return leftMost;
	  	}
	}
	/**
	 * Exception thrown when an item is not within the tree.
	 * @author jakjm
	 */
	public static class ItemNotInTreeException extends Exception
	{
		private static final long serialVersionUID = 1L;
		public ItemNotInTreeException() 
		{
			super();
		}

	}

	public static class ItemInTreeException extends Exception
	{
		private static final long serialVersionUID = 1L;
		public ItemInTreeException() {
			super();
		}
	}


}
