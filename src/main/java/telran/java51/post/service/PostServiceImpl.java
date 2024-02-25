package telran.java51.post.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import telran.java51.post.dao.PostRepository;
import telran.java51.post.dto.post.DatePeriodDto;
import telran.java51.post.dto.post.NewCommentDto;
import telran.java51.post.dto.post.NewPostDto;
import telran.java51.post.dto.post.PostDto;
import telran.java51.post.dto.exceptions.PostNotFoundException;
import telran.java51.post.model.Comment;
import telran.java51.post.model.Post;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    final PostRepository postRepository;
    final ModelMapper modelMapper;

    @Override
    public PostDto addNewPost(String author, NewPostDto newPostDto) {
        Post post = modelMapper.map(newPostDto, Post.class);
        post.setAuthor(author);
        post = postRepository.save(post);
        return modelMapper.map(post, PostDto.class);
    }

    @Override
    public PostDto findPostById(String id) {
        Post post = postRepository.findById(id).orElseThrow(PostNotFoundException::new);
        return modelMapper.map(post, PostDto.class);
    }

    @Override
    public PostDto removePost(String id) {
        Post post = postRepository.findById(id).orElseThrow(PostNotFoundException::new);
        postRepository.deleteById(id);
        return modelMapper.map(post, PostDto.class);
    }

    @Override
    public PostDto updatePost(String id, NewPostDto newPostDto) {
        Post post = postRepository.findById(id).orElseThrow(PostNotFoundException::new);
        if (newPostDto.getTitle() != null) {
            post.setTitle(newPostDto.getTitle());
        }

        if (newPostDto.getContent() != null) {
            post.setContent(newPostDto.getContent());
        }

        if (newPostDto.getTags() != null) {
            post.getTags().addAll(newPostDto.getTags());
        }
        postRepository.save(post);
        return modelMapper.map(post, PostDto.class);
    }

    @Override
    public PostDto addComment(String id, String author, NewCommentDto newCommentDto) {
        Post post = postRepository.findById(id).orElseThrow(PostNotFoundException::new);
        Comment comment = modelMapper.map(newCommentDto, Comment.class);
        comment.setUser(author);
        post.addComment(comment);
        postRepository.save(post);
        return modelMapper.map(post, PostDto.class);
    }

    @Override
    public void addLike(String id) {
        Post post = postRepository.findById(id).orElseThrow(PostNotFoundException::new);
        post.addLike();
        postRepository.save(post);
    }

    @Override
    public Iterable<PostDto> findPostByAuthor(String author) {
        return postRepository.findByAuthorIgnoreCase(author)
                .map(p -> modelMapper.map(p, PostDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<PostDto> findPostsByTags(List<String> tags) {
        return postRepository.findByTagsInIgnoreCase(tags)
                .map(p -> modelMapper.map(p, PostDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<PostDto> findPostsByPeriod(DatePeriodDto datePeriodDto) {
        return postRepository.findByDateCreatedBetween(datePeriodDto.getDateFrom(), datePeriodDto.getDateTo())
                .map(p -> modelMapper.map(p, PostDto.class))
                .collect(Collectors.toList());
    }

}
