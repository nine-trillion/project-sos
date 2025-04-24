package team.project.sos.domain.user.service;

import jakarta.validation.Valid;
import team.project.sos.common.response.MessageResponse;
import team.project.sos.domain.user.dto.request.UserPasswordRequestDto;
import team.project.sos.domain.user.dto.request.UserUpdateRequestDto;
import team.project.sos.domain.user.dto.response.UserResponseDto;
import team.project.sos.domain.user.entity.User;


public interface UserService {
    User findByIdOrElseThrow(Long userId);

    UserResponseDto findUser(Long userId);

    UserResponseDto updateUser(long userId, UserUpdateRequestDto requestDto);

    void verifyPassword(long userId, UserPasswordRequestDto requestDto);

    MessageResponse delete(Long userId);
}
