package Game.AISystem;

import java.io.IOException;
import java.util.Random;

//初级AI
public class FirstLevelAI extends AIStrategy{
    Random randX;
    Random randY;
    int boardsize;
    public FirstLevelAI(int boardsize){
        randX = new Random();
        randY = new Random();
        this.boardsize = 8;
    }


    @Override
    public int[] res(int[][] chessb) {
        int[] rr = new int[2];
        rr[0]=randX.nextInt(boardsize) + 1;
        rr[1]=randY.nextInt(boardsize) + 1;
        return rr;
    }

    @Override
    public int resX() {
        return randX.nextInt(boardsize) + 1; // 生成一个1到10的随机数,且不重复。
    }
    public int resY() {
        return randY.nextInt(boardsize) + 1; // 生成一个1到10的随机数,且不重复。;
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        FirstLevelAI Fai = new FirstLevelAI(8);
        int cc = 0;
        while (cc<64){
            System.out.println("X: "+Fai.resX()+",Y: "+Fai.resY());
            cc++;
        }
    }

}
