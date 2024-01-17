package Game.PlayGameSystem;

import Game.PlayGameSystem.ChessFactory.*;

import java.io.IOException;
import java.util.Objects;

public class PlayGameSystem {
    private String gametype = null;//游戏类型
    private String player;//当前执棋手
    private Chess chess;//棋类

    public PlayGameSystem(){//构造函数
        this.player = "black";
    }
    public void startNewGame(String gametype,int boardsize){
        ChessFactory creatF = null;
        if(Objects.equals(gametype, "围棋")){
            creatF = new WeiqiFactory();
        }else if(Objects.equals(gametype, "五子棋")){
            creatF = new GomokuFactory();
        }else if(Objects.equals(gametype, "黑白棋"))
            creatF = new BlackwhiteFactory();
        if (creatF != null) {
            this.chess = creatF.factory(boardsize);
        }else {
            System.out.println("Error!");
        }
        this.gametype = gametype;
    }
    public void setChess(Chess chess){
        this.chess = chess;
        this.gametype =chess.getGametye();
        this.player = chess.getChessplayer();
    }

    public boolean move(int x,int y){//放置棋子。
        return chess.move(x,y);
    }
    public boolean isOver(){//是否结束。
        return chess.isOver(this.player);
    }
    public String getWinner(){//获得胜利者；
        return chess.getWinner();
    }
    public Chess getChess(){
        return this.chess;
    }

    public void restart(){
        chess.initializeBoard();
        this.player=chess.getChessplayer();
//        printGame();
    }
    public String getGametype(){
        return this.gametype;

    }

    public String Surrend() {
        String w;
        if (Objects.equals(chess.getChessplayer(), "black")){
            w = "white";
        }else {
            w = "black";
        }
        return w;
    }

    public void changePlayer() {
        this.chess.changePlayer();
        this.player=chess.getChessplayer();

    }
    public String getPlayer() {
        this.player=chess.getChessplayer();
        return this.player;

    }

    public int getPlayersteps() {
        int i = chess.getPlayersteps();
        this.player=chess.getChessplayer();
        return i;
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {

    }

}
