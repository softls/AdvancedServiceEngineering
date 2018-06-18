FROM jeanblanchard/java:8

ENV JARNAME=sensorApplication.jar

ADD target/$JARNAME /target.jar

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/target.jar"]

