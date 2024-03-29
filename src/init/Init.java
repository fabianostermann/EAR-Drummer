package init;

import genetic.DrumPattern;
import genetic.Evolution;
import genetic.FitnessEvaluator;
import genetic.Generation;
import genetic.combine.CombiManager;
import genetic.mutations.MutationManager;
import genetic.rules.RuleManager;
import gui.CombiManagerFrame;
import gui.ConsoleArea;
import gui.DrumPatternFrame;
import gui.EvolutionFrame;
import gui.FrameManager;
import gui.InputManagerPane;
import gui.InputWindowFrame;
import gui.LoadSavePanel;
import gui.MetronomeFrame;
import gui.MidiKeyboardDummyFrame;
import gui.MidiManagerFrame;
import gui.OutputManagerPane;
import gui.RuleManagerFrame;
import gui.SimpleBassistManagerFrame;
import gui.SoloRecorderFrame;
import gui.StartupDialog;
import input.InputManager;
import input.InputReceiver;
import input.InputWindow;

import javax.swing.JOptionPane;

import output.BassGenerator;
import output.DrumGenerator;
import output.OutputGenerator;
import output.OutputManager;
import playback.Metronome;
import playback.PatternPlayer;
import record.SoloRecorder;
import bass.SimpleBassistManager;

public class Init {
	
	public static void main(String[] args) {
		
		try {
			// show the program start with startup dialog
			StartupDialog startupDialog = new StartupDialog();
			
			if (args.length > 0) {
				for (String s : args) {
					if (s.equals("--debug"))
						Settings.DEBUG = true;
				}
			}
			if (Settings.DEBUG) {
				Streams.debugStream.println("debug stream enabled");
			}
			else {
				Streams.debugStream.println("debug stream is disabled, use argument '--debug' to enable");
				Streams.debugStream.close();
			}
			
			
			//TODO more settings, static or dynamic
			//needs to be able to set up following (maybe online?): Settings.Ticks, RhythmNote.numberOfDrums, default pattern!
			
			//*************************************
			// initiate all frames and components
			//*************************************
			
			startupDialog.incProgress("Setup Input Modules");
			ConsoleArea consoleArea = new ConsoleArea();
			InputWindow inputWindow = new InputWindow();
			InputReceiver inputReceiver = new InputReceiver(inputWindow);
			InputManager inputManager = new InputManager(inputReceiver);
			InputManagerPane inputManagerPane = new InputManagerPane(inputManager, consoleArea);
			new InputWindowFrame(inputWindow);

			startupDialog.incProgress("Setup Output Modules");
			OutputGenerator outputGenerator = new OutputGenerator();
			OutputManager outputManager = new OutputManager(outputGenerator);
			OutputManagerPane outputManagerPane = new OutputManagerPane(outputManager, consoleArea);
			
			new MidiManagerFrame(consoleArea, inputManagerPane, outputManagerPane);
			
			startupDialog.incProgress("Setup Metronome");
			Metronome metronome = new Metronome(Settings.TICKS, Settings.TPM, Settings.SWING);
			new MetronomeFrame(metronome);
			
			startupDialog.incProgress("Setup Generators");
			DrumGenerator drumGenerator = new DrumGenerator(outputGenerator);
			PatternPlayer patternPlayer = new PatternPlayer(drumGenerator);
			metronome.addMetronomeListener(patternPlayer);

			startupDialog.incProgress("Setup Recorder");
			SoloRecorder soloRecorder = new SoloRecorder(inputManager, outputManager, metronome);
			inputReceiver.setRecorder(soloRecorder);
			new SoloRecorderFrame(soloRecorder);
			
			startupDialog.incProgress("Setup Drum Pattern");
			DrumPattern pattern = new DrumPattern(Settings.TICKS);
			new DrumPatternFrame(pattern, metronome, true);
			DrumPatternFrame currentPatternFrame = new DrumPatternFrame(pattern, metronome, false);
			patternPlayer.addObserver(currentPatternFrame);
			Generation.setInitPattern(pattern);
			
			startupDialog.incProgress("Setup Mutation Manager");
			MutationManager mutationManager = new MutationManager();
			// mutation manager currently just uses one mutation so frame is unnecessary
//			new MutationManagerFrame(mutationManager);
			
			startupDialog.incProgress("Setup Finess Evaluator");
			FitnessEvaluator fitnessEvaluator = null;
			RuleManager ruleManager = null;
			CombiManager combiManager = null;
			
			if (Settings.FITNESS_VERSION == Settings.FitnessVersion.RuleBased) {
				fitnessEvaluator = ruleManager = new RuleManager();
				new RuleManagerFrame(ruleManager);
			}
			else if (Settings.FITNESS_VERSION == Settings.FitnessVersion.CombinationBased) {
				fitnessEvaluator = combiManager = new CombiManager();
				new CombiManagerFrame(combiManager);
			} else {
				throw new IllegalStateException("Unknown fitness version: " + Settings.FITNESS_VERSION);
			}
			
			startupDialog.incProgress("Setup Evolution");
			Evolution evolution = new Evolution(inputWindow, fitnessEvaluator, mutationManager);
			new EvolutionFrame(evolution);
			
			startupDialog.incProgress("Setup Bassist");
			BassGenerator bassGenerator = new BassGenerator(outputGenerator);
			SimpleBassistManager simpleBassist = new SimpleBassistManager(bassGenerator);
			metronome.addMetronomeListener(simpleBassist);
			new SimpleBassistManagerFrame(simpleBassist);
			
			startupDialog.incProgress("Open Keyboard Frame");
			new MidiKeyboardDummyFrame(outputManager, inputManager);
			
			//**********************
			// after frame creating
			//**********************
			
			startupDialog.incProgress("Load Default Files");
			LoadSavePanel.loadAllDefaultFiles();
			
			startupDialog.incProgress("Rearrange Frames");
			FrameManager.rearrangeFrames();			

			// close startup frame
			startupDialog.endProgress("System Startup completed!");
			
			FrameManager.showAll();
		
		} catch (Exception e) {
			if (Settings.DEBUG) {
				e.printStackTrace();
				System.exit(1);
			} else {
				e.printStackTrace();
				JOptionPane.showMessageDialog(null, e.getMessage(), "Fatal Error", JOptionPane.ERROR_MESSAGE);
				System.exit(1);
			}
		}
	}

}
