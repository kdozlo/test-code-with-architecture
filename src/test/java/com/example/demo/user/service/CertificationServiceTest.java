package com.example.demo.user.service;

import com.example.demo.mock.FakeMaliSender;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CertificationServiceTest {

    @Test
    public void 이메일과_컨텐츠가_제대로_만들어져서_보내지는지_테스트한다() throws Exception {
        //given
        FakeMaliSender fakeMaliSender = new FakeMaliSender();
        CertificationService certificationService = new CertificationService(fakeMaliSender);

        //when
        certificationService.send("test@test.com", 1, "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");

        //then
        assertThat(fakeMaliSender.email).isEqualTo("test@test.com");
        assertThat(fakeMaliSender.title).isEqualTo("Please certify your email address");
        assertThat(fakeMaliSender.content).isEqualTo("Please click the following link to certify your email address: http://localhost:8080/api/users/1/verify?certificationCode=aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");
    }

}