FROM openjdk:8-jdk-alpine
VOLUME /tmp
ENV TZ=Asia/Yekaterinburg
COPY target/*.jar demo.jar
ENTRYPOINT ["java","-jar","/demo.jar"]
