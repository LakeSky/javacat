/**
 * @description base.js 是一个公用组件
 */
var GLOBAL = {};

/**
 * @description add switch to log-info ,
 *              set to true to turn on "GLOBAL.debug()" output
 * @type {Boolean} default is false
 */
GLOBAL.isDebug = false;

/**
 * @description 建立命名空间
 * @example GLOBAL.namespace( 'GLOBAL.John' );
 *
 * @param {String} str
 */
GLOBAL.namespace = function (str) {
    "use strict";
    var arr = str.split("."), o = GLOBAL;
    for (var i = (arr[0] == "GLOBAL") ? 1 : 0; i < arr.length; i++) {
        o[arr[i]] = o[arr[i]] || {};
        o = o[arr[i]];
    }
};

// Dom 相关
GLOBAL.Dom = {};

/**
 * 查找并获得下一个html节点
 * @param {String|Object} node node or node name
 * @returns {Object} next node
 *
 */
GLOBAL.Dom.getNextNode = function (node) {
    "use strict";
    node = typeof node == "string" ? document.getElementById(node) : node;
    var nextNode = node.nextSibling;
    if (!nextNode)return null;
    // document.all is a property IE supported
    if (!document.all) {
        while (true) {
            if (nextNode.nodeType == 1) {
                break;
            } else {
                if (nextNode.nextSibling) {
                    nextNode = nextNode.nextSibling;
                } else {
                    break;
                }
            }
        }
    }
    return nextNode;
};

/**
 * 设置透明
 * @param {String} 节点或节点名
 * @param {Num} level 0~100
 */
GLOBAL.Dom.setOpacity = function (node, level) {
    "use strict";
    node = typeof node == "stirng" ? document.getElementById(node) : node;
    if (document.all) {
        node.style.filter = 'alpha(opacity=' + level + ')';
    } else {
        node.style.opacity = level / 100;
    }
};

GLOBAL.Dom.get = function (id) {
    "use strict";
    return document.getElementById(id);
};

/**
 * 通过class name 获得元素
 * @param {String} str class name
 * @param {Object} root
 * @param {String} tag
 * @returns {Array}
 */
GLOBAL.Dom.getElementsByClassName = function (str, root, tag) {
    "use strict";
    if (root) {
        root = (typeof root == "string") ? document.getElementById(root) : root;
    } else {
        root = document.body;
    }
    tag = tag || "*";
    var els = root.getElementsByTagName(tag), arr = [];
    for (var i = 0, n = els.length; i < n; i++) {
        for (var j = 0, k = els[i].className.split(" "), l = k.length; j < l; j++) {
            if (k[j] == str) {
                arr.push(els[i]);
                break;
            }
        }
    }
    return arr;
};

// Event 相关
GLOBAL.Event = {};
/**
 * @description 获得事件对象, 为了兼容 IE 和 Firefox 而提供的一个函数
 * @param {Object} e
 * @returns {Event}
 */
GLOBAL.Event.getEvent = function (e) {
    "use strict";
    return window.event || e;
};
/**
 * @description 获得事件源对象
 * @param {Event} e
 * @returns {Object}
 */
GLOBAL.Event.getEventTarget = function (e) {
    "use strict";
    e = window.event || e;
    return e.srcElement || e.target;
};
/**
 * @description 停止事件冒泡
 * @param {Event} e
 */
GLOBAL.Event.stopPropagation = function (e) {
    "use strict";
    e = window.event || e;
    if (document.all) {
        e.cancelBubble = true;
    } else {
        e.stopPropagation();
    }
};

/**
 * 绑定事件
 * @param {Object|String} node
 * @param {String} eventType
 * @param {Function} handler
 */
GLOBAL.Event.on = function (node, eventType, handler) {
    "use strict";
    node = typeof node == "string" ? document.getElementById(node) : node;
    if (document.all) {
        node.attachEvent("on" + eventType, handler);
    } else {
        node.addEventListener(eventType, handler, false);
    }
};


// interactive 相关
GLOBAL.Interactive = {
    _keyMap: {
        "8": "backspace", "9": "tab", "13": "enter", "16": "shift",
        "17": "ctrl", "18": "alt", "27": "esc", "32": "space",
        "37": "left", "38": "up", "39": "right", "40": "down",
        "46": "delete"
    }
};

/**
 * @description 从键盘事件对象取得 keyCode
 * @param {KeyboardEvent} e
 * @returns {Number}
 */
GLOBAL.Interactive.keyCodeFromEvent = function (e) {
    "use strict";
    return e.which || e.keyCode;
};

/**
 * @description 从键盘事件对象获得 keyName
 * @param {KeyboardEvent} e
 * @returns {String}
 */
GLOBAL.Interactive.keyNameFromEvent = function (e) {
    "use strict";
    var foo = GLOBAL.Interactive;
    if (!foo.keyCodeFromEvent(e)) {
        return "other";
    }
    return foo._keyMap[foo.keyCodeFromEvent(e).toString()] || "other";
};

/**
 * @description 从keyCode 获得 keyName
 * @param keyCode
 * @returns {String}
 */
GLOBAL.Interactive.keyNameFromKeyCode = function (keyCode) {
    "use strict";
    var foo = GLOBAL.Interactive;
    if(keyCode){
        return foo._keyMap[keyCode.toString()] || "other";
    }else{
        return "other";
    }

};

/**
 * @description 比较键盘事件中 keyCode 和 targetCode 是否相等
 * @param {KeyboardEvent} e
 * @param {Number|String} targetCode
 * @returns {Boolean}
 */
GLOBAL.Interactive.isKeyCode = function (e, targetCode) {
    "use strict";
    return GLOBAL.Interactive.keyCodeFromEvent(e).toString() === targetCode.toString();
};

/**
 * @description 比较键盘事件中 keyName 和 targetName 是否相等
 * @param {KeyboardEvent} e
 * @param {String} targetName
 * @returns {Boolean}
 */
GLOBAL.Interactive.isKeyName = function (e, targetName) {
    "use strict";
    return GLOBAL.Interactive.keyNameFromEvent(e) === targetName.toString();
};


/**
 * @description 获取浏览器的长和宽
 * @example var scrollWidth = (parseFloat(GLOBAL.Interactive.W.getWidth())-220)/2;
 *          var scrollHeight = (parseFloat(GLOBAL.Interactive.W.getHeight())-140)/2;
 *          假设有一个浮动框 B 他的长和宽分别是220,140 即 {width:220, height:140}, 那么要让 B 位于浏览器上下居中，scrollWidth 和 scrollHeight 就是位置
 * @type {String} 例如："618px"
 */
GLOBAL.Interactive.W = {
    getWidth: function (type) {
        "use strict";
        var a, b, retVal;
        if ($.browser.msie && $.browser.version >= 8) {
            a = Math.max(document.documentElement.scrollWidth, document.body.scrollWidth);
            b = Math.max(document.documentElement.offsetWidth, document.body.offsetWidth);
            if (a < b) {
                retVal = $(window).width();
            } else {
                retVal = a;
            }
        } else {
            retVal = $(document).width();
        }
        return type && type == "number" ? retVal : retVal + "px";
    },
    getHeight: function (type) {
        "use strict";
        var b, a, retVal;
        if ($.browser.msie && $.browser.version >= 8) {
            b = Math.max(document.documentElement.scrollHeight, document.body.scrollHeight);
            a = Math.max(document.documentElement.offsetHeight, document.body.offsetHeight);
            if (b < a) {
                retVal = (window).height();
            } else {
                retVal = b;
            }
        } else {
            retVal = $(document).height();
        }
        return type && type == "number" ? retVal : retVal + "px";
    }
};

/**
 * @description textArea html组件的鼠标焦点位置获取
 * @param textarea
 */
GLOBAL.Interactive.getCursorPosition = function (textarea) {
    "use strict";
    var rangeData = {text: "", start: 0, end: 0 };
    textarea.focus();
    if (textarea.setSelectionRange) { // W3C
        rangeData.start = textarea.selectionStart;
        rangeData.end = textarea.selectionEnd;
        rangeData.text = (rangeData.start != rangeData.end) ? textarea.value.substring(rangeData.start, rangeData.end) : "";
    } else if (document.selection) { // IE
        var i,
            oS = document.selection.createRange(),
        // Don't: oR = textarea.createTextRange()
            oR = document.body.createTextRange();
        oR.moveToElementText(textarea);

        rangeData.text = oS.text;
        rangeData.bookmark = oS.getBookmark();

        // object.moveStart(sUnit [, iCount])
        // Return Value: Integer that returns the number of units moved.
        for (i = 0; oR.compareEndPoints('StartToStart', oS) < 0 && oS.moveStart("character", -1) !== 0; i++) {
            // Why? You can alert(textarea.value.length)
            if (textarea.value.charAt(i) == '\n') {
                i++;
            }
        }
        rangeData.start = i;
        rangeData.end = rangeData.text.length + rangeData.start;
    }

    return rangeData;
};

/**
 * @description textArea html 组件的鼠标焦点设置
 * @param textarea
 * @param rangeData
 */
GLOBAL.Interactive.setCursorPosition = function (textarea, rangeData) {
    "use strict";
    if (!rangeData) {
        GLOBAL.warning("You must get cursor position first.")
        return;
    }

    if (textarea.setSelectionRange) {
        // W3C
        textarea.focus();
        textarea.setSelectionRange(rangeData.start, rangeData.end);
    } else if (textarea.createTextRange) {
        // IE
        var oR = textarea.createTextRange();
        // Fixbug :
        // In IE, if cursor position at the end of textarea, the setCursorPosition function don't work
        if (textarea.value.length === rangeData.start) {
            oR.collapse(false)
            oR.select();
        } else {
            oR.moveToBookmark(rangeData.bookmark);
            oR.select();
        }
    }
};

// Lang 相关
GLOBAL.Lang = {};

/**
 * @description 滤除字符串两边的空格
 * @param {String} ostr
 * @returns {*}
 */
GLOBAL.Lang.trim = function (ostr) {
    "use strict";
    return ostr ? ostr.replace(/^\s+|\s+$/g, "") : ostr;
};

/**
 * @description 是 Number 吗？
 * @param {*} s
 * @returns {Boolean}
 */
GLOBAL.Lang.isNumber = function (s, isStrict) {
    "use strict";
    var foo = GLOBAL.Lang;
    return  ( foo._isNumberPrototype(s) || (!isStrict && foo.isString(s)) )
        && (!G.isEmpty(s) && !isNaN(s));
};

GLOBAL.Lang._isNumberPrototype = function (s) {
    "use strict";
    return Object.prototype.toString.call(s) === '[object Number]';
};

/**
 * @description 是 Array 类型吗？
 * @param {*} s
 * @return {Boolean}
 */
GLOBAL.Lang.isArray = function (s) {
    "use strict";
    return !!(s &&
        typeof s === 'object' &&
        typeof s.length === 'number' &&
        !s.propertyIsEnumerable('length'));

//    return Object.prototype.toString.call(s) === '[object Array]';
};

/**
 * @description 是 String 类型吗？
 * @param {*} s
 * @return {Boolean}
 */
GLOBAL.Lang.isString = function (s) {
    "use strict";
    return Object.prototype.toString.call(s) === '[object String]';
};

/**
 * @description 是 undefined 吗？
 * @param {*} s
 * @return {Boolean}
 */
GLOBAL.Lang.isUndefined = function (s) {
    "use strict";
    return Object.prototype.toString.call(s) === '[object Undefined]' || s === undefined;
};

/**
 * @description 是 null 吗？
 * @param {*} s
 * @return {Boolean}
 */
GLOBAL.Lang.isNull = function (s) {
    "use strict";
    return Object.prototype.toString.call(s) === '[object Null]' || s === null;
};

/**
 * @description 是否为空, 空的定义：null, "null", undefined, "undefined", "", "   ", [], {}
 * @param {*} str
 * @returns {Boolean}
 */
GLOBAL.Lang.isEmpty = function (o, emptyExtended) {
    "use strict";
    var foo = GLOBAL.Lang,
        isEmpty = foo.isNull(o)
            || foo.isUndefined(o)
            || foo.isString(o) && foo.trim(o) === ""
            || foo.isString(o) && (foo.trim(o) === "null" || foo.trim(o) === "undefined")
            || foo.isArray(o) && o.length === 0
            || foo.isObject(o) && foo.getPropertiesCount(o) === 0,
        emptyDefined = [];

    return isEmpty || G.contains(o, emptyDefined.concat(emptyExtended));
};

/**
 * @description 是否为空, 空的定义：null, "null", undefined, "undefined", "", "   ", [], {}
 * @param {*} str
 * @returns {Boolean}
 */
GLOBAL.Lang.isNotEmpty = function (o, emptyExtended) {
    return !GLOBAL.Lang.isEmpty(o, emptyExtended);
};

/**
 * @description 是 Boolean 类型吗？
 * @param {*} s
 * @return {Boolean}
 */
GLOBAL.Lang.isBoolean = function (s) {
    "use strict";
    return Object.prototype.toString.call(s) === '[object Boolean]';
};

GLOBAL.Lang.isObject = function (s) {
    "use strict";
    return Object.prototype.toString.call(s) === '[object Object]';
};

/**
 * @description 判断字符串是否为 空字符
 * @param s
 */
GLOBAL.Lang.isStringEmpty = function (s) {
    "use strict";
    return GLOBAL.Lang.isString(s) && s === "";
};

/**
 * @description 判断字符串是否包含实际的值
 * @param s
 */
GLOBAL.Lang.isStringHasRealValue = function (s) {
    "use strict";
    var foo = GLOBAL.Lang;
    return !foo.isUndefined(s) && !foo.isNull(s) && !foo.isStringEmpty(s);
};

/**
 * @description 将字符串中的单引号与双引号加转义符
 * @param {String} s
 */
GLOBAL.Lang.addSlash = function (s) {
    "use strict";
    return s.replace(/\"/g, '\\"').replace(/\'/g, "\\'");
};

/**
 * @description 继承类
 * @param {Object} subClass
 * @param {Object} superClass
 */
GLOBAL.Lang.extend = function (subClass, superClass) {
    "use strict";
    var F = function () {
    };
    F.prototype = superClass.prototype;
    subClass.prototype = new F();
    subClass.prototype.constructor = subClass;
    subClass.superclass = SUPERCLASS.prototype;
    if (superClass.prototype.constructor == Object.prototype.constructor) {
        superClass.prototype.constructor = superClass;
    }
};

/**
 * @description 查找字符串是否以 目标字符串开始
 * @param sourceStr
 * @param str
 * @return {Boolean}
 */
GLOBAL.Lang.startWith = function (sourceStr, str) {
    "use strict";
    if(typeof str !== "string" || typeof sourceStr !== "string") {
        return false;
    }
    return sourceStr.indexOf(str) === 0;
};

/**
 * @description 查找字符串是否以 目标字符串结束
 * @param sourceStr
 * @param str
 * @return {Boolean}
 */
GLOBAL.Lang.endWith = function (sourceStr, str) {
    "use strict";
    if(typeof str !== "string" || typeof sourceStr !== "string") {
        return false;
    }

    var index = sourceStr.lastIndexOf(str);

    if(index === -1) {
        return false;
    } else {
        var orginalStr = sourceStr.substring(index);
        return orginalStr === str;
    }
};

/**
 * @description 判断, scope 中是否包含 o 元素
 * @param {*} o 此变量的值范围在 -- >  Number | NaN | String completer| Boolean | null | undefined
 * @param {Array} scope
 * @returns {Boolean}
 */
GLOBAL.Lang.contains = function (o, scope) {
    "use strict";
    for (var i = 0, len = scope.length; i < len; i++) {
        if (o === scope[i]
            || GLOBAL.Lang._isNumberPrototype(o) && isNaN(o))
            return true
    }
    return false;
};

// 尽量少扩展原有String 对象，为了避免冲突。
// String Class Extends
String.prototype.startWith = function (str) {
    "use strict";
    var foo = GLOBAL.Lang;
    return foo.startWith(this.toString(), str);
};

String.prototype.endWith = function (str) {
    "use strict";
    var foo = GLOBAL.Lang;
    return foo.endWith(this.toString(), str);
};

/**
 * @description 获取一个对象中的属性个数
 * @params {Object} o
 * @returns {Number} 属性个数
 */
GLOBAL.Lang.getPropertiesCount = function (o) {
    "use strict";
    var count = 0;
    for (var i in o) {
        if (o.hasOwnProperty(i)) {
            count++;
        }
    }
    return count;
};

/**
 * @description 遍历对象中的属性， 并且合并成字符串输出 , 主要用于 debug
 * @param obj
 * @return {String}
 */
GLOBAL.Lang.displayPro = function (obj) {
    "use strict";
    var names = "";
    for (var name in obj) {
        if (GLOBAL.Lang.isObject(obj[name])) {
            names += GLOBAL.Lang.displayPro(obj[name]);
        }
        else {
            names += name + ": " + obj[name] + ", ";
        }
    }
    return names;
};

/**
 * @description 字符串归一化函数,  作用为： 若传入的参数是"空", 如: null, "null", undefined, "undefined"， 则都归一化成字符串 ""
 * @param s
 * @param {String} 若要自定义 "空" 归一化后的字符串， 请传入你的标准
 * @param {Array of String} 若要扩展 "空" 的定义， 请传入参数
 * @return {String}
 */
GLOBAL.Lang.normalize = function (s, std, nullExtended) {
    "use strict";
    var nullDefined = [null, undefined, "null", "undefined"].concat(nullExtended);
    return G.Lang.contains(s, nullDefined) ? (std || "") : s;
};

/**
 * 比较两个字符串是否相同，"",NULL,undefined 当相同处理
 * @param str1
 * @param str2
 * @returns {Boolean} 相同的时候return true
 */
GLOBAL.Lang.compareStrSame = function (str1, str2) {

    if (G.Lang.isEmpty(str1) && G.Lang.isEmpty(str2)) {
        return true;
    } else if (G.Lang.isEmpty(str1) && !G.Lang.isEmpty(str2) || !G.Lang.isEmpty(str1) && G.Lang.isEmpty(str2)) {
        return false;
    } else {
        return str1 === str2;
    }
};

/**
 ["a","b","c"],",","--"
 组装成a,b,c
 */
GLOBAL.Lang.generateString = function (array, splitStr, emptyExtended) {
    var resultStr = "";
    var foo = GLOBAL.Lang;
    if (foo.isNotEmpty(array)) {
        for (var i = 0; i < array.length; i++) {
            if (foo.isNotEmpty(array[i])) {
                if (splitStr && foo.isNotEmpty(resultStr)) {
                    resultStr += splitStr;
                }
                resultStr += array[i];
            }
        }
    }
    if (foo.isNotEmpty(emptyExtended) && foo.isEmpty(resultStr)) {
        resultStr += emptyExtended;
    }
    return resultStr;
};

// Number Package
GLOBAL.Number = {};

/**
 * @description 比较 number 是否在 min 和 max 之间,
 *              mode 代表区间定义： "[]" -- 闭闭， "()" -- 开开, "[)" -- 闭开, "(]" -- 开闭
 * @param number
 * @param min
 * @param max
 * @param mode
 * @return {*}
 */
GLOBAL.Number.between = function (number, min, max, mode) {
    "use strict";
    if (!GLOBAL.Lang.isNumber(number, true)
        || !GLOBAL.Lang.isNumber(min, true)
        || !GLOBAL.Lang.isNumber(max, true)
        || (mode && mode.search(/[\[\(][\]\)]/g) === -1)) {
        throw new Error("Parameter Error");
        return false;
    }

    // mode state : [], (), (], [)
    if (GLOBAL.Lang.isUndefined(mode)) mode = "[]";

    var compare = {
        greater: function (number, base, mode) {
            return mode === "[" ? (number >= base) : (number > base);
        },

        less: function (number, base, mode) {
            return mode === "]" ? (number <= base) : (number < base)
        }
    };
    return compare.greater(number, min, mode.charAt(0)) && compare.less(number, max, mode.charAt(1));
};

/**
 * @description 查看一个数 number 是否在 min 和 max 之间？ 如果在，返回原值；不在，返回min 或 max
 * @param number
 * @param min
 * @param max
 * @returns {Number}
 */
GLOBAL.Number.constrain = function (number, min, max) {
    "use strict";
    return Math.min(Math.max(number, min), max);
};
Number.prototype.constrain = function (min, max) {
    "use strict";
    return GLOBAL.Number.constrain(this, min, max);
};

GLOBAL.Number.equalsTo = function (numA, numB, decimals) {
    "use strict";
    var regNumber = /^[-+]?\d+$|^[-+]?\d*\.\d+$/g,
        lang = GLOBAL.Lang;
    if (lang.isNumber(numA) === false && !(lang.isString(numA) && numA.search(regNumber) === -1 )
        || lang.isNumber(numB) === false && !(lang.isString(numB) && numB.search(regNumber) === -1)) {
        throw new Error("arguments[0] or arguments[1] error! It must be a \"Number\"!");
    }

    var a = parseFloat(numA),
        b = parseFloat(numB),
        d = lang.isNumber(decimals) && decimals >= 0 ? decimals : 5; // default to 5
    return Math.abs(a - b) < Math.pow(0.1, d);
};

/**
 * @description 归一化数字， 如果输入是 null, undefined, "" 等非法输入都输出0， 其余功能同 parseFloat
 * @param a
 * @return {Number}
 */
GLOBAL.Number.normalize = function (a) {
    "use strict";
    return parseFloat("0" + a);
};

/**
 * 四舍五入保留len位小数，同时过滤num末尾的0
 * @param num
 * @param len 默认值为2
 * @returns {number}
 */
GLOBAL.Number.filterZero = function (num, len) {
    if (isNaN(num)) {
        return 0;
    } else {
        var target = new Number(num).toFixed(len == null ? 2 : len);
        var result = Number(target);
        var exec = /0*$/.exec(target);
        if (exec != null) {
            result = Number(target.substring(0, exec.index));
        }
        return result
    }
}
/**
 * 四舍五入保留len位小数
 * @param num
 * @param len 默认值为2
 * @returns {number}
 */
GLOBAL.Number.round = function (num, len) {
    if (isNaN(num)) {
        return 0;
    } else {
        return Number(Number(num).toFixed(len == null ? 2 : len));
    }
};

GLOBAL.Number.format = function (num, len) {
    if (isNaN(num)) {
        return 0;
    } else {
        return Number(num).toFixed(len == null ? 2 : len);
    }
};

GLOBAL.namespace("GLOBAL.Math");

/**
 * @description 对数组求和， 必须保证数组的每个元素都是数字， 或者为字符数字
 */
GLOBAL.Math.summation = function () {
    "use strict";
    var retVal = 0,
        p = arguments[0],
        t = 0;
    if (arguments.length === 1 && GLOBAL.Lang.isArray(p)) {
        for (var i = 0, len = p.length; i < len; i++) {
            t = parseFloat(p[i]);

            if (GLOBAL.Lang.isNumber(t)) {
                retVal += t;
            } else {
                throw new Error("function arguments Error!");
                break;
            }
        }
        return retVal;
    } else {
        throw new Error("function arguments Error!");
    }
};


// 大数运算库
GLOBAL.namespace("GLOBAL.Big");


// Collection related
GLOBAL.Collection = {};
GLOBAL.Collection.Comparator = {
    equalTo: function (sourceArr, targetArr) {
        "use strict";
        var foo = GLOBAL.Lang;
        if (foo.isArray(sourceArr) && foo.isArray(targetArr) && sourceArr.length == targetArr.length) {
            for (var i = 0, len = sourceArr.length; i < len; i++) {
                if (sourceArr[i] !== targetArr[i])
                    return false;
            }
            return true;
        }
        return false;
    }
};

// 查看数组中是否有某个值， 使用 jQuery 中的 inArray


/**
 * @description 对于指定 node 增加 class
 * @param {String|Number} node node 或者 node 名
 * @param {String} str
 */
GLOBAL.Dom.addClass = function (node, str) {
    "use strict";
    if (!new RegExp("(^|\\s+)" + str).test(node.className)) {
        node.className = node.className + " " + str;
    }
};

/**
 * @description 从指定的 node 移除指定 class
 * @param {String|Number} node 或者 node 名
 * @param {String} str
 */
GLOBAL.Dom.removeClass = function (node, str) {
    "use strict";
    node.className = node.className.replace(new RegExp("(^|\\s+)" + str), "");
};


// Util 相关
GLOBAL.Util = {};

/**
 * @description 生成 UUID(GUID)
 * @returns {String} UUID(GUID)
 */
GLOBAL.Util.generateUUID = (typeof(window.crypto) != 'undefined' &&
    typeof(window.crypto.getRandomValues) != 'undefined') ?
    function () {
        "use strict";
        // 如果我们有一个加密安全的伪随机数发生器, 参考文章: http://statmath.wu.ac.at/prng/
        var buf = new Uint16Array(8);
        // cryptographically secure PRNG(Pseudo-Random Number Generator), use this
        window.crypto.getRandomValues(buf);
        var S4 = function (num) {
            var ret = num.toString(16);
            while (ret.length < 4) {
                ret = "0" + ret;
            }
            return ret;
        };
        return (S4(buf[0]) + S4(buf[1]) + "-" + S4(buf[2]) + "-" + S4(buf[3]) + "-" + S4(buf[4]) + "-" + S4(buf[5]) + S4(buf[6]) + S4(buf[7]));
    }

    :

    function () {
        "use strict";
        // 否则，我们使用 Math.random() 来生成伪随机数
        return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
            var r = Math.random() * 16 | 0, v = c == 'x' ? r : (r & 0x3 | 0x8);
            return v.toString(16);
        });
    };

/**
 * @description 得到URL中param值
 * @param {String} name
 */
GLOBAL.Util.getUrlParameter = function (name) {
    "use strict";
    return decodeURIComponent((new RegExp('[?|&]' + name + '=' + '([^&;]+?)(&|#|;|$)').exec(location.search) || [, ""])[1].replace(/\+/g, '%20')) || "";
};

/**
 * 动态加载Js/Css文件
 * @param fileName
 * @param fileType  可为 js 或 css
 */
GLOBAL.Util.loadJsCssFile = function (fileName, fileType) {
    "use strict";
    if (document.createStyleSheet) {
        document.createStyleSheet(fileName);
        return;
    }
    if (fileType == "js") {
        var fileref = document.createElement('script');
        fileref.setAttribute("type", "text/javascript");
        fileref.setAttribute("src", fileName);
    }
    else if (fileType == "css") {
        var fileref = document.createElement("link");
        fileref.setAttribute("rel", "stylesheet");
        fileref.setAttribute("type", "text/css");
        fileref.setAttribute("href", fileName);
    }
    if (typeof fileref != "undefined")
        document.getElementsByTagName("head")[0].appendChild(fileref);
};

/**
 * 动态移除Js或Css文件
 * @param fileName
 * @param fileType 可为 js 或 css
 */
GLOBAL.Util.removeJsCssFile = function (fileName, fileType) {
    "use strict";
    var targetelement = (fileType == "js") ? "script" : (fileType == "css") ? "link" : "none";
    var targetattr = (fileType == "js") ? "src" : (fileType == "css") ? "href" : "none";
    var allsuspects = document.getElementsByTagName(targetelement);
    for (var i = allsuspects.length; i >= 0; i--) {
        if (allsuspects[i] && allsuspects[i].getAttribute(targetattr) != null && allsuspects[i].getAttribute(targetattr).indexOf(fileName) != -1)
            allsuspects[i].parentNode.removeChild(allsuspects[i]);
    }
};

GLOBAL.namespace("Array");
GLOBAL.Array.indexOf = function (arr, item) {
    "use strict";
    for (var i = 0, len = arr.length; i < len; i++) {
        if (arr[i] === item) {
            return i;
        }
    }
    return -1;
};

GLOBAL.Array.lastIndexOf = function (arr, item) {
    "use strict";
    for (var len = arr.length, i = len - 1; i >= 0; i--) {
        if (arr[i] === item) {
            return i
        }
    }
    return -1;
};

/**
 * 通过 index 删除 数组某个元素
 * @param arr 必须是索引数组
 * @param index
 * @returns {Array}
 */
GLOBAL.Array.remove = function(arr, index) {
    "use strict";
    var retArr = [];
    for (var i = 0, len = arr.length; i < len; i++) {
        if (-1 === index) {
            break;
        }

        if (i === index) {
            continue;
        }

        retArr.push(arr[i]);
    }

    return retArr;
};


/*
 * @description 把字符串转换为日期对象, 精确到天
 *              注：IE下不支持直接实例化日期对象，如new Date("2011-04-06"), 这个方法为解决此问题而存在
 * @param {String} yyyy-mm-dd或dd/mm/yyyy或 yyyy-MM-dd HH:mm 形式的字符串
 * @return {Date}
 */
GLOBAL.Util.getDate = function (date) {
    "use strict";
    date = $.trim(date);
    var formatPattern = {
            "yyyy-mm-dd": /\d{4}-\d{1,2}-\d{1,2}$/g,
            "yyyy-mm-dd HH:mm": /\d{4}-\d{1,2}-\d{1,2}\s\d{1,2}:\d{1,2}$/g,
            "yyyy年mm月dd日 HH时mm分": /\d{4}年\d{1,2}月\d{1,2}日\s\d{1,2}时\d{1,2}分$/g,
            "dd/mm/yyyy": /\d{1,2}\/\d{1,2}\/\d{4}$/g
        },
        format = "",
        arr,
        newDate = new Date();

    for (var k in formatPattern) {
        if (formatPattern[k].test(date)) {
            format = k;
            break;
        }
    }

    // if now match , return null
    if (format === "")
        return null;

    if (format === "yyyy-mm-dd HH:mm") {
        date = date.split(/\s/)[0];
    }
    arr = date.split(/\/|-/g);
    if (format === "dd/mm/yyyy") {
        arr.reverse();
    }
    newDate.setFullYear(arr[0], arr[1] - 1, arr[2]);
    newDate.setHours(0, 0, 0);
    return newDate;
};

/*
 * @description 把字符串转换为日期对象, 精确到分
 * @param {String} yyyy-mm-dd或dd/mm/yyyy或 yyyy-MM-dd HH:mm 形式的字符串
 * @return {Date}
 */
GLOBAL.Util.getExactDate = function (date) {
    "use strict";
    date = $.trim(date);
    var formatPattern = {
            "yyyy-mm-dd": /\d{4}-\d{1,2}-\d{1,2}$/g,
            "yyyy-mm-dd HH:mm": /\d{4}-\d{1,2}-\d{1,2}\s\d{1,2}:\d{1,2}$/g,
            "dd/mm/yyyy": /\d{1,2}\/\d{1,2}\/\d{4}$/g
        },
        format = "",
        arr,
        newDate = new Date();

    for (var k in formatPattern) {
        if (formatPattern[k].test(date)) {
            format = k;
            break;
        }
    }

    // if now match , return null
    if (format === "")
        return null;

    var hour = 0, min = 0;
    if (format === "yyyy-mm-dd HH:mm") {
        var houmin = date.split(/\s/)[1];
        date = date.split(/\s/)[0];
        hour = houmin.split(/:/)[0];
        min = houmin.split(/:/)[1];
    }
    arr = date.split(/\/|-/g);
    if (format === "dd/mm/yyyy") {
        arr.reverse();
    }
    newDate.setFullYear(arr[0], arr[1] - 1, arr[2]);
    newDate.setHours(hour, min, 0);
    return newDate;
};


// Cookie 相关
GLOBAL.Cookie = {
    /**
     * @description 通过键值名读取 Cookie
     * @param {String} name
     * @returns {String}
     */
    read: function (name) {
        "use strict";
        var cookieStr = "; " + document.cookie + "; ";
        var index = cookieStr.indexOf("; " + name + "=");
        if (index != -1) {
            var s = cookieStr.substring(index + name.length + 3, cookieStr.length);
            return unescape(s.substring(0, s.indexOf("; ")));
        } else {
            return null;
        }
    },

    /**
     * @description 通过键值名设置 Cookie
     * @param {String} name
     * @param {String} value
     * @param {Number} expires 单位是 (天)
     */
    set: function (foo, value, expires) {
        "use strict";
        var expDays = expires * 24 * 60 * 60 * 1000;
        var expDate = new Date();
        expDate.setTime(expDate.getTime() + expDays);
        var expString = expires ? "; expires=" + expDate.toGMTString() : "";
        document.cookie = foo + "=" + escape(value) + expString;
    },

    /**
     * @description 通过键值名删除 Cookie
     * @param {String} name
     */
    del: function (name) {
        "use strict";
        var exp = new Date(new Date().getTime() - 1);
        var s = this.read(name);
        if (s != null) {
            document.cookie = name + "=" + s + ";expires=" + exp.toGMTString() + ";path=/";
        }
    }
};


// Display related
GLOBAL.Display = {
    // get x-axis position (px)
    getX: function (element) {
        "use strict";
        var x = 0;
        while (element) {
            x = x + element.offsetLeft;
            element = element.offsetParent;
        }
        return x;
    },

    // get y-axis position (px)
    getY: function (element) {
        "use strict";
        var y = 0;
        while (element) {
            y = y + element.offsetTop;
            element = element.offsetParent;
        }
        return y;
    }
};

/**
 * @description 有时候我们需要判断当前页面是否处于iframe中，这个问题看起来简单，但其实有很多潜在的问题。下面我们就来看看几种常见的方法以及它们的问题。
 *              常见方法：
 *              方法一：
 *  if(top!=this){
 *      //在iframe中
 *  }
 *              问题：当top被重置（参见：top和parent的重置）时，则无法通过此方法来判断。
 *              方法二：
 *  if(self.frameElement.tagName=="IFRAME"){
 *      //在iframe中
 *  }
 *              问题：若当前页面与上层页面之间跨域时，则无法通过此方法来判断。
 *              方法三：
 *  var isInIframe = (window.location != window.parent.location) ? true : false;
 *              问题：当parent被重置（参见：top和parent的重置）时，则无法通过此方法来判断。
 *
 *  新方法从另外一个角度来检测：
 *      优点是解决了上面其它方法所不能解决的问题；
 *      缺点是过程中进行了DOM操作，因此不适用于高频度的调用。
 */
GLOBAL.Display.isInIframe = function () {
    "use strict";
    var tmp_iframe = document.body.appendChild(document.createElement("iframe")),
        result = window != tmp_iframe.contentWindow.top;
    document.body.removeChild(tmp_iframe);
    return result;
};

// Date 相关
GLOBAL.Date = {};

/**
 * 获取当前格式化后的时间字符串
 * @returns {String} 格式化后的时间
 */
GLOBAL.Date.getCurrentFormatDate = function () {
    "use strict";
    var date = new Date();
    var y = 0, m = 0, d = 0, formattedDate = "";
    // date init
    // compatible for IE and Firefox
    y = date.getFullYear();
    m = date.getMonth() + 1;
    d = date.getDate();

    formattedDate += y + "-";
    if (m < 10) {
        formattedDate += "0";
    }
    formattedDate += m + "-";
    if (d < 10) {
        formattedDate += "0";
    }
    formattedDate += d;

    return formattedDate;
};

/**
 * 获取当前格式化后的时间字符串, 精确到分钟
 * @param {long} 毫秒数- 可选, 不传则为当前客户端时间.
 * @returns {String} 格式化后的时间
 */
GLOBAL.Date.getCurrentFormatDateMin = function (timeInMilli) {
    "use strict";
    var date = new Date();
    if(timeInMilli){
        date.setTime(timeInMilli);
    }
    var y = 0, m = 0, d = 0, h = 0, min = 0, formattedDate = "";
    // date init
    // compatible for IE and Firefox
    y = date.getFullYear();
    m = date.getMonth() + 1;
    d = date.getDate();
    h = date.getHours();
    min = date.getMinutes();

    formattedDate += y + "-";
    if (m < 10) {
        formattedDate += "0";
    }
    formattedDate += m + "-";
    if (d < 10) {
        formattedDate += "0";
    }
    formattedDate += d;

    formattedDate += " ";
    if (h < 10) {
        formattedDate += "0";
    }
    formattedDate += h;
    formattedDate += ":";
    if (min < 10) {
        formattedDate += "0";
    }
    formattedDate += min;

    return formattedDate;
};

/**
 * @description 此方法为私有方法，不对外提供，打印日志基础函数
 * @param info
 * @private
 */
GLOBAL._log = function (info) {
    "use strict";
    try {
        console.log(info);
    } catch (e) {
        ;
    }
};

/**
 * @description info 级别 log 打印，此log用于基本的提示信息，log级别最低
 * @param info
 */
GLOBAL.info = function (info) {
    "use strict";
    if (!GLOBAL.isDebug)
        return;
    GLOBAL._log("== INFO ==");
    GLOBAL._log(info);
};

/**
 * @description debug 级别 log 打印，此log
 * @param info
 */
GLOBAL.debug = function (info) {
    "use strict";
    if (!GLOBAL.isDebug)
        return;

    GLOBAL._log("== DEBUG ==");
    GLOBAL._log(info);
};

/**
 * @description warning 级别 log 打印
 * @param info
 */
GLOBAL.warning = function (info) {
    "use strict";
    GLOBAL._log("== WARNING ==");
    GLOBAL._log(info);
};

/**
 * @description error 级别 log 打印
 * @param info
 */
GLOBAL.error = function (info) {
    "use strict";
    GLOBAL._log("== ERROR ==");
    GLOBAL._log(info);
};

/**
 * 原生扩展
 * */
Function.prototype.method = function (name, func) {
    "use strict";
    if (!this.prototype[name]) {
        this.prototype[name] = func;
    }
    return this;
};

/**
 * extends the WebStorage , WebStorage is a class
 */
GLOBAL.namespace("WebStorage");

(function () {
    "use strict";

    var w = window,
        isSupport = function () {
            return !!localStorage;
        };

    var WebStorage = function (name) {
        var errorMsg = {
            "notSupport":"not support webStorage",
            "paramError":"parameters error, only support undefined|\"localStorage\"|\"messageStorage\""
        };

        if(!isSupport()) {
            G.error(errorMsg.notSupport);
            throw new Error(errorMsg.notSupport);
        }

        if(!G.isEmpty(name) && !G.contains(name, ["localStorage", "sessionStorage"])) {
            G.error(errorMsg.paramError);
            throw new Error(errorMsg.paramError);
        }

        this._name = name || "localStorage";
    };

    WebStorage.method("name", function () {
        return this._name;
    });

    WebStorage.method("set", function (key, value) {
        w[this.name()].setItem(key, value);
        return this;
    });

    WebStorage.method("get", function (key) {
        return w[this.name()].getItem(key);
    });

    WebStorage.method("has", function (key) {
        return !G.isEmpty(w[this.name()].getItem(key));
    });

    WebStorage.method("equal", function (key, targetValue) {
        if (!this.has(key)) return false;

        return w[this.name()].getItem(key) === targetValue;
    });

    WebStorage.method("remove", function(key) {
        w[this.name()].removeItem(key);
        return this;
    });

    WebStorage.method("clear", function() {
        w[this.name()].clear();
        return this;
    });

    GLOBAL.WebStorage = WebStorage;
}());

var G = GLOBAL;
GLOBAL.get = GLOBAL.Dom.get;
GLOBAL.trim = GLOBAL.Lang.trim;
GLOBAL.betweenNum = GLOBAL.Number.between;
GLOBAL.constrainNum = GLOBAL.Number.constrain;
GLOBAL.isNumber = GLOBAL.Lang.isNumber;
GLOBAL.isArray = GLOBAL.Lang.isArray;
GLOBAL.isBoolean = GLOBAL.Lang.isBoolean;
GLOBAL.isString = GLOBAL.Lang.isString;
GLOBAL.isObject = GLOBAL.Lang.isObject;
GLOBAL.isEmpty = GLOBAL.Lang.isEmpty;
GLOBAL.isNotEmpty = GLOBAL.Lang.isNotEmpty;
GLOBAL.isNull = GLOBAL.Lang.isNull;
GLOBAL.isUndefined = GLOBAL.Lang.isUndefined;
GLOBAL.normalize = GLOBAL.Lang.normalize;
GLOBAL.contains = GLOBAL.Lang.contains;
GLOBAL.getCurrentFormatDate = GLOBAL.Date.getCurrentFormatDate;
GLOBAL.getCurrentFormatDateMin = GLOBAL.Date.getCurrentFormatDateMin;
GLOBAL.getDate = GLOBAL.Util.getDate;
GLOBAL.isInIframe = GLOBAL.Display.isInIframe;
GLOBAL.getX = GLOBAL.Display.getX;
GLOBAL.getY = GLOBAL.Display.getY;
GLOBAL.generateUUID = GLOBAL.Util.generateUUID;
GLOBAL.isKeyCode = GLOBAL.Interactive.isKeyCode;
GLOBAL.isKeyName = GLOBAL.Interactive.isKeyName;
GLOBAL.keyCodeFromEvent = GLOBAL.Interactive.keyCodeFromEvent;
GLOBAL.keyNameFromEvent = GLOBAL.Interactive.keyNameFromEvent;
GLOBAL.keyNameFromKeyCode = GLOBAL.Interactive.keyNameFromKeyCode;
GLOBAL.getEvent = GLOBAL.Event.getEvent;
GLOBAL.getEventTarget = GLOBAL.Event.getEventTarget;
GLOBAL.stopPropagation = GLOBAL.Event.stopPropagation;
GLOBAL.localStorage = new GLOBAL.WebStorage("localStorage");
GLOBAL.sessionStorage = new GLOBAL.WebStorage("sessionStorage");
GLOBAL.contextPath = null;

$(document).ajaxComplete(function (evt, request, settings) {
    var permissionstatus=request.getResponseHeader("permissionstatus"); //通过XMLHttpRequest取得响应头，sessionstatus，
    if (permissionstatus === "nopermission") {
        layer.msg("对不起，您的操作权限不足!");
    }
});
