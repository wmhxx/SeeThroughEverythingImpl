package com.wmhxx.tool.easyExcel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author :WangMengHe
 * @since :2021-10-28 11:20
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public
class MergeRange {
    public int startRow;
    public int endRow;
    public int startCell;
    public int endCell;
    public String lastValue;

    public MergeRange(String lastValue, int startRow, int endRow, int startCell, int endCell) {
        this.startRow = startRow;
        this.endRow = endRow;
        this.startCell = startCell;
        this.endCell = endCell;
        this.lastValue = lastValue;
    }
}
