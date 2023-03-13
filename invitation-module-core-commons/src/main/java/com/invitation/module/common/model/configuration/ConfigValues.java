package com.invitation.module.common.model.configuration;

public enum ConfigValues {

    CONFIG_DATABASE_INVITATION("INVITATION"),
    CONFIG_DATABASE_PRIVACY("PRIVACY"),

    CONFIG_INVITATION_TABLE_T_CONFIGURATION("INVITATION", "T_CONFIGURATION"),
    CONFIG_PRIVACY_TABLE_T_CONFIGURATION("PRIVACY", "T_CONFIGURATION"),
    CONFIG_INVITATION_TABLE_T_CONFIGURATION_REFERENCE("INVITATION", "T_CONFIGURATION_REFERENCE"),

    CONFIG_INVITATION_TABLE_T_CONFIGURATION_SUPER_USER("INVITATION", "T_CONFIGURATION", "CONFIG", "SUPER_USER"),
    CONFIG_INVITATION_TABLE_T_CONFIGURATION_ALLOWED_PAGE("INVITATION", "T_CONFIGURATION", "CONFIG","ALLOWED_PAGE"),
    CONFIG_INVITATION_TABLE_T_CONFIGURATION_MODE("INVITATION", "T_CONFIGURATION", "CONFIG", "MODE"),
    CONFIG_INVITATION_TABLE_T_CONFIGURATION_BLOCKED_USER("INVITATION", "T_CONFIGURATION", "CONFIG", "BLOCKED_USER"),
    CONFIG_INVITATION_TABLE_T_CONFIGURATION_BLOCKED_ADDRESS("INVITATION", "T_CONFIGURATION", "CONFIG", "BLOCKED_ADDRESS"),
    CONFIG_INVITATION_TABLE_T_CONFIGURATION_PASSWORD("INVITATION", "T_CONFIGURATION", "CONFIG", "PASSWORD"),

    CONFIG_INVITATION_TABLE_T_CONFIGURATION_REFERENCE_BLOCKED_ADDRESS("INVITATION", "T_CONFIGURATION", "CONFIG", "BLOCKED_ADDRESS"),
    CONFIG_INVITATION_TABLE_T_CONFIGURATION_REFERENCE_BLOCKED_USER("INVITATION", "T_CONFIGURATION", "CONFIG", "BLOCKED_USER"),
    CONFIG_INVITATION_TABLE_T_CONFIGURATION_REFERENCE_ALLOWED_PAGE("INVITATION", "T_CONFIGURATION", "CONFIG", "ALLOWED_PAGE"),

    // CONFIG_데이터베이스명_테이블명
    CONFIG_INVITATION_T_CONFIGURATION("INVITATION", "T_CONFIGURATION", "com.invitation.module.rds.repository.invitation.configuration.InvitationConfigurationRepository", "findAll", ""),
    CONFIG_PRIVACY_T_CONFIGURATION("PRIVACY", "T_CONFIGURATION", "com.invitation.module.rds.repository.privacy.configuration.PrivacyConfigurationRepository", "findAll",""),
    CONFIG_INVITATION_T_CONFIGURATION_REFERENCE("INVITATION", "T_CONFIGURATION_REFERENCE", "com.invitation.module.rds.repository.invitation.configuration.InvitationConfigurationReferenceRepository", "findAll", "");

    private String database;
    private String table;
    private String repository;
    private String method;
    private String category;
    private String code;
    private String etc;

    ConfigValues(String database, String table, String repository, String method, String etc) {
        this.database = database;
        this.table = table;
        this.repository = repository;
        this.method = method;
        this.etc = etc;
    }

    ConfigValues(String database) {
        this.database = database;
    }

    ConfigValues(String database, String table) {
        this.database = database;
        this.table = table;
    }

    ConfigValues(String database, String table, String category, String code) {
        this.database = database;
        this.table = table;
        this.category = category;
        this.code = code;
    }

    public String DATABASE() {return database;}
    public String TABLE() {return table;}
    public String REPOSITORY() {return repository;}
    public String METHOD() {return method;}
    public String CATEGORY() {return category;}
    public String CODE() {return code;}

}
