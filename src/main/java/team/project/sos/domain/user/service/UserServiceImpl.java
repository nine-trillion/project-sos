package team.project.sos.domain.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.project.sos.common.excepion.BaseException;
import team.project.sos.common.response.MessageResponse;
import team.project.sos.domain.auth.dto.request.FindPasswordRequestDto;
import team.project.sos.domain.auth.dto.response.FindPasswordResponseDto;
import team.project.sos.domain.user.dto.request.UserPasswordRequestDto;
import team.project.sos.domain.user.dto.request.UserUpdateRequestDto;
import team.project.sos.domain.user.dto.response.UserResponseDto;
import team.project.sos.domain.user.entity.User;
import team.project.sos.domain.user.repository.UserRepository;
import team.project.sos.domain.user.security.PasswordEncoder;
import team.project.sos.domain.user.security.PasswordGenerator;


import static team.project.sos.domain.auth.exception.AuthError.*;
import static team.project.sos.domain.user.exception.UserError.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PasswordGenerator passwordGenerator;

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

    @Override
    @Transactional
    public UserResponseDto updateUser(long userId, UserUpdateRequestDto requestDto) {
        if (userRepository.existsByPhoneNumber(requestDto.getPhoneNumber())) {
            throw new BaseException(DUPLICATE_PHONE_NUMBER);
        }
        User findUser = findByIdOrElseThrow(userId);

        findUser.update(requestDto.getNickName(), requestDto.getPhoneNumber());
        return UserResponseDto.from(findUser);
    }

    @Override
    public MessageResponse verifyPassword(long userId, UserPasswordRequestDto requestDto) {
        User findUser = findByIdOrElseThrow(userId);

        if (!passwordEncoder.matches(requestDto.getPassword(), findUser.getPassword())) {
            throw new BaseException(INVALID_PASSWORD);
        }
        return new MessageResponse("비밀번호가 확인되었습니다");
    }


    @Override
    @Transactional
    public MessageResponse delete(Long userId) {
        User findUser = findByIdOrElseThrow(userId);
        findUser.deleteUser();
        return new MessageResponse("탈퇴되었습니다.");
    }

    @Override
    @Transactional
    public FindPasswordResponseDto findPassword(FindPasswordRequestDto requestDto) {
        User findUser = userRepository.findByPhoneNumber(requestDto.getPhoneNumber()).orElseThrow(
                () -> new BaseException(USER_NOT_FOUND)
        );
        if (!findUser.getEmail().equals(requestDto.getEmail())) {
            throw new BaseException(LOGIN_FAILED);
        }
        String tempPassword = passwordGenerator.generateTempPassword();

        String encodedTempPwd = passwordEncoder.encode(tempPassword);

        findUser.updatePassword(encodedTempPwd);
        userRepository.save(findUser);
        log.info("임시비밀번호(plain) = {}", tempPassword);
        log.info("암호화된 비밀번호 = {}", encodedTempPwd);

        return new FindPasswordResponseDto(tempPassword);
    }

}
