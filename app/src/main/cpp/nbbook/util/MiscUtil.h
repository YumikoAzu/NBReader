/*
 * Copyright (C) 2004-2015 FBReader.ORG Limited <contact@fbreader.org>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301, USA.
 */

#ifndef __MISCUTIL_H__
#define __MISCUTIL_H__

#include <string>
#include "../reader/text/tag/TextKind.h"

class MiscUtil {

private:
    MiscUtil();

public:
    static TextKind referenceType(const std::string &link);

    static std::string htmlDirectoryPrefix(const std::string &fileName);

    static std::string htmlFileName(const std::string &fileName);

    // 用于解决 url 浏览器编码的问题，如在浏览器中显示 https://www.xxx.com?wd=%05%47%27%44
    // 详见 https://www.w3school.com.cn/tags/html_ref_urlencode.html(不支持中文)
    static std::string decodeHtmlURL(const std::string &encodedURL);
};

#endif /* __MISCUTIL_H__ */
