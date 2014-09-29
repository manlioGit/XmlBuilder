package structure;

import java.util.ArrayList;
import java.util.List;

public class Node<T> {
	
	private T _data;
	private final Node<T> _parent;
	private final List<Node<T>> _children;

	public Node(T data) {
		this(data, null);
	}
	
	public Node(T data, Node<T> parent) {
		_data = data;
		_parent = parent;
		_children = new ArrayList<Node<T>>();
	}

	public boolean isLeaf(){
		return _children.isEmpty();
	}
	
	public Node<T> addChild(T data){
		_children.add(new Node<T>(data, this));
		return this;
	}
	
	public Node<T> add(T data){
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
