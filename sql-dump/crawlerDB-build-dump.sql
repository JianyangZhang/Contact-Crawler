DROP SCHEMA IF EXISTS EmailCrawlerDB;
Create DataBase EmailCrawlerDB;
USE EmailCrawlerDB;

CREATE TABLE User
  (user_id varchar(255) NOT NULL PRIMARY KEY,
    user_name varchar(255),
    user_password varchar(255));

CREATE TABLE Templete
  (templete_id  varchar(255) NOT NULL PRIMARY KEY,
  templete_name varchar(255),
  templete_content text);

CREATE TABLE Linkedin_Account
  (linkedin_id varchar(255) NOT NULL PRIMARY KEY,
    linkedin_username varchar(255),
    linkedin_password varchar(255));

CREATE TABLE Internal_Company
  (internal_company_id varchar(255) NOT NULL PRIMARY KEY,
    internal_company_name varchar(255),
    linkedin_id varchar(255),
    FOREIGN KEY(linkedin_id) REFERENCES Linkedin_Account(linkedin_id));

CREATE TABLE Login
  (login_id varchar(255) NOT NULL PRIMARY KEY,
    login_name varchar(255),
    login_password varchar(255),
    internal_company_id varchar(255),
    FOREIGN KEY(internal_company_id) REFERENCES Internal_Company(internal_company_id));

CREATE TABLE Search
    (search_id varchar(255) NOT NULL PRIMARY KEY,
    search_count int,
    search_keywords varchar(255),
    search_location varchar(255),
    search_company_size varchar(255),
    search_progress enum('pending', 'processing', 'completed', 'failed') NOT NULL,
    user_id varchar(255),
    internal_company_id varchar(255),
    FOREIGN KEY(user_id) REFERENCES User(user_id),
    FOREIGN KEY(internal_company_id) REFERENCES Internal_Company(internal_company_id));

CREATE TABLE Company
  (company_name varchar(255) NOT NULL PRIMARY KEY,
    company_domain varchar(255));

CREATE TABLE Compaign
  (compaign_step varchar(255) NOT NULL,
  internal_company_id varchar(255) NOT NULL,
    templete_id varchar(255),
    PRIMARY KEY( compaign_step, internal_company_id),
    FOREIGN KEY (templete_id) REFERENCES Templete(templete_id),
    FOREIGN KEY (internal_company_id) REFERENCES Internal_Company(internal_company_id)
    );

CREATE TABLE Customer
  (customer_linkedin_url varchar(255) NOT NULL PRIMARY KEY,
    customer_name varchar(255),
    customer_title varchar(255),
    customer_location varchar(255),
    customer_keywords varchar(255),
    customer_create_time varchar(255),
    customer_touch_time varchar(255),
    internal_company_id varchar(255),
    compaign_step varchar(255),
    FOREIGN KEY(internal_company_id) REFERENCES Internal_Company(internal_company_id),
    FOREIGN KEY (compaign_step) REFERENCES Compaign (compaign_step));

CREATE TABLE Email
  (email_address varchar(255) NOT NULL PRIMARY KEY,
    customer_linkedin_url varchar(255),
    company_name varchar(255),
    email_response_time int,
    FOREIGN KEY(company_name) REFERENCES Company(company_name),
    FOREIGN KEY(customer_linkedin_url) REFERENCES Customer(customer_linkedin_url));

CREATE TABLE Employment
  (customer_linkedin_url varchar(255),
    company_name varchar(255),
    PRIMARY KEY(customer_linkedin_url, company_name),
    FOREIGN KEY (customer_linkedin_url) REFERENCES Customer (customer_linkedin_url),
    FOREIGN KEY(company_name) REFERENCES Company(company_name));
    
CREATE TABLE Result
  (search_id varchar(255),
    customer_linkedin_url varchar(255),
    PRIMARY KEY(search_id, customer_linkedin_url),
    FOREIGN KEY (search_id) REFERENCES Search (search_id),
    FOREIGN KEY (customer_linkedin_url) REFERENCES Customer (customer_linkedin_url));