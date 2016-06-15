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
		
		//keep original rules
		list.add(new OriginalRule());
		list.add(new InstrumentsRule());
		list.add(new TicksRule());
		
		//random rule
		list.add(new RandomRule());
		
		//loudness rule
		list.add(new LoudnessRule());
		
		//action rules
		list.add(new VirtuosoRule());
		list.add(new ChromaticRule());
		list.add(new HoldsworthRule());
		list.add(new LegatoRule());
		list.add(new StaccatoRule());
		list.add(new FreeJazzRule());
		list.add(new OstinatoRule());
		list.add(new WideRule());
		list.add(new PedalRule());
		
		if (Settings.DEBUG)
			Streams.ruleOut.println("Rules initiated.");
	}
	
	public ArrayList<Rule> getList() {
		return this.list;
	}
	
}
