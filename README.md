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

Upon completion, you should see something as the following in your terminal.
![frontend terminal](https://github.com/mgarciaferreiro/PennSearch/blob/master/screenshots/frontend-terminal.png)

**Backend**

We are using Spring to set up the Rest API in Java which uses Gradle to manage dependencies. Install Gradle [here](https://gradle.org/install/) if you have not. (`brew install gradle` is what I used). After that, do the following to start the backend Java server.

```
cd backend
gradle build
./gradlew bootRun
```

Upon comlpetion, you should see something as the following in your terminal. Note that it will be running when it says 75% so you don't need to wait any longer.
![backend terminal](https://github.com/mgarciaferreiro/PennSearch/blob/master/screenshots/backend-terminal.png)

**Database**
We are also using MongoDB for storing the websites. So make sure you have MongoDB installed on your local machine as well.

### User Manual

![PennSearch frontend](https://github.com/mgarciaferreiro/PennSearch/blob/master/screenshots/PennSearchScreenshot.png)

After doing the setup, you will be able to see this page on http://localhost:3000/. Type any query into the Search box to search Penn-related websites.
