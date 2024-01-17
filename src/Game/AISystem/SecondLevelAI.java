package Game.AISystem;

import java.io.IOException;
import java.util.Objects;

//中级AI
public class SecondLevelAI extends AIStrategy{
//    List<Integer> pos;
    int y;

    int x;

    int score;
    int boardsize;
    public int[][] chessboard;//
    private String chessplayer ="white";


    public SecondLevelAI(int boardsize,int[][] chessb){
        this.boardsize = boardsize+2;
        //复制一份：
        this.chessboard = new int[this.boardsize][this.boardsize];
        for (int i = 0; i < chessb.length; i++) {
            System.arraycopy(chessb[i], 0, chessboard[i], 0, chessb[i].length);
        }
        this.score=0;
        this.x=1;
        this.y=1;
    }



    @Override
    public int[] res(int[][] chessb) {
        this.boardsize = boardsize+2;
        //复制一份：
        this.chessboard = new int[this.boardsize][this.boardsize];
        for (int i = 0; i < chessb.length; i++) {
            System.arraycopy(chessb[i], 0, chessboard[i], 0, chessb[i].length);
        }
        this.score=0;
        this.x=1;
        this.y=1;


       int[] rr = new int[2];


        //获取X，Y
        this.getXY();

        rr[0]=this.x;
        rr[1]=this.y;
        return rr;
    }

    private void getXY() {
        int f = Objects.equals(this.chessplayer, "black") ? 1 : -1;//当前颜色
        //使用连成线的策略以及活三的策略完成中级AI

        //策略是：所有的空位置，然后计算空位置的得分
        //得分计算方式：看是否能同色相连，相连棋子的数目即为得分
        for (int i = 1; i <= boardsize - 2; i++) {
            for (int j = 1; j <= boardsize - 2; j++) {
                //找 i,j的八个邻居，并计算得分

                if (chessboard[i][j] != 0)
                    continue;
                int[] dx = {-1, -1, -1, 0, 0, 1, 1, 1};
                int[] dy = {-1, 0, 1, -1, 1, -1, 0, 1};
                int ss=1;
                for (int t = 0; t < 8; t++) {
                    int nx = i+dx[t], ny = j+dy[t];
                    if (nx <= 0 || nx >= this.boardsize-1 || ny <= 0 || ny >= this.boardsize-1) {
                        continue;
                    }
                    int ss1 = this.getScore(i, j,dx[t],dy[t]);
                    if(ss1>ss)
                        ss=ss1;
                }
                if(ss>score){
                    x=i;y=j;score=ss;
                }
            }
        }
    }

    private int getScore(int nx, int ny,int dx,int dy) {
        int f = Objects.equals(this.chessplayer, "black") ? 1 : -1;//当前颜色
        int ss = 1;
        int nnx = nx;
        int nny = ny;
        while(true){
            nnx+=dx;
            nny+=dy;
            if(chessboard[nnx][nny] == f)
                ss++;
            else
                break;
        }
        while(true){
            nx-=dx;
            ny-=dy;
            if(chessboard[nx][ny] == f)
                ss++;
            else
                break;
        }
        return ss;
    }

    @Override
    public int resX() {
        return 0;
    }

    @Override
    public int resY() {
        return 0;
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        int boardsize=10;
        int[][] matrix1 = new int[10][10];
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                matrix1[i][j] = 0; // 或者您想要的任何初始值
                if(i==1){
                    if(j==2)
                        matrix1[i][j] = -1;
                    if(j==3)
                        matrix1[i][j] = -1;
                    if(j==4)
                        matrix1[i][j] = -1;
                }
                if(i==1)
                    if(j==5)
                            matrix1[i][j] = 1;
            }
        }
        System.out.print("  ");
        for(int i=1; i<=boardsize-2; i++){
            System.out.printf(" %2d",(i));
        }
        System.out.println(" ");
        for (int i = 1; i <= boardsize-2; i++) {
            System.out.printf("%2d",i);
            for (int j = 1; j <= boardsize-2; j++) {
                if (matrix1[i][j] == 1) {
                    System.out.print("| ●");
                }else if (matrix1[i][j] == -1){
                    System.out.print("| ⊙");
                }else {
                    System.out.print("| +");
                }
            }
            System.out.println("|");
        }

        SecondLevelAI ai = new SecondLevelAI(8,matrix1);
    }

}


