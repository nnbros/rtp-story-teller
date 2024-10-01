FROM openjdk:17-alpine
ARG BOT_TOKEN
ENV RTP_BOT_TOKEN=${BOT_TOKEN}
COPY ./target/*.jar /app/rtp-story-teller.jar
RUN chmod 777 /app/rtp-story-teller.jar
ENTRYPOINT ["java", "-jar", "/app/rtp-story-teller.jar"]
