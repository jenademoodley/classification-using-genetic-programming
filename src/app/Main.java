package app;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class Main {
	Node[] populi = null;

	public static void main(String[] args) {
		long seed = 0;
		int popSize = 0;
		int maxDepth = 0;
		int method = 0;
		int tournSize = 0;
		String[][] fitCase = null;
		int countFc = 0;
		int countFcCol = 0;

		String[] paramVals = new String[10];
		int classType = 0;
		int maxOffspringSize = 0;
		int maxMutationDepth = 0;
		int repRate = 0;
		int mutRate = 0;
		int crossRate = 0;
		int numGens = 0;

		Main m = new Main();
		Scanner sc = new Scanner(System.in);

		System.out
				.println("Choose Dataset (Number only) \n1-balloons.txt\n2-lenses.txt");
		classType = sc.nextInt();

		String file = "";
		switch (classType) {
		case 1:
			file = "balloons.txt";
			break;
		case 2:
			file = "lenses.txt";
			break;
		default:
			System.out.println("Invalid Option");
			System.exit(0);
			break;
		}

		String params = "input.txt";

		String[][] fc1 = new String[100][7];

		Scanner contents = null;

		try {
			contents = new Scanner(new FileReader(file));

			while (contents.hasNext()) {
				String x = contents.nextLine();

				if (file.equals("balloons.txt")) {
					classType = 1;
					String[] a = x.split(",");
					countFcCol = a.length;

					for (int i = 0; i < countFcCol; i++) {
						fc1[countFc][i] = a[i];
					}
				}

				else {
					String[] a = x.split("\\s+");
					countFcCol = a.length - 1;
					fc1[countFc][0] = a[a.length - 1];
					for (int i = 1; i < countFcCol; i++) {
						fc1[countFc][i] = a[i];
					}
				}

				countFc++;
			}
			contents = new Scanner(new FileReader(params));

			for (int i = 0; i < paramVals.length; i++) {
				paramVals[i] = contents.next();
			}

			contents.close();
		} catch (FileNotFoundException e) {
			System.out.println("Error " + e.getMessage());
		}

		fitCase = new String[countFc][countFcCol];

		for (int i = 0; i < countFc; i++) {
			for (int j = 0; j < countFcCol; j++) {
				fitCase[i][j] = fc1[i][j];
			}
		}

		fc1 = null;

		try {
			sc = new Scanner(new File(params));

			while (!sc.hasNextLong())
				sc.next();

			seed = sc.nextLong();
			System.out.println("Seed:\t\t\t" + seed);

			while (!sc.hasNextInt())
				sc.next();
			popSize = sc.nextInt();
			System.out.println("Population:\t\t" + popSize);

			while (!sc.hasNextInt())
				sc.next();
			maxDepth = sc.nextInt();
			System.out.println("Maximum Tree Depth:\t" + maxDepth);

			while (!sc.hasNextInt())
				sc.next();

			method = sc.nextInt();
			System.out.println("Method:\t\t\t" + method);

			while (!sc.hasNextInt())
				sc.next();

			tournSize = sc.nextInt();
			System.out.println("Tournament Size:\t" + tournSize);

			while (!sc.hasNextInt())
				sc.next();
			maxOffspringSize = sc.nextInt();
			System.out.println("Maximum Offspring Size:\t" + maxOffspringSize);

			while (!sc.hasNextInt())
				sc.next();
			maxMutationDepth = sc.nextInt();
			System.out.println("Maximum Mutation Depth:\t" + maxMutationDepth);

			while (!sc.hasNextInt())
				sc.next();

			repRate = sc.nextInt();
			System.out.println("Reproduction Rate:\t" + repRate + "%");

			while (!sc.hasNextInt())
				sc.next();

			mutRate = sc.nextInt();
			System.out.println("Mutation Rate:\t\t" + mutRate + "%");

			while (!sc.hasNextInt())
				sc.next();

			crossRate = sc.nextInt();
			System.out.println("Crossover Rate:\t\t" + crossRate + "%");

			while (!sc.hasNextInt())
				sc.next();

			numGens = sc.nextInt();
			System.out.println("Number of Offspring:\t" + numGens + "\n");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		sc.close();

		m.evolve(popSize, maxDepth, method, numGens, mutRate, crossRate,
				repRate, maxMutationDepth, maxOffspringSize, fitCase,
				tournSize, classType, seed);
	}

	public void evolve(int popSize, int maxDepth, int method, int numGens,
			int mutRate, int crossRate, int repRate, int maxMutationDepth,
			int maxOffspringSize, String[][] fitCase, int tournSize,
			int classType, long seed) {
		Node best = new Node();
		Node current = new Node();

		InitialPopulation genInitPop = new InitialPopulation(classType, seed);
		Evaluation ts = new Evaluation(classType, seed);

		String[] bestEval = new String[fitCase.length];
		int besttHR = 0;

		populi = new Node[popSize];
		populi = genInitPop.generatePop(maxDepth, method, popSize);

		boolean fin = false;

		String[] evaluatePop[] = ts.evaluatePop(populi, fitCase);
		int[] rawFitness = new int[popSize];
		rawFitness = ts.rawFit(popSize, fitCase, evaluatePop);

		Node s = new Node();
		s = checkSolution(populi, rawFitness, fitCase.length);
		if (s != null) {
			fin = true;
			System.out.println();
			System.out.println("Solution!");
			System.out.println("Found in initial population (generation 0) ");
			genInitPop.printIndivNoBranch(s);
			System.out.println();
		}

		for (int i = 0; i < numGens && !fin; i++) {
			current = reGen(popSize, mutRate, crossRate, repRate,
					maxMutationDepth, maxOffspringSize, fitCase, tournSize,
					classType, seed);

			String[] currentEval = ts.evaluate(current, fitCase);
			int currentHR = ts.rawF(fitCase, currentEval);

			if (currentHR == fitCase.length) {
				fin = true;
				System.out.println();
				System.out.println("Solution!");
				System.out.println("Found in generation " + (i + 1));
				System.out.println("Accuracy : 100% ");
				genInitPop.printIndivNoBranch(current);
				System.out.println();
			} else {
				if (best == null) {
					best = current;
					bestEval = ts.evaluate(best, fitCase);
					besttHR = ts.rawF(fitCase, bestEval);
				} else if (currentHR > besttHR) {
					bestEval = currentEval;
					besttHR = currentHR;
					best = current;
				}

			}

		}
		if (s == null && fin == false) {
			System.out.println();
			System.out.println("No Solution Found");
			System.out.println("Best Individual");
			genInitPop.printIndivNoBranch(best);
			System.out.println("");
			System.out.println("Hit Ratio " + besttHR);
			double acc = ((double) besttHR / fitCase.length) * 100;
			System.out.printf("Accuracy : %.2f%s ", acc, "%");
		}
	}

	public Node reGen(int popSize, int mutRate, int crossRate, int repRate,
			int maxMutationDepth, int maxOffspringSize, String[][] fitCase,
			int tournSize, int classType, long seed) {
		InitialPopulation genInitPop = new InitialPopulation(classType, seed);
		Evaluation ts = new Evaluation(classType, seed);
		GeneticOp go = new GeneticOp(classType, seed);
		Selection select = new Selection(classType, seed);

		Node best = new Node();
		Node[] newPop = new Node[popSize];

		int rInds = (int) (popSize * ((double) repRate / 100));
		int cInds = (int) (popSize * ((double) crossRate / 100)) / 2;
		int mInds = (int) (popSize * ((double) mutRate / 100));
		int diff = popSize - (rInds + cInds + mInds);

		int ran = genInitPop.randomGenerator(3);
		if (ran == 0)
			rInds += diff;
		else if (ran == 1)
			mInds += diff;
		else
			cInds += diff;

		if (cInds % 2 > 0) {
			cInds -= 1;
			mInds += 1;
		}

		String[] evaluatePop[] = ts.evaluatePop(populi, fitCase);
		int[] rawFitness = new int[popSize];
		rawFitness = ts.rawFit(popSize, fitCase, evaluatePop);

		int j = 0;

		for (int i = 0; i < rInds; i++) {
			Node[] tournament = select.tournamentSelection(populi, tournSize,
					rawFitness);
			newPop[j] = go.reproduction(tournament[tournament.length - 1]);
			j++;
		}

		for (int i = 0; i < mInds; i++) {
			Node[] tournament = select.tournamentSelection(populi, tournSize,
					rawFitness);
			newPop[j] = go.mutation(tournament[tournament.length - 1],
					maxMutationDepth, maxOffspringSize);
			j++;
		}

		for (int i = 0; i < cInds / 2; i++) {
			Node[] tournament = select.tournamentSelection(populi, tournSize,
					rawFitness);
			Node[] tournament1 = select.tournamentSelection(populi, tournSize,
					rawFitness);
			Node[] offspring = go.crossover(tournament[tournament.length - 1],
					tournament1[tournament.length - 1], maxOffspringSize);

			newPop[j] = offspring[0];
			j++;
			newPop[j] = offspring[1];
			j++;
		}

		String[] evaluateNewPop[] = ts.evaluatePop(newPop, fitCase);
		int[] rawFitnessNewPop = new int[popSize];
		rawFitnessNewPop = ts.rawFit(popSize, fitCase, evaluateNewPop);

		Node s = new Node();
		s = checkSolution(newPop, rawFitnessNewPop, fitCase.length);
		if (s != null) {
			best = s;
		} else {
			best = newPop[0];
			int bestHit = rawFitnessNewPop[0];
			for (int i = 1; i < popSize; i++) {
				if (rawFitnessNewPop[i] > bestHit) {
					best = newPop[i];
					bestHit = rawFitnessNewPop[i];
				}
			}
		}

		populi = newPop;
		newPop = null;

		return best;
	}

	public Node checkSolution(Node[] popn, int[] rf, int fcLength) {
		boolean flag = false;
		Node soln = null;

		for (int i = 0; i < popn.length && !flag; i++) {
			if (rf[i] == fcLength) {
				flag = true;
				soln = new Node();
				soln = popn[i];
			}
		}

		return soln;
	}

}
