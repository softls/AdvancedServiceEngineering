# stop script on error
set -e

# run pub/sub sample app using certificates downloaded in package
printf "\nRunning publish application...\n"
mvn exec:java -Dexec.mainClass="PublishController" -Dexec.args="-clientEndpoint endpoint -clientId clientId -certificateFile fileName.cert.pem -privateKeyFile fileName.private.key -topic mainTopic/subTopic -thingName sensorName"
