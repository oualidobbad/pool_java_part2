package app;

import manager.OrmManager;
import models.*;

import java.awt.*;

public class Main {
    static void main() {
        User user = new User(null, "oualid", "obbad", 23);

        OrmManager orm = new OrmManager();
        orm.save(user);

        orm.findById(1L, User.class);
//        System.out.println(orm.findAll(User.class));
    }
}

