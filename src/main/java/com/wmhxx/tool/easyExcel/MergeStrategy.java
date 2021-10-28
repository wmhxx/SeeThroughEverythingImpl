package com.wmhxx.tool.easyExcel;


import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.write.merge.AbstractMergeStrategy;
import org.apache.commons.collections4.map.HashedMap;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


/**
 * 合并策略
 *
 * @author WangMengHe
 * @date 2021/10/28
 */
public class MergeStrategy extends AbstractMergeStrategy {

    /**
     * 合并的列编号，从0开始，指定的index或自己按字段顺序数
     */
    private final Set<Integer> MERGE_CELL_INDEX = new HashSet<>();

    /**
     * 数据集大小，用于区别结束行位置
     */
    private final Integer MAX_ROW;

    /**
     * 合并策略
     *
     * @param maxRow         马克斯行
     * @param mergeCellIndex 合并单元格指数
     */
    public MergeStrategy(Integer maxRow, int... mergeCellIndex) {
        Arrays.stream(mergeCellIndex).forEach(this.MERGE_CELL_INDEX::add);
        this.MAX_ROW = maxRow;
    }


    /**
     * 记录上一次合并的信息
     */
    private final Map<Integer, MergeRange> LAST_ROW = new HashedMap<>();

    /**
     * 每行每列都会进入，绝对不要在这写循环
     *
     * @param sheet            表
     * @param cell             细胞
     * @param head             头
     * @param relativeRowIndex 相对的行索引
     */
    @Override
    protected void merge(Sheet sheet, Cell cell, Head head, Integer relativeRowIndex) {
        //返回此单元格的列索引
        int currentCellIndex = cell.getColumnIndex();
        // 判断该列是否需要合并
        if (MERGE_CELL_INDEX.contains(currentCellIndex)) {
            //单元格的值作为字符串
            String currentCellValue = cell.getStringCellValue();
            //包含此单元格的工作表中一行的从零开始的行索引
            int currentRowIndex = cell.getRowIndex();
            if (!LAST_ROW.containsKey(currentCellIndex)) {
                // 记录首行起始位置
                LAST_ROW.put(currentCellIndex, new MergeRange(currentCellValue, currentRowIndex, currentRowIndex, currentCellIndex, currentCellIndex));
                return;
            }
            //有上行这列的值了，拿来对比.
            MergeRange mergeRange = LAST_ROW.get(currentCellIndex);
            if (!(mergeRange.lastValue != null && mergeRange.lastValue.equals(currentCellValue))) {
                // 结束的位置触发下合并.
                // 同行同列不能合并，会抛异常
                if (mergeRange.startRow != mergeRange.endRow || mergeRange.startCell != mergeRange.endCell) {
                    sheet.addMergedRegionUnsafe(new CellRangeAddress(mergeRange.startRow, mergeRange.endRow, mergeRange.startCell, mergeRange.endCell));
                }
                // 更新当前列起始位置
                LAST_ROW.put(currentCellIndex, new MergeRange(currentCellValue, currentRowIndex, currentRowIndex, currentCellIndex, currentCellIndex));
            }
            // 合并行 + 1
            mergeRange.endRow += 1;
            // 结束的位置触发下最后一次没完成的合并
            if (relativeRowIndex.equals(MAX_ROW - 1)) {
                MergeRange lastMergeRange = LAST_ROW.get(currentCellIndex);
                // 同行同列不能合并，会抛异常
                if (lastMergeRange.startRow != lastMergeRange.endRow || lastMergeRange.startCell != lastMergeRange.endCell) {
                    sheet.addMergedRegionUnsafe(new CellRangeAddress(lastMergeRange.startRow, lastMergeRange.endRow, lastMergeRange.startCell, lastMergeRange.endCell));
                }
            }
        }
    }
}




