create table if not exists communities(
id int primary key auto_increment,
name varchar(50) not null unique,
creator_id int not null,
description varchar(128),
foreign key(creator_id)
references users(id)
)
