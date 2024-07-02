FROM tomcat:9.0-jdk17-openjdk
COPY target/template-0.0.1-SNAPSHOT.jar /app/template.jar
WORKDIR /app
EXPOSE 8080
CMD ["java", "-jar", "template.jar"]