package Model;

import Utils.CellType;

import javax.swing.table.AbstractTableModel;

public class PacmanTableModel extends AbstractTableModel {
    private GameModel gameModel;

    public PacmanTableModel(GameModel gameModel) {
        this.gameModel = gameModel;
    }

    @Override
    public int getRowCount() {
        return gameModel.getRows();
    }

    @Override
    public int getColumnCount() {
        return gameModel.getCols();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return gameModel.getCellType(rowIndex, columnIndex);
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return CellType.class;
    }
}
