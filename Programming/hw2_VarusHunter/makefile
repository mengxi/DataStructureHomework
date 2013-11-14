JC = javac
JAR = jar

all: Class main.jar

Class:
	$(JC) *.java -cp "lib/*" -d .

main.jar: Class 
	$(JAR) cfm main.jar VirusHunter.mf VirusHunter/*.class

clean:
	$(RM) -rf main.jar VirusHunter/
