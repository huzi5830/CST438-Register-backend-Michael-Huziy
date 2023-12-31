INSERT INTO student VALUES 
(2,'david','dwisneski@csumb.edu',NULL,0),
(3,'test', 'test@csumb.edu', NULL, 0),
(4,'tom', 'tom@csumb.edu', NULL, 0);

INSERT INTO course VALUES 
(2020,'Fall',30157,1,'BUS 203 - Financial Accounting','We 6:00PM - 7:20PM','506','112','cchou@csumb.edu','2020-08-24','2020-12-13'),
(2020,'Fall',30163,1,'BUS 306 - Fundamentals of Marketing','Mo 11:00AM - 11:50AM','Library','1180','anariswari@csumb.edu','2020-08-24','2020-12-13'),
(2020,'Fall',30291,1,'BUS 304 - Business Communication, Pro-seminar & Critical Thinking','Mo 8:00AM - 9:50AM','506','108','kposteher@csumb.edu','2020-08-24','2020-12-13'),
(2020,'Fall',31045,1,'CST 363 - Introduction to Database Systems','MoWe 4:00PM - 5:50PM','506','104','dwisneski@csumb.edu','2020-08-24','2020-12-13'),
(2020,'Fall',31249,1,'CST 237 - Intro to Computer Architecture','TuTh 2:00PM - 3:50PM','506','104','sislam@csumb.edu','2020-08-24','2020-12-13'),
(2020,'Fall',31253,1,'BUS 307 - Finance','We 2:00PM - 3:50PM','506','112','hwieland@csumb.edu','2020-08-24','2020-12-13'),
(2020,'Fall',31747,1,'CST 238 - Introduction to Data Structures','Mo 2:00PM - 2:50PM','506','117','jgross@csumb.edu','2020-08-24','2020-12-13'),
(2021,'Fall',40442,1,'CST 363 - Introduction to Database Systems','MoWe 4:00PM - 5:50PM','506','104','dwisneski@csumb.edu', '2021-08-24', '201-12-13'),
(2021,'Fall',40443,1,'CST 438 - engineering', 'MoWe 2:00PM - 3:50PM','506','104','instructor@csumb.edu', '2021-08-24', '201-12-13')
;

insert into enrollment values 
(2, 2, 2020, 'Fall', 30163, null),
(4, 2, 2020, 'Fall', 31045, null),
(5, 4, 2020, 'Fall', 40443 , null);

insert into user_table
(alias, email, password, role) values 
('tom', 'tom@csumd.edu', '$2a$10$h/92Jk8.WdRsixr.NIndJuD6Pp0KiZag7Q48glJMTbp48WZIOYRDu','STUDENT');

