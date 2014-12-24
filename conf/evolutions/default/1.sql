# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table books (
  id                        bigint not null,
  isbn_code                 varchar(255),
  book_name                 varchar(255),
  publisher                 varchar(255),
  author                    varchar(255),
  image                     varchar(255),
  owner_id                  bigint,
  borrower_id               bigint,
  room_id                   bigint,
  owner_name                varchar(255),
  borrower_name             varchar(255),
  book_status               varchar(255),
  delete_status             varchar(255),
  constraint pk_books primary key (id))
;

create table rooms (
  id                        bigint not null,
  room_name                 varchar(255),
  create_user_id            bigint,
  constraint pk_rooms primary key (id))
;

create table users (
  id                        bigint not null,
  username                  varchar(255),
  email                     varchar(255),
  password                  varchar(255),
  constraint pk_users primary key (id))
;

create sequence books_seq;

create sequence rooms_seq;

create sequence users_seq;

alter table books add constraint fk_books_owner_1 foreign key (owner_id) references users (id);
create index ix_books_owner_1 on books (owner_id);
alter table books add constraint fk_books_borrower_2 foreign key (borrower_id) references users (id);
create index ix_books_borrower_2 on books (borrower_id);
alter table books add constraint fk_books_room_3 foreign key (room_id) references rooms (id);
create index ix_books_room_3 on books (room_id);
alter table rooms add constraint fk_rooms_create_user_4 foreign key (create_user_id) references users (id);
create index ix_rooms_create_user_4 on rooms (create_user_id);



# --- !Downs

drop table if exists books cascade;

drop table if exists rooms cascade;

drop table if exists users cascade;

drop sequence if exists books_seq;

drop sequence if exists rooms_seq;

drop sequence if exists users_seq;

