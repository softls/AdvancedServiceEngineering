import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * taken from http://www.irc.atr.jp/crest2010_HRI/ATC_dataset/
 *
 * Sample class for reading raw 3D range sensor data from the ATC sample dataset
 * (see: http://www.irc.atr.jp/crest2010_HRI/ATC_dataset/)
 *
 * @author drazen@atr.jp
 *
 * Copyright (c) 2013 ATR
/*
 * Format of files: the files contain a sequence of "frames".
 * Each frame corresponds to a single measurement and has the following format:
 *  _________________________________________________________________________________________________________
 *  | header marker |  version  |  unixtime[s] |  time[ms]  | reserved |  data length  ||        data       |
 *  +---------------+-----------+--------------+------------+----------+---------------++-------------------+
 *  |      '+'      |   '003'   |    10 byte   |   3 byte   |  10byte  |  5 byte(hex)  ||  2 byte * length  |
 *  ---------------------------------------------------------------------------------------------------------
 *
 *  The data is stored as 16-bit integers (short) in binary format
 **/

public class RawDataFrame {
    private static final int HEADER_LENGTH = 31;
    private BufferedInputStream inputStream;

    // these fields contain the data read from single frame
    public long time; // time stamp = unixtime[s]*1000 + milliseconds
    public int[] data; // data
    public byte[] dataBuf;

    /**
     * Open range sensor data file, read data, close file.
     * @param name Name of the file to use
     * @throws IOException
     */

    public void readSingleFrame(String name) throws IOException {
        inputStream = new BufferedInputStream(new FileInputStream(name));
        int retval = 0;
        ByteBuffer bb = null;
        // byte dataBuf[] = null;

		/*------- read header --------*/
        byte headderBuf[] = new byte[1+HEADER_LENGTH];

        retval = inputStream.read(headderBuf);
        if (retval == -1) throw new IOException("Error reading header from stream");

        // check header
        String headerString = new String(headderBuf);
        if (!headerString.substring(0,1).equals("+")) {
            throw new IOException("Wrong header marker");
        }
        if (Integer.parseInt(headerString.substring(1, 1+3),16) != 3) {
            throw new IOException("Wrong data format");
        }

        // read time stamp
        time = Long.parseLong(headerString.substring(4, 4+13))*1000000; //*1000000 because influx reads nanoseconds


        // read data length, radix=16, i.e., parse from hexadecimal
        int dataLength = Integer.parseInt(headerString.substring(1+HEADER_LENGTH-5, 1+HEADER_LENGTH),16);
        System.out.println("dataLength="+dataLength);

		/*------- read data --------*/
        // buffer for data
        dataBuf = new byte[2*dataLength+ HEADER_LENGTH+1]; //read all
        retval = inputStream.read(dataBuf);
        if (retval == -1) throw new IOException("Error reading data from stream");

        bb = ByteBuffer.wrap(dataBuf);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        bb.position(1+HEADER_LENGTH); //start from data, and not header

        data = new int[dataLength];
        for (int i = 0; i < dataLength; ++i) {
            data[i] = bb.getShort();
        }
        if (inputStream!=null) inputStream.close();

    }
}