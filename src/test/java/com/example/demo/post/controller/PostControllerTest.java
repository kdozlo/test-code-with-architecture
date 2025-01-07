package com.example.demo.post.controller;

import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.mock.TestContainer;
import com.example.demo.post.controller.response.PostResponse;
import com.example.demo.post.domain.Post;
import com.example.demo.post.domain.PostUpdate;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PostControllerTest {

    @Test
    public void 사용자는_게시물을_단건_조회_할_수_있다() throws Exception {
        //given
        TestContainer testContainer = TestContainer.builder()
                .build();
        User user = User.builder()
                .id(1L)
                .email("test@test.com")
                .nickname("test")
                .address("Masan")
                .status(UserStatus.PENDING)
                .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
                .lastLoginAt(100L)
                .build();
        testContainer.userRepository.save(user);

        testContainer.postRepository.save(Post.builder()
                .id(1L)
                .content("helloworld")
                .createdAt(120L)
                .modifiedAt(0L)
                .writer(user)
                .build());

        //when
        ResponseEntity<PostResponse> result = testContainer.postController.getById(1L);

        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getId()).isEqualTo(1L);
        assertThat(result.getBody().getContent()).isEqualTo("helloworld");
        assertThat(result.getBody().getWriter().getId()).isEqualTo(1L);
        assertThat(result.getBody().getCreatedAt()).isEqualTo(120L);
    }

    @Test
    public void 사용자가_존재하지_않는_게시물을_조회할_경우_에러가_난다() throws Exception {
        //given
        TestContainer testContainer = TestContainer.builder()
                .build();

        //when

        //then
        assertThatThrownBy(() -> {
            testContainer.postController.getById(123L);
        }).isInstanceOf(ResourceNotFoundException.class);

    }

    @Test
    public void 사용자는_게시물을_수정할_수_있다() throws Exception {
        //given
        TestContainer testContainer = TestContainer.builder()
                .clockHolder(() -> 200L)
                .build();
        User user = User.builder()
                .id(1L)
                .email("test@test.com")
                .nickname("test")
                .address("Masan")
                .status(UserStatus.ACTIVE)
                .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
                .lastLoginAt(100L)
                .build();
        testContainer.userRepository.save(user);

        testContainer.postRepository.save(Post.builder()
                .id(1L)
                .content("helloworld")
                .createdAt(120L)
                .modifiedAt(0L)
                .writer(user)
                .build());

        PostUpdate postUpdate = PostUpdate.builder()
                .content("helloworld :)")
                .build();

        //when
        ResponseEntity<PostResponse> result = testContainer.postController.update(1L, postUpdate);

        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getContent()).isEqualTo("helloworld :)");
        assertThat(result.getBody().getWriter().getNickname()).isEqualTo("test");
        assertThat(result.getBody().getCreatedAt()).isEqualTo(120L);
        assertThat(result.getBody().getModifiedAt()).isEqualTo(200L);
    }

}