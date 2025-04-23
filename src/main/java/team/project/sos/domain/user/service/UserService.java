package team.project.sos.domain.user.service;

import team.project.sos.domain.user.entity.User;


public interface UserService {
    User findByIdOrElseThrow(Long userId);
}
