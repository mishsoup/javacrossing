#!/bin/sh
find ./src -name "*.java" > sources.txt

javac -target 8 -source 8 -classpath ./out/production/javacrossing:./dependency/json-20200518.jar -d ./out/production/javacrossing/  @sources.txt

java  -Dfile.encoding=UTF-8 -classpath ./out/production/javacrossing:./dependency/json-20200518.jar Main

echo "Code has been injected into the input project"

cd ./input/moozeek/

find . -name "*.java" > sources.txt

javac -target 8 -source 8 -classpath ./target/classes:./musicDependency/jfugue-5.0.9.jar -d target/classes/  @sources.txt

java -Dfile.encoding=UTF-8 -classpath ./target/classes:./musicDependency/jfugue-5.0.9.jar ui.Main

echo "Injected code finished running"

cd ../../

java -Dfile.encoding=UTF-8 -classpath ./out/production/javacrossing:./dependency/json-20200518.jar PlotlyMain

echo "Generated JSON input for front end"

cp data.json ./javacrossing-react/src/data.json

echo "Copied the JSON input for front end to javacrossing-react"

cd ./javacrossing-react/

echo "Starting front-end"

# uncomment the line beflow if it is first time running this program to install npm dependencies
#npm install

npm start