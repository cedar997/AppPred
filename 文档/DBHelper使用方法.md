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
