package genetic;

import genetic.mutations.MutationManager;
import genetic.rules.Rule;
import genetic.rules.RuleManager;
import init.Settings;
import init.Streams;
import input.InputAnalysis;
import input.InputWindow;
import input.MelodyNote;

import java.util.ArrayList;
import java.util.Collections;

public class Evolution extends ProgressObservable implements Runnable {
	
	//TODO optimize on performance by minimizing declarations
	
	// initial
	public int POPULATION_SIZE = 1;
	public int SLEEP_TIME = 250;
	public int MUTATION_EXPANSION_LIMIT = 5000;
	public int INPUT_WINDOW_SIZE = 1500;

	private ArrayList<DrumPattern> nextGeneration = new ArrayList<DrumPattern>();
	
	private boolean isRunning;
	private boolean stopRequest;
	private boolean isPaused;

	private InputWindow inputWindow;
	// TODO watch out! made public for bassist!
	public InputAnalysis inputAnalysis;
	
	private RuleManager ruleManager;
	private MutationManager mutationManager;
	
	public Evolution(InputWindow inputWindow, RuleManager ruleManager, MutationManager mutationManager) {
		
		this.inputWindow = inputWindow;
		this.ruleManager = ruleManager;
		this.mutationManager = mutationManager;
	}
	
	public void startEvolution() {
		if (!isRunning) {
			new Thread(this).start();
		}
	}
	
	public void stopEvolution() {
		if (isRunning) {
			this.stopRequest = true;
		}
	}
	
	public void resumeEvolution() {
		if (isRunning) {
			this.isPaused = false;
		}
	}
	
	public void pauseEvolution() {
		if (isRunning) {
			this.isPaused = true;
		}
	}
	
	@Override
	public void run() {
		
		this.isRunning = true;
		this.stopRequest = false;
		this.isPaused = false;
		
		initialize();
		
		int round = 0;
		long deltaTime = 0;
		long startTime = System.currentTimeMillis();
		
		while(!stopRequest) {
			
			try { //sleep
				deltaTime = System.currentTimeMillis() - startTime;
				if (SLEEP_TIME - deltaTime > 0) {
					this.alarm(false);
					Thread.sleep(SLEEP_TIME - deltaTime);
				}
				else {
					System.err.println("EVOLUTION: calculation delayed in round "+round+" by "+(deltaTime-SLEEP_TIME)+"ms");
					this.alarm(true);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			
			
			startTime = System.currentTimeMillis();
			
			if (this.isPaused) {
				//MESSAGE debug message evolution paused
				if (Settings.DEBUG) 
					Streams.evolutionOut.println("EVOLUTION: paused");
				
				setProgress(0, "Paused.");
				continue;
			}
			
			setProgress(0, "Starting..");
			
			round++;
			
			setProgress(5, "Copy population..");
			
			// make copy of population
			this.nextGeneration = Generation.getPopulationCopy();
			
			Collections.shuffle(nextGeneration);
			
			setProgress(15, "Mutate..");
			
			mutate();
			
			setProgress(30, "Clear input window..");
			
			ArrayList<MelodyNote> notes = this.inputWindow.getNotesCopy();
			this.inputWindow.clearOlder(System.currentTimeMillis(), INPUT_WINDOW_SIZE);
			
			//MESSAGE debug cleared old notes
			if (Settings.DEBUG) 
				Streams.evolutionOut.println("Removed notes older than "+INPUT_WINDOW_SIZE+"ms from input window.");
			
			setProgress(45, "Analyze input..");
			
			this.inputAnalysis = new InputAnalysis(notes);
			
			//MESSAGE debug inputAnalysis message
			if (Settings.DEBUG) 
				Streams.inputAnalysisOut.println(this.inputAnalysis.toString());
			
			setProgress(60, "Evaluate..");
			
			evaluate();
			
			setProgress(75, "Shuffle generation..");
			
			//write generation
			Collections.shuffle(nextGeneration);
			Collections.sort(nextGeneration);
			
			setProgress(90, "Write population (select)..");
			
			ArrayList<String> stringList = new ArrayList<String>();
			
			ArrayList<DrumPattern> newPopulation = new ArrayList<DrumPattern>();
			DrumPattern p = null;
			for (int i = 0; i < nextGeneration.size() && i < POPULATION_SIZE; i++) {
				
				p = nextGeneration.get(nextGeneration.size()-(i+1));
				
				newPopulation.add(p.copy());
				
				stringList.add(p.toString()+" fitness:"+p.fitness);
			}
			Generation.writePopulation(newPopulation);
			
			this.setStringList(stringList);
			
			//MESSAGE debug population written
			if (Settings.DEBUG) 
				Streams.evolutionOut.println("New population has "+newPopulation.size()+" indiviuals.");
			
			//MESSAGE debug message evolution done
			if (Settings.DEBUG) 
				Streams.evolutionOut.println("EVOLUTION: done in round "+round+", deltaTime:"+deltaTime+"ms/"+SLEEP_TIME+"ms");
		
			setProgress(100, "Finished round "+round+", sleeping for "+SLEEP_TIME+"ms");
			
		}
		
		//MESSAGE debug message evolution ended
		if (Settings.DEBUG) 
			Streams.evolutionOut.println("EVOLUTION: ended.");
		
		setProgress(100, "Evolution ended.");
		
		this.isRunning = false;
		Generation.writePopulation(null);
	}
	
	private void initialize() {
		
		//MESSAGE debug init population started
		if (Settings.DEBUG) 
			Streams.evolutionOut.println("Initialize population..");
		
		ArrayList<DrumPattern> initPopulation = new ArrayList<DrumPattern>();
		for (int i = 0; i < POPULATION_SIZE; i++) {
			initPopulation.add(Generation.getInitPatternCopy());
		}
		Generation.writePopulation(initPopulation);
		
		this.inputAnalysis = new InputAnalysis(null);
		this.inputWindow.clear();
	}
	
	private void mutate() {
		
		//MESSAGE debug mutation started
		if (Settings.DEBUG) 
			Streams.evolutionOut.println("Mutation started..");
		
		ArrayList<ArrayList<DrumPattern>> lists = new ArrayList<ArrayList<DrumPattern>>();
		lists.add(new ArrayList<DrumPattern>());
		
		int counter = nextGeneration.size();
		int random;
		DrumPattern pattern;
		
		while (counter < MUTATION_EXPANSION_LIMIT) {
			
			random = Random.nextInt(nextGeneration.size());
			pattern = nextGeneration.get(random).copy();
			this.mutationManager.useRandomMutation(pattern);
			lists.get(0).add(pattern);
			counter++;
			
			for (int i = 1; counter < MUTATION_EXPANSION_LIMIT
					&& i < lists.size(); i++) {
				if (lists.get(i-1).size()/2 > lists.get(i).size()) {
					random = Random.nextInt(lists.get(i-1).size());
					pattern = lists.get(i-1).get(random).copy();
					this.mutationManager.useRandomMutation(pattern);
					lists.get(i).add(pattern);
					counter++;
				}
			}
			
			if (counter < MUTATION_EXPANSION_LIMIT
					&& lists.get(lists.size()-1).size()>=2) {
				random = Random.nextInt(lists.get(lists.size()-1).size());
				pattern = lists.get(lists.size()-1).get(random).copy();
				this.mutationManager.useRandomMutation(pattern);
				lists.add(new ArrayList<DrumPattern>());
				lists.get(lists.size()-1).add(pattern);
				counter++;
			}

		}
		
		for (ArrayList<DrumPattern> list : lists) {
			nextGeneration.addAll(list);
		}
		
		//MESSAGE debug nextGeneration.size
		if (Settings.DEBUG) 
			Streams.evolutionOut.println("Expanded generation.size to "+nextGeneration.size());
	}

	private void evaluate() {
		
		//MESSAGE debug evaluation started
		if (Settings.DEBUG) 
			Streams.evolutionOut.println("Evaluation started..");
		
		for (Rule rule : ruleManager.getList()) {
			for (DrumPattern pattern : nextGeneration) {
				rule.rate(pattern, inputAnalysis);
			}
		}
	}
}
