package app;

public class Selection {
	InitialPopulation genInitPop;

	public Selection(int objectsToClassify, long seed) {
		genInitPop = new InitialPopulation(objectsToClassify, seed);
	}

	public Node[] tournamentSelection(Node[] populi, int tourneySize, int[] rawFit) {
		Node[] tourn = new Node[tourneySize + 1];
		int[] tournFit = new int[tourneySize];
		int rand = 0;

		for (int i = 0; i < tourneySize; i++) {
			rand = genInitPop.randomGenerator(populi.length);

			tourn[i] = populi[rand];
			tournFit[i] = rawFit[rand];
		}

		Node max = tourn[0];
		int maxIndex = 0;

		for (int i = 1; i < tourneySize; i++) {
			if (tournFit[i] > tournFit[maxIndex]) {
				max = tourn[i];
				maxIndex = i;
			}
		}
		tourn[tourneySize] = max;
		return tourn;
	}

}
