package accountManager;


import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

public class AccountManager {
    //要求不允许重名，而且AI和游客的构造必须相同
    User blackuser;
    List<String> nameList;

    public AccountManager(){
        //反序列化读取namelist文件，检查当前的用户名是否已经存在了:
        try (FileInputStream fis = new FileInputStream("cuserInfo/namelist.ser");
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            this.nameList = (List<String>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        if(nameList == null)
            nameList = new ArrayList<>();
        System.out.println("Manager is ready. 读取的nameList:"+nameList);
    }

    //用户的登录，退出以及注册
    public boolean loginAccount(String name,String password){
        int index = nameList.indexOf(name);
        System.out.println(index);
        if(index == -1)
            return false;
        //序列化读入文件：D:/Project/02.IDEA/Course_autumn_Java/GamePlat/GamePlat/cuserInfo
        try (FileInputStream fis = new FileInputStream("cuserInfo/User"+String.valueOf(index)+".ser");
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            blackuser= (User) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        if(Objects.equals(blackuser.getName(), name) && Objects.equals(blackuser.getPassword(), password))
            return true;
        return false;
    }
    public boolean logoutAccount(){
        int index = nameList.indexOf(blackuser.getName());
        if(index == -1)
            return false;
        //序列化写入文件
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("cuserInfo/User"+String.valueOf(index)+".ser"))) {
            out.writeObject(blackuser);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
    public boolean createAccount(String name,String password){
//        检查password是否合法：长度3-16，且包含大小写字母和数字
        if(password.length() < 3 || password.length() > 16)
            return false;//System.out.println("用户名或密码不合法");
        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]*$(?!.*[一-龥])";
        Pattern pattern = Pattern.compile(regex);
        if(!pattern.matcher(password).matches())
            return false;
//        检查Name是否已经存在了

        if(nameList.contains(name))
            return false;
        blackuser = new User(name,password);
        nameList.add(name);
        //创建完成，序列化写回
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("cuserInfo/namelist.ser"))) {
            out.writeObject(nameList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        logoutAccount();
        return true;
    }


    //用户信息的维护
    public boolean updateAcc(String name,String oldPassword, String password){
        if(!Objects.equals(blackuser.getPassword(), oldPassword)){
            return false;
        }
        String oldname = blackuser.getName();
        if(!Objects.equals(name, oldname) && nameList.contains(name))
            return false;
        if(password.length() < 3 || password.length() > 16)
            return false;//System.out.println("用户名或密码不合法");
        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]*$(?!.*[一-龥])";
        Pattern pattern = Pattern.compile(regex);
        if(!pattern.matcher(password).matches())
            return false;
        int lindex = nameList.indexOf(blackuser.getName());
        blackuser.setPassword(password);
        blackuser.setName(name);
        nameList.set(nameList.indexOf(oldname),name);
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("cuserInfo/namelist.ser"))) {
            out.writeObject(nameList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        logoutAccount();
        //存档改名字:
        int Nindex = nameList.indexOf(name);
        // 指定原文件路径
        File oldFile = new File("cuserInfo/User"+String.valueOf(lindex)+".ser");

        // 指定新文件名（包括路径，如果需要）
        File newFile = new File("cuserInfo/User"+String.valueOf(Nindex)+".ser");
        File oldFile2 = new File("cuserInfo/User_"+oldname+"List.ser");
        File newFile2 = new File("cuserInfo/User_"+name+"List.ser");

        // 检查原文件是否存在
        if (oldFile.exists()) {
            if (oldFile.renameTo(newFile)) {
                System.out.println("文件重命名成功！");
            } else {
                System.out.println("文件重命名失败！");
            }
        } else {
            System.out.println("文件不存在，无法重命名！");
        }
        if (oldFile2.exists()) {
            if (oldFile2.renameTo(newFile2)) {
                System.out.println("文件重命名成功！");
            } else {
                System.out.println("文件重命名失败！");
            }
        } else {
            System.out.println("文件不存在，无法重命名！");
        }


        return true;
    }
    //获取对战信息，以及更新对战信息
    public List<Integer> getChessCount(String chessName){
        int ii = -1;
        switch (chessName){
            case "围棋": ii=0;break;
            case "五子棋":ii=1;break;
            case "黑白棋": ii=2;break;
            default:break;
        }
        return blackuser.getChessCount(ii);
    }
    public void updateCount(String chessName){
        int ii = -1;
        switch (chessName) {
            case "围棋" -> ii = 0;
            case "五子棋" -> ii = 1;
            case "黑白棋" -> ii = 2;
            default -> {}
        }
        blackuser.updateWinCount(ii);
        blackuser.updateLoseCount(ii);

    }



    //测试
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        AccountManager mm = new AccountManager();

    }



}
