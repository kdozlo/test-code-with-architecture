package com.example.demo.post.service;

import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.common.service.port.ClockHolder;
import com.example.demo.post.domain.Post;
import com.example.demo.post.domain.PostCreate;
import com.example.demo.post.domain.PostUpdate;
import com.example.demo.post.service.port.PostRepository;
import com.example.demo.user.domain.User;

import com.example.demo.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserService userService;
    private final ClockHolder clockHolder;

    public Post getPostById(long id) {
        return postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Posts", id));
    }

    public Post create(PostCreate postCreateDto) {
        User writer = userService.getById(postCreateDto.getWriterId());
        Post post = Post.from(postCreateDto, writer, clockHolder);

        return postRepository.save(post);
    }

    public Post update(long id, PostUpdate postUpdateDto) {
        Post post = getPostById(id);
        post = post.update(postUpdateDto, clockHolder);

        return postRepository.save(post);
    }
}