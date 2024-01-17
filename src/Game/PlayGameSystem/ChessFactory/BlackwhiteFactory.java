package Game.PlayGameSystem.ChessFactory;

import Game.PlayGameSystem.ChessFactory.blackwhite.BlackWhite;

public class BlackwhiteFactory implements ChessFactory{
    @Override
    public Chess factory(int size){
        return new BlackWhite(size);
    }
}
