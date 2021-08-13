drop table member;

create table member(
	memberid varchar(50) primary key,
	name varchar(50) not null,
	password  varchar(50) not null,
	regdate timestamp not null
);

select * from member;
select * from member where memberid = 'Example';