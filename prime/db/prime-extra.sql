
alter table classicmodels.Employees add column version int not null default 0;
alter table classicmodels.Offices add column version int not null default 0;

CREATE TABLE sequence (
  name varchar(30) NOT NULL,
  nextid int(11) NOT NULL,
  PRIMARY KEY (name)
) ENGINE=InnoDB;

insert into sequence(name, nextid) values ('employeeNumber', 3000);
insert into sequence(name, nextid) values ('customerNumber', 500);
insert into sequence(name, nextid) values ('officeCode', 10);


ALTER TABLE classicmodels.Employees
ADD CONSTRAINT fk_Employees_Offices
FOREIGN KEY (officeCode)
REFERENCES Offices(officeCode);

ALTER TABLE classicmodels.Employees
ADD CONSTRAINT fk_Employees_Employees
FOREIGN KEY (reportsTo)
REFERENCES Employees(employeeNumber);

ALTER TABLE classicmodels.Customers
ADD CONSTRAINT fk_Customers_Employees
FOREIGN KEY (salesRepEmployeeNumber)
REFERENCES Employees(employeeNumber);


ALTER TABLE classicmodels.Payments
ADD CONSTRAINT fk_Payments_Customers
FOREIGN KEY (customerNumber)
REFERENCES Customers(customerNumber);

ALTER TABLE classicmodels.Orders
ADD CONSTRAINT fk_Orders_Customers
FOREIGN KEY (customerNumber)
REFERENCES Customers(customerNumber);


ALTER TABLE classicmodels.OrderDetails
ADD CONSTRAINT fk_OrderDetails_Orders
FOREIGN KEY (orderNumber)
REFERENCES Orders(orderNumber);


ALTER TABLE classicmodels.OrderDetails
ADD CONSTRAINT fk_OrderDetails_Products
FOREIGN KEY (productCode)
REFERENCES Products(productCode);

ALTER TABLE classicmodels.Products
ADD CONSTRAINT fk_Products_ProductLines
FOREIGN KEY (productLine)
REFERENCES ProductLines(productLine);
