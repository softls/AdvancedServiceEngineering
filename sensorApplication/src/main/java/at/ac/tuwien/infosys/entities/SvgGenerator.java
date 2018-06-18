package at.ac.tuwien.infosys.entities;


import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.svggen.SVGGraphics2D;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;

import java.awt.*;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by lenaskarlat on 6/8/17.
 */
public class SvgGenerator {

    private String sensorId;
    private String timeStamp;


    public SvgGenerator(String sensorId, String timeStamp) {
        this.sensorId = sensorId;
        this.timeStamp = timeStamp;
    }

    public void draw() throws IOException {

        // Get a DOMImplementation.
        DOMImplementation domImpl =
                GenericDOMImplementation.getDOMImplementation();

        // Create an instance of org.w3c.dom.Document.
        String svgNS = "http://www.w3.org/2000/svg";
        Document document = domImpl.createDocument(svgNS, "svg", null);

        // Create an instance of the SVG Generator.
        SVGGraphics2D svgGraphics2D = new SVGGraphics2D(document);

        // Ask the test to render into the SVG Graphics2D implementation.
        svgGraphics2D.setPaint(Color.red);
        svgGraphics2D.fill(new Rectangle(10, 10, 100, 100));

        // Finally, stream out SVG to the standard output using
        // UTF-8 encoding.
        boolean useCSS = true; // we want to use CSS style attributes
//        File directory = new File ("src/main/resources/static/images/"+sensorId+"/");
//        if (!directory.exists()) {
//            System.out.println("creating directory: " + directory.getName());
//            boolean result = false;
//
//            try{
//                directory.mkdir();
//                result = true;
//            }
//            catch(SecurityException se){
//            }
//            if(result) {
//                System.out.println("Directory created /"+sensorId);
//            }
//        }
      //  File file = new File("src/main/resources/static/images/"+sensorId+"/", timeStamp+".svg");
      //  file.createNewFile();
     //   OutputStream outputStream       = new FileOutputStream(file,false);
        Writer writer = new StringWriter();
       // Writer out = new OutputStreamWriter(outputStream, "UTF-8");
        svgGraphics2D.stream(writer, useCSS);
        String svgImageString = writer.toString();
        System.out.println(svgImageString);

    }
}
