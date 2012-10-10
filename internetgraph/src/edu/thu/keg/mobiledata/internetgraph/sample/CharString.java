/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.thu.keg.mobiledata.internetgraph.sample;

/**
 *
 * @author Yuan Bozhi
 * 
 */
public class CharString {
    String S;
    public CharString(String s){
    S=s;
    }
    int len(){
        int i=S.length();
        return(i);
    }
    //返回字符串下标的第一个Cs的匹配
    public int indexOf(CharString cS){
        int k=-1,i=1,j=1;
        int [] next;
        if(S.equals(""))
            return(-1);
        next=KMPnext(cS.S);
        while(i<=S.length() && j<=cS.S.length()){
            if(j==0 || S.charAt(i-1)==cS.S.charAt(j-1)){
                i++;
                j++;
            }
            else{
                j=next[j];
            }
        }
        if(j>cS.S.length())
            return(i-j);
        else
            return(-1);
    }
    //返回字符串下标的第一个Cs的匹配(从第bi个字符串下标的字符开始搜索)
    public int indexOf(CharString cS,int bi){
            if(bi<0)
                bi=0;
        int k=-1,i=bi+1,j=1;
        int [] next;
        if(S.equals(""))
            return(-1);
        next=KMPnext(cS.S);
        while(i<=S.length() && j<=cS.S.length()){
            if(j==0 || S.charAt(i-1)==cS.S.charAt(j-1)){
                i++;
                j++;
            }
            else{
                j=next[j];
            }
        }
        if(j>cS.S.length())
            return(i-j);
        else
            return(-1);
    }
    private int [] KMPnext(String s){
        int j=1,k=0;
        int [] next= new int[s.length()+1];
        next[1]=0;
        while(j<s.length()){
            if(k==0 || s.charAt(j-1)==s.charAt(k-1)){
                k++;j++;
                next[j]=k;
            }
            else
                k=next[k];
        }
        return next;
    }
    public CharString subString(int begin,int len){
        CharString temp=new CharString("");
        if(len==0 || begin>=S.length()){
            temp.S="";
            return(temp);
        }
        if(len<0){
            System.out.println("字符串截取时参数出错");
            temp.S="";
            return(temp);
        }
        String subS=new String();
        for(int i=begin;i<S.length();i++){
            subS=subS+S.charAt(i);
            if(i==begin+len-1)
                break;
        }
        temp.S=subS;
        return (temp);
    }
    public CharString subString(int begin){

        CharString temp=new CharString(S);
        String subS=new String("");
        if(begin>=S.length()){
            temp.S="";
            return(temp);
        }
        for(int i=begin;i<S.length();i++)
            subS=subS+temp.S.charAt(i);
        temp.S=subS;
        return(temp);
    }
    public CharString concat(CharString s){
        CharString temp = new CharString(S);
        String conS= new String();
        temp.S=S+s.S;
        return (temp);
    }
    //找到第一个与字符串a相等的字符串，置换成b
    String changeFirstAintoB(String a, String b){
        if(a.equals(""))
            return(S);
        return(S.replaceFirst(a,b));
    
        
    }
    CharString changeFirstAintoBforCs(CharString a, CharString b){
        if(a.equals(""))
            return(this);
        int i=this.indexOf(a);
        S=this.subString(0, i).S+b.S+this.subString(i+a.S.length(), this.len()).S;
        return(this);
    }
    boolean equals(String s){
        return(S.equals(s));
    }
    int compare(CharString cS) {
        int i=S.compareTo(cS.S);
        return(i);
    }
    boolean startWith(CharString cS){
        boolean b=S.startsWith(cS.S);
        return(b);
    }
    //判断是否为字符
    static boolean Chi(char a){
        if(a<'0'&& a>'9'&&a<'a'&&a>'z'&&a<'A'&&a>'Z')
            return (true);
        return(false);
    }
}
