package com.example.proyectoaplicaciones.api_server.post;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public Post createPost(Post post) {
        return postRepository.save(post);
    }

    public void deletePost(Integer id) {
        // Aquí se asume que tienes 'postRepository' inyectado en esta clase.
        // El método deleteById es un método estándar que provee JpaRepository.
        postRepository.deleteById(id);
    }
}