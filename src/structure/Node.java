package structure;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class Node<T> {
	
	private T _data;
	private final Node<T> _parent;
	private final List<Node<T>> _children;
	private boolean _leaf;

	public Node(T data) {
		this(data, null, true);
	}
	
	public Node(T data, Node<T> parent, boolean leaf) {
		_data = data;
		_parent = parent;
		_children = new ArrayList<Node<T>>();
		_leaf = leaf;
	}

	public boolean isLeaf(){
		return _leaf;
	}
	
	public void setLeaf(boolean leaf){
		_leaf = leaf;
	}
	
	public Node<T> addChild(T data){
		_children.add(new Node<T>(data, this, true));
		return this;
	}
	
	public Node<T> add(T data){
		_leaf = false;
		return addChild(data).children().get(children().size() - 1);
	}

	public Node<T> parent() {
		return _parent;
	}

	public Node<T> root(){
		Node<T> root = this;
		while(root.parent() != null) {
			root = root.parent();
		}
		
		return root;
	}
	
	public List<Node<T>> children() {
		return _children;
	}
	
	public T data() {
		return _data;
	}
	
	public void data(T data) {
		_data = data;
	}
	
	@Override
	public String toString() {
		return data().toString();
	}
}
