package com.example.demo.post.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PostJapRepository extends JpaRepository<PostEntity, Long> {

}