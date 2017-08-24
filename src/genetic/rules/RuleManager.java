package genetic.rules;

import init.Settings;
import init.Streams;

import java.util.ArrayList;

public class RuleManager {

	//TODO give rules detailed descriptions
	
	private ArrayList<Rule> list = new ArrayList<Rule>();
	
	public RuleManager() {
		initiateRules();
	}
	
	private void initiateRules() {
		
		// TODO erase hard coded weighting, do it in loadSavePanel stuff
		
		//keep original rules
		list.add(new OriginalRule());
		list.get(list.size()-1).weight = 1.f;
		list.add(new InstrumentsRule());
		list.get(list.size()-1).weight = 0.7f;
		list.add(new TicksRule());
		list.get(list.size()-1).weight = 0.7f;
		
		//random rule
		list.add(new RandomRule());
		list.get(list.size()-1).weight = 0.1f;
		
		//loudness rule
		list.add(new LoudnessRule());
		list.get(list.size()-1).weight = 0.5f;
		
		//action rules
		list.add(new VirtuosoRule());
		list.get(list.size()-1).weight = 0.5f;
		list.add(new ChromaticRule());
		list.get(list.size()-1).weight = 1.f;
		list.add(new HoldsworthRule());
		list.get(list.size()-1).weight = 1.f;
		list.add(new LegatoRule());
		list.get(list.size()-1).weight = 0.5f;
		list.add(new StaccatoRule());
		list.get(list.size()-1).weight = 1.f;
		list.add(new FreeJazzRule());
		list.get(list.size()-1).weight = 1.f;
		list.add(new OstinatoRule());
		list.get(list.size()-1).weight = 1.f;
		list.add(new WideRule());
		list.get(list.size()-1).weight = 0.5f;
		list.add(new PedalRule());
		list.get(list.size()-1).weight = 1.f;
		
		if (Settings.DEBUG)
			Streams.ruleOut.println("Rules initiated.");
	}
	
	public ArrayList<Rule> getList() {
		return this.list;
	}
	
}
