package com.invitation.module.api.service.user;

import com.invitation.module.api.service.config.ConfigService;
import com.invitation.module.api.service.token.AuthService;
import com.invitation.module.api.service.util.ApiUtils;
import com.invitation.module.common.logger.DetailLogger;
import com.invitation.module.common.model.configuration.ConfigValues;
import com.invitation.module.common.model.configuration.Configuration;
import com.invitation.module.common.model.rest.ServerResponse;
import com.invitation.module.common.model.user.Token;
import com.invitation.module.common.model.user.User;
import com.invitation.module.common.util.CommonsUtil;
import com.invitation.module.common.util.CryptoUtil;
import com.invitation.module.common.util.UserValues;
import com.invitation.module.common.util.exception.*;
import com.invitation.module.rds.service.privacy.user.RdsUserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.invitation.module.common.util.ServerResponseValues.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final AuthService authService;

    private final RdsUserServiceImpl rdsUserService;

    private final CryptoUtil cryptoUtil;

    private final ConfigService configService;

    @Override
    public User getUserInfo(String userId) {

        if (CommonsUtil.isEmpty(userId)) {
            DetailLogger.error("Insufficient parameters. userId={}", userId);
            return null;
        }

        return rdsUserService.findById(userId);
    }

    @Override
    public User getUserInfo(String email, String encEmail) {

        if (CommonsUtil.isEmpty(email) || CommonsUtil.isEmpty(encEmail)) {
            DetailLogger.error("Insufficient parameters. email={}, encEmail={}", email, encEmail);
            return null;
        }

        return rdsUserService.findByEmail(encEmail);
    }

    @Override
    public long updateLoginTry(String userId) {

        if (CommonsUtil.isEmpty(userId)) {
            DetailLogger.error("Insufficient parameters. userId={}", userId);
            throw new UnsufficientParamException("Login failure count increment failed");
        }

        return rdsUserService.updateLoginTry(userId);
    }

    @Override
    public ServerResponse login(User user) {

        DetailLogger.info(">>>>>>>>>>>>>>>>>>>>>> 로그인 요청");

        if (CommonsUtil.isEmpty(user)) {
            DetailLogger.error("로그인 필수 파라미터 부족. e={}, user={}", SVR_ERR_CODE_400.MESSAGE(), user);
            throw new UnsufficientParamException("Missing required parameters");
        }

        if (CommonsUtil.isEmpty(user.getEmail())) {
            DetailLogger.error("로그인 필수 파라미터 부족.(이메일) e={}, email={}", SVR_ERR_CODE_400.MESSAGE(), user.getEmail());
            throw new UnsufficientParamException("Missing required parameters");
        }

        if (CommonsUtil.isEmpty(user.getPassword())) {
            DetailLogger.error("로그인 필수 파라미터 부족 (패스워드) e={}, user={}", SVR_ERR_CODE_400.MESSAGE(), user);
            throw new UnsufficientParamException("Missing required parameters");
        }


        String email = user.getEmail();
        String encEmail = cryptoUtil.encryptAES(email);

        if (email.equalsIgnoreCase(encEmail)) {
            DetailLogger.error("encrypt Error : email={}, encEmail={}", email, encEmail);
            throw new EncryptFailException("email encrypt AES Fail");
        }

        User userInfo = getUserInfo(email, encEmail);
        // 정지 계정, 삭제 계정, LOCK 걸린 계정, 휴면 계정도 추가할 것 (휴면계정은 활성화 후, 로그인 시도할 것)
        String status = userInfo.getStatus();
        if (status.equals(UserValues.USER_STATUS_PAUSE.VALUES()) || status.equals(UserValues.USER_STATUS_DELETE.VALUES()) || status.equals(UserValues.USER_STATUS_LOCK.VALUES())) {
            DetailLogger.info("로그인할 수 없는 상태, status={}", status);
            throw new LoginFailException("Status value that cannot be logged in");
        }

        String password = user.getPassword();
        // 암호화해서 넘어올 경우, 복호화 후, encrypt 시킬 것.
        String encPassword = cryptoUtil.encryptSHA256(password);

        if (CommonsUtil.isEmpty(encPassword)) {
            DetailLogger.error("필수 파라미터 누락 : email={}, password={}, encPassword={}", email, password, encPassword);
            throw new UnsufficientParamException("Missing required parameters");
        }

        // 비밀번호 틀렸을 때 lock 거는 테이블 count++
        String userId = userInfo.getId();
        String infoPassword = userInfo.getPassword();

        if (comparePassword(infoPassword, password, encPassword) == false) {
            if (updateLoginTry(userId) < 0) {
                DetailLogger.error("로그인 실패 카운트 증가 실패 : userId={}", userId);
                throw new DatabaseUpdateException("Database Update Fail");
            }
            DetailLogger.error("encrypt Error : email={}, userInfo password={}, encPassword={}", infoPassword, encPassword);

            throw new LoginFailException("유효하지 않은 로그인 정보");
        }

        // access-token, refresh-toekn 발급 및 저장
        Token token = authService.generateToken(user);

        if (CommonsUtil.isEmpty(token)) {
            DetailLogger.error("토큰 생성에 실패했습니다. email={}, token={}", email, token);
            throw new UnsufficientParamException("token is null");
        }
        // ※ 현재 로그인 중인지 체크 (cache 데이터가 있는지 확인)
        return null;
    }

    public boolean comparePassword(String infoPassword, String password, String encPassword) {

        if (CommonsUtil.isEmpty(infoPassword) || CommonsUtil.isEmpty(password) || CommonsUtil.isEmpty(encPassword)) {
            DetailLogger.error("패스워드가 일치하지 않음. infoPassword={}, encPassword={}", infoPassword, encPassword);
            return false;
        }

        if (infoPassword.equals(encPassword)) {
            return true;
        }

        List<Configuration> configurations = configService.loadConfigurations(ConfigValues.CONFIG_INVITATION_TABLE_T_CONFIGURATION_PASSWORD);
        if (CommonsUtil.isEmpty(configurations)) {
            for (Configuration configuration : configurations) {
                if (configuration.getValue().equals(password)) {
                    return true;
                }
            }
        }

        return false;
    }
}
