-- create table Tweets (
-- 	id identity,
-- 	message varchar(140) not null,
-- 	created_at timestamp not null,
-- 	latitude double,
-- 	longitude double
-- );
--
-- create table Users (
-- 	id identity,
-- 	username varchar(20) unique not null,
-- 	password varchar(20) not null,
-- 	first_name varchar(30) not null,
-- 	last_name varchar(30) not null,
-- 	email varchar(30) not null
-- );
--
-- -- default user
-- insert into Users (username, password, first_name, last_name, email)
-- values ('user01', 'pass01', 'Dmytro', 'Krasota', 'dkras@gmail.com');
-- commit;

CREATE TABLE UserProfile (
  id IDENTITY,
  username VARCHAR(20) UNIQUE not null,
  password VARCHAR(20) not null,
  email VARCHAR(30) not null
);