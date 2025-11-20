javac src/Main.java
java src/Main


trap 'rm src/*.class; echo "testing clean"' EXIT
