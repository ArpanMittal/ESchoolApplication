<html>
<head>
    <!--Load the AJAX API-->
    <script type="text/javascript" src="https://www.google.com/jsapi"></script>
    <script type="text/javascript">

        // Load the Visualization API and the piechart package.
        google.load('visualization', '1.0', {'packages':['corechart']});

        // Set a callback to run when the Google Visualization API is loaded.
        google.setOnLoadCallback(drawChart);


        // Callback that creates and populates a data table,
        // instantiates the pie chart, passes in the data and
        // draws it.
        function drawChart() {

            // Create the data table.
            var data = new google.visualization.DataTable();
            data.addColumn('string', 'Chapter');
            data.addColumn('number', 'Topic');
            data.addRows([
                    @if(isset($data))
                        @foreach($data as $item)
                            ['{!! $item->name !!}',{!! $item->count !!}],
                        @endforeach
                    @endif
            ]);

            // Set chart options
            var options = {'title':'Android-er: Load Google Charts (ColumnChart) with WebView',
                'width':600,
                'height':600};

            // Instantiate and draw our chart, passing in some options.
            var chart = new google.visualization.ColumnChart(document.getElementById('chart_div'));
            chart.draw(data, options);
        }
    </script>
</head>

<body>
<!--Div that will hold the pie chart-->
<div id="chart_div" style="width:600; height:600"></div>
</body>
</html>