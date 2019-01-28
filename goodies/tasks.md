###First run
In order to run your application, `cd` into the application directory, and do `sbt run`. This will start the application on port 9000. To start the application on a different port (8080 for example), do `sbt "run 8080"`.

You will probably encounter errors on your first run. We will be fixing those in the upcoming tasks.

###Get DB up and running
From your earlier SQL exploits:

`docker run --name postgres -d -p 5432:5432 -e POSTGRES_PASSWORD=password postgres`

Run the schema and table create statements in `goodies/sql/create.sql`.

Double check that your application connection is configured correctly in `conf/application.conf`.

Starting your application with `sbt run` and then visiting `localhost:9000` should throw errors if DB connection attempts fails.

By the end of this task, your application should display a very basic login screen.


###User creation
Before we can log in to our application, we need to create a user! Let's make an endpoint that creates a user:

Endpoint: `POST /api/users`
Body:
```
{
    "username": "youremail@domain.com",
    "password": "suuuuper secure password",
    "displayName": "your very cool display name"
}
```

Return values:

On successful creation: 
```
201 {
    "id": 12,
    "username": "youremail@domain.com",
    "displayName": "your very cool display name"
}

```
On uniqueness constraint failure:
```
409
```

**Required File changes:**

`user/Repository.scala`: Responsible for the DB operation. See `goodies/sql/create-user.sql` for query.

`user/Service.scala`: Responsible for dependency management. Will just expose the function implemented in the repository.

`user/Controller.scala`: Responsible for communication with the outside. Will receive and parse the request, and translate service output into a response.

`conf/routes`: Defines the http verb, route and which controller function is called.

By the end of this task, the endpoint can be tested with either `goodies/endpoints/create-user.sh` or `goodies/endpoints/create-user.postman`

###Implement auth
Our authentication check in `auth/Action.scala` currently fails by default.

**Auth steps**
1. Get the value from the session for the session key defined in `auth.Service`.
2. Parse an unexpired session token from the session value using `auth.Service.parseUnexpiredSession`.
3. Look up the user in the DB using `user.Service`
4. Construct a new `auth.UserRequest` using the user found in the previous step.
5. If any of the above failed, return an Unauthorized response, otherwise, return the UserRequest
6. Make use of an auth action to display the logged in user's display name