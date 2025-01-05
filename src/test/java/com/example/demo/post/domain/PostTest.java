package com.example.demo.post.domain;

import com.example.demo.mock.TestClockHolder;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PostTest {
    
    @Test
    public void PostCreate으로_게시물을_만들_수_있다() throws Exception {
        //given
        User writer = User.builder()
                .id(1L)
                .email("test@test.com")
                .nickname("test")
                .address("Masan")
                .status(UserStatus.ACTIVE)
                .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
                .build();

        PostCreate postCreate = PostCreate.builder()
                .writerId(1L)
                .content("helloworld")
                .build();



        //when
        Post post = Post.from(postCreate, writer, new TestClockHolder(1679530673958L));
        
        //then
        assertThat(post.getContent()).isEqualTo("helloworld");
        assertThat(post.getWriter().getId()).isEqualTo(1L);
        assertThat(post.getWriter().getEmail()).isEqualTo("test@test.com");
        assertThat(post.getWriter().getNickname()).isEqualTo("test");
        assertThat(post.getWriter().getAddress()).isEqualTo("Masan");
        assertThat(post.getWriter().getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(post.getWriter().getCertificationCode()).isEqualTo("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");
        assertThat(post.getCreatedAt()).isEqualTo(1679530673958L);

    }
}
