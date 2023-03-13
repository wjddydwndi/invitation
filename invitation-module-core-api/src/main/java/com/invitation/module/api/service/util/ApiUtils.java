package com.invitation.module.api.service.util;

import com.invitation.module.common.logger.DetailLogger;
import com.invitation.module.common.util.CommonsUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class ApiUtils {

    @Value("${server.mode}")
    public static String mode;

    public static boolean isProd() {
        if (CommonsUtil.isEmpty(mode)) {
            DetailLogger.error("서버모드 확인 불가 mode={}", mode);
            return false;
        }

        if (mode.equals("prod")) {
            return true;
        }
        return false;
    }
}
