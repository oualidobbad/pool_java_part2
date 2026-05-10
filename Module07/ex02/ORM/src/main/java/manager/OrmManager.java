package manager;

import annotations.OrmColumn;
import annotations.OrmColumnId;
import annotations.OrmEntity;

import java.lang.reflect.Field;
import java.util.*;

public class OrmManager {

    public static String buildCreateTableSql(Class<?> clazz) {
        if (clazz.isAnnotationPresent(OrmEntity.class)) {
            OrmEntity ormEntity = clazz.getAnnotation(OrmEntity.class);
            StringBuilder SQL_QUERY = new StringBuilder();

            SQL_QUERY.append("DROP TABLE IF EXISTS " + ormEntity.table() + ";\n");
            SQL_QUERY.append("CREATE TABLE " + ormEntity.table() + " (\n");
            boolean first = true;
            for (Field field : clazz.getDeclaredFields()) {
                Class<?> type = field.getType();
                if (field.isAnnotationPresent(OrmColumnId.class)) {
                    if (!first)
                        SQL_QUERY.append(",\n");
                    if (type == String.class)
                        SQL_QUERY.append(field.getName() + " VARCHAR(255) PRIMARY KEY");
                    else if (type == int.class || type == Integer.class)
                        SQL_QUERY.append(field.getName() + " SERIAL PRIMARY KEY");
                    else if (type == long.class || type == Long.class)
                        SQL_QUERY.append(field.getName() + " BIGINT PRIMARY KEY AUTO_INCREMENT");
                    else if (type == boolean.class || type == Boolean.class)
                        SQL_QUERY.append(field.getName() + " BOOLEAN PRIMARY KEY");
                    else if (type == Double.class)
                        SQL_QUERY.append(field.getName() + " DOUBLE PRIMARY KEY AUTO_INCREMENT");
                    else
                        throw new IllegalArgumentException("Unsupported data type for primary key: " + type.getName());
                    first = false;
                } else if (field.isAnnotationPresent(OrmColumn.class)) {
                    if (!first)
                        SQL_QUERY.append(",\n");

                    OrmColumn ormColumn = field.getAnnotation(OrmColumn.class);

                    if (type == String.class)
                        SQL_QUERY.append(ormColumn.name() + " VARCHAR(" + ormColumn.length() + ")");
                    else if (type == int.class || type == Integer.class)
                        SQL_QUERY.append(ormColumn.name() + " INT");
                    else if (type == long.class || type == Long.class)
                        SQL_QUERY.append(ormColumn.name() + " BIGINT");
                    else if (type == boolean.class || type == Boolean.class)
                        SQL_QUERY.append(ormColumn.name() + " BOOLEAN");
                    else if (type == Double.class)
                        SQL_QUERY.append(ormColumn.name() + " DOUBLE");
                    else
                        throw new IllegalArgumentException("Unsupported data type for column: " + type.getName());
                    first = false;

                }
            }
            SQL_QUERY.append(");");
            return SQL_QUERY.toString();
        } else
            throw new IllegalArgumentException("The class " + clazz.getName() + " is not annotated with @OrmEntity");
    }

    public void save (Object entity)
    {
        Class<?> clazz = entity.getClass();
        try{
            String CREATE_TABLE = buildCreateTableSql(clazz);
            StringBuilder INSERT = new StringBuilder("INSERT INTO " + clazz.getAnnotation(OrmEntity.class).table() + "(");
            List<String> values = new LinkedList<>();

            boolean first = true;

            for (Field field : clazz.getDeclaredFields())
            {
                if (!field.isAnnotationPresent(OrmColumn.class))
                    continue;
                if (!first)
                    INSERT.append(", ");
                first = false;
                OrmColumn ormColumn = field.getAnnotation(OrmColumn.class);
                INSERT.append(ormColumn.name());
                field.setAccessible(true);
                if (field.getType() == String.class)
                    values.add("'"+field.get(entity).toString() + "'");
                else
                    values.add(field.get(entity).toString());
            }
            first = true;
            INSERT.append(")\n VALUES \n(");
            for (int i = 0; i < values.size(); i++){
                if (!first)
                    INSERT.append(", ");
                INSERT.append(values.get(i));
                first = false;
            }
            INSERT.append(");");
            System.out.println(CREATE_TABLE);
            System.out.println(INSERT);

        }
        catch (Exception e) {
            System.out.printf(e.getMessage());
        }
    }

    public void update(Object entity){
        try {
            Class<?> clazz = entity.getClass();
            if (!clazz.isAnnotationPresent(OrmEntity.class))
                throw new IllegalArgumentException("The class " + clazz.getName() + " is not annotated with @OrmEntity");
            OrmEntity ormEntity = clazz.getAnnotation(OrmEntity.class);
            StringBuilder UPDATE = new StringBuilder("UPDATE ");
            UPDATE.append(ormEntity.table()).append("\nSET ");
            boolean first = true;
            boolean first2 = true;
            StringBuilder WHERE = new StringBuilder("\nWHERE ");
            for (Field field : clazz.getDeclaredFields())
            {
                field.setAccessible(true);
                if (field.isAnnotationPresent(OrmColumnId.class))
                {
                    if (!first2)
                        WHERE.append(", ");
                    WHERE.append(field.getName() + " = ");
                    if (field.getType() == String.class)
                        WHERE.append("'" + field.get(entity)+"'");
                    else
                        WHERE.append(field.get(entity));
                    first2 = false;
                    continue;
                }
                if (!field.isAnnotationPresent(OrmColumn.class)) continue;
                OrmColumn ormColumn = field.getAnnotation(OrmColumn.class);
                if (!first)
                    UPDATE.append(",\n");
                if (field.getType() == String.class)
                    UPDATE.append(ormColumn.name() + " = '" + field.get(entity) + "'");
                else
                    UPDATE.append(ormColumn.name() + " = " + field.get(entity));
                first = false;
            }
            UPDATE.append(WHERE + ";");
            System.out.println(UPDATE);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public <T> T findById(Long id, Class<T> aClass) {
        try {
            if (!aClass.isAnnotationPresent(OrmEntity.class))
                throw new IllegalArgumentException("The class " + aClass+ " is not annotated with @OrmEntity");
            OrmEntity ormEntity = aClass.getAnnotation(OrmEntity.class);
            StringBuilder SELECT = new StringBuilder("SELECT ");
            StringBuilder WHERE = new StringBuilder(" WHERE ");
            boolean first = true;
            for (Field field : aClass.getDeclaredFields()) {
                if (!first)
                    SELECT.append(", ");
                field.setAccessible(true);
                if (field.isAnnotationPresent(OrmColumnId.class)) {
                    WHERE.append(field.getName()).append(" = ").append(id);
                    SELECT.append(field.getName());
                    first = false;
                }
                if (!field.isAnnotationPresent(OrmColumn.class))
                    continue;
                OrmColumn ormColumn = field.getAnnotation(OrmColumn.class);
                SELECT.append(ormColumn.name());
                first = false;
            }
            SELECT.append(" FROM ").append(ormEntity.table());
            SELECT.append(WHERE).append(";");
            System.out.println(SELECT);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
