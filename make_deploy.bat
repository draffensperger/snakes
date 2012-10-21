del *.class
javac *.java
jar cvf snakes.jar *.class
mkdir

jar -cf mazegame.jar draff/awt/*.class draff/awt/event/*.class draff/io/*.class draff/lang/reflect/*.class mazegame/*.class mazecomponents/*.class imagedisplayer/*.class mazegenerator/*.class
rmdir /Q /S _deploy
mkdir _deploy
copy snakes.jar _deploy
copy show_applet.html _deploy
copy *.wav _deploy
copy *.mid _deploy
copy *.jpg _deploy
copy game0.xml _deploy