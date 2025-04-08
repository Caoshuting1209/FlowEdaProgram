## Flow-eda-oauth2

## Brief

> #### A low-code programming application based on event-driven principles, utilizing a visual programming approach to draw processes by connecting nodes, thereby enabling business programming through the composition of these nodes.

## Functions

- ### Operation flow

  > #### Each 「nodeType」 has a specified set of 「nodeTypeParams」, which can be entered by the user. By combining a series of 「nodeTypes」, a 「nodedata」 is generated, which is used to determine the transaction flow.

- ### Authentication and authentication

  > #### JwtToken-based back-end authentication, form authentication and OAuth2-based third-party authentication have been introduced.

## Module

- #### Flow-eda-commons: Common configuration classes, exception classes, and constant classes.

- #### Flow-eda-

## Feature

- #### The entire process running engine is implemented at the back end, and only lightweight front end is needed.

- #### Multithreaded processing, parallel and serial processes are supported.

- #### The modular and configured design makes it easy to implement extension and development of capabilities.

- #### The back-end design adopts microservice architecture(spring boot), and each module is decoupled from each other according to business functions, which makes it convenient for secondary development and function expansion.

- #### Front-end and back-end decoupled authentication model is supported.

## Back-end technology stack

- #### Spring Boot: Microservices framework

- #### Maven: Project build management tool

- #### Mybatisplus: Object-relational mapping(ORM) framework

- #### Mysql: Database

- #### Redis: Token storage tool

- #### Lombok：Code plug-in

- #### SpringSecurity + Oauth2: Authentication and authentication framework

## System architecture diagram