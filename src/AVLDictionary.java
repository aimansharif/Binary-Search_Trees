public class AVLDictionary <E, K extends Sortable> implements Dictionary <E,K>{

	protected static final int nodeLarger = 1;
	protected static final int depthInit = 0;
	protected static final int nodeSmaller = -1;
	protected AVLNode<E, K> root = null;
	
	public AVLDictionary(){
		
	}
	/*
	 * (non-Javadoc)
	 * @see Dictionary#search(Sortable)
	 */
	public E search(K k){
		AVLNode<E, K> n = searchHelper(root, k);
		if (n == null) {
			return null;
		} else {
			return n.getElement();
		}
	}
	/*
	 * 
	 */
	protected AVLNode<E, K> searchHelper(AVLNode<E, K> n, K k) {
		if ((n == null) || (k.compareTo(n.getKey()) == 0)) {
			return n;
		} else {
			if (k.compareTo(n.getKey()) < 0) {
				return searchHelper(n.getLeft(), k);
			} else {
				return searchHelper(n.getRight(), k);
			}
		}
	} 
	/*
	 * (non-Javadoc)
	 * @see Dictionary#insert(Sortable, java.lang.Object)
	 */
	public void insert(K k, E e){
		NodeInfo<E, K> info = insertHelper(root, k, e);
		root = info.node;
	}
	/*
	 * 
	 */
	protected NodeInfo<E, K> insertHelper(AVLNode<E, K> n, K k, E e) {
		int newState; 

		if (n == null) {
			n = new AVLNode<E, K>(k, e, null, null, AVLNode.EVEN);
			newState = nodeLarger;
		} 
		else if (k.compareTo(n.getKey()) == 0) {
			n.setElement(e);
			newState = depthInit;
		}
		else if (k.compareTo(n.getKey()) < 0) {
			NodeInfo<E, K> info = insertHelper(n.getLeft(), k, e);
			n.setLeft(info.node);

			if (info.stateChange == nodeLarger) {
				if (n.getBalance() == AVLNode.MORE_LEFT) {
					info = delLeftBalance(n);
					n = info.node;

					if (info.stateChange == nodeSmaller)
						newState = depthInit;
					else
						newState =  nodeLarger;
				}
				else if (n.getBalance() == AVLNode.EVEN) {
					n.setBalance(AVLNode.MORE_LEFT);
					newState = nodeLarger;
				}
				else {
					n.setBalance(AVLNode.EVEN);
					newState = depthInit;
				}
			}
			else {
				newState = depthInit;
			}
		} 
		else {
			NodeInfo<E, K> info = insertHelper(n.getRight(), k, e);
			n.setRight(info.node);

			if (info.stateChange == nodeLarger) {
				if (n.getBalance() == AVLNode.MORE_LEFT) {
					n.setBalance(AVLNode.EVEN);
					newState = depthInit;
				}
				else if (n.getBalance() == AVLNode.EVEN) {
					n.setBalance(AVLNode.MORE_RIGHT);
					newState = nodeLarger;
				}
				else {
					info = delRightBalance(n);
					n = info.node;

					if (info.stateChange == nodeSmaller)
						newState = depthInit;
					else
						newState = nodeLarger;
				}
			}
			else {
				newState = depthInit;
			}
		}
		return new NodeInfo<E, K>(n, newState);
	} 
	/*
	 * (non-Javadoc)
	 * @see Dictionary#delete(Sortable)
	 */
	public void delete(K k) {
		NodeInfo<E, K> info = deleteHelper(root, k);
		root = info.node;
	}
	/*
	 * 
	 */
	protected NodeInfo<E, K> deleteHelper(AVLNode<E, K> n, K k) {
		if (n == null) {
			return new NodeInfo<E, K>(n, depthInit);
		} 
		else if (k.compareTo(n.getKey()) == 0) {
			return remove(n);
		} 
		else if (k.compareTo(n.getKey()) < 0) {
			NodeInfo<E, K> info = deleteHelper(n.getLeft(), k);
			n.setLeft(info.node);
			return comeUpFromLeft(n, info.stateChange);
		}
		else {
			NodeInfo<E, K> info = deleteHelper(n.getRight(), k);
			n.setRight(info.node);
			return comeUpFromRight(n, info.stateChange);
		}
	}
	/*
	 * 
	 */
	protected NodeInfo<E, K> remove(AVLNode<E, K> n) {
		if (n.getLeft() == null) {
			return new NodeInfo<E, K>(n.getRight(), nodeSmaller);
		} 
		else if (n.getRight() == null) {
			return new NodeInfo<E, K>(n.getLeft(), nodeSmaller);
		} 
		else {
			NodeInfo<E, K> info = findReplacement(n, n.getRight());
			n.setRight(info.node);
			return comeUpFromRight(n, info.stateChange);
		}
	} 
	/*
	 * (non-Javadoc)
	 * @see Dictionary#printTree()
	 */
	public void printTree() {
		printTreeHelper(root);
		System.out.println();
	} 
	/*
	 * 
	 */
	protected void printTreeHelper(AVLNode<E, K> node) {
		if (node == null)
			return;
		printTreeHelper(node.getLeft());
		System.out.print(node.getKey() + " ");
		printTreeHelper(node.getRight());
	} 
	/*
	 * (non-Javadoc)
	 * @see Dictionary#depth()
	 */
	public int depth() {
		return depthHelper(root);
	} 

	/*
	 * 
	 */
	protected int depthHelper(AVLNode<E, K> node) {
		if (node == null) {
			return 0;
		} 
		else {
			return 1 + Math.max(depthHelper(node.getLeft()), depthHelper(node
					.getRight()));
		}
	} 
	/*
	 * 
	 */
	public void printrootRepresentation() {
		printrootRepresentationHelper(root);
	} 

	/*
	 * 
	 */
	public void printrootRepresentationHelper(AVLNode<E, K> node) {
		if (node == null) {
			System.out.print("[]");
		}
		else {
			System.out.print("[" + node.getKey());
			if (node.getBalance() == AVLNode.MORE_LEFT) {
				System.out.print("<");
			}
			else if (node.getBalance() == AVLNode.EVEN) {
				System.out.print("=");
			}
			else 
			{
				System.out.print(">");
			}
			if ((node.getLeft() != null) || (node.getRight() != null)) {
				printrootRepresentationHelper(node.getLeft());
				printrootRepresentationHelper(node.getRight());
			}
			System.out.print("]");
		}
	} 
	/*
	 * 
	 */
	public void validateRoot() {
		validateRootHelper(root);
	} 
	/*
	 * 
	 */
	public void validateRootHelper(AVLNode<E, K> node) {
		if (node != null) {
			validateRootHelper(node.getRight());
			if (node.getBalance() == AVLNode.MORE_LEFT) {
				if (depthHelper(node.getLeft()) != depthHelper(node.getRight()) + 1) {
					System.out.println(node.getKey() + " has bad balance of <"
							+ " [Left Depth:" + depthHelper(node.getLeft())
							+ "  Right Depth:" + depthHelper(node.getRight())
							+ "]");
					printrootRepresentation();
				}
			}
			else if (node.getBalance() == AVLNode.EVEN) {
				if (depthHelper(node.getLeft()) != depthHelper(node.getRight())) {
					System.out.println(node.getKey() + " has bad balance of ="
							+ " [Left Depth:" + depthHelper(node.getLeft())
							+ "  Right Depth:" + depthHelper(node.getRight())
							+ "]");
					printrootRepresentation();
				}
			}
			else 
			{
				if (depthHelper(node.getLeft()) + 1 != depthHelper(node.getRight())) {
					System.out.println(node.getKey() + " has bad balance of >"
							+ " [Left Depth:" + depthHelper(node.getLeft())
							+ "  Right Depth:" + depthHelper(node.getRight())
							+ "]");
					printrootRepresentation();
				}
			}
			if (node.getLeft() != null)
				if (node.getKey().compareTo(node.getLeft().getKey()) <= 0) {
					System.out.println(node.getKey()
							+ " not greater than left node");
					printrootRepresentation();
				}
			if (node.getRight() != null)
				if (node.getKey().compareTo(node.getRight().getKey()) >= 0) {
					System.out.println(node.getKey()
							+ " not less than right node");
					printrootRepresentation();
				}
			validateRootHelper(node.getLeft());

		}
	} 
	/*
	 * 
	 */
	protected AVLNode<E, K> rotateRight(AVLNode<E, K> node) {
		AVLNode<E, K> t = node.getLeft();
		node.setLeft(t.getRight());
		t.setRight(node);
		return t;
	} 
	/*
	 * 
	 */
	protected AVLNode<E, K> rotateLeft(AVLNode<E, K> node) {
		AVLNode<E, K> t = node.getRight();
		node.setRight(t.getLeft());
		t.setLeft(node);
		return t;
	} 
	/*
	 * 
	 */
	protected NodeInfo<E, K> rightBalance(AVLNode<E, K> node) {
		AVLNode<E, K> right = node.getRight();
		if (right.getBalance() == AVLNode.MORE_RIGHT) {
			node.setBalance(AVLNode.EVEN);
			right.setBalance(AVLNode.EVEN);
			node = rotateLeft(node);
			return new NodeInfo<E, K>(node, depthInit);
		} 
		else // Assume r.getBalance (*) == AVLNode.MORE_LEFT
		{
			AVLNode<E, K> left = right.getLeft();
			if (left.getBalance() == AVLNode.MORE_LEFT) {
				node.setBalance(AVLNode.EVEN);
				right.setBalance(AVLNode.MORE_RIGHT);
			}
			else if (left.getBalance() == AVLNode.EVEN) {
				node.setBalance(AVLNode.EVEN);
				right.setBalance(AVLNode.EVEN);
			}
			else // left.getBalance == AVLNode.MORE_RIGHT
			{
				node.setBalance(AVLNode.MORE_LEFT);
				right.setBalance(AVLNode.EVEN);
			}

			left.setBalance(AVLNode.EVEN);
			right = rotateRight(right);
			node.setRight(right);
			node = rotateLeft(node);
			return new NodeInfo<E, K>(node, depthInit);
		}
	} 
	/*
	 * 
	 */
	protected NodeInfo<E, K> leftBalance(AVLNode<E, K> node) {
		AVLNode<E, K> left = node.getLeft();
		if (left.getBalance() == AVLNode.MORE_LEFT) {
			node.setBalance(AVLNode.EVEN);
			left.setBalance(AVLNode.EVEN);
			node = rotateRight(node);
			return new NodeInfo<E, K>(node, depthInit);
		} 
		else // Assume left.getBalance () == AVLNode.MORE_RIGHT
		{
			AVLNode<E, K> right = left.getRight();
			if (right.getBalance() == AVLNode.MORE_RIGHT) {
				node.setBalance(AVLNode.EVEN);
				left.setBalance(AVLNode.MORE_LEFT);
			} 
			else if (right.getBalance() == AVLNode.EVEN) {
				node.setBalance(AVLNode.EVEN);
				left.setBalance(AVLNode.EVEN);
			}
			else // right.getBalance () == AVLNode.MORE_LEFT
			{
				node.setBalance(AVLNode.MORE_RIGHT);
				left.setBalance(AVLNode.EVEN);
			}

			right.setBalance(AVLNode.EVEN);
			left = rotateLeft(left);
			node.setLeft(left);
			node = rotateRight(node);
			return new NodeInfo<E, K>(node, depthInit);
		}
	}
	/*
	 * 
	 */
	protected NodeInfo<E, K> delRightBalance(AVLNode<E, K> node) {
		AVLNode<E, K> right = node.getRight(); 

		if (right.getBalance() == AVLNode.EVEN) {
			node.setBalance(AVLNode.MORE_RIGHT);
			right.setBalance(AVLNode.MORE_LEFT);
			node = rotateLeft(node);
			return new NodeInfo<E, K>(node, depthInit);
		} 
		else if (right.getBalance() == AVLNode.MORE_RIGHT) {
			node.setBalance(AVLNode.EVEN);
			right.setBalance(AVLNode.EVEN);
			node = rotateLeft(node);
			return new NodeInfo<E, K>(node, nodeSmaller);
		}
		else // right.getBalance () == AVLNode.MORE_LEFT
		{
			AVLNode<E, K> left = right.getLeft();
			if (left.getBalance() == AVLNode.MORE_LEFT) {
				node.setBalance(AVLNode.EVEN);
				right.setBalance(AVLNode.MORE_RIGHT);
			}
			else if (left.getBalance() == AVLNode.EVEN) {
				node.setBalance(AVLNode.EVEN);
				right.setBalance(AVLNode.EVEN);
			}
			else // left.getBalance () == AVLNode.MORE_RIGHT
			{
				node.setBalance(AVLNode.MORE_LEFT);
				right.setBalance(AVLNode.EVEN);
			}
			left.setBalance(AVLNode.EVEN);
			right = rotateRight(right);
			node.setRight(right);
			node = rotateLeft(node);
			return new NodeInfo<E, K>(node, nodeSmaller);
		}
	} 
	/*
	 * 
	 */
	protected NodeInfo<E, K> delLeftBalance(AVLNode<E, K> node) {
		AVLNode<E, K> left = node.getLeft(); 
		
		if (left.getBalance() == AVLNode.EVEN) {
			node.setBalance(AVLNode.MORE_LEFT);
			left.setBalance(AVLNode.MORE_RIGHT);
			node = rotateRight(node);
			return new NodeInfo<E, K>(node, depthInit);
		} 
		else if (left.getBalance() == AVLNode.MORE_LEFT) {
			node.setBalance(AVLNode.EVEN);
			left.setBalance(AVLNode.EVEN);
			node = rotateRight(node);
			return new NodeInfo<E, K>(node, nodeSmaller);
		}
		else // left.setBalance () == AVLNode.MORE_RIGHT
		{
			AVLNode<E, K> right = left.getRight();
			if (right.getBalance() == AVLNode.MORE_RIGHT) {
				node.setBalance(AVLNode.EVEN);
				left.setBalance(AVLNode.MORE_LEFT);
			}
			else if (right.getBalance() == AVLNode.EVEN) {
				node.setBalance(AVLNode.EVEN);
				left.setBalance(AVLNode.EVEN);
			}
			else // right.getBalance () == AVLNode.MORE_LEFT
			{
				node.setBalance(AVLNode.MORE_RIGHT);
				left.setBalance(AVLNode.EVEN);
			}
			right.setBalance(AVLNode.EVEN);
			left = rotateLeft(left);
			node.setLeft(left);
			node = rotateRight(node);
			return new NodeInfo<E, K>(node, nodeSmaller);
		}
	} 
	/*
	 * 
	 */
	protected NodeInfo<E, K> comeUpFromLeft(AVLNode<E, K> root, int stateChange) {
		if (stateChange == nodeSmaller) {
			if (root.getBalance() == AVLNode.EVEN) {
				root.setBalance(AVLNode.MORE_RIGHT);
				return new NodeInfo<E, K>(root, depthInit);
			}
			else if (root.getBalance() == AVLNode.MORE_LEFT) {
				root.setBalance(AVLNode.EVEN);
				return new NodeInfo<E, K>(root, stateChange);
			}
			else // root.getBalance () == AVLNode.MORE_RIGHT
			{
				return delRightBalance(root);
			}
		}
		return new NodeInfo<E, K>(root, stateChange);
	} 
	/*
	 * 
	 */
	protected NodeInfo<E, K> comeUpFromRight(AVLNode<E, K> root, int stateChange) {
		if (stateChange == nodeSmaller) {
			if (root.getBalance() == AVLNode.EVEN) {
				root.setBalance(AVLNode.MORE_LEFT);
				return new NodeInfo<E, K>(root, depthInit);
			}
			else if (root.getBalance() == AVLNode.MORE_RIGHT) {
				root.setBalance(AVLNode.EVEN);
				return new NodeInfo<E, K>(root, stateChange);
			}
			else // root.getBalance () == AVLNode.MORE_LEFT
			{
				return delLeftBalance(root);
			}
		}
		return new NodeInfo<E, K>(root, stateChange);
	} 
	/*
	 * 
	 */
	protected NodeInfo<E, K> findReplacement(AVLNode<E, K> change, AVLNode<E, K> node) {
		if (node.getLeft() == null) {
			change.setKey(node.getKey());
			change.setElement(node.getElement());
			node = node.getRight();
			return new NodeInfo<E, K>(node, nodeSmaller);
		} 
		else {
			NodeInfo<E, K> info = findReplacement(change, node.getLeft());
			node.setLeft(info.node);
			return comeUpFromLeft(node, info.stateChange);
		}
	} 
	/*
	 * 
	 */
	public class NodeInfo<E, K extends Sortable> {
		public AVLNode<E, K> node;

		public int stateChange;

		public NodeInfo(AVLNode<E, K> node, int stateChange) {
			this.node = node;
			this.stateChange = stateChange;
		} 
	} 

	
}