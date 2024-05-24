FROM openjdk:17-jdk
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
RUN ln -snf /usr/share/zoneinfo/Asia/Seoul /etc/localtime