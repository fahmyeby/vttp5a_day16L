"# vttp5a_day16L" 

Process Flow
Create Book
1) fill form at /books/create
2) form submitted to BookController
3) BookController process and validate data
4) If ok then pass to BookService
5) BookService converts to JSON and store in redis via repo class
6) User redirected to list page

View Book
1) User navigate to /books/list
2) BookController call BookService.findAll()
3) BookService get JSON strings from redis via repo class
4) BookService convert JSON back to Book objects
5) Books pass to html thymeleaf
