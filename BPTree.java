import java.security.KeyPair;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BPTree {
    int maxNode = 2;
    InternalNode root;
    LeafNode leaf;

    public class InternalNode {
        InternalNode left;
        InternalNode right;
        LeafNode leftLeaf;
        LeafNode rightLeaf;
        ArrayList<String> key;
        InternalNode parent;

        public InternalNode() {
            this.key = new ArrayList<>();
        }

        public void addKey(String key) {
            this.key.add(key);
            if (this.key.size() >= maxNode) {
                ArrayList<InternalNode> s = this.split();
                String mid = this.getMiddleKey();
                this.left = s.get(0);
                this.right = s.get(1);
                if (this.parent == null) {
                    this.parent = new InternalNode();
                }
                this.removeMiddleKey();
                this.parent.addKey(mid);
            }
        }

        public void LinkLeafNode(LeafNode leftLeaf, LeafNode leafRight) {
            this.leftLeaf = leftLeaf;
            this.rightLeaf = leafRight;
        }

        public String getMiddleKey() {
            int mid = maxNode / 2;
            return key.get(mid);
        }

        public void removeMiddleKey() {
            int mid = maxNode / 2;
            key.remove(mid);
        }

        public ArrayList<InternalNode> split() {
            List<String> first = key.subList(0, maxNode / 2);
            List<String> second = key.subList(0, maxNode / 2);
            ArrayList<InternalNode> splitKeys = new ArrayList<InternalNode>();
            InternalNode l1 = new InternalNode();
            InternalNode l2 = new InternalNode();
            //link leaf node after split
            for (String s : first) {
                l1.addKey(s);
            }
            for (String s : second) {
                l2.addKey(s);
            }
            splitKeys.add(l1);
            splitKeys.add(l2);
            return splitKeys;
        }
    }

    public class LeafNode {
        ArrayList<KeyPair> k;
        Integer nodeNumber;
        InternalNode parent;
        LeafNode sibling;

        public LeafNode() {
            this.k = new ArrayList<>();
            this.nodeNumber = 0;
        }

        public void insertKeyPair(String key, Integer pointer) {
            this.k.add(new KeyPair(key, pointer));
            //sort
            this.nodeNumber++;
        }

        public String getMiddleKey() {
            int mid = maxNode / 2;
            return k.get(mid).key;
        }

        public ArrayList<LeafNode> split() {
            List<KeyPair> first = k.subList(0, maxNode / 2);
            List<KeyPair> second = k.subList(0, maxNode / 2);
            ArrayList<LeafNode> splitLeaf = new ArrayList<LeafNode>();
            LeafNode l1 = new LeafNode();
            for (KeyPair key : first) {
                l1.insertKeyPair(key.key, key.pointer);
            }
            LeafNode l2 = new LeafNode();
            for (KeyPair key : second) {
                l2.insertKeyPair(key.key, key.pointer);
            }
            //link leaf node after split
            l1.sibling = l2;
            splitLeaf.add(l1);
            splitLeaf.add(l2);
            return splitLeaf;
        }

    }

    public class KeyPair {
        String key;
        Integer pointer;

        public KeyPair(String key, Integer pointer) {
            this.key = key;
            this.pointer = pointer;
        }
    }

    private LeafNode findLeafNode(String key) {

        ArrayList<String> keys = this.root.key;
        int i;

        for (i = 0; i < this.root.key.size(); i++) {
            int c = key.compareTo(keys.get(i));
            if (c < 0) {
                break;
            }
        }
        return new LeafNode();
    }

    public void insert(String key, int value) {
        if (leaf == null) {
            leaf = new LeafNode();
            leaf.insertKeyPair(key, value);
        } else {
            LeafNode leafNode;
            if (this.root == null) {
                leafNode = this.leaf;
            } else {
//                leafNode = findLeafNode();
                leafNode = null;
            }
            leafNode.insertKeyPair(key, value);
            //check if need to split
            if (leafNode.nodeNumber > maxNode) {
                //implement split
                ArrayList<LeafNode> split = leafNode.split();
                String midKey = leafNode.getMiddleKey();

                if (leafNode.parent == null) {
                    this.root = new InternalNode();
                    root.addKey(midKey);
                    root.LinkLeafNode(split.get(0), split.get(1));
                    leafNode.parent = this.root;
                } else {
                    leafNode.parent.addKey(midKey);
                    leafNode.parent.LinkLeafNode(split.get(0), split.get(1));
                }
            }
        }
    }
}
