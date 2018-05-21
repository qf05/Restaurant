[![Dependency Status](https://www.versioneye.com/user/projects/5b026d370fb24f0e57e3e64f/badge.svg?style=flat-square)](https://www.versioneye.com/user/projects/5b026d370fb24f0e57e3e64f)

[![Codacy Badge](https://api.codacy.com/project/badge/Grade/292c090f96aa4b4499f5ce39650e8197)](https://www.codacy.com/app/qf05/Restaurant?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=qf05/Restaurant&amp;utm_campaign=Badge_Grade)




# Restaurant

### Тестовое задание на оплачиваемую стажировку

Design and implement a REST API using Hibernate/Spring/SpringMVC (or Spring-Boot) without frontend.

The task is:

Build a voting system for deciding where to have lunch.

* 2 types of users: admin and regular users
* Admin can input a restaurant and it's lunch menu of the day (2-5 items usually, just a dish name and price)
* Menu changes each day (admins do the updates)
* Users can vote on which restaurant they want to have lunch at
* Only one vote counted per user
* If user votes again the same day:
  * If it is before 11:00 we asume that he changed his mind.
  * If it is after 11:00 then it is too late, vote can't be changed
  
Each restaurant provides new menu each day.

As a result, provide a link to github repository.

It should contain the code and README.md with API documentation and curl commands to get data for voting and vote.

---
P.S.: Make sure everything works with latest version that is on github :)

P.P.S.: Asume that your API will be used by a frontend developer to build frontend on top of that.

---

### Description

**For bild use Java 9.**

Configuration The application configured with predefined users(role, email, password):

* ADMIN, admin@gmail.com, admin
* USER, user1@yandex.ru, password
* USER, user2@yandex.ru, password

**Wherever the date is used if the date is empty or absent:**
* date = Today,
* startDate = MinDate (year=1, mouth=1, day=1),
* endDate = MaxDate (year=3000, mouth=1, day=1),
* for copy menu date = Yesterday.

### CURL Commands

---
##### RESTAURANT

get Restaurant 100003

`curl -s http://localhost:8080/restaurant/100003 --user user1@yandex.ru:password`


get All Restaurant width All Voice

`curl -s http://localhost:8080/restaurant --user user1@yandex.ru:password`


get All Restaurant width Voice for date

`curl -s http://localhost:8080/restaurant/date?date=2018-03-24 --user user1@yandex.ru:password`


get All Restaurant width All Voice and Menu for date

`curl -s http://localhost:8080/restaurant/menu?date=2018-03-24 --user user1@yandex.ru:password`


get All Restaurant width Voice and Menu for date

`curl -s http://localhost:8080/restaurant/date/menu?date=2018-03-24 --user user1@yandex.ru:password`


get All Restaurant width history Voice from date to date

`curl -s "http://localhost:8080/restaurant/date/menu?startDate=2018-03-24&endDate=2018-03-25" --user user1@yandex.ru:password`


get All Restaurant width history Voice from date to date and Menu for date

`curl -s "http://localhost:8080/restaurant/history/menu?startDate=2018-03-24&endDate=2018-03-25&dateMenu=2018-03-24" --user user1@yandex.ru:password`


get All Restaurant width history Voice from date to date and history Menu from date to date 

`curl -s "http://localhost:8080/restaurant/history/menu/history?startDateVoice=2018-03-24&endDateVoice=2018-03-25&startDateMenu=2018-03-24&endDateMenu=2018-03-25" --user user1@yandex.ru:password`


create Restaurant

`curl -s -X POST -d '{"name":"Dizzy"}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/restaurant --user admin@gmail.com:admin`


delete Restaurant 100003

`curl -s -X DELETE http://localhost:8080/restaurant/100003 --user admin@gmail.com:admin`


update Restaurant 100003

`curl -s -X PUT -d '{"id":100003,"name":"newRestaurant"}' -H 'Content-Type: application/json' http://localhost:8080/restaurant/100003 --user admin@gmail.com:admin`


---
##### MENU


get All Eat from Restaurant 100003 for date

`curl -s http://localhost:8080/menu/100003?date=2018-03-24 --user user1@yandex.ru:password`


get history Eat from Restaurant 100003 from date to date

`curl -s "http://localhost:8080/menu/history/100003?startDate=2018-03-24&endDate=2018-03-25" --user user1@yandex.ru:password`


crate Eat for Restaurant 100003

`curl -s -X POST -d '{"name":"Cacke","price":100}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/menu/100003 --user admin@gmail.com:admin`


update Eat 100007 for Restaurant 100003

`curl -s -X PUT -d '{"id":100007,"date":"2018-03-26","name":"Pudding","price":100}' -H 'Content-Type: application/json' http://localhost:8080/menu/100003/100007 --user admin@gmail.com:admin`


delete Eat 100007

`curl -s -X DELETE http://localhost:8080/menu/100007 --user admin@gmail.com:admin`


get Eat 100007

`curl -s http://localhost:8080/menu/is/100007 --user user1@yandex.ru:password`


copy Menu for Restaurant 100003 from date (if date is absent or date is today, then use yesterday
 date)

`curl -s http://localhost:8080/menu/copy/100003?date=2018-03-24 --user admin@gmail.com:admin`


---
##### VOICES

get All Voices from date

`curl -s http://localhost:8080/voice?date=2018-03-24 --user user1@yandex.ru:password`


get All Voices from Restaurant 100003 for date

`curl -s http://localhost:8080/voice/100003?date=2018-03-24 --user user1@yandex.ru:password`


get history Voices from Restaurant 100003 from date to date

`curl -s "http://localhost:8080/voice/history/100003?startDate=2018-03-24&endDate=2018-03-25" --user user1@yandex.ru:password`


get history All Voices from date to date

`curl -s "http://localhost:8080/voice/history?startDate=2018-03-24&endDate=2018-03-25" --user user1@yandex.ru:password`


get Voice for User 100000 for date

`curl -s http://localhost:8080/voice/is/100000?date=2018-03-24 --user user1@yandex.ru:password`


Voice to Restaurant 100003

`curl -s -X POST http://localhost:8080/voice/100003 --user user1@yandex.ru:password`


---
##### USER

Registration new User (No sequrity)

`curl -s -X POST -d '{"name":"newUser","email":"newuser@ya.ru","password":"newPass"}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/login/registration`


get User

`curl -s http://localhost:8080/profile --user user1@yandex.ru:password`


update User

`curl -s -X PUT -d '{"id":100001,"name":"updateUser","email":"update@ya.ru","password":"newPass"}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/profile --user user2@yandex.ru:password`


delete User

`curl -s -X DELETE http://localhost:8080/profile --user user2@yandex.ru:password`


---
##### ADMIN

get All User

`curl -s http://localhost:8080/admin/users --user admin@gmail.com:admin`


get By email

`curl -s http://localhost:8080/admin/users/by?email=user1@yandex.ru --user admin@gmail.com:admin`


get User 100000

`curl -s http://localhost:8080/admin/users/100000 --user admin@gmail.com:admin`


create User

`curl -s -X POST -d '{"name":"newUser2","email":"newuser2@ya.ru","password":"newPass"}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/admin/users/ --user admin@gmail.com:admin`


update User

`curl -s -X PUT -d '{"id":100000,"name":"updateUser","email":"update@ya.ru","password":"newPass"}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/admin/users/100000 --user admin@gmail.com:admin`


delete User 100000

`curl -s -X DELETE http://localhost:8080/admin/users/100000 --user admin@gmail.com:admin`

