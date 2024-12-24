# application.properties 설정
```properties
spring.application.name=member

spring.datasource.driver-class-name=org.postgresql.Driver
spring.datasource.url=jdbc:postgresql://localhost:5432/postgres
spring.datasource.username=postgres
spring.datasource.password=manager

#mybatis-mapper
mybatis.mapper-locations=classpath:/mybatis/mapper/**/*.xml
mybatis.type-aliases-package=com.ksj.vo
mybatis.configuration.map-underscore-to-camel-case=true

#log-level
logging.level.root=debug

#Swagger
spring.mvc.pathmatch.matching-strategy=ant_path_matcher
```

# DB
### create문

```
CREATE TABLE member (
id          VARCHAR(100) NOT NULL PRIMARY KEY, -- 회원 ID
name        VARCHAR(100) NOT NULL,            -- 회원 이름
passwd      VARCHAR(300),                     -- 비밀번호
cell_phone  VARCHAR(20),                      -- 휴대폰 번호
email       VARCHAR(100),                     -- 이메일 주소
zip_code    VARCHAR(5),                       -- 우편번호
address     VARCHAR(100),                     -- 주소
status      CHAR(1)                           -- 상태 (활성화 여부)
);


-- 컬럼에 주석 추가
COMMENT ON COLUMN member.id IS '고유 ID';
COMMENT ON COLUMN member.name IS '이름';
COMMENT ON COLUMN member.passwd IS '비밀번호';
COMMENT ON COLUMN member.cell_phone IS '휴대폰 번호';
COMMENT ON COLUMN member.email IS '이메일 주소';
COMMENT ON COLUMN member.zip_code IS '우편번호';
COMMENT ON COLUMN member.address IS '상세 주소';
COMMENT ON COLUMN member.status IS '회원 상태 (Y: 활성, N: 비활성)';
```

### insert문
```
INSERT INTO member (id, name, passwd, cell_phone, email, zip_code, address, status) VALUES
('M001', '홍길동', 'password123', '010-1234-5678', 'hong@gmail.com', '12345', '서울특별시 강남구', 'Y'),
('M002', '김철수', 'securepass456', '010-2345-6789', 'chulsoo@gmail.com', '54321', '부산광역시 해운대구', 'Y'),
('M003', '이영희', 'mypassword789', '010-3456-7890', 'younghee@gmail.com', '10101', '대구광역시 수성구', 'Y'),
('M004', '박민수', 'pass1234', '010-4567-8901', 'minsoo@gmail.com', '20202', '광주광역시 북구', 'N'),
('M005', '최지영', 'pass5678', '010-5678-9012', 'jiyeong@gmail.com', '30303', '인천광역시 남동구', 'Y'),
('M006', '장윤아', 'secure098', '010-6789-0123', 'yuna@gmail.com', '40404', '울산광역시 중구', 'N'),
('M007', '윤재희', 'mypassword321', '010-7890-1234', 'jaehee@gmail.com', '50505', '대전광역시 서구', 'Y'),
('M008', '백승호', 'abc123', '010-8901-2345', 'seungho@gmail.com', '60606', '경기도 성남시', 'Y'),
('M009', '오현지', 'pass9999', '010-9012-3456', 'hyunji@gmail.com', '70707', '경상남도 창원시', 'N'),
('M010', '정유진', 'secure555', '010-0123-4567', 'yujin@gmail.com', '80808', '충청북도 청주시', 'Y');

commit;
```


## 11.27 정리
```java
@ModelAttribute MemberVO memberVO;
@RequestParam Map<String, String> param;
```
@ModelAttribute를 쓰면 내가 만든 VO에 바로 담을 수 있다.

@RequestParam을 쓰면 param에서 뽑아서 VO에 다시 담아서 Service단으로 넘겨야 함.
 
* 데이터만 통신할 시에는 RestController 이용.

* json data 통신의 경우
  * @ResponseBody, @RequestBody 사용.

* form data 통신의 경우
  * 낱개로 받을 때
    * @RequestParam("변수명") String [변수명]

  * 여러개 받을 때
    * @RequestParam Map<String, String> param

* DTO, VO를 따로 쓸 때가 있음.
  * DTO : 클라이언트-서버 단 통신 시 이용
  * VO : DB와의 통신 시 이용
    * 이 둘의 변수가 같을 시 자동매핑해주는 함수도 있음. 직접 구현도 가능.

## 12.06 정리
* .loginProcessingUrl("/login/member_login") ----> loadUserByUsername()
```javascript
<!-- 기존코드 -->
<div class="login-container">
    <h1>로그인</h1>
    <form id="loginForm" th:action="@{/member_main.page}" method="post">
        <input type="text" id="id" name="id" placeholder="아이디" required />
        <input type="password" id="passwd" name="passwd" placeholder="비밀번호" required />
        <button type="button" th:onclick="chkLogin()">로그인</button>
    </form>
    <a th:href="@{/login/member_register.page}">회원가입</a>
</div>

<!-- SpringSecurity 쓰면서바뀐 코드 -->
<div class="login-container">
    <h1>로그인</h1>
    <form id="loginForm" th:action="@{/login/member_login}" method="post">
        <input type="text" id="id" name="id" placeholder="아이디" required />
        <input type="password" id="passwd" name="passwd" placeholder="비밀번호" required />
        <button type="button" th:onclick="chkLogin()">로그인</button>
    </form>
    <a th:href="@{/login/member_register.page}">회원가입</a>
</div>
```
* th:action="@{/member_main.page}" --> th:action="@{/login/member_login}"
* 기존 /member_main.page는 로그인 버튼(chkLogin())누르면 ajax 이용해서 
  ```java
  memberService.selectMemberOne(memberVO);
  ```
  호출해서 회원 조회 sql 호출해서 status 값이 Y인지 N인지 확인 후 N이면 이메일 인증값에 대해 isStatusN return, Y면 이메일 인증값에 대해
  ```java
  memberService.loginMember(memberVO);
  ```
  로그인 호출 하여 아이디, 비밀번호 들고가서 계정이 맞는지 확인 후, loginForm.submit()하여 action에 위치한 member_main.page로 화면이동 하는 로직이었음.
* **
* 바뀐 /login/member_login는 
  ```java
  .formLogin(formLogin -> formLogin
          .loginPage("/login/member_login.page")
          .loginProcessingUrl("/login/member_login")
          .usernameParameter("id")
          .passwordParameter("passwd")
          .defaultSuccessUrl("/member_main.page")
          .permitAll()
  )
  ```
  (Config설정에 .formLogin()에서 셋팅을 먼저 한다.)
  Config설정에 셋팅이 되어있으니 loginForm.submit() 했을 때 /login/member_login(로그인 처리 URL) 찾아 security 설정 되어있는 곳으로 연결됌.
  id를 loadUserByUsername() 여기로 보내서
  ```java
  memberMapper.selectMemberPasswd(memberVO);
  ```
  그 아이디의 passwd, role를 조회해오고 User.builder() 하면
  UserDetails 객체를 생성하고 이 객체는 SpringSecurity가 내부적으로 사용자 인증 및 권한 관리를 처리할 때 사용 됌.

## 12.17 정리

## 12.22 정리
### AWS 환경구성
* tomcat 설치
```shell
wget http://archive.apache.org/dist/tomcat/tomcat-9/v9.0.98/bin/apache-tomcat-9.0.98.tar.gz
tar zxvf apache-tomcat-9.0.98.tar.gz
```
* java 설치
```shell
sudo apt upgrade
sudo apt-get update
sudo apt install openjdk-17-jdk

```
* DB 설치 
```shell
sudo apt show postgresql-16
sudo sh -c 'echo "deb http://apt.postgresql.org/pub/repos/apt $(lsb_release -cs)-pgdg main" > /etc/apt/sources.list. d/pgdg.list'
sudo apt-get update
sudo apt-get -y install postgresql
psql --version

```
* DB 계정 생성
```shell
[ubuntu]sudo -i -u postgres
psql
create user sjkang password '1234' superuser;
\du --> 계정 조회
create database sjdb owner sjkang;
\l --> database 조회
exit
[ubuntu]sudo service postgresql restart

error남 설정 추가해야함
vi /etc/postgresql/16/main/pg_hba.conf
# "local" is for Unix domain socket connections only
local   all             all                                     md5
# IPv4 local connections:
host    all             all             0.0.0.0/0               scram-sha-256
vi postgresql.conf
listen_addresses = '*' 

[ubuntu]sudo service postgresql restart
[ubuntu]sudo -i -u postgres
psql -U sjkang -d sjdb
select * from PG_DATABASE;
```

* prod 설정 
   * application-prod.properties 하나 더 만들기 -> aws private ip 넣어서 설정하기
   * package
   * server
      ```shell
      java -jar -Dspring.profiles.active=prod member-0.0.1-SNAPSHOT.jar
      ```

