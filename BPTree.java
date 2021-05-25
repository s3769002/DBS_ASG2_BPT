import java.security.KeyPair;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BPTree {
    int maxNode = 2;
    InternalNode root;
    LeafNode firstLeaf;

    public class Node {
        InternalNode parent;
    }

    public class InternalNode extends Node {
        ArrayList<LeafNode> leaf;
        ArrayList<String> keys;
        ArrayList<InternalNode> child;

        public InternalNode() {
            this.keys = new ArrayList<>();
            this.leaf = new ArrayList<>();
            this.child = new ArrayList<>();
        }

        public void addKey(String key) {
            this.keys.add(key);
            this.validateTree();
        }

        public void addKey(String key, LeafNode lf) {
            this.leaf.add(lf);
            this.keys.add(key);
            this.validateTree();
        }

        public void validateTree() {
            if (this.keys.size() >= maxNode) {
                ArrayList<InternalNode> s = this.split();
                String mid = this.getMiddleKey();
//                this.left = s.get(0);
//                this.right = s.get(1);
                if (this.parent == null) {
                    this.parent = new InternalNode();
                    this.parent.addKey(mid);
                }
                this.removeMiddleKey();
                //sort before add
                this.parent.addKey(mid);
                this.parent.child.add(this);
            }
        }

        public String getMiddleKey() {
            int mid = maxNode / 2;
            return keys.get(mid);
        }

        public void removeMiddleKey() {
            int mid = maxNode / 2;
            keys.remove(mid);
        }

        public ArrayList<InternalNode> split() {
            List<String> first = keys.subList(0, maxNode / 2);
            List<String> second = keys.subList(0, maxNode / 2);
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

    public class LeafNode extends Node {
        ArrayList<KeyPair> k;
        LeafNode sibling;

        public LeafNode() {
            this.k = new ArrayList<>();
        }

        public void insertKeyPair(String key, Integer pointer) {
            this.k.add(new KeyPair(key, pointer));
        }

        public String getMiddleKey() {
            int mid = maxNode / 2;
            return k.get(mid).key;
        }

        public void removeMiddleKey() {
            int mid = maxNode / 2;
            k.remove(mid);
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

    public LeafNode findLeafNode(String key) {

        ArrayList<String> keys = this.root.keys;
        int i;
        for (i = 0; i < keys.size(); i++) {
            int c = key.compareTo(keys.get(i));
            if (c > 0) {
                //a is larger
                break;
            }
        }
        if (this.root.child.size() == 0) {
            ArrayList<KeyPair> keys1 = this.root.leaf.get(i).k;
            int j;
            for (j = 0; j < keys1.size(); j++) {
                int c = key.compareTo(keys1.get(j).key);
                if (c > 0) {
                    //a is larger
                    break;
                }
            }
            return this.root.leaf.get(j);
        } else {
            InternalNode child = this.root.child.get(i);
            return findLeafNode(child, key);
        }
    }

    private LeafNode findLeafNode(InternalNode node, String key) {

        ArrayList<String> keys = node.keys;
        int i;
        for (i = 0; i < keys.size(); i++) {
            int c = key.compareTo(keys.get(i));
            if (c < 0) {
                //a is smaller
            } else if (c > 0) {
                //a is larger
                break;
            } else {
                //a is equal to b
            }
        }
        InternalNode child = node.child.get(i);
        if (child.leaf.size() == 0) {
            return findLeafNode(child, key);
        } else {
            ArrayList<KeyPair> keys1 = child.leaf.get(i).k;
            int j = 0;
            for (j = 0; j < keys1.size(); j++) {
                int c = key.compareTo(keys1.get(j).key);
                if (c < 0) {
                    //a is smaller
                } else if (c > 0) {
                    //a is larger
                    break;
                }
            }
            return child.leaf.get(i);
        }
    }

    public void insert(String key, int value) {
        if (firstLeaf == null) {
            firstLeaf = new LeafNode();
            firstLeaf.insertKeyPair(key, value);
        } else {
            LeafNode leafNode;
            if (this.root == null) {
                leafNode = this.firstLeaf;
            } else {
                leafNode = findLeafNode(key);
//                leafNode = null;
            }
            leafNode.insertKeyPair(key, value);
            //check if need to split
            if (leafNode.k.size() >= maxNode) {
                //implement split
                ArrayList<LeafNode> split = leafNode.split();
                String midKey = leafNode.getMiddleKey();

                if (leafNode.parent == null) {
                    leafNode.parent = new InternalNode();
                    leafNode.parent.leaf.add(leafNode);
                    this.root = leafNode.parent;
                } else {
                    leafNode.parent.addKey(midKey);
                    leafNode.removeMiddleKey();
                }
            }
        }
    }
}
