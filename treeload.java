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
            tree.insertBulk("test11", 200);
            tree.insertBulk("test12", 200);
            tree.insertBulk("test13", 200);
            tree.insertBulk("test14", 200);
            tree.insertBulk("test15", 200);
            tree.insertBulk("test16", 200);
            tree.insertBulk("test17", 200);
            tree.insertBulk("test18", 200);
            tree.insertBulk("test19", 200);
            tree.insertBulk("test20", 200);
            tree.insertBulk("test21", 200);
            tree.insertBulk("test22", 200);
            tree.insertBulk("test23", 200);
            tree.insertBulk("test24", 200);
            tree.insertBulk("test25", 200);
            tree.insertBulk("test26", 200);
            tree.insertBulk("test27", 200);
            tree.constructInternalNode();
            tree.mergeInternalNode(tree.internalNodeList);

//            tree.printTree();

//            tree.printTree(tree.root);
            Bulkloading.LeafNode xx = tree.searchLeaf("test25");
            Bulkloading.LeafNode x1 = tree.searchLeaf("test16");
            Bulkloading.LeafNode x2 = tree.searchLeaf("test");
            Bulkloading.LeafNode x3 = tree.searchLeaf("test");
            Bulkloading.LeafNode x4 = tree.searchLeaf("test17");
            Bulkloading.LeafNode x5 = tree.searchLeaf("test18");
            Bulkloading.LeafNode x6 = tree.searchLeaf("test10");
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
        Bulkloading tree = new Bulkloading();
        BPlusTree tree1 = new BPlusTree(3);

        try {
            int numBytesRead = 0;
            int pageNo = 0;
            String x = "";
            inStream = new FileInputStream(datafile);

            while ((numBytesRead = inStream.read(page)) != -1) {
                pageNo++;
                for (int i = 0; i < numRecordsPerPage; i++) {
                    System.arraycopy(page, (i * numBytesInOneRecord), sdtnameBytes, 0, numBytesInSdtnameField);
                    String sdtNameString = new String(sdtnameBytes);
                    x = sdtNameString;
//                    System.out.println(sdtNameString);
//                    System.out.println(i*numBytesInOneRecord);
//                    System.out.println(numBytesInSdtnameField);
//                    System.out.println(pageNo);
//                    tree1.insert(numBytesInSdtnameField,numBytesInSdtnameField);
                    tree.insertBulk(sdtNameString, numBytesInSdtnameField);
                }
                System.out.println(pageNo);
//                System.out.println(x);
            }
            tree.constructInternalNode();
            tree.mergeInternalNode(tree.internalNodeList);
            Bulkloading.LeafNode xx = tree.searchLeaf("2910/15/2018 08:00:00 PM");

            System.out.println("xx");
        } catch (FileNotFoundException e) {
            System.err.println("File not found " + e.getMessage());
        } finally {
            if (inStream != null) {
                inStream.close();
            }
        }
    }
}
