-- Creation Date: 2021-11-21
-- Description: Creates the initial tables

create table oauth2_registered_client (
    id varchar(100) not null,
    client_id varchar(100) not null,
    client_id_issued_at timestamp default CURRENT_TIMESTAMP not null,
    client_secret varchar(200) default null,
    client_secret_expires_at timestamp default null,
    client_name varchar(200) not null,
    client_authentication_methods varchar(1000) not null,
    authorization_grant_types varchar(1000) not null,
    redirect_uris varchar(1000) default null,
    scopes varchar(1000) not null,
    client_settings varchar(2000) not null,
    token_settings varchar(2000) not null,
    primary key (id)
);

create table oauth2_authorization (
    id varchar(100) not null,
    registered_client_id varchar(100) not null,
    principal_name varchar(200) not null,
    authorization_grant_type varchar(100) not null,
    attributes varchar(4000) default null,
    state varchar(500) default null,
    authorization_code_value bytea default null,
    authorization_code_issued_at timestamp default null,
    authorization_code_expires_at timestamp default null,
    authorization_code_metadata varchar(2000) default null,
    access_token_value bytea default null,
    access_token_issued_at timestamp default null,
    access_token_expires_at timestamp default null,
    access_token_metadata varchar(2000) default null,
    access_token_type varchar(100) default null,
    access_token_scopes varchar(1000) default null,
    oidc_id_token_value bytea default null,
    oidc_id_token_issued_at timestamp default null,
    oidc_id_token_expires_at timestamp default null,
    oidc_id_token_metadata varchar(2000) default null,
    refresh_token_value bytea default null,
    refresh_token_issued_at timestamp default null,
    refresh_token_expires_at timestamp default null,
    refresh_token_metadata varchar(2000) default null,
    primary key (id)
);
