#!/bin/bash
java  -Dfile.encoding=UTF-8 -classpath ./out/production/javacrossing:./dependency/json-20200518.jar Main

echo "Code has been injected into the input project"

cd ./input/moozeek/

java -Dfile.encoding=UTF-8 -classpath ./target/classes:./musicDependency/jfugue-5.0.9.jar ui.Main

echo "Injected code finished running"

cd ../../

java -Dfile.encoding=UTF-8 -classpath ./out/production/javacrossing:./dependency/json-20200518.jar PlotlyMain

echo "Generated JSON input for front end"

cp data.json ./javacrossing-react/src/data.json

echo "Copied the JSON input for front end to javacrossing-react"

cd ./javacrossing-react/

echo "Starting front-end"

npm start