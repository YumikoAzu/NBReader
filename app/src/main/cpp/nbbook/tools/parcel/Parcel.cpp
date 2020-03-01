// author : newbiechen
// date : 2020/3/1 7:37 PM
// description : 
//

#include "Parcel.h"

Parcel::Parcel(TextBufferAllocator *allocator) : mAllocator(allocator) {
}

void Parcel::writeInt16(int16_t value) {
    char *ptr = requestBuffer(2);
    *ptr++ = (0xff00 & value) >> 8;
    *ptr = 0xff & value;
}

void Parcel::writeInt32(int32_t value) {
    char *ptr = requestBuffer(4);
    *ptr++ = (0xff000000 & value) >> 24;
    *ptr++ = (0xff0000 & value) >> 16;
    *ptr++ = (0xff00 & value) >> 8;
    *ptr = 0xff & value;
}

void Parcel::writeString16(const std::string &value) {
    if (value.empty()) {
        return;
    }
    const size_t size = value.size();
    // 写入 String 长度
    writeInt16(size);
    // 写入 String 内容
    writeStringInternal(value);
}

void Parcel::writeString32(const std::string &value) {
    if (value.empty()) {
        return;
    }
    const size_t size = value.size();
    // 写入 String 长度
    writeInt32(size);
    // 写入 String 内容
    writeStringInternal(value);
}

void Parcel::writeStringInternal(const std::string &value) {
    size_t size = value.size();
    char *ptr = requestBuffer(size);
    memcpy(ptr, &value.front(), size);
}

void Parcel::writeString16Array(const std::vector<std::string> &valueArr) {
    size_t arrSize = valueArr.size();
    writeInt32(arrSize);
    for (auto itr = valueArr.begin(); itr != valueArr.end(); ++itr) {
        writeString16(*itr);
    }
}

void Parcel::writeString32Array(const std::vector<std::string> &valueArr) {
    size_t arrSize = valueArr.size();
    writeInt32(arrSize);
    for (auto itr = valueArr.begin(); itr != valueArr.end(); ++itr) {
        writeString32(*itr);
    }
}

char *Parcel::requestBuffer(size_t len) {
    return mAllocator->allocate(len);
}