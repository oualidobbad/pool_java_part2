package app;

import manager.OrmManager;
import models.User;

public class Main
{
    static void main()
    {
        // User user = new User();

        Class<?> clazz = User.class;
        System.out.println( OrmManager.buildCreateTableSql(clazz));
    }
}

