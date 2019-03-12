package com.house.dom4j;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.util.Iterator;

/**
 * @author wuzhihao
 * @version V1.0
 * @since 2019/3/12
 */
public class Demo {
    public static void main(String[] args) {
        SAXReader read = new SAXReader();
        try {
            Document doc = read.read(Demo.class.getResourceAsStream("/TransTypeFormate.xml"));
            //获取根节点
            Element rootElement = doc.getRootElement();
            //获取根节点下面的节点
            Iterator<Element> elementIterator = rootElement.elementIterator();
            while (elementIterator.hasNext()) {
                Iterator<Element> elementIterator1 = elementIterator.next().elementIterator();
                String typeName = "";
                while (elementIterator1.hasNext()) {
                    Element next = elementIterator1.next();
                    String name = next.getName();
                    String text = next.getText();
                    if ("TypeName".equals(name)) {
                        typeName = text;
                    } else {
                        if (text != null && !"".equals(text)) {
                            System.out.println(String.format("push.formatter.%s.%s=%s",typeName, name, text));
                        }
                    }
                }
                System.out.println();
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }
}
