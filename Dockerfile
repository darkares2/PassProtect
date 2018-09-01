FROM java:openjdk-8-jre
COPY ./target/PassProtect-1.0.0-SNAPSHOT.jar /PassProtect.jar
CMD java -jar /PassProtect.jar