//
// Created by 陈广祥 on 2019-09-19.
//

#include <sstream>
#include "TxtPlugin.h"
#include "PlainTextFormat.h"
#include "TxtReader.h"
#include "TxtChapterDetector.h"

static const std::string TAG = "TxtPlugin";

TxtPlugin::TxtPlugin() : mTxtReaderPtr(nullptr) {
}

TxtPlugin::~TxtPlugin() {
    if (mTxtReaderPtr != nullptr) {
        delete mTxtReaderPtr;
        mTxtReaderPtr = nullptr;
    }
}

bool TxtPlugin::readEncodingInternal(std::string &outEncoding) {
    return detectEncoding(outEncoding);
}

bool TxtPlugin::readLanguageInternal(std::string &outLanguage) {
    return detectLanguage(outLanguage);
}

bool
TxtPlugin::readChaptersInternal(std::string &chapterPattern,
                                std::vector<TextChapter> &chapterList) {

    std::string encoding;
    bool result = readEncoding(encoding);

    // 如果文件地址为空，或者地址是目录
    if (mFilePtr == nullptr || mFilePtr->isDirectory()) {
        return false;
    }

    if (result) {
        // 创建章节探测器，进行章节探测。
        TxtChapterDetector(chapterPattern).detector(*mFilePtr, encoding, chapterList);
    }

    return result;
}

bool
TxtPlugin::readChapterContentInternal(TextChapter &inChapter, TextContent &outContent) {
    // TODO:对于文本独有的数据信息，如何进行缓存？就是解析 format 的情况，看下效率吧，如果效率特别高，不缓存也没问题(暂时不考虑)

    File chapterFile(inChapter.url);
    // 如果参数信息未初始化
    if (!mFormat.hasInitialized()) {
        // 调用探测器进行探测
        PlainTextDetector detector;
        detector.detect(chapterFile, mFormat);
    }

    // 如果无法探测到信息，直接 return
    if (!mFormat.hasInitialized()) {
        return false;
    }

    std::string bookEncoding;

    readEncoding(bookEncoding);

    // 创建文本阅读器
    if (mTxtReaderPtr == nullptr) {
        mTxtReaderPtr = new TxtReader(mFormat, bookEncoding);
    }
    // 读取内容
    return mTxtReaderPtr->readContent(inChapter, outContent);
}
