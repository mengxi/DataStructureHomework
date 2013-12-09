JC = javac
JAR = jar
SOURCE = Cities.java File_util.java MyGraphMap13.java CmdInterface.java Flags.java mainf13.java myRandom.java Dijkstra.java Logging.java
PACKAGE = hw3_graph

all: Class main.jar

Class:
	$(JC) $(SOURCE) -cp "lib/*" -d .

main.jar: Class 
	$(JAR) cfm main.jar $(PACKAGE).mf $(PACKAGE)/*.class

clean:
	$(RM) -rf main.jar $(PACKAGE)/
