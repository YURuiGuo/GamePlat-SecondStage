package accountManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {
    private String name;
    private String password;
    private List<Integer> wincount;//存储各个棋类游戏的胜利回合数目
    private List<Integer> losecount;//存储各个棋类游戏的失败回合数目


    public User(String name,String password){
        this.name = name;
        this.password = password;
        wincount = new ArrayList<>();
        losecount = new ArrayList<>();
        for(int i=0;i<3;i++){
            wincount.add(0);
            losecount.add(0);
        }
        System.out.println("用户："+name+",注册成功！");
    }
    public User(String name){
        this.name = name;
        wincount = new ArrayList<>();
        losecount = new ArrayList<>();
        System.out.println("用户："+name+",注册成功！");
    }


    //定义方法：
    public String getName(){return this.name;}
    public void setName(String name){this.name = name;}
    public boolean setPassword(String password){
        if(password == null || password.equals(this.password))
            return false;
        this.password = password;
        return true;
    }
    public String getPassword(){
        return password;
    }

    public List<Integer> getCount(){//返回全部的对局场次&胜利场次。
        List<Integer> thisGames = new ArrayList<>();
        int totalGames=0;
        int winGames = 0;
        for (int i = 0; i < wincount.size(); i++) {
            totalGames += wincount.get(i) + losecount.get(i);
            winGames += wincount.get(i);
        }
        thisGames.add(totalGames);
        thisGames.add(winGames);
        System.out.println("From UserClass：总场次为："+ thisGames.get(0)+" 胜利场次为："+thisGames.get(1));
        return thisGames;
    }
    public List<Integer> getChessCount(int index){//返回某一的对局场次
        //index默认：围棋是0，五子棋是1，黑白棋是2；
        List<Integer> thisGames = new ArrayList<>();
        thisGames.add(wincount.get(index));
        thisGames.add(losecount.get(index));
        System.out.println("From UserClass：胜利场次为："+wincount.get(index)+"，失败场次为："+losecount.get(index));
        return thisGames;
    }

    //更新对局数目：
    public void updateWinCount(int index){
        int co = wincount.get(index);
        wincount.set(index, co+1);
    }
    public void updateLoseCount(int index){
        int co = losecount.get(index);
        losecount.set(index, co+1);
    }




}
