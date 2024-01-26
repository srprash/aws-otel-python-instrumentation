const axios = require('axios');
const cron = require('node-cron');

const imageUrl = process.env.URL ? `${process.env.URL}/images` : 'http://0.0.0.0:8000/images'
const vehicleURL = process.env.URL ? `${process.env.URL}/vehicle-inventory` : 'http://0.0.0.0:8001/vehicle-inventory'

function getRandomNumber(min, max) {
    return Math.floor(Math.random() * (max - min + 1)) + min;
}


function sleep(ms) {
    return new Promise(resolve => setTimeout(resolve, ms));
}

// sends two requests every minute, 1 POST request and 1 GET request
const postGetCarsTrafficTask = cron.schedule('* * * * *', async () => {
        console.log('add 1 car every 1 minutes');
        const carData = {"make": "BMW", "model": "M340", "year": 2022, "image_name": "newCar.jpg"}
        axios.post(`${vehicleURL}/`, carData, { timeout: 10000 })
            .catch(err => {
                console.error(err.response && err.response.data);
            });
        
        // gets image from image service through the vehicle service
        axios.get(`${vehicleURL}/1/image`, { timeout: 10000 })
            .catch(err => {
                console.error(err.response && err.response.data);
            }); // Catch and log errors
}, { scheduled: false });
postGetCarsTrafficTask.start();

// sends a burst of traffic sending 10 requests every hour:
// 5 POST requests and 5 GET requests. 
const postGetCarsTrafficBurstTask = cron.schedule('0 * * * *', async () => {
    console.log('add 5 cars within 1 minutes');
    const carData = {"make": "BMW", "model": "M340", "year": 2022, "image_name": "newCar.jpg"}
    for (let i = 0; i < 5; i++) {
        axios.post(`${vehicleURL}/`, carData, { timeout: 10000 })
            .catch(err => {
                console.error(err.response && err.response.data);
            }); // Catch and log errors

        // gets image from image service through the vehicle service
        axios.get(`${vehicleURL}/1/image`, { timeout: 10000 })
            .catch(err => {
                console.error(err.response && err.response.data);
            }); // Catch and log errors
    }
}, { scheduled: false });
postGetCarsTrafficBurstTask.start();

// sends a GET request with custom throttle parameter in the body that mimics being throttled. The throttle time
// is going to be random between 5 - 20 secs.
const getCarThrottle = cron.schedule('*/5 * * * *', async () => {    
    sleepSecs = getRandomNumber(30,60);
    console.log(`sleep ${sleepSecs} seconds`);
    await sleep(sleepSecs*1000);
    throttleSecs = getRandomNumber(5,20);
    console.log(`request will be throttled for ${throttleSecs} seconds`)
    axios.get(`${vehicleURL}/1`, {params: {"throttle": 5}}, { timeout: 10000 })
        .catch(err => {
            console.error(err.response && err.response.data);
        }); // Catch and log errors
}, { scheduled: false });
getCarThrottle.start();

// sends an invalid GET request with a non existent car id to trigger 404 error
const getInvalidRequest = cron.schedule('*/5 * * * *', async () => {    
    sleepSecs = getRandomNumber(30,120);
    console.log(`sleep ${sleepSecs} seconds`);
    await sleep(sleepSecs*1000);
    console.log("getting non existent car to trigger 404");
    axios.get(`${vehicleURL}/123456789`, { timeout: 10000 })
        .catch(err => {
            console.error(err.response && err.response.data);
        }); // Catch and log errors
}, { scheduled: false });
getInvalidRequest.start();

// sends an invalid GET request with a non existent image name to trigger 500 error due to S3 Error:
// "An error occurred (NoSuchKey) when calling the GetObject operation: The specified key does not exist."
const getNonExistentImage = cron.schedule('*/5 * * * *', async () => {
    sleepSecs = getRandomNumber(30,120);
    console.log(`sleep ${sleepSecs} seconds`);
    await sleep(sleepSecs*1000);
    console.log('get an non existent image to trigger aws error');
    axios.get(`${imageUrl}/name/doesnotexist.jpeg`)
        .catch(err => {
            console.error(err.response && err.response.data);
        }); // Catch and log errors
}, { scheduled: false });
getNonExistentImage.start();