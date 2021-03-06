package com.newbiechen.nbreader.ui.component.book.text.entity.element

import com.newbiechen.nbreader.ui.component.book.text.entity.resource.image.TextImage
import com.newbiechen.nbreader.ui.component.book.text.entity.tag.TextStyleTag

/**
 *  author : newbiechen
 *  date : 2019-10-21 17:27
 *  description :文本元素
 */

open class TextElement {
    companion object {
        val HSpace = TextElement()
        // NON-BREAK-SPACE 表示该标志所在的字母组合(单词)，不允许换行，即单词要连在一行显示
        // wiki：不可中断空白连同前面一个、后面一个字符都在同一行，不会换行。
        val NBSpace: TextElement = TextElement()
        val AfterParagraph: TextElement = TextElement()
        val Indent: TextElement = TextElement()
        val StyleClose: TextElement = TextElement()
    }
}

data class TextStyleElement(val styleTag: TextStyleTag) : TextElement()

data class TextImageElement(val image: TextImage) : TextElement()

// TODO:占位标签
class TextHyperlinkControlElement() : TextElement()
