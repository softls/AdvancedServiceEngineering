# stop script on error
set -e

# run pub/sub sample app using certificates downloaded in package
printf "\nRunning publish application...\n"
mvn exec:java -Dexec.mainClass="Publistroller" -Dexec.args="-clientEndpoint a3kjvw1kd2ijg5.iot.us-west-2.amazonaws.com -clientId sensor_A1 -certificateFile /home/lenaskarlat/.ssh/sensor_A1-certificate.pem.crt -privateKeyFile /home/lenaskarlat/.ssh/sensor_A1-private.pem.key -topic sensors/sensor_A1 -thingName sensor_A1"
