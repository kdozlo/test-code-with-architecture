package com.example.demo.service;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.dto.PostCreateDto;
import com.example.demo.model.dto.PostUpdateDto;
import com.example.demo.repository.PostEntity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@TestPropertySource("classpath:test-application.properties")
@SqlGroup({
        @Sql(value = "/sql/post-service-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(value = "/sql/delete-all-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
        })
class PostServiceTest {

    @Autowired
    private PostService postService;

    @Test
    public void getById는_존재하는_게시물을_내려준다() throws Exception {
        //given
        long postId = 1L;

        //when
        PostEntity result = postService.getPostById(postId);

        //then
        assertThat(result.getContent()).isEqualTo("helloworld");
        assertThat(result.getWriter().getEmail()).isEqualTo("test@test.com");
    }

    @Test
    public void getById는_존재하지_않는_게시물_아이디를_받으면_에러를_던진다() throws Exception {
        //given
        long postId = 2L;

        //when

        //then
        Assertions.assertThatThrownBy(() -> {
            postService.getPostById(postId);
        }).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    public void postCreateDto를_이용하여_게시물을_생성할_수_있다() throws Exception {
        //given
        PostCreateDto postCreateDto = PostCreateDto.builder()
                .writerId(1)
                .content("this is test")
                .build();

        //when
        PostEntity result = postService.create(postCreateDto);

        //then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getContent()).isEqualTo("this is test");
        assertThat(result.getCreatedAt()).isGreaterThan(0);
    }

    @Test
    public void postUpdateDto를_이용하여_게시물을_수정할_수_있다() throws Exception {
        //given
        PostUpdateDto postUpdateDto = PostUpdateDto.builder()
                .content("hello world :)")
                .build();

        //when
        postService.update(1, postUpdateDto);

        //then
        PostEntity postEntity = postService.getPostById(1);
        assertThat(postEntity.getContent()).isEqualTo("hello world :)");
        assertThat(postEntity.getModifiedAt()).isGreaterThan(0);
    }
}
