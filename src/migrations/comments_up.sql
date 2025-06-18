create table if not exists comments(
id int primary key auto_increment,
author_id int not null,
reply_to int,
reply_to_comment int,
body varchar(256) not null,
creation_time timestamp default current_timestamp,

foreign key (reply_to)
references posts(id),

foreign key(author_id)
references users(id)
)
