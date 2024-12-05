# 1. JDK 이미지 선택
FROM openjdk:17-jdk-slim

# 2. 작업 디렉토리 생성
WORKDIR /app

# 3. Maven 빌드 결과물 JAR 파일 복사
COPY target/member-0.0.1-SNAPSHOT.jar app.jar

# 4. 컨테이너 내부에서 사용할 포트 지정
EXPOSE 8080

# 5. 애플리케이션 실행 명령어
ENTRYPOINT ["java", "-jar", "app.jar"]

