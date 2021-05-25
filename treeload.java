import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class treeload {

    public static void main(String[] args) throws IOException {
        if (args.length != constants.TREELOAD_ARG_COUNT) {
            System.out.println("Error: Incorrect number of arguments were input");
            return;
        }
        if(args[0].equals("test")){
            //test create index
            BPTree tree= new BPTree();
            tree.insert("test",200);
            tree.insert("test1",200);
            tree.insert("test2",200);
           BPTree.LeafNode xx = tree.findLeafNode("test1");
//            tree.insert("test3",200);
//            tree.insert("test4",200);
//            tree.insert("test5",200);
            System.out.println("test ");
        }else {
            return;
        }

        int pageSize = Integer.parseInt(args[constants.TREELOAD_PAGE_SIZE_ARG]);
        String datafile = "heap." + pageSize;
        FileInputStream inStream = null;
        byte[] page = new byte[pageSize];
        int numBytesInOneRecord = constants.TOTAL_SIZE;
        int numRecordsPerPage = pageSize/numBytesInOneRecord;
        int numBytesInSdtnameField = constants.STD_NAME_SIZE;
        byte[] sdtnameBytes = new byte[numBytesInSdtnameField];

        try {
            int numBytesRead = 0;
            int pageNo =0;
            inStream = new FileInputStream(datafile);
            while ((numBytesRead = inStream.read(page)) != -1) {
                pageNo++;
                for (int i = 0; i < numRecordsPerPage; i++) {
                    System.arraycopy(page, (i*numBytesInOneRecord), sdtnameBytes, 0, numBytesInSdtnameField);
                    String sdtNameString = new String(sdtnameBytes);
                    System.out.println(sdtNameString);
                    System.out.println(i*numBytesInOneRecord);
                    System.out.println(numBytesInSdtnameField);
                    System.out.println(pageNo);
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("File not found " + e.getMessage());
        } finally {
            if (inStream != null) {
                inStream.close();
            }
        }
    }
}
