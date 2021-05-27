//import java.security.KeyPair;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
//public class BPTree {
//    int maxNode = 2;
//    InternalNode root;
//    LeafNode firstLeaf;
//
//    public class Node {
//        InternalNode parent;
//    }
//
//    public class InternalNode extends Node {
//        ArrayList<LeafNode> leaf;
//        ArrayList<String> keys;
//        ArrayList<InternalNode> child;
//
//        public InternalNode() {
//            this.keys = new ArrayList<>();
//            this.leaf = new ArrayList<>();
//            this.child = new ArrayList<>();
//        }
//
//        public void addKey(String key) {
//            this.keys.add(key);
//            this.validateTree();
//        }
//
//        public void addKey(String key, LeafNode lf) {
//            this.leaf.add(lf);
//            this.keys.add(key);
//            this.validateTree();
//        }
//
//        public void validateTree() {
//            if (this.keys.size() > maxNode) {
//                ArrayList<InternalNode> s = this.split();
//                String mid = this.getMiddleKey();
//
////                this.left = s.get(0);
////                this.right = s.get(1);
//                if (this.parent == null) {
//                    this.parent = new InternalNode();
//                }
//                this.removeMiddleKey();
//                //sort before add
//                this.parent.addKey(mid);
//                this.parent.child.add(s.get(0));
//                this.parent.child.add(s.get(1));
//            }
//        }
//
//        public String getMiddleKey() {
//            int mid = maxNode / 2;
//            return keys.get(mid);
//        }
//
//        public void removeMiddleKey() {
//            int mid = maxNode / 2;
//            keys.remove(mid);
//        }
//
//        public ArrayList<InternalNode> split() {
//            List<String> first = keys.subList(0, maxNode / 2);
//            List<String> second = keys.subList(maxNode / 2, keys.size());
//            ArrayList<InternalNode> splitKeys = new ArrayList<InternalNode>();
//            InternalNode l1 = new InternalNode();
//            InternalNode l2 = new InternalNode();
//            //link leaf node after split
//            for (String s : first) {
//                l1.addKey(s);
//            }
//            for (String s : second) {
//                l2.addKey(s);
//            }
//            splitKeys.add(l1);
//            splitKeys.add(l2);
//            return splitKeys;
//        }
//    }
//
//    public class LeafNode extends Node {
//        ArrayList<KeyPair> k;
//        LeafNode sibling;
//
//        public LeafNode() {
//            this.k = new ArrayList<>();
//        }
//
//        public void insertKeyPair(String key, Integer pointer) {
//            this.k.add(new KeyPair(key, pointer));
//        }
//
//        public String getMiddleKey() {
//            int mid = maxNode / 2;
//            return k.get(mid).key;
//        }
//
//        public void removeMiddleKey() {
//            int mid = maxNode / 2;
//            k.remove(mid);
//        }
//
//        public ArrayList<LeafNode> split() {
//            List<KeyPair> first = k.subList(0, maxNode / 2);
//            List<KeyPair> second = k.subList(maxNode / 2, k.size());
//            ArrayList<LeafNode> splitLeaf = new ArrayList<LeafNode>();
//            LeafNode l1 = new LeafNode();
//            for (KeyPair key : first) {
//                l1.insertKeyPair(key.key, key.pointer);
//            }
//            LeafNode l2 = new LeafNode();
//            for (KeyPair key : second) {
//                l2.insertKeyPair(key.key, key.pointer);
//            }
//            //link leaf node after split
//            l1.sibling = l2;
//            splitLeaf.add(l1);
//            splitLeaf.add(l2);
//            return splitLeaf;
//        }
//
//    }
//
//    public class KeyPair {
//        String key;
//        Integer pointer;
//
//        public KeyPair(String key, Integer pointer) {
//            this.key = key;
//            this.pointer = pointer;
//        }
//    }
//
//    public LeafNode findLeafNode(String key) {
//
//        ArrayList<String> keys = this.root.keys;
//        InternalNode c1 = null;
//        LeafNode ln1 = null;
//        int i;
//        if(this.root.leaf.size() == 1){
//            return this.root.leaf.get(0);
//        }
//        for (i = 0; i < keys.size(); i++) {
//            int c = key.compareTo(keys.get(i));
//            if (c > 0) {
//                i++;
//                //key is larger
//                break;
//            }
//        }
//        System.out.println(i);
//        if (this.root.child.size() != 0) {
//            c1 = this.root.child.get(i);
//        }
//        if (this.root.leaf.size() != 0) {
//            ln1 = this.root.leaf.get(i);
//        }
//
//        if (ln1 != null) {
////            ArrayList<KeyPair> keys1 = ln1.k;
////            LeafNode ln2 = null;
////
////            for (int j = 0; j < keys1.size(); j++) {
////                ln2 = this.root.leaf.get(j);
////                int c = key.compareTo(keys1.get(j).key);
////                if (c > 0) {
////                    //a is larger
////                    break;
////                }
////            }
//            return ln1;
//        } else if (c1 != null) {
//            return findLeafNode(c1, key);
//        } else {
//            return new LeafNode();
//        }
//    }
//
//    private LeafNode findLeafNode(InternalNode node, String key) {
//
//        ArrayList<String> keys = node.keys;
//        InternalNode c1 = null;
//        LeafNode ln1 = null;
//        int i;
//        for (i = 0; i < keys.size(); i++) {
//            int c = key.compareTo(keys.get(i));
//            if (c > 0) {
//                //a is larger
//                i++;
//                break;
//            }
//        }
//        if (node.child.size() != 0) {
//            c1 = node.child.get(i);
//        }
//        if (node.leaf.size() != 0) {
//            ln1 = node.leaf.get(i);
//        }
//        InternalNode child = c1;
//        if (ln1 == null) {
//            return findLeafNode(child, key);
//        } else {
//            return ln1;
//        }
//    }
//
//    public void insert(String key, int value) {
//        LeafNode leafNode = new LeafNode();
//        if (this.root != null) {
//            leafNode = findLeafNode(key);
//        }
//
//        leafNode.insertKeyPair(key, value);
//        //check if need to split
//        if (leafNode.k.size() > maxNode) {
//            //implement split
//            ArrayList<LeafNode> split = leafNode.split();
//            String midKey = leafNode.getMiddleKey();
//
//            if (leafNode.parent == null) {
//                leafNode.parent = new InternalNode();
//                leafNode.parent.addKey(midKey);
//                leafNode.parent.leaf.add(split.get(0));
//                leafNode.parent.leaf.add(split.get(1));
//                this.root = leafNode.parent;
//            } else {
//                leafNode.parent.addKey(midKey);
//                leafNode.removeMiddleKey();
//            }
//        }
//        if (this.root == null) {
//            this.root = new InternalNode();
//            this.root.leaf.add(leafNode);
//            this.root.addKey(key);
//        }
//
//    }
//}
