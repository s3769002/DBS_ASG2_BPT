import java.io.*;
import java.nio.ByteBuffer;
import java.security.KeyPair;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class treeload {
    public static Bulkloading.InternalNode root;

    public static void main(String[] args) throws IOException {
        if (args.length != constants.TREELOAD_ARG_COUNT) {
            System.out.println("Error: Incorrect number of arguments were input");
            return;
        }
        String text = args[0];
        int pageSize = Integer.parseInt(args[constants.TREELOAD_PAGE_SIZE_ARG]);
        String datafile = "heap." + pageSize;
        FileInputStream inStream = null;
        int numBytesIntField = Integer.BYTES;
        byte[] page = new byte[pageSize];
        int numBytesInOneRecord = constants.TOTAL_SIZE;
        int numRecordsPerPage = pageSize / numBytesInOneRecord;
        int numBytesInSdtnameField = constants.STD_NAME_SIZE;
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
        byte[] sdtnameBytes = new byte[numBytesInSdtnameField];
        Bulkloading tree = new Bulkloading();
        long startTime = 0;
        long finishTime = 0;
        startTime = System.nanoTime();
        try {
            int pageNo = 0;
            inStream = new FileInputStream(datafile);

            while ((inStream.read(page)) != -1) {
                pageNo++;
                for (int i = 0; i < numRecordsPerPage; i++) {
                    System.arraycopy(page, (i * numBytesInOneRecord), sdtnameBytes, 0, numBytesInSdtnameField);
                    String sdtNameString = new String(sdtnameBytes);
                    tree.insertBulk(sdtNameString, i * numBytesInOneRecord, pageNo);
                }
            }
            tree.constructInternalNode();
            tree.mergeInternalNode(tree.internalNodeList);
            finishTime = System.nanoTime();
            long timeInMilliseconds = (finishTime - startTime) / constants.MILLISECONDS_PER_SECOND;
            System.out.println("Index Created");
            System.out.println("Index create Time taken: " + timeInMilliseconds + " ms");
            startTime = System.nanoTime();
            Bulkloading.LeafNode lfn = tree.searchLeaf(text);
            int offset = 0;
            int searchPage = 0;
            boolean indexFound = false;
            for (Bulkloading.KeyPair ln : lfn.keyPairs) {
                if (ln.key.equals(text)) {
                    offset = ln.record;
                    searchPage = ln.pageNumber;
                    indexFound = true;
                }
            }
            FileInputStream input = new FileInputStream(datafile);

            byte[] idBytes = new byte[constants.ID_SIZE];
            byte[] dateBytes = new byte[constants.DATE_SIZE];
            byte[] yearBytes = new byte[constants.YEAR_SIZE];
            byte[] monthBytes = new byte[constants.MONTH_SIZE];
            byte[] mdateBytes = new byte[constants.MDATE_SIZE];
            byte[] dayBytes = new byte[constants.DAY_SIZE];
            byte[] timeBytes = new byte[constants.TIME_SIZE];
            byte[] sensorIdBytes = new byte[constants.SENSORID_SIZE];
            byte[] sensorNameBytes = new byte[constants.SENSORNAME_SIZE];
            byte[] countsBytes = new byte[constants.COUNTS_SIZE];
            String sdtNameString = new String(sdtnameBytes);

            if (indexFound) {
                byte[] record = new byte[numBytesInOneRecord];
                input.skip(((long) (searchPage - 1) * pageSize) + offset);
                int readfile = input.read(record);
                if (readfile != -1) {
                    {

                        System.arraycopy(record, 0, sdtnameBytes, 0, numBytesInSdtnameField);
                        sdtNameString = new String(sdtnameBytes);
                        System.arraycopy(record, (constants.ID_OFFSET), idBytes, 0, numBytesIntField);
                        System.arraycopy(record, (constants.DATE_OFFSET), dateBytes, 0, constants.DATE_SIZE);
                        System.arraycopy(record, (constants.YEAR_OFFSET), yearBytes, 0, numBytesIntField);
                        System.arraycopy(record, (constants.MONTH_OFFSET), monthBytes, 0, constants.MONTH_SIZE);
                        System.arraycopy(record, (constants.MDATE_OFFSET), mdateBytes, 0, numBytesIntField);
                        System.arraycopy(record, (constants.DAY_OFFSET), dayBytes, 0, constants.DAY_SIZE);
                        System.arraycopy(record, (constants.TIME_OFFSET), timeBytes, 0, numBytesIntField);
                        System.arraycopy(record, (constants.SENSORID_OFFSET), sensorIdBytes, 0, numBytesIntField);
                        System.arraycopy(record, (constants.SENSORNAME_OFFSET), sensorNameBytes, 0, constants.SENSORNAME_SIZE);
                        System.arraycopy(record, (constants.COUNTS_OFFSET), countsBytes, 0, numBytesIntField);
                    }
                }
            } else {
                byte[] p = new byte[pageSize];
                while ((input.read(p)) != -1) {
                    for (int i = 0; i < numRecordsPerPage; i++) {
                        System.arraycopy(p, (i * numBytesInOneRecord), sdtnameBytes, 0, numBytesInSdtnameField);
                        sdtNameString = new String(sdtnameBytes);
                        if (sdtNameString.equals(text)) {
                            sdtNameString = new String(sdtnameBytes);
                            System.arraycopy(p, ((i * numBytesInOneRecord) + constants.ID_OFFSET), idBytes, 0, numBytesIntField);
                            System.arraycopy(p, ((i * numBytesInOneRecord) + constants.DATE_OFFSET), dateBytes, 0, constants.DATE_SIZE);
                            System.arraycopy(p, ((i * numBytesInOneRecord) + constants.YEAR_OFFSET), yearBytes, 0, numBytesIntField);
                            System.arraycopy(p, ((i * numBytesInOneRecord) + constants.MONTH_OFFSET), monthBytes, 0, constants.MONTH_SIZE);
                            System.arraycopy(p, ((i * numBytesInOneRecord) + constants.MDATE_OFFSET), mdateBytes, 0, numBytesIntField);
                            System.arraycopy(p, ((i * numBytesInOneRecord) + constants.DAY_OFFSET), dayBytes, 0, constants.DAY_SIZE);
                            System.arraycopy(p, ((i * numBytesInOneRecord) + constants.TIME_OFFSET), timeBytes, 0, numBytesIntField);
                            System.arraycopy(p, ((i * numBytesInOneRecord) + constants.SENSORID_OFFSET), sensorIdBytes, 0, numBytesIntField);
                            System.arraycopy(p, ((i * numBytesInOneRecord) + constants.SENSORNAME_OFFSET), sensorNameBytes, 0, constants.SENSORNAME_SIZE);
                            System.arraycopy(p, ((i * numBytesInOneRecord) + constants.COUNTS_OFFSET), countsBytes, 0, numBytesIntField);
                        }
                    }
//                System.out.println(pageNo);
                }
            }
            // Convert long data into Date object
            Date date = new Date(ByteBuffer.wrap(dateBytes).getLong());
            // Get a string representation of the record for printing to stdout
            String rec = sdtNameString.trim() + "," + ByteBuffer.wrap(idBytes).getInt()
                    + "," + dateFormat.format(date) + "," + ByteBuffer.wrap(yearBytes).getInt() +
                    "," + new String(monthBytes).trim() + "," + ByteBuffer.wrap(mdateBytes).getInt()
                    + "," + new String(dayBytes).trim() + "," + ByteBuffer.wrap(timeBytes).getInt()
                    + "," + ByteBuffer.wrap(sensorIdBytes).getInt() + "," +
                    new String(sensorNameBytes).trim() + "," + ByteBuffer.wrap(countsBytes).getInt();
            System.out.println(rec);
            finishTime = System.nanoTime();
            timeInMilliseconds = (finishTime - startTime) / constants.MILLISECONDS_PER_SECOND;
            System.out.println("Query with index Time taken: " + timeInMilliseconds + " ms");
        } catch (FileNotFoundException e) {
            System.err.println("File not found " + e.getMessage());
        } finally {
            if (inStream != null) {
                inStream.close();
            }
        }
    }
}
