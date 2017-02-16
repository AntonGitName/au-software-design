### Task 1: CLI

It is a simple CLI application that imitates shell that has several built-in commands:

* cat [FILE]
* echo
* exit
* pwd
* wc [FILE]
* grep [-e] PATTERN [-i] [-w] [-A value] [-h | --help]


You can also pipe commands and run external executable files (for external files pipe does not work and they always use stdin/stdout/stderr)

---

#### Installation:

1. Clone/Fork
2. Run ```./gradlew :lab-bash:jar``` in root directory of the project

#### Running the shell:

1. Run ```java -jar lab-bash/build/libs/lab-bash-1.0.jar```

#### Tracker
You can track this project at [Pivotal Tracker](https://www.pivotaltracker.com/projects/1959073).

#### Documentation

You can find class diagram under `/doc` folder. It contains compiled pdf file and a source xml that can be opened with [draw.io](https://www.draw.io/).
 
The whole project consists of four logical parts:
1. `Application` class

   It is an entry point of the program that is responsible for interactions with user. Basically, it just delegates all the work to other parts.

2. `cli` package

   Classes in this package responsible for parsing of the user's input and wrapping this information into classes that can be used by the last two parts of the project.
   
3. `CommandExecutor` class from `execution` package

   This class receives wrapped by `cli` package information from the user and translates it into the commands.
    
4. Everything else in `execution` package
 
   These classes represents different executables that our hand-made bash can execute. They receive control from the `CommandExecutor` when user calls them from command line. As it was pointed out in the begging of this readme file, these executables can be of two different types:
   
   * built-in (all of them located in package `execution.builtin` and must be declared in `BuiltInCommands`)
   * external (this type represented by sing class `External`)
   