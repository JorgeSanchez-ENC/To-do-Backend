#To do application Backend
The backend of a project intended for tasks administration, developed using spring boot and maven.

##Geting started
1. Clone the repository:
```bash
git clone https://github.com/JorgeSanchez-Encora/To-do-Backend.git
```
2. Change to the project directory:
```bash
cd To-Do-Backend
```

##Runing the app
Run the application:
```bash
mvn spring-boot:run
```
The application runs on port 9090

##Test the app
Test the application:
```bash
mvn test
```
##Todo entity
The todo entity is composed of this parameters.
-Long id
-String text
-LocalDateTime dueDate
-boolean done
-LocalDateTime doneDate
-Priority priority (LOW,MEDIUM,HIGH)
-LocalDateTime creationDate


##Endpoints
You can find the following endpoints

###Fetch the todos
-`Get /todos`
-**Description:** Gets the information about the todos that matches certain filtering parameters, along with the current page for pagination, and the total amount of pages
-**Parameters:**
    -`text`. an **optional** parameter, part of the name of a todo.
    -`priority`. an **optional** parameter, the priority of a todo, must be LOW, MEDIUM or HIGH.
    -`state`. an **optional** parameter, a boolean representing if a todo is done, false if you want the undo todos, true if else.
    -`page`. though it is an optional parameter, as its default value is 1,this parameter accepts numbers representing the current page.
    All of the parameters must be on the body of the request.
-**Example response**
```JSON

    {
      "totalPages": 0,
      "currentPage": 1,
      "todosList": []
    }

```

###Create a todo
-`POST /todos`
-**Description:** this endpoint allows to insert a new todo.
-**Parameters:**
    -`todo`. **not optional**. The todo that is going to be added, at leas has to have text and priority
        All of the parameters must be on the body of the request.
-**Response:** this endpoint will return a http status created if done right.

###Update a todo
-`PUT /todos/{id}`
-**Description:** this endpoint allows to update a given todo.
-**Parameters:**
    -`todo`. **not optional**. The todo information that is going to be modified, at leas has to have text and priority. Must be in the body.
    -`id`. **not optional**. The id of the todo that is going to be modified, must be in the path.
-**Response:** this endpoint will return a http status ok if done right.

###Delete a todo
-`POST /todos/{id}`
-**Description:** this endpoint allows to delete a given todo.
-**Parameters:**
    -`id`. **not optional**. The id of the todo that is going to be deleted, must be in the path.
-**Response:** this endpoint will return a http status ok if done right.

###Mark a todo as done
-`POST /todos/{id}/done`
-**Description:** this endpoint allows to mark as done a given todo.
-**Parameters:**
-`id`. **not optional**. The id of the todo that is going to be marked as done, must be in the path.
-**Response:** this endpoint will return a http status ok if done right.

###Mark a todo as undone
-`POST /todos/{id}/undone`
-**Description:** this endpoint allows to mark as undone a given todo.
-**Parameters:**
-`id`. **not optional**. The id of the todo that is going to be marked as undone, must be in the path.
-**Response:** this endpoint will return a http status ok if done right.

###Get the metrics
-`GET /todos/metrics`
-**Description:** this endpoint will return the average minutes needed to finish a task, in general and per each priority value.
-**Parameters:** This endpoint need no parameter.
-**Example response:** 
```JSON

{
  "ALL": 0.0,
  "HIGH": 0.0,
  "MEDIUM": 0.0,
  "LOW": 0.0
}

```

