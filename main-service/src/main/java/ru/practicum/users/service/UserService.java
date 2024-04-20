package ru.practicum.users.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exceptions.ObjectNotFoundException;
import ru.practicum.exceptions.SQLConstraintViolationException;
import ru.practicum.users.dto.NewUserDTO;
import ru.practicum.users.dto.UserDTO;
import ru.practicum.users.entity.User;
import ru.practicum.users.mapper.UserMapper;
import ru.practicum.users.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional(readOnly = true)
    public List<UserDTO> getUsers(List<Long> ids, Integer from, Integer size) {
        log.info("Calling getUsers data: with ids {}", ids);
        Pageable pageable = PageRequest.of(from / size, size, Sort.by("id").ascending());

        if (ids == null) {
            return userRepository.findAll(pageable).stream()
                    .map(userMapper::userToUserDto)
                    .collect(Collectors.toList());
        } else {
            return userRepository.findAllByIdIn(ids, pageable).stream()
                    .map(userMapper::userToUserDto)
                    .collect(Collectors.toList());
        }
    }

    @Transactional
    public UserDTO addUser(NewUserDTO newUserRequestDto) {
        User user = userMapper.userDtoToUser(newUserRequestDto);

        User userDtoToSave;

        try {
            userDtoToSave = userRepository.save(user);
            log.info("Calling addUser data: with object {}", newUserRequestDto);
        } catch (DataIntegrityViolationException e) {
            log.error("Calling addUser data: with object {}", newUserRequestDto);
            throw new SQLConstraintViolationException("User name and/or email already exists.");
        }

        return userMapper.userToUserDto(userDtoToSave);
    }

    @Transactional
    public void deleteUserById(Long userId) {
        try {
            userRepository.deleteById(userId);
            log.error("Calling deleteUserById data: with id {}", userId);
        } catch (EmptyResultDataAccessException e) {
            log.error("Calling deleteUserById data: with id {}", userId);
            throw new ObjectNotFoundException("User with id = " + userId + " was not found.");
        }
    }
}
