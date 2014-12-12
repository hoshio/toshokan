# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table admins (
  id                        bigint not null,
  username                  varchar(255),
  email                     varchar(255),
  password                  varchar(255),
  constraint pk_admins primary key (id))
;

create table book (
  id                        bigint not null,
  book_name                 varchar(255),
  owner_name                varchar(255),
  borrower                  varchar(255),
  book_status               varchar(255),
  delete_status             varchar(255),
  constraint pk_book primary key (id))
;

create table room (
  id                        bigint not null,
  room_name                 varchar(255),
  constraint pk_room primary key (id))
;

create sequence admins_seq;

create sequence book_seq;

create sequence room_seq;




# --- !Downs

drop table if exists admins cascade;

drop table if exists book cascade;

drop table if exists room cascade;

drop sequence if exists admins_seq;

drop sequence if exists book_seq;

drop sequence if exists room_seq;

