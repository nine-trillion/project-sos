package team.project.sos.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.project.sos.common.excepion.BaseException;
import team.project.sos.domain.user.dto.request.UserUpdateRequestDto;
import team.project.sos.domain.user.dto.response.UserResponseDto;
import team.project.sos.domain.user.entity.User;
import team.project.sos.domain.user.repository.UserRepository;

import static team.project.sos.domain.user.exception.UserError.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

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
        if(userRepository.existsByPhoneNumber(requestDto.getPhoneNumber())){
            throw new BaseException(DUPLICATE_PHONE_NUMBER);
        }
        User findUser = findByIdOrElseThrow(userId);

        findUser.update(requestDto.getNickName(), requestDto.getPhoneNumber());
        return UserResponseDto.from(findUser);
    }


}
