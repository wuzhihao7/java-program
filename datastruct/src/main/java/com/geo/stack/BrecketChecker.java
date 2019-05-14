package com.geo.stack;


/**
 * @author wuzhihao
 * @version V1.0
 * @since 2019/5/14
 */
public class BrecketChecker {
    private String strInput;

    public BrecketChecker(String strInput){
        this.strInput = strInput;
    }

    /**
     * 括号检查
     */
    public void check(){
        int length = strInput.length();
        Stack stack = new Stack(length);
        for(int i=0; i<length; i++){
            char ch = strInput.charAt(i);
            switch (ch){
                case '{':
                case '[':
                case '(':
                    stack.push(ch);
                    break;
                case '}':
                case ']':
                case ')':
                    if(!stack.isEmpty()){
                        char pop = stack.pop();
                        boolean a = (ch == '}' && pop != '{');
                        boolean b = (ch == ']' && pop != '[');
                        boolean c = (ch == ')' && pop != '(');
                        if(a || b || c){
                            System.out.println("匹配出错！字符："+ch+",下标："+i);
                        }
                    }else{
                        System.out.println("匹配出错！字符："+ch+",下标："+i);
                    }
                    default:
                        break;
            }
        }
        if(!stack.isEmpty()){
            System.out.println("有括号没有关闭！");
        }
    }
}
