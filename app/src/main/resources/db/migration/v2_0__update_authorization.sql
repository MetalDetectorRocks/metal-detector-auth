-- Creation Date: 2022-02-20
-- Description: Update oauth2 authorization because of spring authorization server update 0.2.2

alter table oauth2_authorization
    drop column if exists attributes;
alter table oauth2_authorization
    drop column if exists authorization_code_metadata;
alter table oauth2_authorization
    drop column if exists access_token_metadata;
alter table oauth2_authorization
    drop column if exists oidc_id_token_metadata;
alter table oauth2_authorization
    drop column if exists refresh_token_metadata;

alter table oauth2_authorization
    add column if not exists attributes bytea default null;
alter table oauth2_authorization
    add column if not exists authorization_code_metadata bytea default null;
alter table oauth2_authorization
    add column if not exists access_token_metadata bytea default null;
alter table oauth2_authorization
    add column if not exists oidc_id_token_metadata bytea default null;
alter table oauth2_authorization
    add column if not exists refresh_token_metadata bytea default null;
