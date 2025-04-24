package team.project.sos.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import team.project.sos.common.excepion.BaseException;
import team.project.sos.domain.user.dto.response.UserResponseDto;
import team.project.sos.domain.user.entity.User;
import team.project.sos.domain.user.exception.UserError;
import team.project.sos.domain.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    // userId 로 user 찾기
    @Override
    public User findByIdOrElseThrow(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new BaseException(UserError.USER_NOT_FOUND)
        );
    }

    @Override
    public UserResponseDto findUser(Long userId) {
        User findUser = findByIdOrElseThrow(userId);
        return UserResponseDto.from(findUser);
    }


}
