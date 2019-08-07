// The "NodeInfo" class.
// This class is used to return an AVLNode and whether the node has  
// become taller (i.e. its depth has increased) which is indicated by setting
// stateChange to NODE_IS_TALLER, shorter (i.e its depth has decreased) 
// which is indicated by setting stateChange to NODE_IS_SHORTER, or  
// stayed the same, which is indicated by setting stateChange to 
// DEPTH_UNCHANGED.

public class NodeInfo<E, K extends Sortable> {
	public AVLNode<E, K> node;

	public int stateChange;

	public NodeInfo(AVLNode<E, K> node, int stateChange) {
		this.node = node;
		this.stateChange = stateChange;
	} // NodeInfo constructor
} /* NodeInfo class */