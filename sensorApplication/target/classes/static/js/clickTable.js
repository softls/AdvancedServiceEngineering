$("#sensorsTable").find("tbody tr").click(function () {
  //highlight chosen row of the table
  $(this).addClass('active').siblings().removeClass('active');
  $(this).addClass('');
  var value = $(this).find('td').eq(1).text();

  //show chosen concept in the textbox

  //change color of the vertex, corresponding to the chosen row of the table
  $(document.getElementById(value)).css("fill", "red");
  $(document.getElementById(value)).siblings('.sensorOnMap').css("fill", "orange");

   $(document.getElementById(value)).siblings('.noteOnMap').css("visibility", "hidden");
   $(document.getElementById(value+"note")).css("visibility", "visible");
   $(document.getElementById(value+"textNote")).css("visibility", "visible");
});