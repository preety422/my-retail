FROM tomcat:9.0
ADD target/catalogue.war /usr/local/tomcat/webapps/
EXPOSE 8083
EXPOSE 27018
CMD ["catalina.sh", "run"]