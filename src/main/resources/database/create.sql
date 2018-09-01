create or replace table if not exists PassProtect.`Key`
(
	id int auto_increment
		primary keyStore,
	userId int not null,
	name varchar(50) charset utf8 null,
	`keyStore` varchar(512) charset utf8 null
)
;

create or replace table if not exists PassProtect.Password
(
	id int auto_increment
		primary keyStore,
	userId int not null,
	name varchar(50) charset utf8 null,
	description varchar(500) charset utf8 null,
	password varchar(500) charset utf8 null,
	keyId int not null
)
;

create or replace table if not exists PassProtect.User
(
	id int auto_increment
		primary keyStore,
	name varchar(20) charset utf8 not null,
	password varchar(64) not null,
	salt varchar(32) charset utf8 null,
	constraint User_name_uindex
		unique (name)
)
;

create or replace table if not exists PassProtect.UserRoles
(
	id int auto_increment
		primary keyStore,
	name varchar(20) null,
	role varchar(20) charset utf8 null,
	constraint UserRoles_name_role_uindex
		unique (name, role)
)
;

