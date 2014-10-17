# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table library (
  id                        bigint not null,
  name                      varchar(255),
  create_date               timestamp not null,
  update_date               timestamp not null,
  constraint pk_library primary key (id))
;

create sequence library_seq;




# --- !Downs

drop table if exists library cascade;

drop sequence if exists library_seq;

