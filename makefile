FLAGS = -g
JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
    BattleshipGUI.java \
    PlayerOptionsGUI.java \
    MenuScene.java \
    OverScene.java \
   


default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class

run:
	java BattleShip
