## 使用方法

    拷贝com.rom471.userdb.DBHelper 到你的包即可使用
## 示例代码
```java
class Test{ 
    public void test(){
        DBHelper db=new DBHelper(this);//构造方法，要传入Context对象
        db.InitialWithTestData(this);//使用测试数据初始化数据库
        db.haveUser("wkq");//判断是否有用户wkq
        db.loginWith("xxs", "cedar");//使用用户名和密码登录，并返回是否成功}
        db.inserUser(3,"wkq","password" ,null);//添加用户，头像暂时为空
    }
}
       

```
## 方法说明

| 方法签名                                              | 说明                           |
| ----------------------------------------------------- | ------------------------------ |
| public DBHelper(Context context)                      | 构造方法，需要传入上下文       |
| public  void InitialWithTestData()                    | 用测试数据初始化数据库         |
| public boolean haveUser(String name)                  | 判断用户名为name的用户是否存在 |
| public boolean loginWith(String name,String password) | 使用用户名和密码登录           |
| public Drawable getAvatarByName(String name)          | 通过用户名获取头像             |

