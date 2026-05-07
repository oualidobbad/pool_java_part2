package manager;

import annotations.OrmColumn;
import annotations.OrmColumnId;
import annotations.OrmEntity;

import java.lang.reflect.Field;

public class OrmManager {

    public static String buildCreateTableSql(Class<?> clazz) {
        if (clazz.isAnnotationPresent(OrmEntity.class)) {
            OrmEntity ormEntity = clazz.getAnnotation(OrmEntity.class);
            StringBuilder SQL_QUERY = new StringBuilder();

            SQL_QUERY.append("DROP TABLE IF EXISTS " + ormEntity.table() + ";\n");
            SQL_QUERY.append("CREATE TABLE " + ormEntity.table() + " (\n");
            boolean first = true;
            for (Field field : clazz.getDeclaredFields())
            {
                Class<?> type = field.getType();
                if (field.isAnnotationPresent(OrmColumnId.class)) {
                    if (!first)
                    {
                        SQL_QUERY.append(",\n");
                    }
                    if (type == String.class) {
                        SQL_QUERY.append(field.getName() + " VARCHAR(255) PRIMARY KEY");
                    }
                    else if (type == int.class || type == Integer.class) {
                        SQL_QUERY.append(field.getName() + " SERIAL PRIMARY KEY");
                    }
                    else if (type == long.class || type == Long.class) {
                        SQL_QUERY.append(field.getName() + " BIGINT PRIMARY KEY AUTO_INCREMENT");
                    }
                    else if (type == boolean.class || type == Boolean.class) {
                        SQL_QUERY.append(field.getName() + " BOOLEAN PRIMARY KEY");
                    }
                    else if (type == Double.class) {
                        SQL_QUERY.append(field.getName() + " DOUBLE PRIMARY KEY AUTO_INCREMENT");
                    }
                    else
                    {
                        throw new IllegalArgumentException("Unsupported data type for primary key: " + type.getName());
                    }
                    first = false;
                }
                else if (field.isAnnotationPresent(OrmColumn.class)) {
                    if (!first)
                    {
                        SQL_QUERY.append(",\n");
                    }
                    OrmColumn ormColumn = field.getAnnotation(OrmColumn.class);

                    if (type == String.class) {
                        SQL_QUERY.append(ormColumn.name() + " VARCHAR(" + ormColumn.length() + ")");
                    }
                    else if (type == int.class || type == Integer.class) {
                        SQL_QUERY.append(ormColumn.name() + " INT");
                    }
                    else if (type == long.class || type == Long.class) {
                        SQL_QUERY.append(ormColumn.name() + " BIGINT");
                    }
                    else if (type == boolean.class || type == Boolean.class) {
                        SQL_QUERY.append(ormColumn.name() + " BOOLEAN");
                    }
                    else if (type == Double.class) {
                        SQL_QUERY.append( ormColumn.name() + " DOUBLE");
                    }
                    else
                    {
                        throw new IllegalArgumentException("Unsupported data type for column: " + type.getName());
                    }
                    first = false;

                }
            }
            SQL_QUERY.append(");");
            return SQL_QUERY.toString();  
        }else
        {
            throw new IllegalArgumentException("The class " + clazz.getName() + " is not annotated with @OrmEntity");
        }
    }

}
