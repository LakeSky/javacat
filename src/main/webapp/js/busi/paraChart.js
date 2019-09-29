function displayChart(chart, ids, chartType, connectionType) {
    // 指定图表的配置项和数据
    var option = {
        animation: false,
        legend: {
            data: []
        },
        tooltip: {
            show: true,
            formatter: function (params, ticket, callback) {
                console.log(params);
                var yValue = params.value[1];
                if (chartType === "Z" || chartType === "R" || chartType === "X" ||
                    chartType === "Zc" || chartType === "Rc" || chartType === "Xc" ||
                    chartType === "Zd" || chartType === "Rd" || chartType === "Xd") {
                    yValue = formatOhm(yValue);
                }
                if (chartType === "L") {
                    yValue = formatH(yValue);
                }
                if (chartType === "C") {
                    yValue = formatF(yValue);
                }
                console.log(yValue);
                var xTip = formatHz(params.value[0].toFixed(1), "Hz");
                var tip = xTip + " , " + yValue;
                return tip;
            },
            axisPointer: {
                show: true,
                snap: true
            }
        },
        xAxis: {
            name: 'Frequency[Hz]',
            nameLocation: 'middle',
            nameGap: 25,
            type: 'log',
            axisLabel: {
                formatter: function (x, index) {
                    x = formatHz(x, "Hz");
                    return x;
                }
            }
        },
        yAxis: {
            type: 'value',
            axisLabel: {}
        },
        series: [{
            type: 'line'
        }]
    };
    $.ajax({
        type: "POST",
        url: "back/device/chartData",
        data: {
            ids: ids,
            chartType: chartType,
            connectionType: connectionType
        },
        success: function (result) {
            console.log(result);
            if (result.success) {
                freq_units = result.data.xunit;
                if (chartType === "Z" || chartType === "R" || chartType === "X" ||
                    chartType === "Q" || chartType === "DF" || chartType === "L" || chartType === "C" ||
                    chartType === "Zc" || chartType === "Rc" || chartType === "Xc" ||
                    chartType === "Zd" || chartType === "Rd" || chartType === "Xd") {
                    option.yAxis.type = "log";
                }
                if (chartType === "Z" || chartType === "R" || chartType === "X") {
                    option.yAxis.axisLabel.formatter = formatOhm;
                }
                if (chartType === "L") {
                    option.yAxis.axisLabel.formatter = formatH;
                }
                if (chartType === "C") {
                    option.yAxis.axisLabel.formatter = formatF;
                }
                option.yAxis.name = result.data.yaxisName;
                option.series = result.data.series;
                $.each(option.series, function (index, ele) {
                    ele.showSymbol = true;
                    ele.symbolSize = 2;
                    var xx = {
                        name: ele.name
                    };
                    option.legend.data.push(xx);
                });
                console.log(option);
                chart.setOption(option);
            } else {
                layer.msg(result.msg);
            }
        }
    });
}

function similarChart(chart, result, chartType, connectionType) {
    var freq_units = "Hz";
    // 指定图表的配置项和数据
    var option = {
        animation: false,
        legend: {
            data: []
        },
        tooltip: {
            show: true,
            formatter: function (params, ticket, callback) {
                var yValue = params.value[1];
                if (chartType === "Z" || chartType === "R" || chartType === "X" ||
                    chartType === "Zc" || chartType === "Rc" || chartType === "Xc" ||
                    chartType === "Zd" || chartType === "Rd" || chartType === "Xd") {
                    yValue = formatOhm(yValue);
                }
                if (chartType === "L") {
                    yValue = formatH(yValue);
                }
                if (chartType === "C") {
                    yValue = formatF(yValue);
                }
                var xTip = formatHz(params.value[0].toFixed(1), freq_units);
                var tip = xTip + " , " + yValue;
                return tip;
            }
        },
        xAxis: {
            name: 'Frequency[Hz]',
            nameLocation: 'middle',
            nameGap: 25,
            type: 'log',
            axisLabel: {
                formatter: function (x, index) {
                    x = formatHz(x, freq_units);
                    return x;
                }
            }
        },
        yAxis: {
            type: 'value',
            axisLabel: {}
        },
        series: [{
            type: 'line'
        }]
    };

    if (chartType === "Z" || chartType === "R" || chartType === "X" ||
        chartType === "Q" || chartType === "DF" || chartType === "L" || chartType === "C" ||
        chartType === "Zc" || chartType === "Rc" || chartType === "Xc" ||
        chartType === "Zd" || chartType === "Rd" || chartType === "Xd") {
        option.yAxis.type = "log";
    }
    if (chartType === "Z" || chartType === "R" || chartType === "X") {
        option.yAxis.axisLabel.formatter = formatOhm;
    }
    if (chartType === "L") {
        option.yAxis.axisLabel.formatter = formatH;
    }
    if (chartType === "C") {
        option.yAxis.axisLabel.formatter = formatF;
    }
    option.yAxis.name = result.data.yaxisName;
    option.xAxis.min = result.data.startFreq;
    option.xAxis.max = result.data.endFreq;
    option.series = result.data.series;
    $.each(option.series, function (index, ele) {
        ele.showSymbol = true;
        ele.symbolSize = 2;
        var xx = {
            name: ele.name
        };
        option.legend.data.push(xx);
    });
    chart.setOption(option);
}

var formatHz = function (x, freq_units) {
    var units = ["thz", "gHz", "MHz", "kHz", "Hz", "mHz", "uHz"];
    var unitsLowCase = ["THZ", "GHZ", "MHZ", "KHZ", "HZ", "MHZ", "UHZ"];
    var unitsShort = ["T", "G", "M", "k", "", "m", "u"];
    var standardIndex = unitsLowCase.indexOf(freq_units.toUpperCase());
    return formatUnit(x, unitsShort, standardIndex);
};

var formatOhm = function (x, index, notInt) {
    var units = ["GΩ", "MΩ", "kΩ", "Ω", "mΩ", "uΩ"];
    var unitsShort = ["G", "M", "k", "", "m", "u"];
    var standardIndex = units.indexOf("Ω");
    return formatUnit(x, unitsShort, standardIndex, notInt);
};

var formatH = function (x, index) {
    var units = ["H", "mH", "uH", "nH", "pH"];
    var unitsShort = ["", "m", "u", "n", "p"];
    var standardIndex = units.indexOf("H");
    return formatUnit(x, unitsShort, standardIndex);
};

var formatF = function (x, index) {
    var units = ["F", "mF", "uF", "nF", "pF"];
    var unitsShort = ["", "m", "u", "n", "p"];
    var standardIndex = units.indexOf("F");
    return formatUnit(x, unitsShort, standardIndex);
};

function formatUnit(x, units, standardIndex, notInt) {
    if (x != 0) {
        while (x < 1 || x >= 1000) {
            if (x < 1) {
                x = x * 1000;
                standardIndex += 1;
            } else {
                x = x / 1000;
                standardIndex -= 1;
                if (standardIndex === 0) {
                    break;
                }
            }
        }
    }
    var xx = x.toFixed(1) + units[standardIndex];
    return xx;
}