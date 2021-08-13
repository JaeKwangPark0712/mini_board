select * from member;
create table article (
	article_no int auto_increment primary key,
	writer_id varchar(50) not null,
	writer_name varchar(50) not null,
	title varchar(255) not null,
	regdate datetime not null,
	moddate datetime not null,
	read_cnt int
);

create table article_content(
	article_no int primary key,
	content text
);

select * from article;
select * from article_content;

select * from article where article_no = 5;