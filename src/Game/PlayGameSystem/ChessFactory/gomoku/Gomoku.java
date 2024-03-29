package Game.PlayGameSystem.ChessFactory.gomoku;

import Game.PlayGameSystem.ChessFactory.Chess;

import java.io.Serializable;
import java.util.Objects;

public class Gomoku implements Chess, Serializable {

    private String chessplayer = "black";//执棋手，第一手默认为黑棋先行
    public int[][] chessboard;//棋盘，19*19的棋盘变为20*20棋盘，以便更好的处理边界情况

    private int boardsize = 8;//棋盘大小
    private int playersteps = 0;//回合
    private String winner = null;//胜利者
    private int x;//上一次的坐标x
    private int y;//上一次的坐标y


    public Gomoku(int size){
        this.boardsize = size+2;
        this.chessboard= new int[this.boardsize][this.boardsize];
        this.playersteps = 0;
        this.initializeBoard();
    }

    @Override
    public void initializeBoard() {//初始化所有参数
        for (int i = 0; i < boardsize; i++) {
            for (int j = 0; j < boardsize; j++) {
                chessboard[i][j] = 0;
            }
        }
        playersteps = 0;
        chessplayer = "black";
        winner = null;
    }
    @Override
    public int[][] printABoard(){//打印棋盘
//        System.out.print("  ");
//        for(int i=1; i<=boardsize-2; i++){
//            System.out.printf(" %2d",(i));
//        }
//        System.out.println(" ");
//        for (int i = 1; i <= boardsize-2; i++) {
//            System.out.printf("%2d",i);
//            for (int j = 1; j <= boardsize-2; j++) {
//                if (this.chessboard[i][j] == 1) {
//                    System.out.print("| ●");
//                }else if (this.chessboard[i][j] == -1){
//                    System.out.print("| ⊙");
//                }else {
//                    System.out.print("| +");
//                }
//            }
//            System.out.println("|");
//        }
        return chessboard;
    }

    //下一步棋子
    @Override
    public boolean move(int x,int y){
        int f = Objects.equals(this.chessplayer, "black") ? 1 : -1;//黑色或者白色
        //检查合法？
        if(x<1||x>boardsize-2||y<1||y>boardsize-2)
            return false;
        if(this.chessboard[x][y]!=0)
            return false;
        //合法，则落子
        chessboard[x][y] = f;
        this.x = x;
        this.y = y;

        return true;//成功落子
    }

    //判断棋局是否结束
    @Override
    public boolean isOver(String  player) {//true 结束 false未结束。
        int f = Objects.equals(this.chessplayer, "black") ? 1 : -1;
        //棋盘满或者连成五子
        boolean over2 =true;

        boolean over = judge(x,y,f);
        if (over)
            this.winner = player;
        for (int i = 1; i <= boardsize-2; i++) {
            for (int j = 1; j <= boardsize - 2; j++) {
                if(chessboard[i][j] == 0){
                    over2 = false;

                }
            }
        }
        if(over || over2)
            return true;
        return false;
    }
    @Override
    public void changePlayer(){
        playersteps++;
        this.chessplayer = Objects.equals(this.chessplayer, "white") ? "black" : "white";
    }
    //判断player是否胜利
    public boolean judge(int x,int y,int f){
        //横向
        int[]  dr = {-4, -3, -2, -1, 1, 2, 3, 4};
        int count = 0;
        for(int i=0; i<8; i++){
            if((x+dr[i])<1 || (x+dr[i])>boardsize-2)
                continue;
            else if(chessboard[x+dr[i]][y] == f){
                count++;
                if(count == 4)
                    return true;
            }else {
                count = 0;
            }
        }
        //竖向
        count = 0;
        for(int i=0; i<8; i++){
            if((y+dr[i])<1 || (y+dr[i])>boardsize-2)
                continue;
            else if(chessboard[x][y+dr[i]] == f){
                count++;
                if(count == 4)
                    return true;
            }
            else {
                count = 0;
            }
        }
        //正斜向
        count = 0;
        for(int i=0; i<8; i++){
            if((x+dr[i])<1 || (x+dr[i])>boardsize-2|| (y+dr[i])<1 || (y+dr[i])>boardsize-2)
                continue;
            else if(chessboard[x+dr[i]][y+dr[i]] == f){
                count++;
                if(count == 4)
                    return true;
            }
            else {
                count = 0;
            }
        }
        //逆斜向
        count = 0;
        for(int i=0; i<8; i++){
            if((x-dr[i])<1 || (x-dr[i])>boardsize-2|| (y+dr[i])<1 || (y+dr[i])>boardsize-2)
                continue;
            else if(chessboard[x-dr[i]][y+dr[i]] == f){
                count++;
                if(count == 4)
                    return true;
            }
            else {
                count = 0;
            }
        }
        return false;
    }
    public String getWinner(){
        if (this.winner == null)
            return "draw";
        return this.winner;
    }
    @Override
    public String getGametye(){
        return "五子棋";
    }
    @Override
    public String getChessplayer(){
        return chessplayer;
    }
    @Override
    public int getPlayersteps(){
        return playersteps;
    }

}

