# Start with a base image that includes OpenJDK
FROM openjdk:21-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the build artifact to the container
COPY build/libs/emotionCore-0.0.1-SNAPSHOT.jar app.jar

# Expose the port that the app will run on
EXPOSE 8080

ENV GOOGLE_REDIRECT_URI=https://emotioncores.com/login/oauth2/code/google
ENV GOOGLE_CLIENT_ID=99641822043-1jm8ubb0f99m5jngt439jsklqhisgn70.apps.googleusercontent.com
ENV GOOGLE_CLIENT_SECRET=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

ENV NAVER_REDIRECT_URI=https://emotioncores.com/signin/naver
ENV NAVER_CLIENT_ID=MN6cUHxKhmM0Ki3Bp6ry
ENV NAVER_CLIENT_SECRET=k_UiUuhUEj

ENV KAKAO_REDIRECT_URI=https://emotional-core.vercel.app/auth/oauth2/kakao
ENV KAKAO_CLIENT_ID=70b56d076d4b3cb76a70a83f80021abd
ENV KAKAO_CLIENT_SECRET=wBBUE6jA0A89mh4pddaqH7JQMoxcXKGQ

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]
