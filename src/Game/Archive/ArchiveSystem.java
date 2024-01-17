package Game.Archive;


import Game.PlayGameSystem.ChessFactory.Chess;

import java.io.File;
import java.io.IOException;

public class ArchiveSystem {
    private Caretaker caretaker;
    public ArchiveSystem(){
        this.caretaker = new Caretaker();
    }


    //基本
    public void addMemento(Chess chess) throws IOException, ClassNotFoundException {
        caretaker.addMemento(new Memento(chess));//创建一个存档
        System.out.println("存档了");
    }

    //悔棋
    public Chess undoChess(){
        MementoIF me = null;
        int lenthList = caretaker.getLen();
        if(lenthList >=1){
            if(lenthList - 3 >=0 ){
                me = caretaker.getMemento((lenthList - 3));
                caretaker.removeMemento(lenthList-1);
                caretaker.removeMemento(lenthList-2);
            }else {
                me = caretaker.getMemento(0);
                while (lenthList > 0){
                    lenthList--;
                    caretaker.removeMemento(lenthList);
                }
            }
        }
        if(me == null)
            return null;
        if(((Memento) me).getChessState() == null)
            return null;
        return ((Memento) me).getChessState();
    }

    //存档与恢复：
    public void saveArc(String name){
        int lenthList = caretaker.getLen();
        caretaker.saveSingleMemento(lenthList-1,name);
    }
    public Chess restoreArc(String name){
        //需要先判断本地有无文件，有就恢复，没有就null
        if(checkIfFileExists(name)){
            MementoIF me = caretaker.reMemento(name);
            return ((Memento) me).getChessState();
        }
        return null;
    }

    //录像与回放
    public void video(String name){
        caretaker.saveListMemento(name);
    }//保存录像
    public int playback(String name){
        if(checkIfVideo(name)){
            return caretaker.restoreListMemento(name);//返回的是录像保存的总长度。
        }
        return 0;
    }
    public Chess backByOneCheck(int index){
        MementoIF me;
        me = caretaker.getMemento(index);
        if (me == null)
            return null;
        return ((Memento) me).getChessState();
    }


    //销毁MeList
    public void desMeList(){
        caretaker.desMelist();
    }



    public boolean checkIfFileExists(String name) {
        String folderPath = "chessArchive";
        String username = name;
        String fileName = "chess"+name+".ser";
        File folder = new File(folderPath);
        File fileToCheck = new File(folder, fileName);
        return fileToCheck.exists() && !fileToCheck.isDirectory();
    }
    public boolean checkIfVideo(String name) {
        String folderPath = "cuserInfo";
        String fileName = "User_"+name+"List.ser";
        File folder = new File(folderPath);
        File fileToCheck = new File(folder, fileName);
        return fileToCheck.exists() && !fileToCheck.isDirectory();
    }


    //
    public void createSingleMemento(String player,Chess chess) throws IOException, ClassNotFoundException {

        caretaker.createSingleMemento(player,new Memento(chess));//创建一个存档
    }
    public boolean isSingleMemento(String player) {
        return caretaker.isSingleMemento(player);
    }
    public void restartSingleMemento() {
        caretaker.restartSingleMemento();
    }
    public Chess restoreSingleMemento(String player){
        MementoIF me = caretaker.restoreSingleMemento(player);
        return ((Memento) me).getChessState();
    }



}
