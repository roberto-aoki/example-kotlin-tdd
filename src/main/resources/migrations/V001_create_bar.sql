--liquibase formatted sql

--changeset roberto.aoki:001

create table bar(
    id uuid primary key,
    name text not null
)
--rollback drop table foo