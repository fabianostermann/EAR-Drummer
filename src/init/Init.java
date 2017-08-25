package init;

import genetic.DrumPattern;
import genetic.Evolution;
import genetic.Generation;
import genetic.mutations.MutationManager;
import genetic.rules.RuleManager;
import gui.DrumPatternFrame;
import gui.EvolutionFrame;
import gui.FrameManager;
import gui.InputManagerFrame;
import gui.InputWindowFrame;
import gui.MetronomeFrame;
import gui.MidiKeyboardDummyFrame;
import gui.OutputManagerFrame;
import gui.PrimitiveBassistFrame;
import gui.RuleManagerFrame;
import gui.SoloRecorderFrame;
import input.InputManager;
import input.InputReceiver;
import input.InputWindow;
import output.BassGenerator;
import output.DrumGenerator;
import output.OutputGenerator;
import output.OutputManager;
import playback.Metronome;
import playback.PatternPlayer;
import playback.PrimitiveBassist;
import record.SoloRecorder;

public class Init {
	
	public static void main(String[] args) {
		
		try {
			
			if (args.length > 0) {
				for (String s : args) {
					if (s.equals("--debug"))
						Settings.DEBUG = true;
				}
			}
			if (Settings.DEBUG) {
				Streams.debugStream.println("debug stream enabled");
				System.out.println("debug stream enabled");
			}
			else {
				Streams.debugStream.println("debug stream is disabled, use argument '--debug' to enable");
				System.out.println("debug stream is disabled, use argument '--debug' to enable");
			}
			
			
			//TODO settings window!
			//needs to be able to set up following (maybe online?): Settings.Ticks, RhythmNote.numberOfDrums, default pattern!
			
			
			//initiate all frames and components
			InputWindow inputWindow = new InputWindow();
			InputReceiver inputReceiver = new InputReceiver(inputWindow);
			InputManager inputManager = new InputManager(inputReceiver);
			new InputManagerFrame(inputManager);
			new InputWindowFrame(inputWindow);
			
			OutputGenerator outputGenerator = new OutputGenerator();
			OutputManager outputManager = new OutputManager(outputGenerator);
			new OutputManagerFrame(outputManager);
			
			Metronome metronome = new Metronome(Settings.TICKS, Settings.TPM, Settings.SWING);
			new MetronomeFrame(metronome);
			DrumGenerator drumGenerator = new DrumGenerator(outputGenerator);
			PatternPlayer patternPlayer = new PatternPlayer(drumGenerator);
			metronome.addMetronomeListener(patternPlayer);
			
			SoloRecorder soloRecorder = new SoloRecorder(inputManager, outputManager, metronome);
			inputReceiver.setRecorder(soloRecorder);
			new SoloRecorderFrame(soloRecorder);
			
			DrumPattern pattern = new DrumPattern(Settings.TICKS);
			
			//TODO: set init pattern empty
			try {
				for (int i = 0; true; i+=4) {
					pattern.set(i, 3, 80);
					pattern.set(i+2, 3, 90);
					pattern.set(i+2, 1, 80);
					pattern.set(i+3, 3, 80);
				}
			} catch (IndexOutOfBoundsException e) {}
			
			new DrumPatternFrame(pattern, metronome, true);
			
			DrumPatternFrame currentPatternFrame = new DrumPatternFrame(pattern, metronome, false);
			patternPlayer.addObserver(currentPatternFrame);
			
			Generation.setInitPattern(pattern);
			
			RuleManager ruleManager = new RuleManager();
			new RuleManagerFrame(ruleManager);
			MutationManager mutationManager = new MutationManager();
//			new MutationManagerFrame(mutationManager);
			Evolution evolution = new Evolution(inputWindow, ruleManager, mutationManager);
			new EvolutionFrame(evolution);
			
			// TODO work on Bassist
			//BassGenerator bassGenerator = new BassGenerator(outputGenerator);
			//PrimitiveBassist bassist = new PrimitiveBassist(bassGenerator, evolution);
			//metronome.addMetronomeListener(bassist);
			//new PrimitiveBassistFrame(bassist);
			
			new MidiKeyboardDummyFrame(outputManager, inputManager);
			
			FrameManager.getInstance().rearrange();
		
		} catch (Exception e) {
			// TODO show error information window
			e.printStackTrace();
			System.exit(1);
		}
	}

}
