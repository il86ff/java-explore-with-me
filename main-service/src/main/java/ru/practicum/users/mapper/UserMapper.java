package ru.practicum.users.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;
import ru.practicum.users.dto.NewUserDTO;
import ru.practicum.users.dto.UserDTO;
import ru.practicum.users.dto.UserShortDTO;
import ru.practicum.users.entity.User;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "id", ignore = true)
    User userDtoToUser(NewUserDTO newUserRequestDto);

    UserDTO userToUserDto(User user);

    UserShortDTO userToUserShortDto(User user);
}
