My approach of the task :

I think one of the catchs is that the whole api is going to run in a distributed cluster environment, meaning that any database access or scheduled task is going to have to be locked to avoid multiple instances running them at the same time.

My first step is to create the structure of the project, I will for this purpose create the following packages : 
DTOs  (I prefer separating any data that will go or come from outside the API from our models and entities)
Models (mapped directly from the DTOs when possible to then have any potential logic applied to them)
Entities (to communicate with DB)
Controllers (Receiving rest calls)
Services (logic layer, where the data is treated and transformed)
Repositories (Data layer)
Config (for now only a generic config, but for the sake of flexibility and futureproofing, I like having a config folder that can hold component-specific configurations)
Exceptions (to have custom exceptions)
Builders

The service and controller layers won't require much work, so my first focus will be to create two simple schemas for the database, one for the customers, and one for the sims, having a one to many relationship (one customer can have multiple sims). For the sake of the exercise, I decided to generate the databases automatically with spring data JPA.

I have never dealt with processes having a scheduled task, so most of my focus will be on implementing them and making sure they are locked to a single instance running them.

I also decided to implement a small controller advise to be able to handle exceptions in a flexible and futureproof way.

To avoid any issues with passing a LocalDate to the controller, I decided to make the birthDate a String in the DTO (with a regex) and implement a specific configuration for modelMapper to convert it to a localDate.

Functionalities : 

This simple API will provide with a database handling customers and sims.
It comes with a few simple functionalities : 
- Registering a new customer
- Registering a new sim
- Linking an existing sim to a customer
- Retrieving all sims from a specific customer
- Retrieving all sims.

It also runs daily two tasks : 

1 : A daily export of customers having their birthday on the date.
- To achieve this, I decided to run a daily task at 6 am checking the customers celebrating their birthday on the day, and then exporting them to a csv file located in the "exports" folder.
To try this functionnality, You can run the test "dailyExportCheck" in the file "AssignmentServiceTest", and then check the created file in the exports folder.

2 : A daily check for customers celebrating their birthday in 7 days, which sends them an email accordingly.
- To achieve this, I decided to run a daily task at 6 am checking the customers celebrating their birthday in exactly seven days, then sending them an email with a configurable template.


Release Notes : 

This API requires the following config entries to be set accordingly : 

* spring.jpa.properties.hibernate.jdbc.batch_size=4
* spring.jpa.properties.hibernate.order_inserts=true
* spring.jpa.properties.hibernate.order_updates=true
* spring.jpa.properties.hibernate.generate_statistics=true
* spring.jpa.properties.javax.persistence.schema-generation.scripts.action=create
* spring.jpa.properties.javax.persistence.schema-generation.scripts.create-target=create.sql
* spring.jpa.properties.javax.persistence.schema-generation.scripts.create-source=metadata
* spring.jpa.properties.hibernate.format_sql=true

* spring.datasource.driverClassName= org.h2.Driver
* spring.datasource.url= jdbc:h2:mem:shedlock_DB
* spring.datasource.username= sa
* spring.datasource.password=

* spring.mail.host=smtp.gmail.com
* spring.mail.port=587
* spring.mail.username : The google smtp username used to send the emails to the customers
* spring.mail.password : The google smtp password used to send the emails to the customers
* email.subject : the subject of the email sent to customers having their birthdays the following week
* email.textTemplate : the body of the email sent to customers having their birthdays the following week
