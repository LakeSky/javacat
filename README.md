
#javacat极简开发框架

现在的快速开发脚手架铺天盖地，功能越来越复杂，上手难度越来越高，基本上都是功能的累积，真的能在开发效率上有所改进的少之又少，
但是很多企业其实需要这样的开发框架，代码简洁，但是基本的菜单管理，角色管理，用户管理，权限控制都有。
简而精致，是我们不懈的追求，当别人加功能加代码的时候，我们逆流而上，重构精简现有的功能，从根本上提高开发的效率和代码的可读性，可控性。

比网络上那些号称极速的框架不知道快多少倍！
看了别人的项目代码才明白自己是多么的幸福，
真的是泪流满面

### 1.软件安装
1. 新建数据库
2. 导入javacat-init.sql
3. 配置jdbc.properties
4. 启动tomcat

### 2.项目特点
1. 纯Spring MVC技术栈，无DTO，没有鸡肋的interface,impl。
2. 扩展SpringSecurity权限控制，精细到任意请求的权限控制。
3. 资源无需手动录入数据库，根据注解自动生成菜单和权限列表，只需选择即可。

### 3.项目亮点
精细的菜单角色权限控制管理。

1.所有的角色由初始的系统管理员创建，每个后创建的角色，只能选择自己角色内的菜单和资源进行分配。

比如一个公司管理员，他具有公司管理的菜单和资源，那么他在新建其他角色的时候只能从自己所有的菜单和资源中组合产生新的角色。

2.再分配角色的数据权限只能在当前角色可分配的范围之内

如：店铺管理员的再分配角色的数据权限，最大的可见数据范围应该是本店铺，不能超出。

3.角色能够对最小粒度的资源单位进行有效的管理，比如按钮或者请求或者单纯的逻辑请求进行管理。(注:该功能目前开源版本不提供，请联系作者获取最新版本)
一般系统的做法是手动添加每个需要控制的url到数据库，然后配置到数据库，这样不仅容易出错，而且费时费力。
改进：直接通过扫描Controller的方式获取所有需要控制的资源，这样就避免了手动添加资源到数据库的烦恼。

**演示效果图：**
![输入图片说明](http://open.mutou888.com/javacat/curd.png "在这里输入图片标题")
![输入图片说明](http://open.mutou888.com/javacat/menu.png "在这里输入图片标题")
![输入图片说明](http://open.mutou888.com/javacat/user.png "在这里输入图片标题")
![输入图片说明](http://open.mutou888.com/javacat/role.png "在这里输入图片标题")

联系方式：QQ 2644328654(月牙儿)