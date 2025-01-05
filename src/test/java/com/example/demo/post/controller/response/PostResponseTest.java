package com.example.demo.post.controller.response;

import com.example.demo.post.domain.Post;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PostResponseTest {
    
    @Test
    public void Post으로_응답을_생성할_수_있다() throws Exception {
        //given
        Post post = Post.builder()
                .content("helloworld")
                .writer(User.builder()
                        .id(1L)
                        .email("test@test.com")
                        .nickname("test")
                        .address("Masan")
                        .status(UserStatus.ACTIVE)
                        .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
                        .build())
                .build();
        
        //when
        PostResponse postResponse = PostResponse.from(post);

        //then
        assertThat(postResponse.getContent()).isEqualTo("helloworld");
        assertThat(postResponse.getWriter().getId()).isEqualTo(1L);
        assertThat(postResponse.getWriter().getEmail()).isEqualTo("test@test.com");
        assertThat(postResponse.getWriter().getNickname()).isEqualTo("test");
        assertThat(postResponse.getWriter().getStatus()).isEqualTo(UserStatus.ACTIVE);
    }
}
