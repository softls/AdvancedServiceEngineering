 $(document).on("click", ".sensorOnMap", function () {
        <!--highlight chosen row of the table-->
        $(this).css("fill", "red");
        $(this).siblings('.sensorOnMap').css("fill", "orange");

        var value = $(this).attr('id');

        $(document.getElementById(value)).siblings('.noteOnMap').css("visibility", "hidden");
        $(document.getElementById(value+"note")).css("visibility", "visible");
        $(document.getElementById(value+"textNote")).css("visibility", "visible");

        var value2 = "";
        var value3 = "";

        $('#sensorsTable').find('tbody  tr').each(function () {
            if ($(this).find('td').eq(1).text() == value) {
                $(this).addClass('active').siblings().removeClass('active');
                $(this).addClass('');
                value2 = $(this).find('td').eq(1).text();
                value3 = $(this).find('td').eq(2).text();
            }
        });

    });