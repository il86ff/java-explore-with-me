package ru.practicum.comments.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.practicum.comments.dto.CommentDTO;
import ru.practicum.comments.dto.NewCommentDTO;
import ru.practicum.comments.entity.Comment;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    CommentMapper INSTANCE = Mappers.getMapper(CommentMapper.class);

    Comment newCommentDtoToComment(NewCommentDTO newCommentDto);

    CommentDTO commentToCommentDto(Comment comment);
}
