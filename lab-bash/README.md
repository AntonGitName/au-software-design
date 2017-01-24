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
