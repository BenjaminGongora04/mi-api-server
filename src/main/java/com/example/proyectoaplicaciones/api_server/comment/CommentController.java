package com.example.proyectoaplicaciones.api_server.comment;

import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api") // Cambiado para tener más control en cada método
public class CommentController {    private final CommentRepository commentRepository;

    public CommentController(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    // Se mantiene el endpoint original para obtener comentarios de un post
    @GetMapping("/posts/{postId}/comments")
    public List<Comment> getCommentsForPost(@PathVariable Integer postId) {
        return commentRepository.findByPostId(postId);
    }

    // Se mantiene el endpoint original para añadir un comentario
    @PostMapping("/posts/{postId}/comments")
    public Comment addCommentToPost(@PathVariable Integer postId, @RequestBody Comment comment) {
        comment.setPostId(postId);
        return commentRepository.save(comment);
    }

    // --- INICIO DEL CAMBIO: MÉTODO AÑADIDO ---
    // Este es el nuevo endpoint que la app llamará para borrar un comentario
    @DeleteMapping("/comments/{id}")
    public void deleteComment(@PathVariable Integer id) {
        commentRepository.deleteById(id);
    }
    // --- FIN DEL CAMBIO ---
}