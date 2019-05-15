package com.geo.linkedlist;

/**
 * @author wuzhihao
 * @version V1.0
 * @since 2019/5/15
 */
public class LinkedList {
    private Node first;

    public LinkedList(){
        this.first = null;
    }

    public void insertFirst(Node node){
        node.next = first;
        first = node;
    }

    public Node deleteFirst(){
        if(isEmpty()){
            throw new UnsupportedOperationException("linked list is empty");
        }
        Node temp = first;
        first = first.next;
        return temp;
    }

    public Node delete(int key){
        Node node = null;
        Node cur = first;
        Node next = first.next;
        Node previous = null;
        while (cur != null){
            //找到了要删除的节点
            if(cur.data == key){
                node = cur;
                //如果当前节点的前驱节点为null，证明其为链表的第一个节点，删除该节点后，需要对first属性重新赋值
                if(previous == null){
                    this.first = next;
                }else{
                    //删除操作，即将前驱的next指针指向当前节点的next
                    previous.next = next;
                }
                break;
            }else if(cur.next == null){
                break;
            }
            next = next.next;
            cur = cur.next;
            previous = cur;
        }
        return node;
    }

    public Node find(int key){
        Node node = null;
        Node cur = first;
        Node next = null;
        while (cur != null){
            if(cur.data == key){
                node = cur;
                break;
            }else if(cur.next == null){
                break;
            }
            next = next.next;
            cur = cur.next;
        }
        return node;
    }

    public void display(){
        Node node = first;
        while (node != null){
            node.display();
            node = node.next;
        }
    }

    public boolean isEmpty(){
        return first == null;
    }
}
