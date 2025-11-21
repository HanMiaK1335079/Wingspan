javac src/Main.java
java src/Main

trap 'rm src/*.class; echo "Cleanup finished: src/*.class removed"' EXIT
echo "Script finished."