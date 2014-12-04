postgres-demo
=============

demo using postgres with java

pre-requisites
==============
*postgres database with the following table
CREATE TABLE test_table
(
  dt timestamp,
  id uuid
);

*properties file with the following content
host=your host

port=5432 (or whichever port is listening on)

user=your db user

pass=your db user password

database=your db

run
===
pass the file path as an argument to the program
