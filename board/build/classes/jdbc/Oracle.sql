drop table member;

create table member(
	memberid varchar(50) primary key,
	name varchar(50) not null,
	password  varchar(50) not null,
	regdate timestamp not null
);

select * from member;
select * from member where memberid = 'Example';

// 게시글 정보 저장 테이블
create table article(
	article_no int primary key,
	writer_id varchar(50) not null,
	writer_name varchar(50) not null,
	title varchar(50) not null,
	regdate timestamp not null,
	moddate timestamp not null,
	read_cnt int
);

// 게시글 번호  자동 증가 시퀀스
create sequence num_seq;

// 게시글 내용 저장 테이블
create table article_content (
	article_no int primary key,
	content long
);

select * from article;
select * from article_content;

select count(*) from article;