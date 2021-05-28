import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Bulkloading {
    InternalNode root;
    LeafNode firstLeaf;
    ArrayList<LeafNode> leaf;
    InternalNode internalNode;
    ArrayList<InternalNode> internalNodeList;
    int max = 2;
    int emptyLeafIndex = 0;

    public Bulkloading() {
        this.leaf = new ArrayList<>();
        this.internalNodeList = new ArrayList<>();
    }

    public class Node {
        InternalNode parent;
    }

    public class InternalNode extends Node {
        ArrayList<String> keys;
        ArrayList<Node> pointers;
        int level;

        private InternalNode() {
            this.keys = new ArrayList<>();
            this.pointers = new ArrayList<>();
        }

        private void setLevel(int l) {
            this.level = l;
        }

        private int getLevel() {
            return this.level;
        }

        private void addKeys(String key) {
            this.keys.add(key);
        }

        private int findIndexOfPointer(Node pointer) {
            for (int i = 0; i < pointers.size(); i++) {
                if (pointers.get(i) == pointer) {
                    return i;
                }
            }
            return -1;
        }

        private void insertChildPointer(Node pointer, int index) {
            this.pointers.add(index, pointer);
        }


        private ArrayList<InternalNode> splitInternal() {
            //ref instead of creating new
            List<String> first = this.keys.subList(0, max / 2);
            List<Node> firstPointer = this.pointers.subList(0, this.pointers.size() / 2);
            List<String> second = this.keys.subList(max / 2, this.keys.size());
            List<Node> secondPointer = this.pointers.subList(this.pointers.size() / 2, this.pointers.size());
            ArrayList<InternalNode> splitInternal = new ArrayList<InternalNode>();
            InternalNode l1 = new InternalNode();
            for (String k : first) {
                l1.addKeys(k);
            }
            for (Node p : firstPointer) {
                p.parent = l1;
                l1.addPointer(p);
            }
            InternalNode l2 = new InternalNode();
            for (String k : second) {
                l2.addKeys(k);
            }
            for (Node p : secondPointer) {
                p.parent = l2;
                l2.addPointer(p);
            }

            //link leaf node after split
            splitInternal.add(l1);
            splitInternal.add(l2);
            return splitInternal;
        }

        private void addPointer(Node p) {
//            int index=0;
//            for (Node n : this.pointers) {
//                if (n == null){}
//                if (n instanceof LeafNode) {
//                    LeafNode ln = (LeafNode) n;
//                    LeafNode aln = (LeafNode) p;
//                    int c = ln.keyPairs.get(0).compareTo(aln.keyPairs.get(0));
//                    if (c > 0) {
//
//                        //key is larger
//                        break;
//                    }else{
//                        index++;
//                    }
//                } else {
//                    InternalNode in = (InternalNode) n;
//                    InternalNode ain = (InternalNode) p;
//                    int c = in.keys.get(0).compareTo(ain.keys.get(0));
//                    if (c > 0) {
//                        //key is larger
//                        break;
//                    }else{
//                        index++;
//                    }
//                }
//            }
            pointers.add(p);
        }

        private void removePointer(Node p) {
            pointers.remove(p);
        }
    }

    public class LeafNode extends Node {
        ArrayList<KeyPair> keyPairs;
        LeafNode sibling;

        private LeafNode() {
            this.keyPairs = new ArrayList<>();
        }

        private void addKeyPair(KeyPair kp) {
            keyPairs.add(kp);
        }

        public ArrayList<LeafNode> splitLeaf() {
            List<KeyPair> first = keyPairs.subList(0, max / 2);
            List<KeyPair> second = keyPairs.subList(max / 2, keyPairs.size());
            ArrayList<LeafNode> splitLeaf = new ArrayList<LeafNode>();
            LeafNode l1 = new LeafNode();
            for (KeyPair k : first) {
                l1.addKeyPair(new KeyPair(k.key, k.value));
            }
            LeafNode l2 = new LeafNode();
            for (KeyPair k : second) {
                l2.addKeyPair(new KeyPair(k.key, k.value));
            }
            //link leaf node after split
            l1.sibling = l2;
            splitLeaf.add(l1);
            splitLeaf.add(l2);
            return splitLeaf;
        }

        public String getMiddleKey() {
            int mid = max / 2;
            return keyPairs.get(mid).key;
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

    public void insert(String key, int value) {
        LeafNode ln = new LeafNode();
        if (this.firstLeaf == null) {
            ln.addKeyPair(new KeyPair(key, value));
            this.firstLeaf = ln;
        } else {
            if (this.root == null) {
                ln = this.firstLeaf;
            }
            if (this.root != null) {
                ln = searchLeaf(key);
            }
            ln.addKeyPair(new KeyPair(key, value));

            if (ln.keyPairs.size() > max) {
                ArrayList<LeafNode> split = ln.splitLeaf();
                String md = ln.getMiddleKey();
                if (ln.parent == null) {
                    InternalNode p = new InternalNode();
                    p.addKeys(md);
                    p.addPointer(split.get(0));
                    p.addPointer(split.get(1));
                    this.root = p;
                    split.get(0).parent = this.root;
                    split.get(1).parent = this.root;
                } else {
                    ln.parent.addKeys(md);
//                    ln.parent.pointers = split.get(0);
                    ln.keyPairs = split.get(0).keyPairs;
                    ln.parent.addPointer(split.get(1));
                    split.get(1).parent = ln.parent;
                    InternalNode In = ln.parent;
                    if (In.keys.size() > max) {
                        ArrayList<InternalNode> splitIn = In.splitInternal();
                        String mid = In.keys.get(max / 2);
                        if (In.parent == null) {
                            In.parent = new InternalNode();
                            In.keys.remove(mid);
                            splitIn.get(1).keys.remove(mid);
                            In.parent.addKeys(mid);
                            In.parent.addPointer(splitIn.get(0));
                            In.parent.addPointer(splitIn.get(1));
                            splitIn.get(0).parent = In.parent;
                            splitIn.get(1).parent = In.parent;
                            root = In.parent;
                        } else {
                            //old link remain
                            //split link require
                            //figure new splitting method
                            In.keys.remove(mid);
                            splitIn.get(1).keys.remove(mid);
                            In.parent.pointers.remove(In);
                            In.parent.addKeys(mid);
                            In.parent.addPointer(splitIn.get(0));
                            In.parent.addPointer(splitIn.get(1));
                            splitIn.get(0).parent = In.parent;
                            splitIn.get(1).parent = In.parent;
                        }
                    }

                }
            }
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

        ArrayList<String> keys = this.root.keys;
        int i;
        for (i = 0; i < this.root.pointers.size() - 1; i++) {
            if (i >= keys.size()) {
                break;
            }
            int c = keys.get(i).compareTo(key);
            if (c > 0) {
                //key is larger
                break;
            }
        }

        Node child = this.root.pointers.get(i);
        if (child instanceof LeafNode) {
            return (LeafNode) child;
        } else {
            return searchLeaf((InternalNode) child, key);
        }
    }

    // Find the leaf node
    private LeafNode searchLeaf(InternalNode node, String key) {

        ArrayList<String> keys = node.keys;
        int i;

        for (i = 0; i < node.pointers.size() - 1; i++) {
            int c = keys.get(i).compareTo(key);
            if (c > 0) {
                //key is larger
                break;
            }
        }
        Node childNode = node.pointers.get(i);
        if (childNode instanceof LeafNode) {
            return (LeafNode) childNode;
        } else {
            return searchLeaf((InternalNode) node.pointers.get(i), key);
        }
    }

    public void insertBulk(String key, int value) {
        //if the collection of leaf size less than maximum
        if (this.leaf.size() == 0) {
            LeafNode ln = new LeafNode();
            ln.addKeyPair(new KeyPair(key, value));
            this.leaf.add(ln);
        } else {
            //check the keys in leaf if it is full or not
            //if the leaf is not full
            if (this.leaf.size() > emptyLeafIndex && this.leaf.get(emptyLeafIndex).keyPairs.size() < max) {
                this.leaf.get(emptyLeafIndex).addKeyPair(new KeyPair(key, value));
                emptyLeafIndex++;
            } else {
                LeafNode ln = new LeafNode();
                ln.addKeyPair(new KeyPair(key, value));
                this.leaf.add(ln);
            }
//                if(){
//                    this.leaf.get(this.leaf.size()-1).addKeyPair(new KeyPair(key,value));
//                    //perform key insertion
//                }
            //if leaf is full create new leaf and insert
            //get the last leaf to insert keypair
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
        for (int i = 0, j = 0; i <= this.leaf.size(); i++, j++) {
            if (i == 0) {
                in = new InternalNode();
            }
            if (j == max + 1) {
                c = 0;
                j = 0;
                this.internalNodeList.add(in);
                in = new InternalNode();
            }
            if (i >= this.leaf.size()) {
                break;
            }
            in.addPointer(this.leaf.get(i));
            if (c == 0) {
                c++;
            } else {
                in.addKeys(this.leaf.get(i).keyPairs.get(0).key);
            }
//            if(this.leaf.size() - i < this.leaf.size()%(max+1)){
//
//            }
        }
        this.leaf.clear();
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
                in.addKeys(ptr.keyPairs.get(0).key);

            }
        }
        if (merged.size() == 1) {
            this.root = merged.get(0);
            this.internalNodeList.clear();
        } else {
            mergeInternalNode(merged);
        }
    }

    public Node lowestLeaf(Node node) {
        if (node instanceof LeafNode) {
            return node;
        } else {
            return lowestLeaf(((InternalNode) node).pointers.get(0));
        }
    }

    public Node highestLeaf(Node node) {
        if (node instanceof LeafNode) {
            return node;
        } else {
            return lowestLeaf(((InternalNode) node).pointers.get(((InternalNode) node).pointers.size() - 1));
        }
    }
}
