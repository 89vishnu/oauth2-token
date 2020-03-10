FROM java:8-jre
MAINTAINER Prasad Prabhakaran

EXPOSE 8008

ENV ENV_PROFILE prod
ADD ./build/libs/auth-service*.jar /app/auth-service.jar
CMD ["java", "-Xmx200m", "-jar", "/app/auth-service.jar"]
