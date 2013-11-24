JC = javac
JAR = jar
SOURCE = Flags.java  Interface.java  Logging.java  ReadWriteFile.java  VirusDatabase.java

all: Class main.jar

Class:
	$(JC) $(SOURCE) -cp "lib/*" -d .

main.jar: Class 
	$(JAR) cfm main.jar VirusHunter.mf VirusHunter/*.class

clean:
	$(RM) -rf main.jar VirusHunter/
