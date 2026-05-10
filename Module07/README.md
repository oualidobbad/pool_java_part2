<img src="https://r2cdn.perplexity.ai/pplx-full-logo-primary-dark%402x.png" style="height:64px;margin-right:32px"/>
```
# <span style="color:#4CAF50">Java Module 07 – Reflection & Annotations</span> 🚀
```

***

## <span style="color:#2196F3">Module Overview</span> 📚

Module 07 is all about **mastering Java reflection and annotations** by building three progressively more advanced mini-frameworks:

- A **runtime reflection console tool** (inspect classes, create objects, call methods).[^1]
- A **compile-time annotation processor** that generates HTML forms from annotated classes.[^1]
- A **mini ORM** that maps Java classes to SQL tables and generates `CREATE`, `INSERT`, `UPDATE`, `SELECT` statements at runtime.[^2][^1]

By the end of this module, the repository showcases not just syntax knowledge, but the ability to design and implement **real-world framework-style code**.

***

## <span style="color:#9C27B0">Project Structure</span> 🧱

```bash
Module07/
├── ex00/
│   └── Reflection/               # Core reflection console app
│       ├── pom.xml
│       └── src/main/java/
│           ├── app/Program.java
│           └── classes/
│               ├── Car.java
│               └── User.java
│
├── ex01/
│   └── Annotations/              # HtmlForm / HtmlInput + HtmlProcessor
│       ├── pom.xml
│       └── src/main/java/
│           ├── annotations/
│           │   ├── HtmlForm.java
│           │   └── HtmlInput.java
│           ├── forms/UserForm.java
│           └── processor/HtmlProcessor.java
│
├── ex02/
│   └── ORM/
│       ├── pom.xml
│       └── src/main/java/
│           ├── annotations/
│           │   ├── OrmEntity.java    # @OrmEntity(table = ...)
│           │   ├── OrmColumn.java    # @OrmColumn(name = ..., length = ...)
│           │   └── OrmColumnId.java  # @OrmColumnId
│           ├── models/
│           │   └── User.java         # Example mapped entity
│           ├── app/
│           │   └── Main.java
│           └── manager/
│               └── OrmManager.java   # buildCreateTableSql, save, update, findById
│
└── README.md                     # You are here ✨
```


***

```
## <span style="color:#FF9800">Exercise 00 – Work with Classes</span> 🔍
```


### 🎯 Goal

Build a **reflection-driven console application** that:

- Lists classes from a `classes` package.
- Shows their fields and methods.
- Lets the user create an instance, modify a field, and call a method **at runtime** using reflection.[^1]


### 🗺 Structural Map

Main pieces (typical layout inside `ex00/Reflection`):

- `classes/`
    - `User.java`, `Car.java`, etc. – simple POJOs with constructors, fields, methods, `toString()`.[^1]
- `app/Program.java`
    - Entry point; uses reflection to:
        - Load a class by name.
        - Inspect fields and methods.
        - Construct instances and invoke methods.

Interaction:

1. `Program` reads user input → class name (`User`).
2. Uses `Class.forName("...")` or package-based discovery to get the `Class<?>`.
3. Uses `getDeclaredFields()` and `getDeclaredMethods()` to display metadata.
4. Uses constructors and `Field` / `Method` APIs to create and manipulate an instance.[^1]

### 🧠 Deep Dive – New Concepts

- **Runtime Reflection Basics**
    - `Class<?>`, `Field`, `Method`, constructors.
    - `getDeclaredFields()`, `getDeclaredMethods()`, `getDeclaredConstructor(...)`.[^1]
- **Accessing Private Members**
    - `field.setAccessible(true)` and `method.setAccessible(true)` to read/write private fields and call private methods when needed.
- **Dynamic Invocation**
    - `constructor.newInstance(args...)` to create objects dynamically.
    - `method.invoke(instance, args...)` to call methods chosen by the user at runtime.

This mirrors what frameworks like **Spring** and **JUnit** do under the hood when they “discover” and operate on your classes dynamically.[^3]

### ▶️ How to Run

From `ex00/`:

```bash
# Compile
mvn clean compile

# Run (adjust main class/package as needed)
mvn exec:java -Dexec.mainClass="app.Program"
```

During execution you should see a flow similar to the subject example: choose class → show fields/methods → create object → edit field → call method.[^1]

***

```
## <span style="color:#3F51B5">Exercise 01 – Annotations-SOURCE</span> 🧾
```


### 🎯 Goal

Create a **compile-time annotation processor** (`HtmlProcessor`) that:

- Reads custom annotations `@HtmlForm` and `@HtmlInput` on a class.
- Generates an HTML form file (e.g. `userform.html`) in the target directory when you run `mvn clean compile`.[^1]


### 🗺 Structural Map

Inside `ex01/Annotations`:

- `annotations/`
    - `HtmlForm.java` – annotation with `fileName`, `action`, `method` and `RetentionPolicy.SOURCE`.
    - `HtmlInput.java` – annotation with `type`, `name`, `placeholder`.[^1]
- `forms/`
    - `UserForm.java` – a class annotated with `@HtmlForm` and `@HtmlInput` on fields.[^1]
- `processor/`
    - `HtmlProcessor.java` – extends `AbstractProcessor` and implements the logic to scan annotated elements and generate HTML via `Filer`.

Interaction:

- `mvn clean compile` triggers the annotation processor.

```
- The processor finds all `@HtmlForm` classes, inspects their `@HtmlInput` fields, and writes an HTML file with `<form>` and `<input>` tags.[^1]
```


### 🧠 Deep Dive – New Concepts

- **Compile-Time Annotation Processing**
    - Annotations with `RetentionPolicy.SOURCE` are visible only to the compiler and processors, not at runtime.[^1]
    - Extending `AbstractProcessor`, overriding `process`, using `RoundEnvironment` and `TypeElement`.
- **Code / File Generation at Build Time**
    - Using `Filer` to create new files in `target` (e.g., HTML templates from Java metadata).
    - Decoupling “metadata” (annotations) from generated artifacts (HTML).
- **Separation of Concerns**
    - The annotated class (`UserForm`) describes the form.
    - The processor builds the HTML.
    - The business code never directly touches HTML string concatenation.

This is the same pattern major frameworks use (e.g., MapStruct, Dagger, Lombok style tools).[^3]

### ▶️ How to Run

From `ex01/`:

```bash
# Compile and trigger annotation processor
mvn clean compile
```

After compilation:

- Check `target/classes` (or processor output directory) for `userform.html`.

```
- Open it in a browser; it should have a `<form>` with `<input>` elements matching the annotations on `UserForm`.[^1]
```


***

```
## <span style="color:#E91E63">Exercise 02 – ORM</span> 🗄️
```


### 🎯 Goal

Implement a **tiny ORM framework** that:

- Uses runtime annotations to map Java classes to database tables.
- Generates SQL strings for:
    - `CREATE TABLE` (on initialization).
    - `INSERT` (`save`).
    - `UPDATE` (`update`).
    - `SELECT ... WHERE id = ?` (`findById`).[^2][^1]


### 🗺 Structural Map

Inside `ex02/ORM`:

- `annotations/`
    - `OrmEntity.java` – `@OrmEntity(table = "...")`.[^4]
    - `OrmColumn.java` – `@OrmColumn(name = "...", length = ...)`.[^5]
    - `OrmColumnId.java` – marks the primary key field.[^6]
- `models/`
    - `User.java` – example mapped entity:

```java
@OrmEntity(table = "simple_user")
public class User {
    @OrmColumnId
    private Long id;

    @OrmColumn(name = "first_name", length = 10)
    private String firstName;

    @OrmColumn(name = "last_name", length = 10)
    private String lastName;

    @OrmColumn(name = "age")
    private Integer age;
}
```


[^7]

- `manager/`
    - `OrmManager.java` – main ORM engine:
        - `buildCreateTableSql(Class<?>)`
        - `save(Object entity)`
        - `update(Object entity)`

```
- `<T> T findById(Long id, Class<T> aClass)`  
```


### 🧠 Deep Dive – New Concepts

- **Runtime Mapping via Annotations**
    - `@OrmEntity` provides the table name.[^4][^7]
    - `@OrmColumnId` marks the primary key (auto-increment in SQL).[^6]
    - `@OrmColumn` describes each normal column: name and optional length.[^5]
- **Type Mapping (Java → SQL)**
    - `String` → `VARCHAR(length)`
    - `Integer` / `int` → `INT`
    - `Long` / `long` → `BIGINT`
    - `Boolean` / `boolean` → `BOOLEAN`
    - `Double` → `DOUBLE`[^2]
- **SQL Generation Patterns**
    - `buildCreateTableSql`
        - Iterate all fields; build column definitions based on annotations and field type.
        - Produce:

```sql
DROP TABLE IF EXISTS simple_user;
CREATE TABLE simple_user (
    id BIGINT PRIMARY KEY AUTOINCREMENT,
    first_name VARCHAR(10),
    last_name VARCHAR(10),
    age INT
);
```


[^2]

- `save(Object entity)`
    - Collect all `@OrmColumn` fields and their values.
    - Generate:

```sql
INSERT INTO simple_user (first_name, last_name, age)
VALUES ('Alice', 'Smith', 25);
```

- `update(Object entity)`
    - Use `@OrmColumnId` field for `WHERE`.
    - Set each `@OrmColumn` column, even if Java field is `null` → `column = NULL`.[^1]

```sql
UPDATE simple_user
SET first_name = 'Bob',
    last_name = 'Brown',
    age = NULL
WHERE id = 5;
```

- `findById(Long id, Class<T> aClass)`
    - Build a full `SELECT` including id and all `@OrmColumn`s:

```sql
SELECT id, first_name, last_name, age
FROM simple_user
WHERE id = 5;
```


[^8][^7][^2]

This mirrors how real ORMs (e.g., Hibernate) use metadata + reflection to stay independent of particular DBMS implementations.[^9]

### ▶️ How to Run

From `ex02/`:

```bash
# Compile
mvn clean compile

# Run (adjust main class/package)
mvn exec:java -Dexec.mainClass="app.Main"
```

In a typical `Main`:

- Instantiate `OrmManager`.
- Call `buildCreateTableSql(User.class)` and print SQL.[^2]
- Create a `User`, call `save(user)` and `update(user)` and `findById(1L, User.class)`; verify the printed SQL matches expectations.[^7][^2]

***

## <span style="color:#00BCD4">Progress \& Status</span> ✅

Badges you might add to the repo:

- 
- 
- 

Suggested progress checklist:

- [x] Exercise 00 – Reflection console tool
- [x] Exercise 01 – HTML annotation processor
- [x] Exercise 02 – Mini ORM with SQL generation
- [ ] Optional – Real DB integration (JDBC)
- [ ] Optional – ResultSet → Object mapping with reflection

***

## <span style="color:#795548">Technical Requirements</span> ⚙️

- **Java:** Latest LTS (e.g., Java 17) as required by the subject.[^1]
- **Build Tool:** Maven
    - Proper `maven-compiler-plugin` config for annotation processing in `ex01`.[^1]
- **Runtimes:**
    - Must be runnable on standard JVM and GraalVM according to the subject rules.[^1]
- **Dependencies (suggested):**
    - `auto-service` (for `@AutoService(Processor.class)` in `ex01`).[^1]
    - JUnit (optional, for your own tests).

***

This README is designed to be the **front page of your GitHub repo**, showing that you understand not just *what* the exercises do, but *why* they exist and how they connect to real-world frameworks.

To tune this even better for you: which section do you want expanded with concrete code snippets next time you refer back (reflection console in ex00, annotation processor in ex01, or ORM manager in ex02)?

<div align="center">⁂</div>

[^1]: en.subject.pdf

[^2]: OrmManager.java

[^3]: https://jenkov.com/tutorials/java-reflection/annotations.html

[^4]: OrmEntity.java

[^5]: OrmColumn.java

[^6]: OrmColumnId.java

[^7]: User.java

[^8]: https://stackoverflow.com/questions/69707257/how-can-i-write-a-sql-query-to-display-id-first-name-last-name-of-all-players

[^9]: https://docs.hibernate.org/orm/4.2/devguide/en-US/html/ch13.html

