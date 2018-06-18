
    var numberOfSensors = document.getElementById("sensorsTable").rows.length;  //number of all sensors
    var table = document.getElementById("sensorsTable");  //table to map the data from sensors
    var svg = document.getElementById("map_picture"); //we have one svg element to draw the ontology graph on it

         var frameRect = document.createElementNS("http://www.w3.org/2000/svg", 'rect');//this is the frame rectangle of the graph
         frameRect.setAttribute("x", 0);
         frameRect.setAttribute("y", 0);
         frameRect.setAttribute("width", 572);
         frameRect.setAttribute("height", 259);
         frameRect.setAttribute("fill", "url(#imgpattern)");
         frameRect.setAttribute("stroke", "#ccc");
         frameRect.setAttribute("stroke-width", 1);
         svg.appendChild(frameRect);

    for (var i=0; i<numberOfSensors;i++){
         var frameRect = document.createElementNS("http://www.w3.org/2000/svg", 'rect');//this is the frame rectangle of the graph
         frameRect.setAttribute("id", table.rows[i].cells[1].textContent);
         frameRect.setAttribute("x", table.rows[i].cells[3].textContent);
         frameRect.setAttribute("y", table.rows[i].cells[4].textContent);
         frameRect.setAttribute("width", 4);
         frameRect.setAttribute("height", 4);
         frameRect.setAttribute("fill", "orange");
         frameRect.setAttribute("stroke", "orange");
         frameRect.setAttribute("stroke-width", 1);
         frameRect.setAttribute("class", "sensorOnMap");
         svg.appendChild(frameRect);}
    for (var i=0; i<numberOfSensors;i++){
         var note = document.createElementNS("http://www.w3.org/2000/svg", 'rect');//this is the frame rectangle of the graph
         note.setAttribute("id", table.rows[i].cells[1].textContent+"note");
         note.setAttribute("x",  parseInt(table.rows[i].cells[3].textContent)+5);
         note.setAttribute("y",  parseInt(table.rows[i].cells[4].textContent)+5);
         note.setAttribute("width", 80);
         note.setAttribute("height", 20);
         note.setAttribute("fill", "lightblue");
         note.setAttribute("stroke", "#ccc");
         note.setAttribute("stroke-width", 1);
         note.setAttribute("visibility", "hidden");
         note.setAttribute("class", "noteOnMap");
         svg.appendChild(note);

         var textNote = document.createElementNS("http://www.w3.org/2000/svg", 'text');//this is the frame rectangle of the graph
         textNote.setAttribute("id", table.rows[i].cells[1].textContent+"textNote");
         textNote.setAttribute("x",  parseInt(table.rows[i].cells[3].textContent)+10);
         textNote.setAttribute("y",  parseInt(table.rows[i].cells[4].textContent)+20);
         textNote.setAttribute("width", 40);
         textNote.setAttribute("height", 10);
         textNote.setAttribute("fill", "black");
         textNote.textContent="Sensor "+table.rows[i].cells[1].textContent;
         textNote.setAttribute("visibility", "hidden");
         textNote.setAttribute("class", "noteOnMap");
         svg.appendChild(textNote);

    }