FLAGS = -g
JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
	BattleShip.java \
    HelloWorldGUI.java \
    BattleshipGUI.java \
	GameBoard.java \
    PlayerOptions.java \


default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class

run:
	java BattleShip
