DROP TABLE IF EXISTS offer;

create table offer (
  id identity,
  description varchar(50) not null,
  price double not null,
  currency varchar(25) not null,
  expiration_date timestamp not null,
  status varchar(10) not null,
  cancelled boolean not null
);
