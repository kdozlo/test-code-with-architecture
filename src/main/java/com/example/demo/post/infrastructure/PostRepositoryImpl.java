package com.example.demo.post.infrastructure;

import com.example.demo.post.service.port.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepository {

    private final PostJapRepository postJapRepository;


    @Override
    public Optional<PostEntity> findById(long id) {
        return postJapRepository.findById(id);
    }

    @Override
    public PostEntity save(PostEntity postEntity) {
        return postJapRepository.save(postEntity);
    }
}
