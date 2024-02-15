package telran.java51.post.dao;

import org.springframework.data.repository.CrudRepository;

import telran.java51.post.model.Post;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public interface PostRepository extends CrudRepository<Post, String> {
    Stream<Post> findByAuthorIgnoreCase(String author);
    Stream<Post> findByDateCreatedBetween(LocalDate dateStart, LocalDate dateEnd);
    Stream<Post> findByTagsInIgnoreCase(List<String> tags);
}
