package Game.PlayGameSystem.ChessFactory;

import Game.PlayGameSystem.ChessFactory.gomoku.Gomoku;

public class GomokuFactory implements ChessFactory{
    @Override
    public Chess factory(int size){
        return new Gomoku(size);
    }
}
