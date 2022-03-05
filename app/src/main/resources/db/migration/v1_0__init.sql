-- Creation Date: 2021-11-21
-- Description: Creates the initial table

create table oauth2_registered_client
(
    id                            varchar(100)                            not null,
    client_id                     varchar(100)                            not null,
    client_id_issued_at           timestamp     default CURRENT_TIMESTAMP not null,
    client_secret                 varchar(200)  default null,
    client_secret_expires_at      timestamp     default null,
    client_name                   varchar(200)                            not null,
    client_authentication_methods varchar(1000)                           not null,
    authorization_grant_types     varchar(1000)                           not null,
    redirect_uris                 varchar(1000) default null,
    scopes                        varchar(1000)                           not null,
    client_settings               varchar(2000)                           not null,
    token_settings                varchar(2000)                           not null,
    primary key (id)
);
