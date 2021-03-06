// author : newbiechen
// date : 2019-12-30 13:37
// description : 
// todo：需要考虑缓冲区太大该怎么办？比如几十 M 的情况下，肯定会出问题。

#include "ParcelBuffer.h"


ParcelBuffer::ParcelBuffer(const size_t defaultBufferSize) : mDefaultBufferSize(
        defaultBufferSize) {
    mCurBlockSize = 0;
    mCurBlockOffset = 0;
    mBufferOffset = 0;
}

ParcelBuffer::~ParcelBuffer() {
    // 删除缓冲块的缓冲数据
    if (!mBufferBlockList.empty()) {
        for (auto block : mBufferBlockList) {
            delete block;
        }
    }
}

size_t ParcelBuffer::getBufferOffset() {
    size_t result = 0;

    for (auto block:mBufferBlockList) {
        result += block->position();
    }
    return result;
}


// TODO:使用 close 是否正确？
size_t ParcelBuffer::flush(char **buffer) {
    size_t bufferSize = getBufferOffset();

    // 缓冲区大小为 0 则不处理。
    if (bufferSize == 0) {
        return 0;
    }

    char *outBuffer = new char[bufferSize];
    char *curBuffer = outBuffer;

    // 循环复制缓冲块的中的内容
    for (auto block:mBufferBlockList) {
        std::memcpy(curBuffer, block->buffer(), block->position());
        curBuffer += block->position();
    }

    (*buffer) = outBuffer;
    return bufferSize;
}

char *ParcelBuffer::allocate(size_t size) {
    if (mBufferBlockList.empty()) {
        mCurBlockSize = std::max(mDefaultBufferSize, size);
        mBufferBlockList.push_back(new CharBuffer(mCurBlockSize));
    } else if (mCurBlockOffset + size > mCurBlockSize) { // 如果请求分配的大小，大于剩余缓冲。
        mCurBlockSize = std::max(mDefaultBufferSize, size);
        // 更新当前缓冲区信息
        mBufferBlockList.push_back(new CharBuffer(mCurBlockSize));
        // 更新缓冲信息
        mCurBlockOffset = 0;
    }

    CharBuffer *curBlock = mBufferBlockList.back();

    char *ptr = curBlock->buffer() + mCurBlockOffset;

    mCurBlockOffset += size;

    curBlock->position(mCurBlockOffset);
    return ptr;
}

char *ParcelBuffer::reallocateLast(char *ptr, size_t newSize) {
    // 作用：如果之前请求分配的区域不够的话，需要重新申请分配内存的意思。 ==> 传入的 ptr 必须是最新 allocate 返回的值。

    CharBuffer *curBlock = mBufferBlockList.back();
    const std::size_t oldOffset = ptr - curBlock->buffer();

    if (oldOffset + newSize <= mCurBlockSize) {
        mCurBlockOffset = oldOffset + newSize;
        curBlock->position(mCurBlockOffset);
        return ptr;
    } else {
        mCurBlockSize = std::max(mDefaultBufferSize, newSize);
        CharBuffer *block = new CharBuffer(mCurBlockSize);

        size_t diffSize = mCurBlockOffset - oldOffset;
        std::memcpy(block->buffer(), ptr, diffSize);
        curBlock->position(mCurBlockOffset - diffSize);

        // 添加新的缓冲区
        mBufferBlockList.push_back(block);
        mCurBlockOffset = newSize;

        block->position(mCurBlockOffset);
        return block->buffer();
    }
}