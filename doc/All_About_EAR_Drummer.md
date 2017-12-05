# About EAR Drummer

The first thing to do is compiling and starting the software.
In order to do so follow the **Setup Guide**.
When the software and its many windows appear on your screen and you got puzzled read on right here.


## What the software wants to do

Simply said, *EAR Drummer* is a reative drum machine. When playing improvised music like jazz music an interactive accompaniment is desired. If there is no human musicians band available to practice playalong records are favoured very often. But records do not react to your playing. Music generating software that is available for personal computers since 20 years from now could do that, but that complex task is often feared by commercial software producers.
*EAR Drummer* tries to fill that gap in the rhythmic area.


## Hardware Setup

**TODO**

## Software Layout

The software will appear on your screen using a multi window layout. Each component of the software is stored in one of the windows. To interact with the software, you first need to know, what you want to do. Then find the window that provides the desired function in order to use it.
The windows are presented in the following.
**Important**: Some windows support loading and saving of presets. They show a corresponding menu at the top. This menu will not repeatedly mentioned in the following.

### Frame Manager Window

This window opens and closes windows. The software will start up showing all of its eleven windows. All of them have their entry in the *Frame Manager*. A closed window is just checked out. Check it in to reopen it.
**Important**: To exit the software close the *Frame Manager* and all windows will disappear!

### Midi Input/Output Manager

These two windows manage your MIDI devices. Choose the desired MIDI instrument input in the checkbox. If no error is printed to the text area at the bottom if the window, your intrument is probably ready to input MIDI events. If so, the event will appear inside the *Input Window*.
For choosing drums output device use the *Midi Output Manager* that looks and works just alike.

### Input Window

This window shows MIDI input events as green beams. Higher notes are displayed above lower notes. The horizontal length describes the length in time. Exact values are not shown. Use this window just to validate an established input MIDI stream.

### DrumPattern Init

This window shows the initial drum pattern, that will be modified inside the evolutionary loop. A pattern consists of eight ticks that will be played on after another. Each instrument (bass, hihat, snare, ride, lowtom, hightom) can be enabled or disabled on each of the ticks by changing the loudness value in each cell. The loudness value is in range 0--127.
**Important**: To change the loudness value right-click on a cell and choose a value from the drop-down list.

### Metronome

To start the rhythmic loop click the start button inside the *Metronome* window. The tempo of the loop can be manipulated by the "Ticks per minute" slider. The "Shuffle factor" slider adds a delay to uneven ticks. The current tick is highlighted at the bottom of the window.

### Evolution Controller

This window controlls the evolution. It takes the initial drum pattern from the *DrumPattern Init* window as starting individual. To start, stop, pause or resume the evolutionary loop use the buttons on the right. Four spinners on the left manipulate the essential parameters of the evolutionary progress.

* *Population size*: How many best individuals are chosen?
* *Sleep time (ms)*: How much time shall the calculation take?
* *Mutation expansion limit*: How many offsprings are created? (Also affects the number of mutations on the same individual.)
* *Input window size (ms)*: How old can notes get before removed from the input window?

The current state of the evolution is shown in the bottom progress bar.

**Important**: Fitness rating is done by rules that are controlled by the *Rule Manager* window.

The mutation of the evolutionary loop is driven by simple random cell mutation.

For more information on evolutionary computation take a look at
https://en.wikipedia.org/wiki/Evolutionary_computation

### Current Drum Pattern

The currently played drum pattern is shown here. This window looks the same like *DrumPattern Init* but can not be manipulated by right-clicking.

### Rule Manager/Combi Manager

This window controls the fitness rating of the individual drum patterns. You can imagine that all generated patterns are rated by a set of rules, that take the patterns characteristics as well as the current played notes by the improvisor into account. The more they fit together the higher the patterns fitness will be and therefore the more likely it will be played.

**TODO** short explanation??

Find a detailed explanation of all the rules in:
"Evaluation Rules for Evolutionary Generation of Drum Patterns in Jazz Solos"
Fabian Ostermann, Igor Vatolkin, Günter Rudolph
https://link.springer.com/chapter/10.1007/978-3-319-55750-2_17?no-access=true

### Solo Recorder

This window provides support for recording a solo. This can be useful when e.g. experiment more on the fitness rating controls.
Just click start and stop to begin and end a record. It will start when the metronome is started or begins the next round.
On playback it will behave the same. Record latency can be corrected manually by the spinner (value in ms).
Metronome settings are also recorded and set by the metronome.

### Midi Keyboard Dummy

This window shows one button that makes the computer keyboard a pentatonic claviature when pressed. Use for tests.


## Audio Examples 
Examples of generated music are available at
http://sig-ma.de/wp-content/uploads/2017/01/JazzDrumPatterns.zip

