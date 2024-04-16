package ru.practicum.users.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.users.dto.NewUserDTO;
import ru.practicum.users.dto.UserDTO;
import ru.practicum.users.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/admin/users")
@RequiredArgsConstructor
@Slf4j
public class UserAdminController {

    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO addAdminUser(@RequestBody @Valid NewUserDTO newUserRequestDto) {

        log.info("Calling addAdminUser: /admin/users with 'newUserRequestDto': {}", newUserRequestDto.toString());
        return userService.addUser(newUserRequestDto);
    }

    @GetMapping
    public List<UserDTO> getAdminUsers(@RequestParam(name = "ids", required = false) List<Long> ids,
                                       @RequestParam(name = "from", defaultValue = "0", required = false) Integer from,
                                       @RequestParam(name = "size", defaultValue = "10", required = false) Integer size) {

        log.info("Calling getAdminUsers: /admin/users with 'ids': {}, 'from': {}, 'size': {}", ids, from, size);
        return userService.getUsers(ids, from, size);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAdminUser(@PathVariable Long userId) {

        log.info("Calling deleteAdminUser: /admin/users/{userId} with 'userId': {}", userId);
        userService.deleteUserById(userId);
    }
}
