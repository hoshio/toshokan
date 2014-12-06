# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table book (
  id                        bigint not null,
  book_name                 varchar(255),
  owner_name                varchar(255),
  delete_flg                varchar(255),
  borrower                  varchar(255),
  constraint pk_book primary key (id))
;

create sequence book_seq;




# --- !Downs

drop table if exists book cascade;

drop sequence if exists book_seq;

