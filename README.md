A Java library that makes it simple to write 6502 Assembler for the Atari 2600 as a Java program, then run it, 
which compiles it into a 4k Cartridge and runs it on a the Javatari Atari 2600 Emulator. 
This allows for very quick development cycles.

Back in the 70s and 80s, writing for the Atari would have been very difficult and would typically involve slow
assemblers.

This small library allows you to write 6502 instructions as regular java function calls. It supports most legal instructions
(there are some that I've not added yet) and will support forward and backwards labels.

To run the program, you simply run the main function of your class, which will compile your code into 6502 assembler and will
then package that up as a 4k Atari 2600 cartridge, before starting up and loading it into the Javatari Emulator.

I've included a small HelloWorld example that you can run.

This will produce a frame full of coloured bars.

I've not included any build scripts. I wrote this in Intellij. If anyone wishes to contribute to this project, feel free
to expand the features.

Thanks go our to https://github.com/ppeccin/javatari for their brilliant Atari 2600 emulator. I hope I'm not violating any 
of their licence agreements by re-shipping their code with this project. If I am, please let me know and I'll see if
I can do something about it. This is meant to be for fun!
