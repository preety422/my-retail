-- Create Database from root user
CREATE DATABASE myretail;
commit;

-- Create User
CREATE USER 'myretail_dev'@'localhost' IDENTIFIED BY 'myretail_dev';
commit;

-- Grant database permission
GRANT ALL PRIVILEGES ON myretail.* TO 'myretail_dev'@'localhost'
commit;


