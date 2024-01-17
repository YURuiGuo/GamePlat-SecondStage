package Game;

import Game.AISystem.AIStrategy;
import Game.AISystem.FirstLevelAI;
import Game.AISystem.SecondLevelAI;
import Game.Archive.ArchiveSystem;
import Game.PlayGameSystem.ChessFactory.Chess;
import Game.PlayGameSystem.PlayGameSystem;
import accountManager.AccountManager;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class GamePlat2 {
    //四个子系统
    private PlayGameSystem game; //棋类子系统
    private ArchiveSystem archive; //就是存档子系统
    private AIStrategy aiplayer= null; //AI对战子
    private AIStrategy aiplayer2= null; //AI对战
    private AccountManager accountManager;//账户子系统

    //其他标志变量
    boolean flag;//判断需要在棋局结束后录像
    private String winner;

    private String username;

    public GamePlat2(){
        this.accountManager = new AccountManager();//启动账户子系统
        this.game = new PlayGameSystem();//启动棋类子系统
        this.archive = new ArchiveSystem();//启动存档子系统
        flag = false;
        winner = null;
    }

    //登陆&注册验证：
    public boolean Login(String name,String password){
        if(accountManager.loginAccount(name,password)) {
            username = name;
            return true;
        }
        return false;
    }
    public boolean register(String name,String password) {
        if(accountManager.createAccount(name,password)) {
            username = name;
            return true;
        }
        return false;
    }


    public boolean updateAcc(String newUsername, String oldPassword, String newPassword) {
        if(accountManager.updateAcc(newUsername,oldPassword,newPassword)) {
            username = newUsername;
            return true;
        }
        return false;

    }
    public double getPerformance(){
        double result=0;
        List<Integer>  bb =accountManager.getChessCount(game.getGametype());
        int firstElement = bb.get(0); // 读取第一个元素
        int secondElement = bb.get(1); // 读取第二个元素
        if((secondElement+firstElement)==0)
            return 1;
        result = (double) firstElement / (secondElement+firstElement); // 进行除法运算
        return result;
    }

    public int[][] getBoard(){
        return this.game.getChess().printABoard();
    }


    //开始游戏
    public void startgame(String gametype,int boardsize) {
        try {
            this.game.startNewGame(gametype, boardsize);//先默认Size为8
            this.archive.addMemento(game.getChess());
            archive.restartSingleMemento();
        }catch (IOException | ClassNotFoundException | NumberFormatException e){
            throw new RuntimeException(e);
        }
    }
    public int playgame(int x,int y) {
        try {
            System.out.println(game.getPlayer());
            this.archive.createSingleMemento(game.getPlayer(),game.getChess());
            if (!this.game.move(x, y)) {
                return -1;//落子错误
            }
            this.archive.addMemento(game.getChess());//存档一次。System.out.println("存档了");
            if (this.game.isOver()) {
                winner = this.game.getWinner();
                if(Objects.equals(winner, "black"))
                    accountManager.updateCount(game.getGametype());
                accountManager.logoutAccount();
                if(flag)//要求保存回放：
                    this.archive.video(username);
                this.archive.desMeList();
                archive.restartSingleMemento();
                flag = false;
                return 0;
            }
            this.game.changePlayer();
            return 1;
        } catch (IOException | ClassNotFoundException | NumberFormatException e) {
            throw new RuntimeException(e);
        }
    }





    //继续游戏：
    public boolean continueGame(){
        Chess che = archive.restoreArc(username);
        if(che==null){
            return false;
        }
        System.out.println("存档点:");
        game.setChess(che);
        this.winner =null;
        flag = false;
        return true;
    }


    //回放：
    public int VideoGame(){
        return archive.playback(username);
    }
    public boolean backByOneCheck(int index){
        Chess che = archive.backByOneCheck(index);
        if(che == null)
            return false;
        game.setChess(che);
        return true;
    }


    //AI对战
    public void startGameForAI(int level){
        this.game.startNewGame("五子棋", 8);//先默认Size为8
        archive.restartSingleMemento();
        if(level == 1)
            aiplayer = new FirstLevelAI(8);
        else
            aiplayer = new SecondLevelAI(8,this.game.getChess().printABoard());
    }
    public void startGameForAI(int level1,int level2){
        this.game.startNewGame("五子棋", 8);//先默认Size为8
        archive.restartSingleMemento();
        if(level1 == 1)
            aiplayer = new FirstLevelAI(8);
        else
            aiplayer = new SecondLevelAI(8,this.game.getChess().printABoard());
        if(level2 == 1)
            aiplayer2 = new FirstLevelAI(8);
        else
            aiplayer2 = new SecondLevelAI(8,this.game.getChess().printABoard());
    }
    //AI vs. 玩家
    public int playgameForAI(int x,int y){
        try {
            System.out.println(game.getPlayer());
            this.archive.createSingleMemento(game.getPlayer(),game.getChess());
            if (!this.game.move(x, y)) {
                return -1;//落子错误
            }
            if (this.game.isOver()) {
                winner = this.game.getWinner();
                if(Objects.equals(winner, "black"))
                    accountManager.updateCount(game.getGametype());
                accountManager.logoutAccount();
                if(flag)//要求保存回放：
                    this.archive.video(username);
                this.archive.desMeList();
                archive.restartSingleMemento();
                flag = false;
                return 0;
            }
            this.archive.addMemento(game.getChess());//存档一次。System.out.println("存档了");
            this.game.changePlayer();
            //AI走一步
            try {
                // 暂停1秒
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // 如果线程被中断，处理中断异常
                e.printStackTrace();
            }
            while (true){
                int [] ll = this.aiplayer.res(this.game.getChess().printABoard());
                System.out.println("rr: "+ll[0]+" "+ll[1]);
                if(this.game.move(ll[0], ll[1]))
                    break;
            }
            if (this.game.isOver()) {
                winner = this.game.getWinner();
                if(Objects.equals(winner, "black"))
                    accountManager.updateCount(game.getGametype());
                accountManager.logoutAccount();
                if(flag)//要求保存回放：
                    this.archive.video(username);
                this.archive.desMeList();
                archive.restartSingleMemento();
                flag = false;
                return 0;
            }
            this.archive.addMemento(game.getChess());//存档一次。System.out.println("存档了");
            this.game.changePlayer();
            return 1;
        } catch (IOException | ClassNotFoundException | NumberFormatException e) {
            throw new RuntimeException(e);
        }

    }
    //AI vs. AI
    public int playgameForAI() throws IOException, ClassNotFoundException {
        //AI 1执行：
        while (true){
            int [] ll = this.aiplayer.res(this.game.getChess().printABoard());
            System.out.println("rr: "+ll[0]+" "+ll[1]);
            if(this.game.move(ll[0], ll[1]))
                break;
        }
        if (this.game.isOver()) {
            winner = this.game.getWinner();
            if(Objects.equals(winner, "black"))
                accountManager.updateCount(game.getGametype());
            accountManager.logoutAccount();
            if(flag)//要求保存回放：
                this.archive.video(username);
            this.archive.desMeList();
            archive.restartSingleMemento();
            flag = false;
            return 0;
        }
        this.archive.addMemento(game.getChess());//存档一次。System.out.println("存档了");
        this.game.changePlayer();


        //AI 2执行：
        try {
            // 暂停1秒
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // 如果线程被中断，处理中断异常
            e.printStackTrace();
        }
        while (true){
            int [] ll = this.aiplayer2.res(this.game.getChess().printABoard());
            System.out.println("rr: "+ll[0]+" "+ll[1]);
            if(this.game.move(ll[0], ll[1]))
                break;
        }
        if (this.game.isOver()) {
            winner = this.game.getWinner();
            if(Objects.equals(winner, "black"))
                accountManager.updateCount(game.getGametype());
            accountManager.logoutAccount();
            if(flag)//要求保存回放：
                this.archive.video(username);
            this.archive.desMeList();
            archive.restartSingleMemento();
            flag = false;
            return 0;
        }
        this.archive.addMemento(game.getChess());//存档一次。System.out.println("存档了");
        this.game.changePlayer();

        return 1;
    }

    //Bar控制：
    public boolean BarControll(String com) {
        if(Objects.equals(com, "R")){//重新开始
            game.restart();//重置棋盘
            System.out.println("新棋盘：");
            archive.desMeList();
            archive.restartSingleMemento();
            flag = false;
            winner = null;
        }  else if (Objects.equals(com, "T")) {//悔棋
            if(archive.isSingleMemento(game.getPlayer())){
                Chess che = archive.restoreSingleMemento(game.getPlayer());
                game.setChess(che);
            }else {
                System.out.println("无法悔棋。");
                return false;
            }
        } else if (Objects.equals(com, "S")) {//认输
            winner = this.game.Surrend();
            if(Objects.equals(winner, "black"))
                accountManager.updateCount(game.getGametype());
            accountManager.logoutAccount();
            if(flag)//要求保存回放：
                this.archive.video(username);
            this.archive.desMeList();
            archive.restartSingleMemento();
            flag=false;
        } else if (Objects.equals(com, "A")) {//存档
            archive.saveArc(username);
        }else if (Objects.equals(com, "E")) {//退出
            accountManager.logoutAccount();
            System.exit(0);
        } else if(Objects.equals(com, "P")){//跳过
            this.game.changePlayer();
        }else if(Objects.equals(com, "D")){//请求结束。
//                game.changePlayer();
            this.winner=game.getWinner();
            accountManager.logoutAccount();
            if(flag)//要求保存回放：
                this.archive.video(username);
            this.archive.desMeList();
            archive.restartSingleMemento();
            flag=false;
        }else if(Objects.equals(com, "V")){
            flag = !flag;
        }else {
            return false;
        }
        return true;
    }
    public String getPlayer() {
        return  game.getPlayer();
    }
    public String getOtherPlayer(){
        if(aimode == 2){
            return "AI2";
        }else if(aimode == 1){
            return "AI";
        }else return "玩家二";
    }
    public int getStep(){
        return game.getPlayersteps()+1;
    }
    public String getUsername(){
        if(aimode==2){
            return "AI1";
        }else return username;
    }

    public String getGametype() {
        return game.getGametype();
    }


    private int aimode;//标记是否为游客模式
    public void setAiMode(int aicou){
        aimode=aicou;
    }
    public String getWinner(){
        return winner;
    }
}
