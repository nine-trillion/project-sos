package team.project.sos.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.project.sos.common.excepion.BaseException;
import team.project.sos.common.response.MessageResponse;
import team.project.sos.domain.user.dto.request.UserPasswordRequestDto;
import team.project.sos.domain.user.dto.request.UserUpdateRequestDto;
import team.project.sos.domain.user.dto.response.UserResponseDto;
import team.project.sos.domain.user.entity.User;
import team.project.sos.domain.user.repository.UserRepository;
import team.project.sos.domain.user.security.PasswordEncoder;

import static team.project.sos.domain.auth.exception.AuthError.INVALID_PASSWORD;
import static team.project.sos.domain.user.exception.UserError.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // userId 로 user 찾기
    @Override
    public User findByIdOrElseThrow(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new BaseException(USER_NOT_FOUND)
        );
    }

    @Override
    public UserResponseDto findUser(Long userId) {
        User findUser = findByIdOrElseThrow(userId);
        return UserResponseDto.from(findUser);
    }

    @Transactional
    @Override
    public UserResponseDto updateUser(long userId, UserUpdateRequestDto requestDto) {
        if (userRepository.existsByPhoneNumber(requestDto.getPhoneNumber())) {
            throw new BaseException(DUPLICATE_PHONE_NUMBER);
        }
        User findUser = findByIdOrElseThrow(userId);

        findUser.update(requestDto.getNickName(), requestDto.getPhoneNumber());
        return UserResponseDto.from(findUser);
    }

    @Override
    public void verifyPassword(long userId, UserPasswordRequestDto requestDto) {
        User findUser = findByIdOrElseThrow(userId);

        if (!passwordEncoder.matches(requestDto.getPassword(), findUser.getPassword())) {
            throw new BaseException(INVALID_PASSWORD);
        }

    }

    @Transactional
    @Override
    public MessageResponse delete(Long userId) {
        User findUser = findByIdOrElseThrow(userId);
        findUser.deleteUser();
        return new MessageResponse("탈퇴되었습니다.");
    }


}
