/**
 * Created by David on 2/9/15.
 */

google.load("visualization", "1", {packages: ["corechart"]});
function drawSeriesChart(dataArray) {


    var data = google.visualization.arrayToDataTable(dataArray);
    /*
    ([
        ['ID', 'Average Compensation', 'Average Compa Ratio',    'Size'],
        ['CAN',    80.66,              1.67,       33739900],
        ['DEU',    79.84,              1.36,             81902307],
        ['DNK',    78.6,               1.84,              5523095],
        ['EGY',    72.73,              2.78,        79716203],
        ['GBR',    80.05,              2,                61801570],
        ['IRN',    72.49,              1.7,         73137148],
        ['IRQ',    68.09,              4.77,      31090763],
        ['ISR',    81.55,              2.96,        7485600],
        ['RUS',    68.6,               1.54,           141850000],
        ['USA',    78.09,              2.05,       307007000]
    ]);*/

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