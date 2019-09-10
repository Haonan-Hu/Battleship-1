FLAGS = -g
JC = javac
.SUFFIXES: .java .class
.java.class:
        $(JC) $(JFLAGS) $*.java

CLASSES = \
        Foo.java \
        Blah.java \
        Library.java \
        Main.java

default: classes

classes: $(CLASSES:.java=.class)

clean:
        $(RM) *.class
