
#!/bin/bash

# Define project directory
PROJECT_DIR="/home/coder/project/workspace/question_generation_service/solutions/332a0778-9402-4e30-ae6a-c44dd4be37ff/springapp"

# Define database name using request ID
DATABASE_NAME="332a0778_9402_4e30_ae6a_c44dd4be37ff"

# Create MySQL database
mysql -u root -pexamly -e "CREATE DATABASE IF NOT EXISTS ${DATABASE_NAME};" 2>/dev/null || echo "Database creation failed, will use default"

# Generate Spring Boot project using Spring CLI
spring init \
  --type=maven-project \
  --language=java \
  --boot-version=3.4.0 \
  --packaging=jar \
  --java-version=17 \
  --groupId=com.examly \
  --artifactId=springapp \
  --name="Organ Donation Management System" \
  --description="Organ Donation Management System for connecting donors with hospitals" \
  --package-name=com.examly.springapp \
  --dependencies=web,data-jpa,validation,mysql,lombok \
  --build=maven \
  ${PROJECT_DIR}

# Wait for project generation to complete
sleep 2

# Create application.properties with MySQL configuration
cat > "${PROJECT_DIR}/src/main/resources/application.properties" << EOL
spring.datasource.url=jdbc:mysql://localhost:3306/${DATABASE_NAME}?createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=examly
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.hibernate.ddl-auto=create
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
EOL

# Create necessary package directories
mkdir -p "${PROJECT_DIR}/src/main/java/com/examly/springapp/model"
mkdir -p "${PROJECT_DIR}/src/main/java/com/examly/springapp/controller"
mkdir -p "${PROJECT_DIR}/src/main/java/com/examly/springapp/repository"
mkdir -p "${PROJECT_DIR}/src/main/java/com/examly/springapp/service"
mkdir -p "${PROJECT_DIR}/src/main/java/com/examly/springapp/exception"
mkdir -p "${PROJECT_DIR}/src/main/java/com/examly/springapp/dto"
