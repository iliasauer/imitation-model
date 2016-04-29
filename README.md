# imitation-model

## Build

It needs to execute commands 'clean' and  'jar' with gradle (http://gradle.org/). But there is the special gradle wrapper among project files, so you can execute next commands:

### Linux/OSX
$ ./gradlew clean jar

### Windows
$ gradlew clean jar
 
Then the built project will be situated on the path %project_dir%/build/libs. Where %project_dir% is the name of the project directory.

## Run

The program can work in two modes. There are 'Command Line' mode and 'UI' mode.

### Command line mode

To run the built project, execute:

$ java -jar %the_name_of_the_built_project%.jar [parameters splited by the space]

Parameters should be specified in this order:
1. The number of jobs
2. The number of workers
3. The storage capacity
4. The service discipline (LIFO or FIFO) (see https://en.wikipedia.org/wiki/FIFO_and_LIFO_accounting)
5. The average time interval of a next job entry
6. The average time of processing for a next job
7. The number of runs
8. The seed of the random generator (for 5th and 6th parameters) (optional parameter)

For example:

$ java -jar %name_of_built_project%.jar 100 6 10 LIFO 60 180 2

If there was not specified any parameters, then the program start with default parameters: 100 6 10 LIFO 180 60 2

### UI mode

To run the built project, execute:

$ java -jar %path_to_jar% UI

Then open a browser on 127.0.0.1:8080
