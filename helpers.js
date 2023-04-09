const axios = require('axios');

const API_KEY = "55a28335b5bb6849ee957f3ae34cd7a3"

async function getWeatherInfo(city) {
    const weatherResponse = await axios.get(`https://api.openweathermap.org/data/2.5/weather?q=${city}&appid=${API_KEY}&units=metric`);
    const weatherResult = weatherResponse.data;
    return {
        city: weatherResult.name,
        weather: weatherResult?.weather?.[0]?.main,
        temp: weatherResult?.main?.temp,
        humidity: weatherResult?.main?.humidity,
        pressure: weatherResult?.main?.pressure
    };
}

module.exports = {getWeatherInfo};
