import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyPair;

public class treeload {

    public static void main(String[] args) throws IOException {
        if (args.length != constants.TREELOAD_ARG_COUNT) {
            System.out.println("Error: Incorrect number of arguments were input");
            return;
        }
        if (args[0].equals("test")) {
            //test create index
            Bulkloading tree = new Bulkloading();

            tree.insertBulk("test", 200);
            tree.insertBulk("test1", 200);
            tree.insertBulk("test2", 200);
            tree.insertBulk("test3", 200);
            tree.insertBulk("test4", 200);
            tree.insertBulk("test5", 200);
            tree.insertBulk("test6", 200);
            tree.insertBulk("test7", 200);
            tree.insertBulk("test8", 200);
            tree.insertBulk("test9", 200);
            tree.insertBulk("test10", 200);
            tree.insertBulk("test11", 200);
            tree.insertBulk("test12", 200);
            tree.insertBulk("test13", 200);
            tree.insertBulk("test14", 200);
            tree.insertBulk("test15", 200);
            tree.insertBulk("test16", 200);
            tree.insertBulk("test17", 200);
            tree.constructInternalNode();

//            tree.printTree();

//            tree.printTree(tree.root);
//           BPTreeSimple.LeafNode xx = tree.searchLeaf("test5");
//           BPTreeSimple.LeafNode x1 = tree.searchLeaf("test6");
//           BPTreeSimple.LeafNode x2 = tree.searchLeaf("test");
//           BPTreeSimple.LeafNode x3 = tree.searchLeaf("test");
//           BPTreeSimple.LeafNode x2 = tree.searchLeaf("test7");
//           BPTreeSimple.LeafNode x3 = tree.searchLeaf("test8");
//           BPTreeSimple.LeafNode x4 = tree.searchLeaf("test10");
            System.out.println("test ");
        } else {
            return;
        }

        int pageSize = Integer.parseInt(args[constants.TREELOAD_PAGE_SIZE_ARG]);
        String datafile = "heap." + pageSize;
        FileInputStream inStream = null;
        byte[] page = new byte[pageSize];
        int numBytesInOneRecord = constants.TOTAL_SIZE;
        int numRecordsPerPage = pageSize / numBytesInOneRecord;
        int numBytesInSdtnameField = constants.STD_NAME_SIZE;
        byte[] sdtnameBytes = new byte[numBytesInSdtnameField];

        try {
            int numBytesRead = 0;
            int pageNo = 0;
            inStream = new FileInputStream(datafile);
            BPlusTree tree = new BPlusTree(3);

            while ((numBytesRead = inStream.read(page)) != -1) {
                pageNo++;
                for (int i = 0; i < numRecordsPerPage; i++) {
                    System.arraycopy(page, (i * numBytesInOneRecord), sdtnameBytes, 0, numBytesInSdtnameField);
                    String sdtNameString = new String(sdtnameBytes);
//                    System.out.println(sdtNameString);
//                    System.out.println(i*numBytesInOneRecord);
//                    System.out.println(numBytesInSdtnameField);
//                    System.out.println(pageNo);
                    tree.insert(i * numBytesInOneRecord, numBytesInSdtnameField);
                }
            }
            System.out.println("x");
        } catch (FileNotFoundException e) {
            System.err.println("File not found " + e.getMessage());
        } finally {
            if (inStream != null) {
                inStream.close();
            }
        }
    }
}
