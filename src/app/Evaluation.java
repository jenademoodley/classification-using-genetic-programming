package app;

public class Evaluation {
	InitialPopulation genInitPop;

	public Evaluation(int classType, long seed) {
		genInitPop = new InitialPopulation(classType, seed);
	}

	public void printEvalIndPop(Node[] pop, String[][] fitnessCases,
			String[] evaluationPop[], int[] raw) {
		for (int i = 0; i < pop.length; i++) {
			System.out.println("Individual " + (i + 1));
			printEvalInd(pop[i], fitnessCases, evaluationPop[i], raw[i]);
		}
	}

	public void printEvalInd(Node root, String[][] fitnessCases,
			String[] evaluation, int rawf) {
		genInitPop.printIndiv(root);
		System.out.println("");
		System.out.println("Fitness\t\tEvaluation");
		for (int i = 0; i < fitnessCases.length; i++) {
			for (int j = 0; j < fitnessCases[0].length; j++) {
				System.out.print(fitnessCases[i][j] + " ");
			}
			System.out.println("\t\t" + evaluation[i]);
		}
		System.out.println("Raw fitness : " + rawf);
		System.out.println();
	}

	public int[] rawFit(int popSize, String[][] fitnessCases,
			String[] evaluation[]) {
		int[] raw = new int[popSize];

		for (int i = 0; i < popSize; i++) {
			raw[i] = rawF(fitnessCases, evaluation[i]);
		}

		return raw;
	}

	public int rawF(String[][] fitnessCases, String[] evaluation) {
		int raw = 0;
		if (fitnessCases[0].length == 5 && genInitPop.classType == 1) {
			for (int i = 0; i < evaluation.length; i++) {
				String checkValue = evaluation[i];
				String fc = (fitnessCases[i][4]);
				if ((checkValue.equals(fc))) {
					raw++;
				}
			}
		} else {
			for (int i = 0; i < evaluation.length; i++) {
				int checkValue = Integer.parseInt(evaluation[i]);
				int fc = Integer.parseInt(fitnessCases[i][0]);
				if ((checkValue == 0 && fc == 3)
						|| (checkValue == 2 && fc == 1)
						|| (checkValue == 1 && fc == 2)) {
					raw++;
				}
			}

		}

		return raw;
	}

	public String[][] evaluatePop(Node[] population, String[][] fitnessCases) {
		int popSize = population.length;
		String[] popEval[] = new String[popSize][];

		for (int i = 0; i < popSize; i++) {
			popEval[i] = evaluate(population[i], fitnessCases);
		}

		return popEval;
	}

	public String[] evaluate(Node root, String[][] fitnessCases) {
		String[] output = new String[fitnessCases.length];
		String target = "";

		for (int i = 0; i < fitnessCases.length; i++) {
			if (genInitPop.classType == 1) {
				target = fitnessCases[i][4];
			} else {
				target = fitnessCases[i][0];
			}
			String[] fc = new String[(fitnessCases[0].length) - 1];

			for (int j = 0; j < fc.length; j++) {
				fc[j] = fitnessCases[i][j + 1];
			}

			output[i] = eval(root, fc, target);
		}
		return output;
	}

	public String eval(Node root, String[] fitnessCases, String target) {
		String output = "";

		while (root.getArity() != 0) {
			int branch = 0;
			String branchValue = "";

			for (int i = 0; i < genInitPop.function.length; i++) {
				if (root.getLabel().equals(genInitPop.function[i])) {

					branchValue = fitnessCases[i];
					if (branchValue.equals("F") || branchValue.equals("YELLOW")
							|| branchValue.equals("LARGE")
							|| branchValue.equals("STRETCH")
							|| branchValue.equals("ADULT")) {
						branch = 0;
					} else if (branchValue.equals("T")
							|| branchValue.equals("PURPLE")
							|| branchValue.equals("SMALL")
							|| branchValue.equals("DIP")
							|| branchValue.equals("CHILD")) {
						branch = 1;
					} else {
						branch = Integer.parseInt(branchValue) - 1;
					}
				}
			}

			root = root.children[branch];
		}

		output = root.getLabel();
		return output;
	}

}
