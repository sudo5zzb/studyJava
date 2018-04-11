package priv.zzb.study.java.blockchain;

import com.google.common.collect.Lists;

import java.util.List;

public class MerkleTree {

    private Node merkleRoot;
    private List<String> datas;
    private int dataSize;

    public MerkleTree(List<String> datas) {
        if(datas!=null){
            this.datas = datas;
            dataSize=datas.size();
            buildTree();
        }

    }

    private void buildTree() {
        if (datas == null) {
            return;
        }
        List<Node> leafNodes = Lists.newArrayList();
        for (String data : datas) {
            String hash = StringUtil.getSHA2HexValue(data);
            leafNodes.add(new Leaf(null, hash, data));
        }
        List<Node> tmpNodes = Lists.newArrayList(leafNodes);
        while (tmpNodes.size() > 1) {
            List<Node> nodes = Lists.newArrayList(tmpNodes);
            tmpNodes.clear();
            int i = 0;
            for (; i < nodes.size(); ) {
                Node parentNode;
                Node left = nodes.get(i);
                i++;
                if (i != nodes.size()) {
                    Node right = nodes.get(i);
                    parentNode = new Node(Lists.newArrayList(left, right), StringUtil.getSHA2HexValue(left.hash + right.hash));
                    i++;
                } else {
                    parentNode = new Node(Lists.newArrayList(left), StringUtil.getSHA2HexValue(left.hash));
                }
                tmpNodes.add(parentNode);
            }
        }
        merkleRoot=tmpNodes.get(0);
    }

    public String getRootHash(){
        return merkleRoot.hash;
    }

    public static void main(String[] args) {
        List<String> datas=Lists.newArrayList("a","b","c","d","e");
        MerkleTree merkleTree = new MerkleTree(datas);
        System.out.println(merkleTree.getRootHash());
    }
}


class Node {
    protected List<Node> childs;
    protected String hash;

    public Node(List<Node> childs, String hash) {
        this.childs = childs;
        this.hash = hash;
    }
}

class Leaf extends Node {
    private String data;

    public Leaf(List<Node> childs, String hash, String data) {
        super(childs, hash);
        this.data = data;
    }
}
