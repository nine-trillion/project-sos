package team.project.sos.domain.user.security;

import org.springframework.stereotype.Component;

import java.util.Random;

/**
 * 임시 비밀번호를 발급할 수 있습니다.
 * 발급한 비밀번호로 암호화한 뒤에 비밀번호를 업데이트 합니다.
 */
@Component
public class PasswordGenerator {
    int length = 8;
    private final String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private final String lower = "abcdefghijklmnopqrstuvwxyz";
    private final String numbers = "0123456789";
    private final String special = "!@#$";
    private final String all = upper + lower + numbers + special;

    private final Random random = new Random();

    public String generateTempPassword() {

        StringBuilder tempPwd = new StringBuilder();

        tempPwd.append(upper.charAt(random.nextInt(upper.length())));
        tempPwd.append(lower.charAt(random.nextInt(lower.length())));
        tempPwd.append(numbers.charAt(random.nextInt(numbers.length())));
        tempPwd.append(special.charAt(random.nextInt(special.length())));

        for (int i = 0; i < length; i++) {
            tempPwd.append(all.charAt(random.nextInt(all.length())));
        }
        char[] pwd = tempPwd.toString().toCharArray();

        for (int i = pwd.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            char temp = pwd[i];
            pwd[i] = pwd[j];
            pwd[j] = temp;
        }
        StringBuilder resultPwd = new StringBuilder();
        for (char c : pwd) {
            resultPwd.append(c);
        }
        return resultPwd.toString();
    }
}
