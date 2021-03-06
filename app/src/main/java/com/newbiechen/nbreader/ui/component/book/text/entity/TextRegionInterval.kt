package com.newbiechen.nbreader.ui.component.book.text.entity

import com.newbiechen.nbreader.ui.component.book.text.entity.element.TextWordElement

/**
 *  author : newbiechen
 *  date : 2019-10-30 19:40
 *  description :文本区域区间
 */

/**
 * @param chapterIndex:章节索引
 * @param paragraphIndex:段落索引
 * @param startElementIndex:起始元素区域
 * @param endElementIndex:终止元素区域
 */
abstract class TextRegionInterval(
    val chapterIndex: Int,
    val paragraphIndex: Int,
    val startElementIndex: Int,
    val endElementIndex: Int
) : Comparable<TextRegionInterval> {

    /**
     * 区间是否包含该 Area
     */
    fun isContain(area: TextElementArea): Boolean {
        return compareTo(area) == 0
    }

    override fun equals(other: Any?): Boolean {
        if (other === this) {
            return true
        }
        if (other !is TextRegionInterval) {
            return false
        }

        val region = other as TextRegionInterval?
        return chapterIndex == region!!.chapterIndex &&
                paragraphIndex == region.paragraphIndex &&
                startElementIndex == region.startElementIndex &&
                endElementIndex == region.endElementIndex
    }

    // 判断是否是包含关系
    override operator fun compareTo(other: TextRegionInterval): Int {
        if (chapterIndex != other.chapterIndex) {
            return if (chapterIndex < other.chapterIndex) -1 else 1
        }

        if (paragraphIndex != other.paragraphIndex) {
            return if (paragraphIndex < other.paragraphIndex) -1 else 1
        }

        if (endElementIndex < other.startElementIndex) {
            return -1
        }
        //
        return if (startElementIndex > other.endElementIndex) {
            1
        } else 0
    }

    operator fun compareTo(area: TextElementArea): Int {
        // 如果不是在同一章节上
        if (chapterIndex != area.getChapterIndex()) {
            return if (chapterIndex < area.getChapterIndex()) -1 else 1
        }

        // 如果不是在同一段落上
        if (paragraphIndex != area.getParagraphIndex()) {
            return if (paragraphIndex < area.getParagraphIndex()) -1 else 1
        }

        // 如果结束的位置区域小于当前区域
        if (endElementIndex < area.getElementIndex()) {
            return -1
        }

        // 如果起始区域大于当前区域
        return if (startElementIndex > area.getElementIndex()) {
            1
        } else 0
    }

    operator fun compareTo(position: TextPosition): Int {
        val pci = position.getChapterIndex()
        if (chapterIndex != pci) {
            return if (chapterIndex < pci) -1 else 1
        }

        val ppi = position.getParagraphIndex()
        if (paragraphIndex != ppi) {
            return if (paragraphIndex < ppi) -1 else 1
        }

        val pei = position.getElementIndex()
        if (endElementIndex < pei) {
            return -1
        }

        return if (startElementIndex > pei) {
            1
        } else 0
    }
}

class TextWordRegionInterval(val wordElement: TextWordElement, position: TextPosition) :
    TextRegionInterval(
        position.getChapterIndex(),
        position.getParagraphIndex(),
        position.getElementIndex(),
        position.getElementIndex()
    )

