package app;

import java.util.Random;
import java.util.Stack;

public class InitialPopulation {
	static long seed = 0;
	static Random generator = null;
	String terminal[];
	String function[];
	int arities[];
	int classType = 0;

	String PRESCRIPTION[] = { "myope", "hypermetrope" };
	String ASTIGMATIC[] = { "no", "yes" };
	String TEAR_RATE[] = { "reduced", "normal" };

	String COLOUR[] = { "YELLOW", "PURPLE" };
	String SIZE[] = { "LARGE", "SMALL" };
	String ACT[] = { "STRETCH", "DIP" };
	String AGE[] = { "ADULT", "CHILD" };
	String INFLATED[] = { "T", "F" };

	public InitialPopulation(int classType, long seedPass) {

		seed = seedPass;
		generator = new Random(seed);
		setTerminal(classType);
	}

	public void setTerminal(int classType) {

		this.classType = classType;

		if (classType == 1) {
			terminal = new String[] { "F", "T" };
			function = new String[] { "COLOUR", "SIZE", "ACT", "AGE" };
			arities = new int[] { 2, 2, 2, 2 };
		} else {
			AGE = new String[] { "young", "pre-presbyopic", "presbyopic" };
			terminal = new String[] { "0", "1", "2" };
			function = new String[] { "AGE", "PRESCRIPTION", "ASTIGMATIC",
					"TEAR_RATE" };
			arities = new int[] { 3, 2, 2, 2 };
		}

	}

	public Node getFunctionNode(String functionSet[]) {
		Node node;
		int ranNum = (int) randomGenerator(functionSet.length);

		String func = functionSet[ranNum];

		node = new Node(func, arities[ranNum]);

		return node;
	}

	public Node getTerminalNode(String terminalSet[]) {
		Node node = new Node(
				terminalSet[(int) randomGenerator(terminalSet.length)], 0);

		return node;
	}

	public Node grow(int maxDepth, int finalDepth) {
		Node r = new Node();

		if (maxDepth == 0
				|| (randomGenerator(2) == 0 && (maxDepth != finalDepth))) {
			r = getTerminalNode(terminal);
		} else {
			r = getFunctionNode(function);

			r.createChildrenArray();

			for (int i = 0; i < r.getArity(); i++) {
				Node c = new Node();
				c = grow(maxDepth - 1, finalDepth);
				r.setChild(i, c);
			}
		}
		return r;

	}

	public Node full(int maxDepth) {
		Node r = new Node();

		if (maxDepth > 0) {
			r = getFunctionNode(function);

			r.createChildrenArray();

			for (int i = 0; i < r.getArity(); i++) {
				Node c = new Node();
				c = full(maxDepth - 1);
				r.setChild(i, c);
			}

		} else {
			r = getTerminalNode(terminal);

		}
		return r;
	}

	public Node[] ramped(int maxDepth, int pop) {
		int noOfTrees = pop / (maxDepth - 1);
		Node[] population = new Node[pop];
		Node node = new Node();

		int popCount = 0;

		for (int j = 1; j < maxDepth; j++) {
			for (int i = 0; i < noOfTrees / 2; i++) {
				node = grow(j, j);
				population[popCount] = node;
				popCount++;
			}

			for (int i = (noOfTrees / 2); i < noOfTrees; i++) {
				node = full(j);
				population[popCount] = node;
				popCount++;
			}
		}

		if (popCount < pop) {
			int ran = randomGenerator(maxDepth - 1) + 2;
			int ranTree = randomGenerator(2);

			if (ranTree == 0) {
				node = grow(ran, ran);
				population[popCount] = node;
			} else {
				node = full(ran);
				population[popCount] = node;
			}

			popCount++;

		}

		return population;
	}

	public Node[] generatePop(int maxDepth, int method, int pop) {
		Node[] population = new Node[pop];
		Node root = new Node();

		if (method == 1) {
			for (int i = 0; i < pop; i++) {
				root = grow(maxDepth - 1, maxDepth - 1);
				population[i] = root;
			}
		} else if (method == 2) {
			for (int i = 0; i < pop; i++) {
				root = full(maxDepth - 1);
				population[i] = root;
			}
		} else {
			population = ramped(maxDepth, pop);
		}
		return population;
	}

	public void displayPop(Node[] n) {
		for (int i = 0; i < n.length; i++) {
			System.out.println("Individual: " + (i + 1));
			printIndiv(n[i]);
			System.out.println();
		}
	}

	public void printIndiv(Node root) {
		Stack<Node> stack = new Stack<Node>();
		Stack<String> stackArity = new Stack<String>();

		if (root == null)
			return;

		stack.push(root);

		while (!stack.empty()) {

			if (!stackArity.empty()) {
				System.out.print(stackArity.pop() + " ");
			}

			root = stack.pop();
			System.out.print(root.getLabel() + " ");

			for (int i = root.getArity() - 1; i >= 0; i--) {

				switch (root.getLabel()) {

				case "COLOUR":
					stackArity.push(COLOUR[i]);
					break;

				case "SIZE":
					stackArity.push(SIZE[i]);
					break;

				case "ACT":
					stackArity.push(ACT[i]);
					break;

				case "AGE":
					stackArity.push(AGE[i]);
					break;

				case "INFLATED":
					stackArity.push(INFLATED[i]);
					break;

				case "PRESCRIPTION":
					stackArity.push(PRESCRIPTION[i]);
					break;

				case "ASTIGMATIC":
					stackArity.push(ASTIGMATIC[i]);
					break;

				case "TEAR_RATE":
					stackArity.push(TEAR_RATE[i]);
					break;
				}

				stack.push(root.children[i]);
			}
		}
	}

	public int randomGenerator(int noOfFunctions) {
		return (int) generator.nextInt(noOfFunctions);
	}

	public void printIndivNoBranch(Node root) {
		Stack<Node> stack = new Stack<Node>();
		if (root == null)
			return;

		stack.push(root);

		while (!stack.empty()) {
			root = stack.pop();
			System.out.print(root.getLabel() + " ");

			for (int i = root.getArity() - 1; i >= 0; i--) {
				stack.push(root.children[i]);
			}
		}
	}

}
