# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table person (
  email                     varchar(255) not null,
  default_score             double,
  fname                     varchar(255),
  lname                     varchar(255),
  score                     double,
  dob                       varchar(255),
  ph_no                     varchar(255),
  gender                    varchar(255),
  password                  varchar(255),
  constraint pk_person primary key (email))
;

create table task_info (
  task_id                   bigint not null,
  title                     varchar(255),
  description               varchar(255),
  created_by                varchar(255),
  email_assigned_to         varchar(255),
  done                      boolean,
  assigned                  boolean,
  recurring_status          boolean,
  start_date                varchar(255),
  end_date                  varchar(255),
  old_points                double,
  new_points                double,
  constraint pk_task_info primary key (task_id))
;

create sequence person_seq;

create sequence task_info_seq;




# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists person;

drop table if exists task_info;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists person_seq;

drop sequence if exists task_info_seq;

