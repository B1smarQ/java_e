create table if not exists users(
id int primary key auto_increment,
username varchar(50) unique not null,
user_password varchar(256) not null,
email varchar(50) not null unique,
created_at timestamp default current_timestamp,
updated_at timestamp default current_timestamp,
user_role varchar(10) default "user" 
  constraint role_check CHECK ( role = "user" or role = "admin" or role = "mod" )
)
