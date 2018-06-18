
function drawSingleView(element) {


    var div = document.getElementById("div__graph");
    //delete previous graph if any
    while (div.firstChild) { div.removeChild(div.firstChild); }

    var currentRowIndex = element.parentNode.rowIndex;
    var dataFramesTable = document.getElementById("dataFramesTable");
    //make active row in table highlighted and other - nor
    for (i=0; i<dataFramesTable.rows.length;i++){
        if (i!=currentRowIndex)
        {
             dataFramesTable.rows[i].removeAttribute("class");
             dataFramesTable.rows[i].setAttribute("class","");
        }
    }

    //get hidden svg from table
    var currentRow = dataFramesTable.rows[currentRowIndex];
    currentRow.setAttribute("class","selected");
    var svg  = currentRow.cells[1].innerHTML;

    //workaround to get to tag <svg> and replace substituted special symbols
    var convert = function(convert){
        return $("<svg />", { html: convert }).text();
    };
    svg = convert(svg);
    var parser = new DOMParser;
    var dom = parser.parseFromString(svg,'text/html');
    var decodedString = dom.body.textContent;
    div.innerHTML=decodedString;
    div.style.visibility = 'visible';
    document.getElementById("div__graph__palette").style.visibility = 'visible';
    document.getElementById("ifNoDataFrames3").style.display='none';

}

