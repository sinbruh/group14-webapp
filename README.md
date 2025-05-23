# group14-webapp

Exam project in course
[IDATA2306 Application Development](https://www.ntnu.edu/studies/courses/IDATA2306#tab=omEmnet) at
[NTNU](https://www.ntnu.edu/) by group 14.

Project theme: Car Rentals

### How to run

The project can be run in two ways. You can (1) run the project using a containerized environment
or (2) run the project locally.

To run the project using a containerized environment, first make sure you have
[Docker](https://www.docker.com/) installed. Then, run the following command:

```shell
docker compose up -f compose.dev.yaml -d
```

To run the project locally, refer to the `backend` and `frontend` project directories.

**Note:** If you are running the project using the first option, make sure you have your
environment set up properly. You should create a `.env` file in the root directory. See
`docker.example.env` for environment variables.

### Extra features

- Admin orders overview
- User order history
- Car search by location
- Admin car management
- User profile management
- CD pipeline
- Select location
- Docker containerization
