package com.invitation.module.rds.service.privacy.configuration;

import com.invitation.module.common.logger.DetailLogger;
import com.invitation.module.common.model.configuration.Configuration;
import com.invitation.module.common.util.CommonsUtil;
import com.invitation.module.rds.repository.privacy.configuration.PrivacyConfigurationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class RdsPrivacyConfigurationServiceImpl implements RdsPrivacyConfigurationService {

    private final PrivacyConfigurationRepository privacyConfigurationRepository;

    @Override
    public List<Configuration> getConfigUpdateTime() {

        List<Configuration> list = new ArrayList<>();

        try {

            list.add(privacyConfigurationRepository.findMaxUpdateTime());


        } catch(Exception e) {
            DetailLogger.error("Exception while querying update date ex={}", e.getMessage());
        }

        return list.stream().filter(x-> !CommonsUtil.isEmpty(x)).collect(Collectors.toList());
    }
}
