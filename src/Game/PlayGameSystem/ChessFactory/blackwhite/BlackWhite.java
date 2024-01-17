package Game.PlayGameSystem.ChessFactory.blackwhite;

import Game.PlayGameSystem.ChessFactory.Chess;

import java.io.IOException;
import java.io.Serializable;
import java.util.Objects;
import java.util.Scanner;

public class BlackWhite implements Chess, Serializable {
    private String chessplayer = "black";//执棋手，第一手默认为黑棋先行
    public int[][] chessboard;//棋盘，19*19的棋盘变为20*20棋盘，以便更好的处理边界情况

    private int boardsize = 8;//棋盘大小
    private int playersteps = 0;//回合
    private String winner = null;//胜利者
    private int x;//上一次的坐标x
    private int y;//上一次的坐标y


    public BlackWhite(int size){//构造函数
        this.boardsize = size+2;
        this.chessboard= new int[this.boardsize][this.boardsize];
        this.playersteps = 0;
        this.initializeBoard();
    }

    @Override
    public void initializeBoard() {//初始化或者重置所有参数
        for (int i = 0; i < boardsize; i++) {
            for (int j = 0; j < boardsize; j++) {
                chessboard[i][j] = 0;
            }
        }
        playersteps = 0;
        chessplayer = "black";
        winner = null;

        //初始化棋盘：
        chessboard[4][4]=-1;
        chessboard[4][5]=1;
        chessboard[5][4]=1;
        chessboard[5][5]=-1;

    }

    @Override
    public int[][] printABoard() {//打印棋盘
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

    @Override
    public boolean move(int x, int y) {
        System.out.println("X,Y:"+x+","+y);
        //检查是否存在棋子
        if(chessboard[x][y]!=0)
            return false;
        if(x<1||x>boardsize-2||y<1||y>boardsize-2)
            return false;

        //是否不存在可落子点：
        //如果不存在，直接虚空落子，然后改变执棋手，返回true


        int f = Objects.equals(this.chessplayer, "black") ? 1 : -1;//黑色或者白色
        //如果存在可落子点，则落子
        if(!Regular(x,y))
            return false;
        //合法，则落子
        chessboard[x][y] = f;
        this.x = x;
        this.y = y;

        //判断胜负
        return true;
    }

    //落子点是否合法
    //落子规则：落子一方落子的位置必须与对方棋子相邻包含斜线方向相邻
    //新落下的棋子，必须让至少1枚棋子翻转才能落下；
    //如果一方没有符合要求的落子位置，对方可以连下。只要一方有位置可下，就必须走棋；

    //合法的落子：
    public Boolean Regular(int x,int y){
        //该函数可以用于判断八个方位
        boolean canPlace = false;
        int f = Objects.equals(this.chessplayer, "black") ? 1 : -1;//当前颜色

        // 检查八个方向
        int[] dx = {-1, -1, -1, 0, 0, 1, 1, 1};
        int[] dy = {-1, 0, 1, -1, 1, -1, 0, 1};
        for (int i = 0; i < 8; i++) {
            int nx = x, ny = y;
            boolean hasOpposite = false; // 是否有对方的棋子
            while (true) {
                nx += dx[i];
                ny += dy[i];
                // 检查边界
                if (nx < 0 || nx >= this.boardsize || ny < 0 || ny >= this.boardsize) {
                    break;
                }
                if (this.chessboard[nx][ny] == 0) {
                    break; // 遇到空格子，停止
                } else if (this.chessboard[nx][ny] != f) {
                    hasOpposite = true; // 找到对方的棋子
                } else {
                    if (hasOpposite) {//因为有可能每次反转的不是一行，有可能是多行
                        //该方向可以反转
                        hasOpposite = false;
                        //反转该方向的棋子，进行反转
                        reverse(x,y,dx[i],dy[i]);

                        canPlace = true; // 找到一串对方的棋子后面是自己的棋子
                    }
//                    break;循环完所有方向后break
                }
            }
        }

        return canPlace;

    }
    public void reverse(int x,int y,int dx,int dy){
        //传入方向和坐标
        int f = Objects.equals(this.chessplayer, "black") ? 1 : -1;//当前颜色
        int nx = x, ny = y;
        while (true) {
            nx += dx;
            ny += dy;
            if (nx < 0 || nx >= this.boardsize || ny < 0 || ny >= this.boardsize) {
                System.out.println("越界");
                break;
            }else if (this.chessboard[nx][ny] == 0) {
                System.out.println("遇到空格子");
                break;
            }else if(this.chessboard[nx][ny] != f){
                this.chessboard[nx][ny] = f;
            }else if(this.chessboard[nx][ny] == f)
                break;
        }

    }





    //胜利的条件很简单：
    // 最终64个格子填满时，根据棋盘上的黑白数量判断胜负。
    // 或者如果途中有对方棋子完全被翻转成另己方的颜色，则己方提前获胜。
    @Override
    public boolean isOver(String player) {
        //或者如果途中有对方棋子完全被翻转成另己方的颜色，则己方提前获胜。也就是同色
        //刚刚落的一手棋可能导致棋局结束，因此如果该执棋手是黑子，则检查是否还存在白子
        boolean ff = false;
        int f = Objects.equals(this.chessplayer, "black") ? -1 : 1;//当前颜色
        for (int i = 1; i < boardsize-1; i++) {
            for (int j = 1; j < boardsize-1; j++) {
                if (chessboard[i][j] == f) {
                    ff = true;
                    break;
                }
            }
        }

        if(!ff){//对手不存在棋子可以使用了
            this.winner = "this.chessplayer";
            return true;
        }

        //最终64个格子填满时，根据棋盘上的黑白数量判断胜负。
        int bc = 0;
        int wc = 0;
        for (int i = 1; i < boardsize-1; i++) {
            for (int j = 1; j < boardsize-1; j++) {
                if(chessboard[i][j] == 0){
                    return false;
                }
                if(chessboard[i][j] == 1){
                    bc++;
                }else {
                    wc++;
                }
            }
        }
        if(bc > wc)
            this.winner = "balck";
        else if(wc > bc)
            this.winner = "white";
        else
            this.winner = "draw";
        return true;
    }




    @Override
    public void changePlayer() {//跳步
        playersteps++;
        this.chessplayer = Objects.equals(this.chessplayer, "white") ? "black" : "white";
    }

    @Override
    public String getWinner() {
        if (this.winner == null)
            return "draw";
        return this.winner;
    }

    @Override
    public String getGametye() {
        return "黑白棋";
    }

    @Override
    public String getChessplayer() {
        return chessplayer;
    }

    @Override
    public int getPlayersteps() {
        return playersteps;
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Scanner sc = new Scanner(System.in);

        BlackWhite bw = new BlackWhite(8);
        while (true){
            bw.printABoard();
            int x,y;
            x = sc.nextInt();
            y = sc.nextInt();
            bw.move(x,y);


        }




    }

}
