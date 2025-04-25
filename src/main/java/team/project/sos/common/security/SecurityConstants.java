package team.project.sos.common.security;

public class SecurityConstants {
    public static final String[] WHITE_LIST = {
            "/api/auth/signup",
            "/api/auth/admin/signup",
            "/api/auth/owner/signup",
            "/api/auth/login",
            "/api/auth/password"    // 비밀번호를 찾는 요청 엔드포인트
    };
}
