package com.example.demo.post.service;

import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.mock.FakePostRepository;
import com.example.demo.mock.FakeUserRepository;
import com.example.demo.mock.TestClockHolder;
import com.example.demo.post.domain.Post;
import com.example.demo.post.domain.PostCreate;
import com.example.demo.post.domain.PostUpdate;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PostServiceTest {

    private PostService postService;

    @BeforeEach
    public void init() {
        FakePostRepository fakePostRepository = new FakePostRepository();
        FakeUserRepository fakeUserRepository = new FakeUserRepository();

        postService = PostService.builder()
                .postRepository(fakePostRepository)
                .userRepository(fakeUserRepository)
                .clockHolder(new TestClockHolder(1234567L))
                .build();

        User user1 = User.builder()
                .email("test@test.com")
                .nickname("test")
                .address("Seoul")
                .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
                .status(UserStatus.ACTIVE)
                .lastLoginAt(0L)
                .build();

        User user2 = User.builder()
                .email("test2@test.com")
                .nickname("test2")
                .address("Seoul")
                .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab")
                .status(UserStatus.PENDING)
                .lastLoginAt(0L)
                .build();

        fakeUserRepository.save(user1);
        fakeUserRepository.save(user2);
        fakePostRepository.save(Post.builder()
                .content("helloworld")
                .createdAt(1678530673958L)
                .modifiedAt(0L)
                .writer(user1)
                .build());
    }

    @Test
    public void getById는_존재하는_게시물을_내려준다() throws Exception {
        //given
        long postId = 1L;

        //when
        Post result = postService.getPostById(postId);

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
        PostCreate postCreateDto = PostCreate.builder()
                .writerId(1)
                .content("this is test")
                .build();

        //when
        Post result = postService.create(postCreateDto);

        //then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getContent()).isEqualTo("this is test");
        assertThat(result.getCreatedAt()).isEqualTo(1234567L);
    }

    @Test
    public void postUpdateDto를_이용하여_게시물을_수정할_수_있다() throws Exception {
        //given
        PostUpdate postUpdateDto = PostUpdate.builder()
                .content("hello world :)")
                .build();

        //when
        postService.update(1, postUpdateDto);

        //then
        Post post = postService.getPostById(1);
        assertThat(post.getContent()).isEqualTo("hello world :)");
        assertThat(post.getModifiedAt()).isEqualTo(1234567L);
    }
}
