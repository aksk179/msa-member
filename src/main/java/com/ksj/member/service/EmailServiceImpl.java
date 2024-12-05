package com.ksj.member.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ksj.member.dto.EmailDTO;
import com.ksj.member.mapper.MemberMapper;
import com.ksj.member.util.AESUtil;
import com.ksj.member.vo.MemberVO;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@Slf4j
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String senderEmail;

    @Autowired
    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Autowired
    MemberMapper memberMapper;

    @Override
    public void sendEmail(MemberVO memberVO) {
        try {
            // 1. MimeMessage 생성
            MimeMessage mimeMessage = mailSender.createMimeMessage();

            // 2. MimeMessageHelper 설정
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setTo(memberVO.getEmail()); // 받는 사람
            helper.setFrom(senderEmail);       // 보내는 사람
            helper.setSubject("회원가입 인증 메일입니다."); // 이메일 제목

            // 이메일 내용 생성
            EmailDTO emailDTO = new EmailDTO();
            emailDTO.setId(memberVO.getId());
            emailDTO.setSendAt(LocalDateTime.now().toString());
            emailDTO.setUuid(memberVO.getUuid());

            // 암호화된 링크 생성
            String link = createEncryptedLink(emailDTO);

            // 이메일 본문 작성 (HTML 형식)
            String htmlContent = """
                <html>
                <body>
                    <h2>회원가입을 환영합니다!</h2>
                    <p>아래 버튼을 클릭하여 계정을 활성화하세요:</p>
                    <a href='%s' style='display: inline-block; padding: 10px 20px; color: white; background-color: blue; text-decoration: none;'>계정 활성화하기</a>
                    <p>감사합니다.</p>
                </body>
                </html>
                """.formatted(link);

            helper.setText(htmlContent, true); // HTML 형식으로 설정

            // 3. 이메일 전송
            mailSender.send(mimeMessage);

            log.debug("================ 메일 전송되었습니다. ==================");
        } catch (Exception e) {
            throw new RuntimeException("이메일 발송 중 오류 발생", e);
        }
    }

    @Override
    public String verifyEmail(String token) {
        log.debug("verifyEmail() : " + token);
        try {
            // 1. 복호화
            String decryptedJson = AESUtil.decrypt(token);

            // 2. JSON 문자열을 DTO로 변환
            ObjectMapper objectMapper = new ObjectMapper();
            EmailDTO emailDTO = objectMapper.readValue(decryptedJson, EmailDTO.class);

            // 3. uuid 맞는지 확인 후
            MemberVO memberVO = new MemberVO();
            memberVO.setId(emailDTO.getId());
            if (emailDTO.getUuid().equals(memberMapper.selectMemberUuid(memberVO).getUuid())) {
                log.debug("================= uuid가 일치합니다. ===================");
                // 4. 토큰 만료 여부 검증
                if (isTokenExpired(emailDTO.getSendAt())) {
                    return "토큰이 만료되었습니다. 다시 요청해주세요.";
                }
            } else {
                return "uuid가 맞지 않습니다.";
            }

            // 5. 인증 성공 처리 (추가 로직)
            memberVO.setStatus("Y");
            memberMapper.updateMemberOne(memberVO);
            return "이메일 인증이 완료되었습니다. 화면으로 돌아가 로그인해주세요.";
        } catch (Exception e) {
            e.printStackTrace();
            return "유효하지 않은 링크입니다.";
        }
    }

    private String createEncryptedLink(EmailDTO emailDTO) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String json = objectMapper.writeValueAsString(emailDTO);
        log.debug("json : " + json);
        String encrypted = AESUtil.encrypt(json);
        return "http://localhost:8080/verify-email.do?token=" + encrypted;
    }

    private boolean isTokenExpired(String sendAt) {
        log.debug("sendAt : " + sendAt);
        String truncatedSendAt = sendAt.substring(0, sendAt.indexOf('.') + 4);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");
        LocalDateTime dateTime = LocalDateTime.parse(truncatedSendAt, formatter);
        log.debug("dateTime : " + dateTime);
        return dateTime.isBefore(LocalDateTime.now().minusMinutes(3)); // 3분 초과 여부 확인
    }
}
