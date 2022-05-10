DROP DATABASE IF EXISTS Market;
CREATE DATABASE IF NOT EXISTS Market;
USE Market;

DROP TABLE IF EXISTS Customer;
CREATE TABLE IF NOT EXISTS Customer(
    custId VARCHAR (6),
    custName VARCHAR (30) NOT NULL DEFAULT 'unknown',
    custSalary DECIMAL (6,2) DEFAULT 0.00,
    custAddress VARCHAR (100),
    CONSTRAINT PRIMARY KEY (custId)
    );
SHOW TABLES;
DESC Customer;

#--------------------------------------------------------
DROP TABLE IF EXISTS Item;
CREATE TABLE IF NOT EXISTS Item(
    itemId VARCHAR (6),
    itemName VARCHAR (50),
    unitPrice DECIMAL (6,2) DEFAULT 0.00,
    qty INT DEFAULT 0,
    CONSTRAINT PRIMARY KEY (itemId)
    );
SHOW TABLES;
DESC Item;

#--------------------------------------------------------

DROP TABLE IF EXISTS Orders;
CREATE TABLE IF NOT EXISTS Orders(
    orderId VARCHAR (6),
    date DATE,
    custId VARCHAR (6),
    cost DECIMAL (10,2) DEFAULT 0.00,
    CONSTRAINT PRIMARY KEY (orderId),
    CONSTRAINT FOREIGN KEY (custId) REFERENCES Customer(custId) ON DELETE CASCADE ON UPDATE CASCADE
    );
SHOW TABLES;
DESC Orders;

#--------------------------------------------------------

DROP TABLE IF EXISTS OrderDetail;
CREATE TABLE IF NOT EXISTS OrderDetail(
    orderId VARCHAR (6),
    itemId VARCHAR (6),
    qty INT,
    price DECIMAL (10,2) DEFAULT 0.00,
    discount DECIMAL (10,2) DEFAULT 0.00,
    CONSTRAINT PRIMARY KEY (orderid,itemId),
    CONSTRAINT FOREIGN KEY (orderId) REFERENCES Orders(orderId) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT FOREIGN KEY (itemId) REFERENCES Item(itemId) ON DELETE CASCADE ON UPDATE CASCADE
    );
SHOW TABLES;
DESC OrderDetail;