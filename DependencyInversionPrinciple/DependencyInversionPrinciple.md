# Dependency Inversion Principle (DIP)

## What is DIP?

> **High-level modules should not depend on low-level modules. Both should depend on abstractions.**

And:

> **Abstractions should not depend on details. Details should depend on abstractions.**

In simple words:

> **A high-level class should depend on an interface/abstraction instead of directly depending on a concrete implementation.**

---

# ❌ Without DIP

Consider this `MacBook` class:

```java
class MacBook {

    private final WiredKeyboard keyboard;
    private final WiredMouse mouse;

    public MacBook() {
        keyboard = new WiredKeyboard();
        mouse = new WiredMouse();
    }
}
```

The important part is:

```java
keyboard = new WiredKeyboard();
mouse = new WiredMouse();
```

Here, `MacBook` is directly creating and depending on concrete classes.

The dependency looks like this:

```text
MacBook
   |
   | depends on
   ↓
WiredKeyboard

MacBook
   |
   | depends on
   ↓
WiredMouse
```

---

# What is the Problem?

Suppose tomorrow we want to use:

```text
WirelessKeyboard
WirelessMouse
```

We have to modify the `MacBook` class:

```java
class MacBook {

    private final WirelessKeyboard keyboard;
    private final WirelessMouse mouse;

    public MacBook() {
        keyboard = new WirelessKeyboard();
        mouse = new WirelessMouse();
    }
}
```

So `MacBook` is tightly coupled to specific implementations:

```text
MacBook
   ↓
WiredKeyboard
WiredMouse
```

The `MacBook` class knows exactly which keyboard and mouse it must create.

This makes the code less flexible.

---

# ✅ Applying DIP

Instead of making `MacBook` depend directly on concrete classes, we create abstractions.

## Keyboard Interface

```java
interface Keyboard {

    void type();
}
```

## Mouse Interface

```java
interface Mouse {

    void click();
}
```

Now `MacBook` can depend on these interfaces instead of concrete classes.

---

# Concrete Implementations

## WiredKeyboard

```java
class WiredKeyboard implements Keyboard {

    @Override
    public void type() {
        System.out.println("Typing using wired keyboard");
    }
}
```

## WirelessKeyboard

```java
class WirelessKeyboard implements Keyboard {

    @Override
    public void type() {
        System.out.println("Typing using wireless keyboard");
    }
}
```

## WiredMouse

```java
class WiredMouse implements Mouse {

    @Override
    public void click() {
        System.out.println("Clicking using wired mouse");
    }
}
```

## WirelessMouse

```java
class WirelessMouse implements Mouse {

    @Override
    public void click() {
        System.out.println("Clicking using wireless mouse");
    }
}
```

Now the design looks like this:

```text
             Keyboard
              /     \
             /       \
WiredKeyboard     WirelessKeyboard


               Mouse
              /     \
             /       \
      WiredMouse   WirelessMouse
```

---

# MacBook After Applying DIP

Now `MacBook` depends on the abstractions:

```java
class MacBook {

    private final Keyboard keyboard;
    private final Mouse mouse;

    public MacBook(Keyboard keyboard, Mouse mouse) {
        this.keyboard = keyboard;
        this.mouse = mouse;
    }

    public void useMacBook() {
        keyboard.type();
        mouse.click();
    }
}
```

Notice the important difference.

### Before DIP

```java
private final WiredKeyboard keyboard;
private final WiredMouse mouse;
```

`MacBook` was dependent on concrete classes.

### After DIP

```java
private final Keyboard keyboard;
private final Mouse mouse;
```

`MacBook` is now dependent on abstractions.

---

# How Do We Provide the Actual Devices?

We provide them through the constructor.

## Using Wired Devices

```java
MacBook macBook = new MacBook(
    new WiredKeyboard(),
    new WiredMouse()
);
```

## Using Wireless Devices

```java
MacBook macBook = new MacBook(
    new WirelessKeyboard(),
    new WirelessMouse()
);
```

Notice that the `MacBook` class itself does not change.

Only the objects we provide to it change.

---

# Complete Working Example

## Keyboard

```java
interface Keyboard {

    void type();
}
```

## Mouse

```java
interface Mouse {

    void click();
}
```

## Wired Keyboard

```java
class WiredKeyboard implements Keyboard {

    @Override
    public void type() {
        System.out.println("Typing using wired keyboard");
    }
}
```

## Wireless Keyboard

```java
class WirelessKeyboard implements Keyboard {

    @Override
    public void type() {
        System.out.println("Typing using wireless keyboard");
    }
}
```

## Wired Mouse

```java
class WiredMouse implements Mouse {

    @Override
    public void click() {
        System.out.println("Clicking using wired mouse");
    }
}
```

## Wireless Mouse

```java
class WirelessMouse implements Mouse {

    @Override
    public void click() {
        System.out.println("Clicking using wireless mouse");
    }
}
```

## MacBook

```java
class MacBook {

    private final Keyboard keyboard;
    private final Mouse mouse;

    public MacBook(Keyboard keyboard, Mouse mouse) {
        this.keyboard = keyboard;
        this.mouse = mouse;
    }

    public void useMacBook() {
        keyboard.type();
        mouse.click();
    }
}
```

## Main

```java
public class Main {

    public static void main(String[] args) {

        MacBook macBook = new MacBook(
            new WirelessKeyboard(),
            new WirelessMouse()
        );

        macBook.useMacBook();
    }
}
```

### Output

```text
Typing using wireless keyboard
Clicking using wireless mouse
```

---

# What Did DIP Change?

## Before DIP

```text
             MacBook
                |
                ↓
        WiredKeyboard
        WiredMouse
```

`MacBook` directly depends on concrete implementations.

---

## After DIP

```text
              MacBook
              /     \
             ↓       ↓
        Keyboard     Mouse
           ↑           ↑
           |           |
     ┌─────┴─────┐ ┌───┴──────┐
     │           │ │          │
   Wired      Wireless     Wired    Wireless
 Keyboard     Keyboard     Mouse      Mouse
```

Now `MacBook` depends on abstractions:

```text
Keyboard
Mouse
```

The concrete implementations also depend on those abstractions.

---

# Why Is It Called "Dependency Inversion"?

Normally, without DIP:

```text
High-level module
       ↓
Low-level module
```

Example:

```text
MacBook
   ↓
WiredKeyboard
```

The high-level class directly depends on the low-level implementation.

With DIP:

```text
           Abstraction
           ↑         ↑
           |         |
       MacBook    WiredKeyboard
```

Both depend on the abstraction.

So instead of:

```text
MacBook → WiredKeyboard
```

we have:

```text
MacBook → Keyboard ← WiredKeyboard
```

The dependency is now centered around an abstraction.

---

# Dependency Injection

Notice that we pass the keyboard and mouse into the constructor:

```java
public MacBook(Keyboard keyboard, Mouse mouse) {
    this.keyboard = keyboard;
    this.mouse = mouse;
}
```

This is called **Dependency Injection (DI)**.

`MacBook` needs a keyboard and mouse, but it does not create them itself.

The dependencies are provided from outside.

For example:

```java
MacBook macBook = new MacBook(
    new WirelessKeyboard(),
    new WirelessMouse()
);
```

Here:

```text
MacBook
   ↑
   |
Dependencies provided from outside
   |
   ├── WirelessKeyboard
   └── WirelessMouse
```

### Important distinction

> **Dependency Injection is a technique. Dependency Inversion Principle is a design principle.**

Dependency Injection is one common way to implement DIP.

---

# Why Is DIP Helpful?

## 1. Loose Coupling

`MacBook` is not tightly coupled to:

```text
WiredKeyboard
WiredMouse
```

It depends on:

```text
Keyboard
Mouse
```

Therefore, the concrete implementation can change without changing the `MacBook` class.

---

## 2. Easy to Change Implementations

We can switch from:

```java
new WiredKeyboard()
```

to:

```java
new WirelessKeyboard()
```

without modifying `MacBook`.

For example:

```java
MacBook macBook = new MacBook(
    new WirelessKeyboard(),
    new WirelessMouse()
);
```

The `MacBook` class remains unchanged.

---

## 3. Easier Testing

We can provide a fake or test keyboard during testing.

```java
class TestKeyboard implements Keyboard {

    @Override
    public void type() {
        System.out.println("Test keyboard");
    }
}
```

Then:

```java
MacBook macBook = new MacBook(
    new TestKeyboard(),
    new WiredMouse()
);
```

We can test `MacBook` without using a real keyboard implementation.

---

# How to Identify a DIP Violation

Look for code like:

```java
class MacBook {

    public MacBook() {
        keyboard = new WiredKeyboard();
        mouse = new WiredMouse();
    }
}
```

Ask yourself:

> **"Is my high-level class directly creating and depending on a specific implementation?"**

If yes, there may be a DIP violation.

A better design is:

```java
class MacBook {

    private final Keyboard keyboard;
    private final Mouse mouse;

    public MacBook(Keyboard keyboard, Mouse mouse) {
        this.keyboard = keyboard;
        this.mouse = mouse;
    }
}
```

Now the high-level class depends on abstractions.

---

# SRP vs OCP vs LSP vs ISP vs DIP

These principles solve different problems.

## SRP — Single Responsibility Principle

> **Does a class have one responsibility?**

```text
Invoice
   ↓
Calculate invoice
```

---

## OCP — Open/Closed Principle

> **Can we extend the system without modifying existing code?**

```text
InvoiceRepository
    /      |       \
 MySQL   MongoDB  PostgreSQL
```

---

## LSP — Liskov Substitution Principle

> **Can a child object safely replace its parent object?**

```text
       Bike
      /    \
MotorBike Bicycle

Both can safely perform:
       ↓
      ride()
```

---

## ISP — Interface Segregation Principle

> **Are classes forced to depend on methods they don't need?**

```text
Chef
 ↓
Cookable

Waiter
 ↓
OrderTaker + CustomerService
```

---

## DIP — Dependency Inversion Principle

> **Does a high-level class depend on abstractions instead of concrete implementations?**

```text
          MacBook
          /     \
         ↓       ↓
    Keyboard     Mouse
       ↑           ↑
       |           |
    Wired       Wireless
```

---

# Remember

### DIP in one line:

> **High-level code should depend on abstractions, not concrete implementations.**

### Simple mental model

### ❌ Without DIP

```text
MacBook
   ↓
WiredKeyboard

MacBook
   ↓
WiredMouse
```

`MacBook` decides which concrete devices to create.

---

### ✅ With DIP

```text
             MacBook
             /     \
            ↓       ↓
       Keyboard     Mouse
          ↑           ↑
          |           |
       Wired       Wireless
```

`MacBook` only says:

> "I need a `Keyboard` and a `Mouse`."

It does **not** decide whether they are wired or wireless.

The concrete implementations are provided from outside.

---

## Final Takeaway

The original code:

```java
keyboard = new WiredKeyboard();
mouse = new WiredMouse();
```

creates **tight coupling** because `MacBook` directly depends on concrete implementations.

The DIP version:

```java
public MacBook(Keyboard keyboard, Mouse mouse) {
    this.keyboard = keyboard;
    this.mouse = mouse;
}
```

creates **loose coupling** because `MacBook` depends on abstractions.

Therefore:

> **DIP = Depend on abstractions, not concrete implementations.**