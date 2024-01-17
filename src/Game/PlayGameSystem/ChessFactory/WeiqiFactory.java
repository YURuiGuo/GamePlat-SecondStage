package Game.PlayGameSystem.ChessFactory;

import Game.PlayGameSystem.ChessFactory.weiqi.Weiqi;

public class WeiqiFactory implements ChessFactory{
    @Override
    public Chess factory(int size){
        return new Weiqi(size);
    }
}
