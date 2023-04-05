// set a global info window
let openInfoWindow = null;

// create 3 different icon for stations
const  lowIcon= {
    path: "M-1.547 12l6.563-6.609-1.406-1.406-5.156 5.203-2.063-2.109-1.406 1.406zM0 0q2.906 0 4.945 2.039t2.039 4.945q0 1.453-0.727 3.328t-1.758 3.516-2.039 3.070-1.711 2.273l-0.75 0.797q-0.281-0.328-0.75-0.867t-1.688-2.156-2.133-3.141-1.664-3.445-0.75-3.375q0-2.906 2.039-4.945t4.945-2.039z",
    fillOpacity: 0.8,
    fillColor: "red",
    scale: 1.5,
};
const  mediumIcon= {
    path: "M-1.547 12l6.563-6.609-1.406-1.406-5.156 5.203-2.063-2.109-1.406 1.406zM0 0q2.906 0 4.945 2.039t2.039 4.945q0 1.453-0.727 3.328t-1.758 3.516-2.039 3.070-1.711 2.273l-0.75 0.797q-0.281-0.328-0.75-0.867t-1.688-2.156-2.133-3.141-1.664-3.445-0.75-3.375q0-2.906 2.039-4.945t4.945-2.039z",
    fillColor: "blue",
    fillOpacity: 0.8,
    scale: 1.5,
};
const  highIcon= {
    path: "M-1.547 12l6.563-6.609-1.406-1.406-5.156 5.203-2.063-2.109-1.406 1.406zM0 0q2.906 0 4.945 2.039t2.039 4.945q0 1.453-0.727 3.328t-1.758 3.516-2.039 3.070-1.711 2.273l-0.75 0.797q-0.281-0.328-0.75-0.867t-1.688-2.156-2.133-3.141-1.664-3.445-0.75-3.375q0-2.906 2.039-4.945t4.945-2.039z",
    fillColor: "green",
    fillOpacity: 0.8,
    scale: 1.5,
};

// add the search block for user to search for the route at future time
function initSearch() {
    var searchInput = document.getElementById('search-input');
    // var searchButton = document.getElementById('search-button');
  
    searchInput.addEventListener('keydown', function(event) {
      // if the user pressed the "Enter" key, simulate a click on the search button
      if (event.key === 'Enter') {
        searchButton.click();
        search_station();
      }
    });
  
    // // if we need add a search botton
    // searchButton.addEventListener('click', function() {
    //   var query = searchInput.value;
    //   alert('Performing search for: ' + query);
    // });


  }
  initSearch()

// function to search for station and display marker and info window
// it will only show the marker user search for
// if we want to set the future date and time, haven't decided yet how to make it realise its function and beatiful:)
// maybe 
function search_station() {
    let search_input = document.getElementById("search_input").value;
  
    fetch("/stations/search?name=" + search_input).then((response) => {
      return response.json();
    }).then((data) => {
      if (data.length > 0) {
        let station = data[0];
        let position = { lat: parseFloat(station.position_lat), lng: parseFloat(station.position_lng) };
  
        // clear left marker
        if (leftMarker) {
          leftMarker.setMap(null);
        }
  
        map.setZoom(17);
        map.setCenter(position);
  
        let marker = new google.maps.Marker({
          position: position,
          map: map,
          title: station.name,
          icon: station.available_bikes <= 10 ? lowIcon :
          station.available_bikes <= 25 ? mediumIcon : highIcon
        });
  
        // open infowindow and display station info
        open_infowindow(station, marker);
        document.getElementById("station_select").value = station.number;
        display_station_info(marker);
  
        // set left marker to the new marker
        leftMarker = marker;
      }
    });
  }
  
  
  

// insert map and call other functions
function initMap() {
    map = new google.maps.Map(document.getElementById("map"),
    {
        center: { lat: 53.346569, lng: -6.265171 },
        zoom: 14,
        zoomControl: true
    });

    // loading the packages of graph drawing
    google.charts.load('current', {'packages':['bar']});
    google.charts.load('current', {'packages':['corechart']});
    add_marker();
    station_dropdown();
    // show the current weather and time each second
    setInterval(display_weather, 1000);
    forecast();
}

// add markers to each station and call the info window of station
// when click the marker in the map, the statistic graph and available table will be called
function add_marker() {
    fetch("/stations").then(response => {
        return response.json();
    }).then(data => {
        data.forEach(station => {
            var marker = new google.maps.Marker({
                position: { lat: station.position_lat, lng: station.position_lng },
                map: map,
                title: station.address,
                icon: station.available_bikes <= 10 ? lowIcon :
                station.available_bikes <= 25 ? mediumIcon : highIcon

            });

            marker.addListener("click", () => {
                open_infowindow(station, marker);
                console.log(station.number);
                document.getElementById("station_select").value = station.number;
                display_station_info(marker);
            });
        });
    });
}

// use to open the info window
function open_infowindow(station, marker){
    console.log("open_infowindow work");
    if (openInfoWindow) {
        openInfoWindow.close();
    }

    const infowindow = new google.maps.InfoWindow();
    infowindow.setContent("<h2>" + station.name + "</h2>"
        + "<p><b>Available Bikes: </b>" + station.available_bikes + "</p>"
        + "<p><b>Available Stands: </b>" + station.available_bike_stands + "</p>"
        + "<p><b>Status: </b>" + station.status + "</p>");

    infowindow.open(map, marker);
    openInfoWindow = infowindow;
}

// create the options to dropdown
function station_dropdown() {
    fetch("/static_stations").then(response => {
        return response.json()
    }).then(data => {
        var station_option_output = "<option value='' disabled selected>-- Please Select --</option>";
        data.forEach(station => {
            station_option_output += "<option value=" + station.number + ">" + station.name + "</option><br>";
        })
        document.getElementById("station_select").innerHTML = station_option_output;
    })
}

// short the name of the day
function getShortDayName(dayName) {
  const shortDayNames = {
    'Sunday': 'Sun',
    'Monday': 'Mon',
    'Tuesday': 'Tue',
    'Wednesday': 'Wed',
    'Thursday': 'Thu',
    'Friday': 'Fri',
    'Saturday': 'Sat'
  };
  return shortDayNames[dayName] || '';
}

// use to show the graph of everyday using
function display_graph_week(){

    const dropdown = document.getElementById('station_select');
    var value = dropdown.value;

    fetch("/availability_data/"+value).then( response => {
        return response.json();
    }).then(data => {

        console.log(data)

        var week_data = google.visualization.arrayToDataTable([]);
        week_data.addColumn('string', 'day of week');
        week_data.addColumn('number', 'bikes');
        week_data.addColumn('number', 'stands');
        week_data.addColumn('number', 'all places');

        data.forEach(day => {
            const all_places = day.avg_bikes + day.avg_stands;
            week_data.addRow([getShortDayName(day.day_name), day.avg_bikes, day.avg_stands, all_places]);
        });

        var options = {
          chart: {
            title: 'using condition of selected station',
            subtitle: 'bikes and stands',
          }
        };

        var chart = new google.charts.Bar(document.getElementById('graph-container'));
        chart.draw(week_data, google.charts.Bar.convertOptions(options));
    });
}

// use to show the graph of hourly using
function display_graph_hourly() {
    const dropdown = document.getElementById('station_select');
    var value = dropdown.value;

    fetch("/hourly_data/"+value).then( response => {
        return response.json();
    }).then(data => {

        console.log(data)

        var hour_data = google.visualization.arrayToDataTable([]);
        hour_data.addColumn('string', 'time of day');
        hour_data.addColumn('number', 'bikes');
        hour_data.addColumn('number', 'stands');
        hour_data.addColumn('number', 'all places');

        data.forEach(day => {
            const all_places = day.avg_bikes + day.avg_stands;
            hour_data.addRow([day.Hourly.toString()+":00", day.avg_bikes, day.avg_stands, all_places]);
        });

        var options = {
          title: 'using condition of selected station',
          legend: { position: 'bottom' }
        };

        var chart = new google.visualization.LineChart(document.getElementById('graph-container'));
        chart.draw(hour_data, options);
    });
}

// use to show the available information
// when showing available information, statistic graph showing
function display_station_info(marker){
    const dropdown = document.getElementById('station_select');
    var value = dropdown.value;

    fetch("/stations").then(response => {
        return response.json();
    }).then(data => {
        data.forEach(station => {
            if(station.number == value){
                var table_content = "<tr>"
                      + "<th>Name: " + station.name + "</th>"
                      + "<th>Available Stands: " + station.available_bike_stands + "</th>"
                      + "<th>Available Bikes: " + station.available_bikes + "</th>"
                      + "<th>Status: " + station.status + "</th>"
                      +"</tr>";
                document.getElementById("station_table").innerHTML = table_content;
                if(typeof(marker) == "undefined") {
                    marker = new google.maps.Marker({
                        position: { lat: station.position_lat, lng: station.position_lng },
                        map: map,
                        title: station.address,
                        icon: station.available_bikes <= 10 ? lowIcon :
                        station.available_bikes <= 25 ? mediumIcon : highIcon
                    });
                }
                open_infowindow(station, marker);
            }
        })

        display_graph_week();
    })
}

// return a string of time of current time
function now_time(){
    var time = new Date();
    var year = time.getFullYear();
    var month = time.getMonth()+1;
    var day = time.getDate();
    var hour = time.getHours();
    var minutes = time.getMinutes().toString().padStart(2, '0');
    var seconds = time.getSeconds().toString().padStart(2, '0');
    var rs = "time: "+year+"/"+month+"/"+day+" "+hour+":"+minutes+":"+seconds;
    return rs;
}

// using to show the weather info of current time
function display_weather() {
    fetch("/weather").then(response => {
        return response.json();
    }).then(data => {

        var weather_output = data[0].weather_description;
        document.getElementById("weather_description").innerHTML = weather_output;

        var temperature = (data[0].temp - 274.15).toFixed(2) + "°C";
        document.getElementById("temperature").innerHTML = temperature;

        var time_string = now_time();
        document.getElementById("time_detail").innerHTML = time_string;

        var weather_img = document.getElementById("curr-weather-icon");
        weather_img.src = "https://openweathermap.org/img/wn/" + data[0].icon + "@2x.png";

    })
}

// there are 8 data points in one day
// acquire the most frequency data as main info
function main_weather_info(list) {
    var frequency = {};
    list.forEach(function(element){
        frequency[element] = (frequency[element] || 0) +1;
    });
    var mostFrequentElement;
    var highestFrequency = 0;

    for(var element in frequency){
        if(frequency[element] > highestFrequency){
        highestFrequency = frequency[element];
        mostFrequentElement = element;
        }
    }

    return mostFrequentElement;
}


// using to show the future weather information
function forecast(){
    fetch("/forecast").then(response=>{
        return response.json();
    }).then(data => {
        // obtain the timestamp of 00:00 of current day
        var time = new Date();
        time.setHours(0, 0, 0, 0);
        var today_timestamp = Math.floor(time.getTime() / 1000);
        var a_daytime = 24*60*60;
        console.log(a_daytime);
        console.log(today_timestamp);
        var temp_max = 0;
        var temp_min = 1000;
        var main_weather = [];
        var main_weather_icon = [];
        var tempt_value = 0;
        var date_future;
        var li_content = "";

        data.forEach(datetime => {
            // calculate the number of days after the current day
            i_day =Math.floor((datetime.dt - (today_timestamp + a_daytime)) / a_daytime)+1;

            // only show 5 days
            if(i_day >= 0 && i_day <6){

                main_weather.push(datetime.main_weather);
                main_weather_icon.push(datetime.icon);

                if(datetime.temp_max > temp_max){
                    temp_max = datetime.temp_max
                }
                if(datetime.temp_min < temp_min){
                    temp_min = datetime.temp_min
                }

                if(tempt_value != i_day){
                    var date = new Date((datetime.dt - a_daytime) * 1000);
                    li_content += "<li class='future_weather'>"
                    + "<img class='weather_icon'src='https://openweathermap.org/img/wn/"
                    + main_weather_info(main_weather_icon)
                    + "@2x.png' alt='Weather icon'><br>"
                    + main_weather_info(main_weather) + "<br>"
                    + "min:"+(temp_min-274) + "<br>max:" + (temp_max-274) + "<br>"
                    + (date.getMonth()+1) +"/"+ date.getDate();
                    + "</li>"

                    tempt_value = tempt_value + 1;
                    temp_max = 0;
                    temp_min = 1000;
                    main_weather = [];
                    main_weather_icon = [];
                }
            }
        })
        document.getElementById("forecast_window").innerHTML = li_content;
    })
}


