create table if not exists posts(
id int primary key,
title varchar(128) not null,
body varchar(512),
author_id int not null,
community_id int not null,
creation_time timestamp default current_timestamp,

foreign key (author_id)
references users(id),

foreign key(community_id)
references communities(id)
)
