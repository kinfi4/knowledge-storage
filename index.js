const express = require('express');
const {getWeatherInfo} = require("./helpers");
const handlebars = require('express-handlebars');

const app = express();

app.set('view engine', 'hbs');
app.engine('hbs', handlebars.engine({
    layoutsDir: __dirname + '/views/layouts',
    extname: 'hbs',
    partialsDir: __dirname + '/views/partials/'
}));
app.use(express.static('public'))


app.get('/', (req, res) => {
    res.render('main', {layout : 'index'});
});

app.get('/weather/:city', async (req, res) => {
    const city = req.params.city;

    let weather = await getWeatherInfo(city);
    res.render('weather', {layout : 'index', weather: weather})
});

app.get('/weather', async (req, res) => {
    const city = req.query.city;
    if(city) {
        let weather = await getWeatherInfo(city);
        res.render('weather', {layout : 'index', weather: weather})
    } else {
        res.render('weather', {layout : 'index'})
    }
});


app.listen(3000, () => console.log("App is running on port: 3000"));