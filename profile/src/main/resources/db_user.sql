-- Create Database from root user
CREATE DATABASE myretail_profile;
commit;

-- Create User
CREATE USER 'profile_dev'@'localhost' IDENTIFIED BY 'profile_dev';
commit;

-- Grant database permission
GRANT ALL PRIVILEGES ON myretail_profile.* TO 'profile_dev'@'localhost'
commit;


