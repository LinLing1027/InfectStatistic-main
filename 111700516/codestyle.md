# 缩进
* 程序块采用缩进风格编写，用TAB键进行缩进

# 变量命名
* 命名尽量使用英文单词，力求简单清楚，避免使用引起误解的词汇和模糊的缩写，使人产生误解。较短的单词可通过去掉“元音”形成缩写；较长的单词可取单词的头几个字母形成缩写；一些单词有大家公认的缩写；
* 采用驼峰命名法，即，第一个单词以小写字母开始，第二个单词的首字母大写或每一个单词的首字母都采用大写字母。

# 每行最多字符数
* 每行不多于80个字符，较长语句分成多行书写。长表达式要在低优先级操作符处划分新行，操作符放在新行之首，划分出的新行要进行适当的缩进，使排版整齐，语句可读。

# 函数最大行数
* 函数规模尽量控制在100行内

# 函数、类命名
* 命名尽量使用英文单词，力求简单清楚，避免使用引起误解的词汇和模糊的缩写，使人产生误解。较短的单词可通过去掉“元音”形成缩写；较长的单词可取单词的头几个字母形成缩写；一些单词有大家公认的缩写；

# 常量
* 常量采取全大写的形式，单词间用下划线进行分割。

# 空行规则
* 函数之间应该用空行分开；
* 用空行将代码按照逻辑片断划分； 
* 每个类声明之后应该加入空格同其他代码分开。 
* 程序块的分界符（如C/C++语言的大括号'{'和'}')应各独占一行并且位于同一列,同时与引用它们的语句左对齐。

# 注释规则
* 函数头部应进行注释，写出函数的目的/功能，输入参数，返回值等；
* 注释内容要清晰明了，没有二义性；
* 注释应与其描述的代码相近，对代码的注释应放在其上方或右方（对单条语句的注释）相邻位置，不可放在下面，如放于上方则需与其上面的代码用空行隔开； 
* 注释与所描述内容进行同样的缩排；
* 将注释与其上面的代码用空行隔开；
* 变量、常量、宏的注释应放在其上方相邻位置或右方；
* 对所有有物理含义的变量、常量或数组、类声明，若其命名不是充分自注释的，在声明时都必须加以注释；
* 分支语句需编写注释。

# 操作符前后空格
* 值操作符、比较操作符、算术操作符、逻辑操作符、位域操作符，如“=”、“+=”、“>=”、“<=”、“+”、“*”、“%”、“&&”、“||”、“<<”、“^”等二元操作符的前后应当加空格；
* 一元操作符如“!”、“~”、“++”、“--”、“&”（地址运算符）等前后不加空格。像“［］”、“.”、“->”这类操作符前后不加空格。

# 其他规则
* 尽量写类的构造、拷贝构造、析构和赋值函数，而不使用系统缺省的；
* 尽量少使用全局变量，尽量去掉没必要的公共变量。