/**
 * Created by David on 2/9/15.
 * This uses the google chart visualization to display comp data
 * * *
 */

google.load("visualization", "1", {packages: ["corechart"]});
function drawSeriesChart(dataArray) {


    var data = google.visualization.arrayToDataTable(dataArray);


    var options = {
        title: 'Showing the average compensation and compa by how many people work at that location',
        hAxis: {title: 'Average Compensation'},
        vAxis: {title: 'Average Compa Ratio'},
        bubble: {textStyle: {fontSize: 11}}
    };

    var chart = new google.visualization.BubbleChart(document.getElementById('series_chart_div'));
    chart.draw(data, options);
}

$.getJSON('/data', function(data) {

    var dataArray = [['ID', 'Average Compensation', 'Average Compa Ratio', 'Location', 'Size']];
    $.each(data, function(key,val){
        //console.log(val);
        var row = [val.groupName, val.avgSalary, val.avgCompaRatio, val.groupName, val.groupCount];
        dataArray.push(row);
    });


    google.setOnLoadCallback(drawSeriesChart(dataArray));


});