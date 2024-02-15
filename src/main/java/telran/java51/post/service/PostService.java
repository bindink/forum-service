package telran.java51.post.service;

import java.util.List;

import telran.java51.post.dto.post.DatePeriodDto;
import telran.java51.post.dto.post.NewCommentDto;
import telran.java51.post.dto.post.NewPostDto;
import telran.java51.post.dto.post.PostDto;

public interface PostService {
	PostDto addNewPost(String author, NewPostDto newPostDto);

	PostDto findPostById(String id);

	PostDto removePost(String id);

	PostDto updatePost(String id, NewPostDto newPostDto);

	PostDto addComment(String id, String author, NewCommentDto newCommentDto);

	void addLike(String id);

	Iterable<PostDto> findPostByAuthor(String author);

	Iterable<PostDto> findPostsByTags(List<String> tags);

	Iterable<PostDto> findPostsByPeriod(DatePeriodDto datePeriodDto);

}
