# FoodNote – Full Stack Food Sharing Platform

## Project Overview
**FoodNote** is a full stack food sharing platform that allows users to explore, share, and discover food experiences. The project demonstrates hands-on experience with Java backend development, RESTful API design, and modern front-end development with Vue.

## Demo Video
[![Watch the Demo](C:\Users\zhent\Desktop\Project\foodnote_files\img_videos\cover.png)](https://youtu.be/yIlt_EJRIQU)

## Key Features
- User registration and authentication
- Create, update, and delete food posts
- Browse and search food categories
- Comment and like system
- Responsive front-end interface
- Admin access control for content management
- Data visualization for admin and users

## Technology Stack
- **Backend:** Java, Spring Boot, REST API, MyBatis  
- **Frontend:** Vue.js, HTML, CSS, JavaScript  
- **Database:** MySQL  
- **Utilities:** Lombok, Maven, Git

## Architecture Overview

The project follows a **layered architecture**:

Frontend (Vue) → Controller → Service → DAO / Mapper → Database (MySQL)

- **Controller**: Handles incoming HTTP requests and maps them to services  
- **Service**: Implements business logic, validates data, and orchestrates DAO calls  
- **DAO / Mapper**: Performs database operations with MyBatis (CRUD operations, custom queries)  
- **DTO / Query Objects**: Used to transfer data between layers and avoid exposing database entities directly


## Contact
**Developer:** Zhenting Zheng  
**GitHub:** [https://github.com/zhentingzheng](https://github.com/ZhengZhenting/)
