import java.util.*;

/**
 * 
 * @author Md Aiman Sharif
 * 	
 * Class providing the implementation of BSTDictionary which implements the Dictionary class interface
 * 
 */
public class BSTDictionary<E, K extends Sortable> implements Dictionary<E,K> {
	//declares instance variables 
	public BSTNode<E, K> rootNode; //creates a rootNode of type BSTNode
	public String str; //creates a string
	
	/**
	 * default constructor initializing instance variables
	 */
	public BSTDictionary(){
		this.rootNode = null; //initially sets rootNode to null 
		this.str = null; //sets the string to null initially
	}
	
	/**
	 * search for an entry with key KEY and return the object
	 * @param key 
	 */
	public E search(K key){
		BSTNode<E, K> currentNode = searchHelper(rootNode, key); //creates a node of type BSTNode 
		
		if(currentNode == null){
			return null; //returns null if the current node is null
		}
		else{
			return currentNode.getElement(); //returns the element in the current node
		}
	}
	
	/**
	 * searchHelper which helps to search for the nodes
	 * @param node 
	 * @param k key
	 * @return node of type BSTNode
	 */
	protected BSTNode<E, K> searchHelper(BSTNode<E, K> node, K k) {
		if ((node == null) || (k.compareTo(node.getKey()) == 0)) { 
			return node; //returns n
		}
		else{
			if (k.compareTo(node.getKey()) < 0) {
				return searchHelper(node.getLeft(), k); //calls search helper recursively using the left node
			}
			else{
				return searchHelper(node.getRight(), k); //calls search helper method recursively using the right node
			}
		}
	}
	
	/**
	 * Removes node n from the tree as given in the parameter
	 * @param n node
	 * @return temporaryNode temporary variable  
	 */
	protected BSTNode <E,K> remove(BSTNode<E,K> n){
		BSTNode<E,K> currentNode = n; 
		if(n.getLeft() == null){
			return n.getRight(); //return the right node
		}
		else if(n.getRight() == null){
			return n.getLeft(); //returns the left node
		}
		else{
			n = n.getRight();
			BSTNode<E,K> tempNode = n; //creates a temporary variable of type BSTNode
			BSTNode<E,K> previousNode = currentNode; //sets the previous BSTNode to the current node
			while(tempNode.getLeft() != null){
				previousNode = tempNode; //sets previous value to tempNode
				tempNode = tempNode.getLeft(); //set temporary value to temp.getLeft()
			}
			
			tempNode.setLeft(currentNode.getLeft());  //set the left node of temporary BSTNode to left node of current node
			
			if(previousNode != currentNode){
				previousNode.setLeft(tempNode.getRight()); //sets the left node of the previous node to the right node of tempNode
			}
			else{
				previousNode.setRight(tempNode.getRight()); //sets right node of previous to right node of tempNode
			}
			tempNode.setRight(currentNode.getRight()); //sets right node of tempNode to right node of current node
			return tempNode; //return valued stored in tempNode
		}
	}
	
	/**
	 * method insert implemented
	 * insert a key-value pair into the dictionary
	 * @param key used for searching
	 * @param element to be inserted
	 */
	public void insert(K key, E element) {
		rootNode = helperInsert(rootNode, key, element); // calls helperInsert function
	}

	/**
	 *
	 * helperInsert method implemented
	 * insert a key-value pair into the dictionary 
	 * @param node
	 * @param k key
	 * @param e element 
	 * @return n node
	 */
	protected BSTNode<E, K> helperInsert(BSTNode<E,K> node, K k, E e){
		if (node == null) {
			node = new BSTNode<E, K>(e, k, null, null); //creates a new BSTNode
		}
		else if (k.compareTo(node.getKey()) == 0) {
			node.setElement(e); //sets element to e
		}
		else if (k.compareTo(node.getKey()) < 0) {
			node.setLeft(helperInsert(node.getLeft(), k, e)); //sets the left node to the left node returned by getLeft
		}
		else {
			node.setRight(helperInsert(node.getRight(), k, e)); //sets the right node to right node returned by getRight 
		}
		return node; //returns n
	}
	
	/**
	 * delete method implemented to delete a node
	 * delete an entry with key "key"
	 * @param key
	 */
	public void delete(K key){
		rootNode = deleteHelper(rootNode, key); 
	}
	
	/**
	 * Helper method implemented to delete node specified with node n and key of value k
	 * @param n node
	 * @param k key
	 * @return n a node is returned by this method
	 */
	protected BSTNode<E, K> deleteHelper(BSTNode<E,K> n, K k){
		if(n == null){
			return null; 
		}
		else if (k.compareTo(n.getKey()) == 0) {
			n = remove(n); //removes n
		}
		else if(k.compareTo(n.getKey()) < 0){
			n.setLeft(deleteHelper(n.getLeft(),k)); //deletes the left node
		}
		else{
			n.setRight(deleteHelper(n.getRight(),k)); //deletes the right node
		}
		return n; //returns n
	}

	/**
	 * print the Dictionary in sorted order (as determined by the keys)
	 */
	public void printTree(){
		printTreeHelper(rootNode);  //calls printTreeHelper
		System.out.println(); //prints out a new line
	}
	
	/*
	 * helper method to print out the BSENode tree
	 */
	protected void printTreeHelper(BSTNode<E,K> node){
		if (node == null) {
			return; //return method if n is null
		}
		printTreeHelper(node.getLeft()); //prints out left node
		System.out.print(node.getKey() + " ");
		printTreeHelper(node.getRight()); //then prints out right node
	}
	
	/**
	 *  method that returns the depth of the underlying tree
	 */
	public int depth(){
		return depthHelper(rootNode);  //returns method by calling depthHelper
	}
	
	/**
	 * helper method that depth method uses to get the depth of the BSTNode tree 
	 *
	 * @param node node of type BSTNode
	 * @return maximum depth of the right and left node of the tree plus 1
	 */
	public int depthHelper(BSTNode<E, K> node){
		if (node == null){
				return 0; //returns 0 if n is null
			}
		else {
			return 1 + Math.max(depthHelper(node.getLeft()), depthHelper(node.getRight())); //returns the maximum depth of the right and left node
		}
	}
}
