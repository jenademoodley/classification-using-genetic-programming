package app;

public class Node {
	private String label = "";
	private int arity = 0;
	Node children[] = null;

	Node() {

	}

	Node(String label, int arity) {
		this.label = label;
		this.arity = arity;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public void setArity(int arity) {
		this.arity = arity;
	}

	public int getArity() {
		return this.arity;
	}

	public String getLabel() {
		return this.label;
	}

	public void createChildrenArray() {
		int a = this.getArity();
		this.children = new Node[a];
	}

	public void setChild(int i, Node c) {
		children[i] = c;
	}

	public void displayChildren() {
		for (int i = 0; i < children.length; i++) {
			this.children[i].displayNode();
		}
	}

	public void displayNode() {
		System.out.println("Label: " + this.getLabel() + " Arity: " + this.getArity());
	}

}
