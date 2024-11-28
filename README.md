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

Form Submission Flow (/create)
User Input -> Form -> Controller -> Service -> Repository -> Redis
   ↑            ↓                                             
Validation   Validation

REST API Flow
POST
JSON Request -> RestController -> Service -> Repository -> Redis
     ↓              ↓             ↓
   Parsing      Validation    Conversion

GET
Redis -> Repository -> Service -> RestController -> JSON Response
                        ↓             ↓
                   Processing     Formatting

// List Operations
rightPush(key, value)    // Add to end
leftPush(key, value)     // Add to start
range(key, start, end)   // Get range
size(key)               // Get list size

// Value Operations
set(key, value)         // Set value
get(key)                // Get value
delete(key)             // Delete value
hasKey(key)             // Check existence
