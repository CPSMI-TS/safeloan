drop database if exists safe_loan_test;
create database safe_loan_test;
use safe_loan_test;

create table user (
user_id bigint primary key auto_increment,
user_login varchar(20) unique not null,
user_password varchar(25) not null,
user_debt double not null default 0.0
);

create table loan (
loan_id bigint primary key auto_increment,
loan_sum double not null default 0.0, /*сумма*/
loan_payer bigint not null, /*ссылка на плательщика*/
users_count int not null, /*количество пользователей (включает плательщика)*/
loan_description varchar(100) default "",
loan_date datetime, /*дата добавления (автоматически ставится в "данный момент" триггером)*/
loan_state int not null default 0, /*состояние: 0 - активен, 1 - закрыт*/
extended int not null, /*сейчас всегда ставим в 0б потом будет использоваться для обозначения, что покупка вносится подробно с описанием item'ов*/
loan_url varchar (255), /*ссылка на фото с чеком*/
foreign key (loan_payer) references user (user_id) on delete cascade
);

/*связующая таблица между user и LOANS*/
create table loan_users (
loan_users_id bigint primary key auto_increment,
user_id bigint not null, /*ссылка на пользователя*/
loan_id bigint not null, /*ссылка на долг*/
loan_user_state int not null default 0, /*статус долга по конкретному юзеру: 0 - не оплачен, 1 - ожидает подтверждения, 2 - оплачен*/
loan_user_share double not null, /*доля юзера в долге*/
is_payer int not null, /*является ли юзер плательщиков этой покупки*/
foreign key (user_id) references user (user_id) on delete cascade,
foreign key (loan_id) references loan (loan_id) on delete cascade
);

create table groups (
group_id bigint primary key auto_increment,
group_name varchar(50) not null unique, /*название (уникальное)*/
group_type varchar(20) not null /*тип (из enum): если другой, будет - OTHER*/
);

/*связующая таблица между user и groups*/
create table users_groups (
users_groups_id bigint primary key auto_increment,
user_id bigint not null, /*ссылка на user*/
group_id bigint not null, /*ссылка на группу*/
foreign key (user_id) references user (user_id) on delete cascade, 
foreign key (group_id) references groups (group_id) on delete cascade
);

/*пока не используется*/
create table item (
item_id bigint primary key auto_increment, 
item_loan bigint not null, /*ссылка на долг, к которому принадлежит item*/
item_cost double not null, /*цена item*/
item_name varchar(50) not null, /*название продукта*/
item_category varchar(20), /*категоря продукта*/
foreign key (item_loan) references loan (loan_id) on delete cascade
);
/*триггер установки даты*/
CREATE TRIGGER loan_insert_date BEFORE INSERT ON loan
    FOR EACH ROW SET NEW.loan_date = IFNULL(NEW.loan_date, NOW());
	
/*on delete cascade значит, что автоматически удаляться все записи, ссылающиеся на запись, если она удаляется*/


/*немного примеров*/
use safe_loan_test;
insert into user (user_login, user_password) values ("user1", "pass1");
insert into user (user_login, user_password) values ("user2", "pass2");
insert into loan (loan_payer, loan_date, extended) value (2, now(), 0);
insert into groups (group_name, group_type) values ("Petroff_family", "FAMILY");
insert into users_groups (user_id, group_id) values (2, 1);
delete from users_groups where user_id = 1 and group_id = 1;
delete from user where user_login = "user2";
insert into loan_users (user_id, loan_id) values (1, 1);
delete from loan where loan_id = 2;
select * from users_groups;
select * from loan_users;
select * from loan;
select * from groups;
select * from loan;
select * from user;
select * from users_groups;
insert into loan_users (user_id, loan_id) values (2, 1);