import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Bulkloading {
    InternalNode root;
    LeafNode firstLeaf;
    LeafNode[] leaf;
    int leafCount;
    int emptyLeafIndex;
    int internalNodeCount;
    InternalNode internalNode;
    InternalNode[] internalNodeList;
    int max = 2;

    public Bulkloading() {
        leaf = new LeafNode[3000000];
        internalNodeList = new InternalNode[1000000];
        this.leafCount = 0;
        this.emptyLeafIndex = 0;
        this.internalNodeCount = 0;
    }
    public void addLeaf(LeafNode n){
        this.leaf[leafCount] = n;
        leafCount++;
    }
    public void addInternal(InternalNode n){
        this.internalNodeList[internalNodeCount] = n;
        internalNodeCount++;
    }
    public class Node {
        InternalNode parent;
    }

    public class InternalNode extends Node {
        String[] keys;
        Node[] pointers;
        int keyCount;
        int pointerCount;

        private InternalNode() {
            this.keys = new String[max];
            this.pointers = new Node[max + 1];
            int elementCount = 0;
        }

        private void addKeys(String key) {
            this.keys[keyCount] = key;
            keyCount++;
        }

        private void addPointer(Node p) {
            this.pointers[pointerCount] = p;
            pointerCount++;
        }
    }

    public class LeafNode extends Node {
        KeyPair[] keyPairs;
        int pairCount;
        LeafNode sibling;

        private LeafNode() {
            this.keyPairs = new KeyPair[max];
            this.pairCount = 0;
        }

        private void addKeyPair(KeyPair kp) {
            keyPairs[pairCount] = kp;
            pairCount++;
        }
    }

    public class KeyPair implements Comparable<KeyPair> {
        String key;
        int value;

        private KeyPair(String key, int value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public int compareTo(KeyPair o) {
            return this.key.compareTo(o.key);
        }
    }

    public void printTree(InternalNode n) {
        for (String txt : n.keys) {
            System.out.println(txt);
        }
        int c = 0;
        for (Bulkloading.Node txt : n.pointers) {
            if (txt instanceof LeafNode) {
                Bulkloading.LeafNode x = (Bulkloading.LeafNode) txt;
                System.out.println("Pointers" + c);
                c++;
                for (Bulkloading.KeyPair key : x.keyPairs) {
                    System.out.println(key.key);
                }
            } else {
                printTree((InternalNode) txt);
            }
        }
    }

    // Find the leaf node
    public LeafNode searchLeaf(String key) {
        String[] keys = this.root.keys;
        int i;
        for (i = 0; i < this.root.pointers.length - 1; i++) {
            if (i >= keys.length) {
                break;
            }
            int c = keys[i].compareTo(key);
            if (c > 0) {
                //key is larger
                break;
            }
        }

        Node child = this.root.pointers[i];
        if (child instanceof LeafNode) {
            return (LeafNode) child;
        } else {
            return searchLeaf((InternalNode) child, key);
        }
    }

    // Find the leaf node
    private LeafNode searchLeaf(InternalNode node, String key) {

        String[] keys = node.keys;
        int i;
        for (i = 0; i < node.pointers.length - 1; i++) {
            int c = keys[i].compareTo(key);
            if (c > 0) {
                //key is larger
                break;
            }
        }
        Node childNode = node.pointers[i];
        if (childNode instanceof LeafNode) {
            return (LeafNode) childNode;
        } else {
            return searchLeaf((InternalNode) node.pointers[i], key);
        }
    }

    public void insertBulk(String key, int value) {
        //if the collection of leaf size less than maximum
        if (this.leafCount == 0) {
            LeafNode ln = new LeafNode();
            ln.addKeyPair(new KeyPair(key, value));
            this.addLeaf(ln);
        } else {
            //check the keys in leaf if it is full or not
            //if the leaf is not full
            if(this.leaf[emptyLeafIndex] != null && this.leaf[emptyLeafIndex].pairCount < max){
                this.leaf[emptyLeafIndex].addKeyPair(new KeyPair(key, value));
                emptyLeafIndex++;
            }else{
                LeafNode ln = new LeafNode();
                ln.addKeyPair(new KeyPair(key, value));
                this.addLeaf(ln);
            }
        }


//        if (this.leaf.size() == max + 1 && this.leaf.get(this.leaf.size() - 1).keyPairs.size() == max) {
//            //if leaf collection size = the maximum
//            //create internal node and link all leaf with internal node
//            InternalNode in = new InternalNode();
//            int c = 0;
//            for (LeafNode n : this.leaf) {
//                in.addPointer(n);
//                if (c == 0) {
//                    c++;
//                } else {
//                    in.addKeys(n.keyPairs.get(0).key);
//                }
//            }
//            this.leaf.clear();
//            if (this.internalNodeList.size() == 0) {
//                in.setLevel(1);
//                this.internalNodeList.add(in);
//            } else {
//                if (this.internalNodeList.size() > 1) {
//                    for(int i = 0;i< this.internalNodeList.size();i++){
//                        if(this.internalNodeList.get(i).getLevel() == this.internalNodeList.get(i+1).getLevel()){
//                            //do merge
//                        }
//                    }
//                }
//            }
//            //clear leaf collection
//        }
        //check number of internal node
        //if number of internal node = the max
        //create new internal node and link with all child node
        //clear internal node collection
        //put new
    }

    public void constructInternalNode() {
        int c = 0;
        InternalNode in = null;
        for (int i = 0, j = 0; i <= this.leafCount; i++, j++) {
            if (i == 0) {
                in = new InternalNode();
            }
            if (j == max + 1) {
                c = 0;
                j = 0;
                this.addInternal(in);
                in = new InternalNode();
            }
            if (i >= this.leaf.length) {
                break;
            }
            in.addPointer(this.leaf[i]);
            if (c == 0) {
                c++;
            } else {
                in.addKeys(this.leaf[i].keyPairs[0].key);
            }
//            if(this.leaf.size() - i < this.leaf.size()%(max+1)){
//
//            }
        }
        this.leaf = null;
    }

    public void mergeInternalNode(ArrayList<InternalNode> n) {
        InternalNode in = null;
        int c = 0;
        ArrayList<InternalNode> merged = new ArrayList<>();
        for (int i = 0, j = 0; i <= n.size(); i++, j++) {
            if (i == 0) {
                in = new InternalNode();
            }
            if (j == max + 1) {
                c = 0;
                j = 0;
                merged.add(in);
                in = new InternalNode();
            }
            if (i >= n.size()) {
                break;
            }
            in.addPointer(n.get(i));
            if (c == 0) {
                c++;
            } else {
                LeafNode ptr = (LeafNode) lowestLeaf(n.get(i));
                //find 2nd internal node lowest leaf
                LeafNode ln = (LeafNode) n.get(i).pointers[0];
                in.addKeys(ptr.keyPairs[0].key);

            }
        }
        if (merged.size() == 1) {
            this.root = merged.get(0);
        } else {
            mergeInternalNode(merged);
        }
    }

    public Node lowestLeaf(Node node) {
        if (node instanceof LeafNode) {
            return node;
        } else {
            return lowestLeaf(((InternalNode) node).pointers[0]);
        }
    }

    public Node highestLeaf(Node node) {
        if (node instanceof LeafNode) {
            return node;
        } else {
            return lowestLeaf(((InternalNode) node).pointers[((InternalNode) node).pointers.length - 1]);
        }
    }
}
