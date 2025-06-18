create table if not exists logs(
id int primary key,
log_level varchar(10),
metadata varchar(256),
time_stamp timestamp not null default current_timestamp
)
