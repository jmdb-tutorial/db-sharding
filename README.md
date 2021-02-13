# Database Sharding


Basically the idea is to split the data across either multiple tables or multiple instances of a database.


https://www.postgresql.org/docs/13/ddl-partitioning.html#DDL-PARTITIONING-DECLARATIVE

https://www.postgresql.org/docs/13/sql-createforeigntable.html
https://www.postgresql.org/docs/13/ddl-partitioning.html#DDL-PARTITIONING-DECLARATIVE


# Pre-requisites

You will need Postgres running somewhere. On a mac an easy way to do this is to install the psotgres.app which will also give you psql on your command line which is helpful.

```
\pset pager

psql -U postgres

select trim(id), trim(name) from customer;
```
