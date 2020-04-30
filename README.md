# PennSearch (NETS 150 Final Project)

There are two parts to this project: a Javascript frontend for visualizing the results and a Java backend for computing the page rank and the ordering of websites.

### Setup
**Frontend**

We are using NextJS for writing the frontend code. So, make sure you have `npm` and `node` installed on your machine. Then, type the following in your terminal to start the frontend JS website.

```
cd frontend
npm install
npm run dev
```

**Backend**

We are using Spring to set up the Rest API in Java which uses Gradle to manage dependencies. Install Gradle [here](https://gradle.org/install/) if you have not. (`brew install gradle` is what I used). After that, do the following to start the backend Java server.

```
cd backend
gradle build
./gradlew bootRun
```