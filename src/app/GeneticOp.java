package app;

import java.util.Stack;

public class GeneticOp {
	InitialPopulation genInitPop;
	Evaluation evalPop;
	Selection tourneySel;
	public static int checkPoint = 0;
	public static int index = 0;

	public GeneticOp(int classType, long seed) {
		genInitPop = new InitialPopulation(classType, seed);
		evalPop = new Evaluation(classType, seed);
		tourneySel = new Selection(classType, seed);
	}

	public Node reproduction(Node parent) {
		return parent;
	}

	public Node mutation(Node parent, int mutDepth, int maxOffspringSize) {

		int mutationPoint = genInitPop.randomGenerator(countNodes(parent));

		Node offSpring = null;
		checkPoint = 0;

		if (mutationPoint == 0) {
			offSpring = genInitPop.grow(mutDepth, mutDepth);
		} else {
			checkPoint = 0;
			offSpring = mutate(mutationPoint, mutDepth, parent);

			if (calHeight(offSpring) + 1 > maxOffspringSize) {
				offSpring = pruneTree(offSpring, maxOffspringSize);
			}
		}
		return offSpring;
	}

	public Node mutate(int mutationPoint, int mutDepth, Node parent) {

		Node o = new Node(parent.getLabel(), parent.getArity());
		Stack<Node> stack = new Stack<Node>();
		Node child;
		stack.push(parent);

		Node[] collect = new Node[10000000];
		int i = 0;

		while (!stack.empty()) {
			child = stack.pop();

			if (checkPoint == mutationPoint) {
				Node subTree = genInitPop.grow(mutDepth, mutDepth + 1);
				child = subTree;
			}

			collect[i] = child;
			i++;
			checkPoint++;

			for (int k = child.getArity() - 1; k >= 0; k--) {
				stack.push(child.children[k]);
			}
		}

		index = 0;
		o = formTree(collect, i);
		collect = null;

		return o;

	}

	public Node formTree(Node[] tree, int i) {
		Node n = new Node();

		if ((i - 1) > index) {
			n = new Node(tree[index].getLabel(), tree[index].getArity());

			n.createChildrenArray();
			for (int j = 0; j < n.getArity(); j++) {
				index++;

				Node c = new Node();
				c = formTree(tree, i);
				n.setChild(j, c);
			}
		} else {
			n = new Node(tree[index].getLabel(), 0);
		}

		return n;
	}

	public Node findSubTree(Node parent, int checkPoint, int crossoverPnt) {
		Node subtree = parent;

		Stack<Node> stack = new Stack<Node>();
		Node child;
		stack.push(parent);

		child = stack.pop();

		for (int k = child.getArity() - 1; k >= 0; k--) {
			stack.push(child.children[k]);
		}

		while (!stack.empty()) {
			child = stack.pop();

			if (checkPoint == crossoverPnt) {
				Node subTree = child;

				return subTree;
			}

			checkPoint++;

			for (int k = child.getArity() - 1; k >= 0; k--) {
				stack.push(child.children[k]);
			}
		}

		return subtree;
	}

	public Node pruneTree(Node parent, int maxSize) {
		Node o = new Node(parent.getLabel(), parent.getArity());
		Stack<Node> stack = new Stack<Node>();
		Node child;
		stack.push(parent);

		Node[] collect = new Node[100000];
		int i = 0;
		int parentHeight = calHeight(parent) + 1;

		while (!stack.empty()) {
			child = stack.pop();
			int checkDepth = parentHeight - calHeight(child);

			if (checkDepth == maxSize) {
				child = genInitPop.getTerminalNode(genInitPop.terminal);
			}

			collect[i] = child;
			i++;

			for (int k = child.getArity() - 1; k >= 0; k--) {
				stack.push(child.children[k]);
			}
		}

		index = 0;
		o = formTree(collect, i);
		collect = null;
		return o;
	}

	public Node cross(Node parent, Node subTree, int checkPoint, int crossoverPnt, int maxOffspringSize) {
		Node o = new Node(parent.getLabel(), parent.getArity());
		Stack<Node> stack = new Stack<Node>();
		Node child;
		stack.push(parent);

		Node[] collect = new Node[100000];
		int i = 0;

		while (!stack.empty()) {
			child = stack.pop();

			if (checkPoint == crossoverPnt) {
				child = subTree;
			}

			collect[i] = child;
			i++;
			checkPoint++;

			for (int k = child.getArity() - 1; k >= 0; k--) {
				stack.push(child.children[k]);
			}
		}

		index = 0;
		o = formTree(collect, i);
		collect = null;
		return o;

	}

	public Node[] crossover(Node parent1, Node parent2, int maxOffspringSize) {
		Node[] offSprings = new Node[2];
		Node offSpring1 = parent1;
		Node offSpring2 = parent2;
		Node subTree1;
		Node subTree2;
		int crossoverPnt1 = genInitPop.randomGenerator(countNodes(parent1));
		int crossoverPnt2 = genInitPop.randomGenerator(countNodes(parent2));
		// int i = 0;

		int checkPoint1 = 0;
		int checkPoint2 = 0;

		checkPoint1++;
		subTree1 = findSubTree(parent1, checkPoint1, crossoverPnt1);

		checkPoint2++;
		subTree2 = findSubTree(parent2, checkPoint2, crossoverPnt2);
		checkPoint1 = 0;
		checkPoint2 = 0;

		offSpring1 = cross(parent1, subTree2, checkPoint1, crossoverPnt1, maxOffspringSize);

		offSpring2 = cross(parent2, subTree1, checkPoint2, crossoverPnt2, maxOffspringSize);

		if (calHeight(offSpring1) + 1 > maxOffspringSize) {
			offSpring1 = pruneTree(offSpring1, maxOffspringSize);
		}

		if (calHeight(offSpring2) + 1 > maxOffspringSize) {
			offSpring2 = pruneTree(offSpring2, maxOffspringSize);
		}

		offSprings[0] = offSpring1;
		offSprings[1] = offSpring2;

		return offSprings;
	}

	public int countNodes(Node root) {
		int nodeCount = 0;
		Stack<Node> stack = new Stack<Node>();
		if (root == null)
			return nodeCount;

		stack.push(root);

		while (!stack.empty()) {
			root = stack.pop();
			nodeCount++;

			for (int i = root.getArity() - 1; i >= 0; i--) {
				stack.push(root.children[i]);
			}
		}

		return nodeCount;
	}

	public int calHeight(Node n) {

		if (n == null) {
			return 0;
		} else {

			if (n.getArity() == 2) {
				return 1 + Math.max(calHeight(n.children[0]), calHeight(n.children[1]));
			} else if (n.getArity() == 1) {
				return 1 + calHeight(n.children[0]);
			} else if (n.getArity() == 3) {
				return 1 + Math.max(calHeight(n.children[0]),
						Math.max(calHeight(n.children[2]), calHeight(n.children[1])));
			} else if (n.getArity() == 4) {
				return 1 + Math.max(Math.max(calHeight(n.children[0]), calHeight(n.children[1])),
						Math.max(calHeight(n.children[2]), calHeight(n.children[3])));
			}
			return 0;
		}
	}

	public int calMaxNodes(int n) {

		int numNodes = 0;
		if (n == 0)
			return numNodes;
		else {
			int sum = 2;
			numNodes = 1;
			for (int i = 1; i < n; i++) {
				numNodes += sum;
				sum = sum + sum;

			}
		}

		return numNodes;
	}

}
