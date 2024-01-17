package Game.Archive;

import java.io.*;
import java.util.ArrayList;
import java.util.Objects;

public class Caretaker {
    private ArrayList<MementoIF> meList;
    private MementoIF mementoBlack = null;
    private MementoIF mementoWhite = null;

    public Caretaker(){
        meList = new ArrayList<>();
    }

    //基本的：
    public int getLen(){
        return meList.size();
    }
    public void addMemento(Memento memento){
        meList.add(memento);
    }
    public boolean removeMemento(int index){
        if(index<meList.size()&&index>0) {
            meList.remove(index);
            return true;
        }
        return false;
    }
    public void desMelist(){
        meList = null;
        meList = new ArrayList<>();
    }


    //获取：包括List
    public Memento getMemento(int index){//MeList获取
        return (Memento)meList.get(index);
    }
    public Memento reMemento(String name){//本地获取
        System.out.println("恢复的存档为："+"chessArchive/chess"+name+".ser");
        //反序列化
        Memento me = null;
        try (FileInputStream fis = new FileInputStream("chessArchive/chess"+name+".ser");
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            me= (Memento) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        if(me == null)
            System.out.println("AA");
        meList.add(me);
        return me;
    }
    public int restoreListMemento(String name) {
        //反序列化写出即可
        try (FileInputStream fis = new FileInputStream("cuserInfo/User_"+name+"List.ser");
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            meList= (ArrayList<MementoIF>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return meList.size();
    }
    public void saveListMemento(String name) {
        //序列化写入即可
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("cuserInfo/User_"+name+"List.ser"))) {
            out.writeObject(meList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //保存
    public void saveSingleMemento(int indexx,String name){
        MementoIF me = meList.get(indexx);
        if(me == null)
            System.out.println("Null;");
        //序列化保存：
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("chessArchive/chess"+name+".ser"))) {
            out.writeObject(me);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //


    public Memento restoreSingleMemento(String player) {
        Memento me;
        if(Objects.equals(player, "black"))
            me = (Memento) mementoBlack;
        else
            me = (Memento) mementoWhite;
        return me;
    }
    public boolean isSingleMemento(String player) {
        if(Objects.equals(player, "black")){
            return mementoBlack != null;
        }else if(Objects.equals(player, "white")){
            return mementoWhite != null;
        }
        return false;
    }
    public void createSingleMemento(String player,MementoIF me){
        if(Objects.equals(player, "black"))
            mementoBlack = me;
        else
            mementoWhite = me;
    }
    public void restartSingleMemento(){
        mementoBlack = null;
        mementoWhite = null;
    }
}