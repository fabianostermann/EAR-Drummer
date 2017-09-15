# PatternJazzDrummer
This is a java8 realtime system that generates rhythmical drum patterns outputted via midi to accompany a jazz soloist sending its melodies in via midi.

#Translation of RuleBased Approach to CombinationBased one (see init/Settings.java)
Rule -> SoloFactor, PatternFactor

chromatic -> chromatic, moreTicks
freeJazz -> freeJazz, random
holdworth -> holdsworth, instrumentJumps
keepInstruments -> -, keepInstruments
legato -> legato, moreTicks
keepOriginal -> -, keepOriginal
ostinato -> ostinato, doubleAccent
pedal -> pedal, morePause
random -> random, random
staccato -> staccato, accent
keepTicks -> -, keepTicks
virtuoso -> virtuoso, newInstruments
wide -> wide, allInstruments

LOUDNESS RULE ??
