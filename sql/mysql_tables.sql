create database kalahadb;
use kalahadb;

create table users (
	I_USERID INT NOT NULL,
	S_USERNAME varchar(20) unique NOT NULL,
	S_PASSWORD varchar(30),
	PRIMARY KEY(I_USERID)
);

insert into users(I_USERID, S_USERNAME, S_PASSWORD) values
(0, "player1", "player1");
insert into users(I_USERID, S_USERNAME, S_PASSWORD) values
(1, "player2", "player2");

select * from users;

create table games (
	I_PLAYER1_ID int not null,
	I_PLAYER2_ID int not null,
	I_PIT_1_1 INT,
	I_PIT_1_2 INT,
	I_PIT_1_3 INT,
	I_PIT_1_4 INT,
	I_PIT_1_5 INT,
	I_PIT_1_6 INT,
	I_STORE_1 INT,
	I_PIT_2_1 INT,
	I_PIT_2_2 INT,
	I_PIT_2_3 INT,
	I_PIT_2_4 INT,
	I_PIT_2_5 INT,
	I_PIT_2_6 INT,
	I_STORE_2 INT,
	B_TURN_P1 boolean,
	primary key(I_PLAYER1_ID, I_PLAYER2_ID),
	CONSTRAINT fk_player1 FOREIGN KEY (I_PLAYER1_ID)
	REFERENCES users(I_USERID),
	CONSTRAINT fk_player2 FOREIGN KEY (I_PLAYER2_ID)
	REFERENCES users(I_USERID)
);

INSERT INTO games(I_PLAYER1_ID,
	I_PLAYER2_ID,
	I_PIT_1_1,
	I_PIT_1_2,
	I_PIT_1_3,
	I_PIT_1_4,
	I_PIT_1_5,
	I_PIT_1_6,
	I_STORE_1,
	I_PIT_2_1,
	I_PIT_2_2,
	I_PIT_2_3,
	I_PIT_2_4,
	I_PIT_2_5,
	I_PIT_2_6,
	I_STORE_2,
	B_TURN_P1) VALUES (0, 1, 6, 6, 6, 6, 6, 6, 0, 6, 6, 6, 6, 6 ,6, 0, true);

select * from games;