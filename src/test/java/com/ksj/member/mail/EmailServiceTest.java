package com.ksj.member.mail;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ksj.member.dto.EmailDTO;
import com.ksj.member.service.EmailService;
import com.ksj.member.service.EmailServiceImpl;
import com.ksj.member.service.MemberService;
import com.ksj.member.util.AESUtil;
import com.ksj.member.vo.MemberVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ExtendWith(MockitoExtension.class) // Mockito 지원
class EmailServiceTest {

//    @Autowired
//    private EmailService emailService;
//
//    @Test
//    void sendEmailTest() {
//        MemberVO member = new MemberVO();
//        member.setId("TTTT2");
//        member.setUuid("dfdfdfdfdfd");
//        member.setEmail("sangchoo0116@gmail.com");
//        emailService.sendEmail(member);
//    }

    @InjectMocks
    private EmailServiceImpl emailServiceImpl; // 테스트 대상 서비스

    @Mock
    private AESUtil aesUtil; // AESUtil Mocking

    @Mock
    private ObjectMapper objectMapper; // ObjectMapper Mocking

    @Mock
    private MemberService memberService; // MemberService Mocking

    private EmailDTO emailDTO;
    private String encryptedToken;

    @BeforeEach
    public void setUp() throws Exception {
        // EmailDTO 테스트 데이터
        emailDTO = new EmailDTO();
        emailDTO.setId("TTTT2");
        emailDTO.setSendAt(LocalDateTime.now().toString());
        emailDTO.setUuid("sample-uuid");

        // JSON 직렬화 및 암호화된 토큰 생성
        ObjectMapper realMapper = new ObjectMapper();
        realMapper.registerModule(new JavaTimeModule()); // LocalDateTime 지원
        String json = realMapper.writeValueAsString(emailDTO);
        encryptedToken = AESUtil.encrypt(json); // 암호화된 토큰
    }

    @Test
    public void verifyEmail_validToken_success() throws Exception {
        // AESUtil Mock 설정
        Mockito.when(aesUtil.decrypt(encryptedToken)).thenReturn(new ObjectMapper().writeValueAsString(emailDTO));

        // ObjectMapper Mock 설정
        Mockito.when(objectMapper.readValue(Mockito.anyString(), Mockito.eq(EmailDTO.class))).thenReturn(emailDTO);

        // MemberService Mock 설정
        Mockito.doNothing().when(memberService).updateMemberOne(Mockito.any(MemberVO.class));

        // 테스트 실행
        String result = emailServiceImpl.verifyEmail(encryptedToken);

        // 검증
        assertEquals("이메일 인증이 완료되었습니다.", result);
        Mockito.verify(memberService, Mockito.times(1)).updateMemberOne(Mockito.any(MemberVO.class));
    }

    @Test
    public void verifyEmail_expiredToken_failure() throws Exception {
        // 만료된 토큰 설정
        emailDTO.setSendAt(LocalDateTime.now().minusMinutes(4).toString()); // 4분 전 생성된 토큰

        // AESUtil Mock 설정
        Mockito.when(aesUtil.decrypt(encryptedToken)).thenReturn(new ObjectMapper().writeValueAsString(emailDTO));

        // ObjectMapper Mock 설정
        Mockito.when(objectMapper.readValue(Mockito.anyString(), Mockito.eq(EmailDTO.class))).thenReturn(emailDTO);

        // 테스트 실행
        String result = emailServiceImpl.verifyEmail(encryptedToken);

        // 검증
        assertEquals("토큰이 만료되었습니다. 다시 요청해주세요.", result);
        Mockito.verify(memberService, Mockito.never()).updateMemberOne(Mockito.any(MemberVO.class));
    }

    @Test
    public void verifyEmail_invalidToken_failure() throws Exception {
        // AESUtil Mock 설정: 복호화 실패를 가정
        Mockito.when(aesUtil.decrypt(Mockito.anyString())).thenThrow(new RuntimeException("Decryption failed"));

        // 테스트 실행
        String result = emailServiceImpl.verifyEmail("invalid-token");

        // 검증
        assertEquals("유효하지 않은 링크입니다.", result);
        Mockito.verify(memberService, Mockito.never()).updateMemberOne(Mockito.any(MemberVO.class));
    }
}