package lambda;

import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.svggen.SVGGraphics2D;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.awt.*;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.UUID;

import static org.apache.commons.lang3.StringEscapeUtils.escapeHtml4;

/**
 * Created by lenaskarlat on 6/21/17.
 */
public class DataFrame {
    private String id;
    private String sensorId;
    private String timeStamp;
    private String data;
    private String[][] matrix;
    private String[][] colors;
    private String svgImage;

    public DataFrame() {
    }

    public DataFrame(String sensorId, String timeStamp, String data) throws IOException {
        this.sensorId  = sensorId;
        this.timeStamp = timeStamp;
        this.data = data;
        this.id = UUID.randomUUID().toString();
        if (!data.equals("")){
            this.matrix = makeFrameMatrix(data);
            this.colors = makeFrameColors();
            this.svgImage = createSvgString();}
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String[][] getColors() { return colors; }

    public String getStringColors(){
        String lineSeparator = System.lineSeparator();
        StringBuilder sb = new StringBuilder();

        for (String[] row : colors) {
            sb.append(Arrays.toString(row))
                    .append(lineSeparator);
        }
        return sb.toString();
    }


    public String getSvgImage() { return svgImage; }


    private String createSvgString() throws IOException {

        DOMImplementation domImpl = GenericDOMImplementation.getDOMImplementation();
        String svgNS = "http://www.w3.org/2000/svg";
        Document document = domImpl.createDocument(svgNS, "svg", null);
        SVGGraphics2D svgGraphics2D = new SVGGraphics2D(document);

        int coordY=0;
        for(int i=0;i<120;i++){
            int coordX = 0;
            for (int j=0;j<160;j++){
                String[] parts = colors[i][j].split(",");
                float h = Float.parseFloat(parts[0])/360;
                float s = Float.parseFloat(parts[1])/100;
                float b = Float.parseFloat(parts[2])/100;
                svgGraphics2D.setPaint(Color.getHSBColor(h,s,b));
                Shape frameRect = new Rectangle(coordX,coordY,3,3);
                svgGraphics2D.fill(frameRect);
                coordX=coordX+3;
            }
            coordY=coordY+3;
        }

        // adding viewBox, this root has to come after all graphics added
        Element root = svgGraphics2D.getRoot();
        root.setAttributeNS(null, "viewBox", "0 0 480 360");

        boolean useCSS = true; // we want to use CSS style attributes
        Writer writer = new StringWriter();
        svgGraphics2D.stream(root,writer, useCSS,false);
        return escapeHtml4(writer.toString());
    }

    //make a matrix according to sensor resolution
    private String[][] makeFrameMatrix(String data) {
        data = data.replaceAll("\\s", "");   //delete all spaces
        data = data.substring(1, data.length() - 1); //delete [ ]
        String[] dataArray = data.split(",");//delete commas
        System.out.println("length= " + dataArray.length);
        String[][] matrix = new String[120][160];
        int row = 0;
        int column = 0;
        for (int i = 0; i < dataArray.length; i++) {
            matrix[row][column] = dataArray[i];
            column++;
            if (column == 160) {
                column = 0;
                row++;
            }
        }
        return matrix;
    }

    private String[][] makeFrameColors() {
        String[][] colors = new String[120][160];
        for (int i = 0; i < this.matrix.length; i++) {
            for (int j = 0; j < this.matrix[i].length; j++) {
                int element = Integer.parseInt(this.matrix[i][j]);
                int elementScaled = element / 100; //step 10 cm, hsb colors
                if ((0<=elementScaled)&&(elementScaled<21))
                    colors[i][j] = "240,0,100";
                if ((21<=elementScaled)&&(elementScaled<31))
                    for (int k=21; k<31;k++){
                        if (k==elementScaled) colors[i][j] = "0,"+(100-(k-21)*5)+",100"; //reds
                    }
                if ((31<=elementScaled)&&(elementScaled<41))
                    for (int k=31; k<41;k++){
                        if (k==elementScaled) colors[i][j] = "30,"+(100-(k-31)*5)+",100";//oranges
                    }
                if ((41<=elementScaled)&&(elementScaled<51))
                    for (int k=41; k<51;k++){
                        if (k==elementScaled) colors[i][j] = "50,"+(100-(k-41)*5)+",100";//yellows
                    }
                if ((51<=elementScaled)&&(elementScaled<61))
                    for (int k=51; k<61;k++){
                        if (k==elementScaled) colors[i][j] = "100,"+(100-(k-51)*5)+",100";//greens
                    }
                if ((61<=elementScaled)&&(elementScaled<71))
                    for (int k=61; k<71;k++){
                        if (k==elementScaled) colors[i][j] = "180,"+(100-(k-61)*5)+",100";//lightblues
                    }
                if ((71<=elementScaled)&&(elementScaled<81))
                    for (int k=71; k<81;k++){
                        if (k==elementScaled) colors[i][j] = "220,"+(100-(k-71)*5)+",100";//blues
                    }
                if ((81<=elementScaled)&&(elementScaled<91))
                    for (int k=81; k<91;k++){
                        if (k==elementScaled) colors[i][j] = "240,"+(100-(k-81)*5)+",100";//darkblues
                    }
                if ((91<=elementScaled)&&(elementScaled<101))
                    for (int k=91; k<101;k++){
                        if (k==elementScaled) colors[i][j] = "260,"+(100-(k-91)*5)+",100";//violets
                    }
                if (elementScaled>=101)
                    colors[i][j] = "240,0,100";
            }
        }
        return colors;
    }


    public String getSensorId() {
        return sensorId;
    }

    public String[][] getMatrix() {
        return matrix;
    }

    public String getStringMatrix(){
        String lineSeparator = System.lineSeparator();
        StringBuilder sb = new StringBuilder();

        for (String[] row : matrix) {
            sb.append(Arrays.toString(row))
                    .append(lineSeparator);
        }

        return sb.toString();
    }


    public void setSensorId(String sensorId) { this.sensorId = sensorId; }

    public void setMatrix(String[][] matrix) { this.matrix = matrix; }

    public void setStringMatrix(String matrixString) {
        String[][] matrix = new String[120][160];
        String[] rows = matrixString.split(System.lineSeparator());
        int r=0;
        for(String row:rows){
            matrix[r]=row.split(",");
            r++;
        }
        this.matrix = matrix;
    }

    public void setColors(String[][] colors) { this.colors = colors; }

    public void setStringColors(String colorsString) {
        String[][] colors = new String[120][160];
        String[] rows = colorsString.split(System.lineSeparator());
        int r=0;
        for(String row:rows){
            colors[r]=row.split(",");
            r++;
        }
        this.colors = colors;
    }

    public void setSvgImage(String svgImage) { this.svgImage = svgImage; }

    @Override
    public String toString() {
        return sensorId +
                ",id="+id+
                ",data=" + data  +
                ",matrix=" + Arrays.toString(matrix) +
                ",colors=" + Arrays.toString(colors) +
                ",svgImage=" + svgImage +
                " " + timeStamp;
    }
}
