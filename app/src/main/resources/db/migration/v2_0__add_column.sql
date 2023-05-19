-- Creation Date: 2023-05-19
-- Description: Ad new column to table

alter table oauth2_registered_client add column post_logout_redirect_uris varchar(1000) default null;
