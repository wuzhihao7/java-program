package com.geo.tree;

/**
 * @author wuzhihao
 * @version V1.0
 * @since 2019/5/16
 */
public class BinaryTree {
    private Node root;

    public Node find(int key){
        Node cur = root;
        if(cur == null){
            return null;
        }
        while(cur.data != key){
            if(key < cur.data){
                cur = cur.leftChild;
            }else{
                cur = cur.rightChild;
            }
            if(cur == null){
                return null;
            }
        }
        return cur;
    }

    public void insert(Node node){
        if(root == null){
            root = null;
        }else{
            Node cur = root;
            while (true){
                if(node.data < cur.data){
                    if(cur.leftChild == null){
                        cur.leftChild = node;
                        return;
                    }
                }else{
                    if(cur.rightChild == null){
                        cur.rightChild = node;
                        return;
                    }
                }
                cur = node;
            }
        }
    }
}
