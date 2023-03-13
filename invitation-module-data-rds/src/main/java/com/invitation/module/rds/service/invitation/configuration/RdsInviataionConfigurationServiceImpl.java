package com.invitation.module.rds.service.invitation.configuration;

import com.invitation.module.common.logger.DetailLogger;
import com.invitation.module.common.model.configuration.Configuration;
import com.invitation.module.common.util.CommonsUtil;
import com.invitation.module.rds.repository.invitation.configuration.InvitationConfigurationReferenceRepository;
import com.invitation.module.rds.repository.invitation.configuration.InvitationConfigurationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class RdsInviataionConfigurationServiceImpl implements RdsInvitationConfigurationService {

    private final InvitationConfigurationRepository invitationConfigurationRepository;
    private final InvitationConfigurationReferenceRepository invitationConfigurationReferenceRepository;

    @Override
    public List<Configuration> getConfigUpdateTime() {

        List<Configuration> list = new ArrayList<>();

        try {
            // 데이터베이스의 모든 Config 테이블을 담는다.
            list = invitationConfigurationRepository.findMaxUpdateTime();

        } catch(Exception e) {
            DetailLogger.error("Exception while querying update date ex={}", e.getMessage());
        }

        return list.stream().filter(x-> !CommonsUtil.isEmpty(x)).collect(Collectors.toList());
    }
}
