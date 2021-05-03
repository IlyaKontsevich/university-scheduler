FROM openjdk:11
EXPOSE 8770
ADD ./build/libs/scheduler-1.0.jar /opt/scheduler-1.0.jar
ENTRYPOINT [ "java", "-jar", "/opt/scheduler-1.0.jar" ]
CMD ["--spring.profiles.active=docker"]
